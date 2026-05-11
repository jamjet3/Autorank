package me.armar.plugins.autorank.util.uuid;

import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.config.SettingsConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Central name-to-UUID resolver. Replaces ad-hoc calls to
 * {@link Bukkit#getOfflinePlayer(String)} — which silently returns an
 * {@code OfflinePlayer} with a hash-derived UUID for unknown names — with a
 * sequence of safe, configurable strategies.
 *
 * <p>Resolution strategies (in order):
 * <ol>
 *   <li>Online exact match via {@link Bukkit#getPlayerExact(String)}.</li>
 *   <li>Local YAML cache via
 *       {@link UUIDStorage#getStoredUUID(String)} (populated for every player
 *       at join, so previously-seen Bedrock players resolve correctly).</li>
 *   <li>For Bedrock-prefixed names only and only when Floodgate is installed
 *       and enabled, the Floodgate external XUID service ({@code getUuidFor}).
 *       Asynchronous. Not used for online or cached resolution.</li>
 *   <li>For non-Bedrock-looking names, the existing Mojang fallback via
 *       {@link UUIDManager#getUUID(String)}.</li>
 * </ol>
 *
 * <p>Design notes:
 * <ul>
 *   <li>This service NEVER returns the synthetic UUID that Bukkit produces
 *       for unknown names.</li>
 *   <li>It NEVER calls Mojang for names that match the configured Bedrock
 *       prefix (Mojang has no record of Bedrock players).</li>
 *   <li>It does NOT silently try a prefixed variant when an unprefixed name
 *       is given. A Java player {@code Foo} and a Bedrock player
 *       {@code .Foo} can coexist; admins must type the exact visible name.</li>
 * </ul>
 */
public final class PlayerLookupService {
    private static final String DEFAULT_BEDROCK_PREFIX = ".";

    private final Autorank plugin;

    public PlayerLookupService(Autorank plugin) {
        this.plugin = plugin;
    }

    /**
     * Synchronous resolution against online players and the local UUID cache
     * only. Safe to call from the main thread; never blocks on I/O.
     *
     * <p>Returns {@code Optional.empty()} when the name is null/blank, when
     * no online player exactly matches, and when the local cache has no
     * entry. Callers wanting a Mojang or Floodgate fallback should use
     * {@link #resolveFull(String)} from an async context.
     */
    public Optional<UUID> resolveOnlineOrCached(String input) {
        if (input == null) return Optional.empty();
        String name = input.trim();
        if (name.isEmpty()) return Optional.empty();

        Player online = Bukkit.getPlayerExact(name);
        if (online != null) return Optional.of(online.getUniqueId());

        UUID cached = plugin.getUUIDStorage().getStoredUUID(name);
        if (cached != null) return Optional.of(cached);

        return Optional.empty();
    }

    /**
     * Asynchronous full resolution. Tries the quick path first, then falls
     * back to:
     * <ul>
     *   <li>Floodgate's external XUID service for names matching the
     *       configured Bedrock prefix (only when Floodgate is installed and
     *       {@code use-floodgate-api} is not {@code never}).</li>
     *   <li>The existing Mojang fallback (via {@link UUIDManager}) for
     *       names that do <b>not</b> match the Bedrock prefix.</li>
     * </ul>
     * Bedrock-prefixed names are never sent to Mojang.
     */
    public CompletableFuture<Optional<UUID>> resolveFull(String input) {
        Optional<UUID> quick = resolveOnlineOrCached(input);
        if (quick.isPresent()) {
            return CompletableFuture.completedFuture(quick);
        }
        if (input == null) return CompletableFuture.completedFuture(Optional.empty());
        String name = input.trim();
        if (name.isEmpty()) return CompletableFuture.completedFuture(Optional.empty());

        if (isBedrockLikeName(name)) {
            if (shouldUseFloodgateForLookup() && FloodgateHook.isAvailable()) {
                String prefix = getActiveBedrockPrefix();
                String gamertag = (!prefix.isEmpty() && name.startsWith(prefix))
                        ? name.substring(prefix.length())
                        : name;
                return FloodgateHook.resolveBedrockUuidAsync(gamertag);
            }
            // Bedrock-prefixed but no Floodgate available: stop here, do
            // NOT fall through to Mojang.
            return CompletableFuture.completedFuture(Optional.empty());
        }

        // Java-looking name: existing Mojang fallback behaviour.
        return CompletableFuture.supplyAsync(() -> {
            try {
                UUID uuid = UUIDManager.getUUID(name).get();
                return Optional.ofNullable(uuid);
            } catch (Exception e) {
                return Optional.empty();
            }
        });
    }

    /**
     * The active Bedrock prefix string. Reads {@code bedrock.prefix} from
     * Settings.yml; when set to {@code auto}, asks Floodgate. Returns an empty
     * string when Bedrock handling is disabled in Settings.yml.
     */
    public String getActiveBedrockPrefix() {
        if (!isBedrockEnabled()) return "";
        String configured = plugin.getSettingsConfig().getBedrockPrefixSetting();
        if (configured == null) return DEFAULT_BEDROCK_PREFIX;
        if (!configured.equalsIgnoreCase("auto")) return configured;
        if ("never".equals(plugin.getSettingsConfig().getBedrockFloodgateMode())) {
            return DEFAULT_BEDROCK_PREFIX;
        }
        return FloodgateHook.getConfiguredPrefix().orElse(DEFAULT_BEDROCK_PREFIX);
    }

    /**
     * Whether the supplied name starts with the configured Bedrock prefix.
     * Returns false when Bedrock handling is disabled or the prefix is empty.
     */
    public boolean isBedrockLikeName(String name) {
        if (name == null || name.isEmpty()) return false;
        if (!isBedrockEnabled()) return false;
        String prefix = getActiveBedrockPrefix();
        return !prefix.isEmpty() && name.startsWith(prefix);
    }

    /**
     * Whether a Mojang lookup should be skipped for the given name. Used by
     * {@link UUIDManager#getUUIDs(java.util.List)} to filter batch lookups.
     */
    public boolean shouldSkipMojangFor(String name) {
        if (!isBedrockEnabled()) return false;
        if (!plugin.getSettingsConfig().skipMojangForBedrockPrefix()) return false;
        return isBedrockLikeName(name);
    }

    private boolean isBedrockEnabled() {
        SettingsConfig settings = plugin.getSettingsConfig();
        return settings != null && settings.isBedrockEnabled();
    }

    private boolean shouldUseFloodgateForLookup() {
        String mode = plugin.getSettingsConfig().getBedrockFloodgateMode();
        return !"never".equals(mode);
    }
}
