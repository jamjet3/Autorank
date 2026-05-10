package me.armar.plugins.autorank.util.uuid;

import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.config.SettingsConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Central resolver for converting a player-name string (typed by an admin or
 * console) into a UUID. Supersedes ad-hoc calls to
 * {@code Bukkit.getOfflinePlayer(String)}, which silently returns a synthetic
 * (hash-derived) UUID for unknown names — a long-standing source of data
 * corruption that especially affects Bedrock/Floodgate players.
 *
 * <p>Resolution order:
 * <ol>
 *   <li>{@code Bukkit.getPlayerExact} — online Java or online Bedrock (with prefix)</li>
 *   <li>If the configured Bedrock prefix is set and the input doesn't carry it,
 *       try again with the prefix prepended</li>
 *   <li>{@link UUIDStorage} local cache (populated for every player at join)</li>
 *   <li>{@link UUIDManager} — Mojang fallback (skipped for prefix-matched names)</li>
 * </ol>
 *
 * <p>This service NEVER returns the synthetic UUID Bukkit produces for unknown
 * names, and NEVER calls Mojang for names matching the configured Bedrock
 * prefix (Mojang has no record of Bedrock players).
 */
public final class PlayerLookupService {
    private static final String DEFAULT_BEDROCK_PREFIX = ".";

    private final Autorank plugin;

    public PlayerLookupService(Autorank plugin) {
        this.plugin = plugin;
    }

    /**
     * Synchronous, non-blocking resolution: online players and local cache only.
     * Returns empty if neither path yields a match. Safe to call from the main thread.
     */
    public Optional<UUID> resolveOnlineOrCached(String input) {
        if (input == null) return Optional.empty();
        String name = input.trim();
        if (name.isEmpty()) return Optional.empty();

        Player online = Bukkit.getPlayerExact(name);
        if (online != null) return Optional.of(online.getUniqueId());

        if (isBedrockEnabled()) {
            String prefix = getActiveBedrockPrefix();
            if (!prefix.isEmpty() && !name.startsWith(prefix)) {
                Player prefixed = Bukkit.getPlayerExact(prefix + name);
                if (prefixed != null) return Optional.of(prefixed.getUniqueId());
            }
        }

        UUID cached = plugin.getUUIDStorage().getStoredUUID(name);
        if (cached != null) return Optional.of(cached);

        return Optional.empty();
    }

    /**
     * Asynchronous full resolution. Tries the quick path first, then falls back
     * to network-based resolution: Floodgate's XUID service for Bedrock-prefixed
     * names, or Mojang for non-prefix names. Mojang is skipped entirely for names
     * matching the configured Bedrock prefix.
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
            return CompletableFuture.completedFuture(Optional.empty());
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                UUID uuid = UUIDManager.getUUID(name).get();
                return Optional.ofNullable(uuid);
            } catch (Exception e) {
                return Optional.empty();
            }
        });
    }

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

    public boolean isBedrockLikeName(String name) {
        if (name == null || name.isEmpty()) return false;
        if (!isBedrockEnabled()) return false;
        String prefix = getActiveBedrockPrefix();
        return !prefix.isEmpty() && name.startsWith(prefix);
    }

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
