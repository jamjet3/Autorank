package me.armar.plugins.autorank.util.uuid;

import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Soft-dependency wrapper around the Floodgate API.
 *
 * All references to {@code org.geysermc.floodgate.api.*} classes are confined
 * to method bodies and guarded by a plugin-presence check. The JVM will not
 * attempt to load Floodgate classes unless the Floodgate plugin is installed,
 * so Autorank runs cleanly on servers without it.
 *
 * <p>Floodgate API capability summary (verified against GeyserMC/Floodgate
 * master, API artifact 2.2.5-SNAPSHOT):
 * <ul>
 *   <li>{@code isFloodgatePlayer(UUID)} and {@code getPlayer(UUID)} are
 *       <b>online-only</b>. They return false / null for offline UUIDs even
 *       if the UUID belongs to a Bedrock account.</li>
 *   <li>{@code getPlayerPrefix()} returns the prefix configured in Floodgate's
 *       own config.yml. Safe to call from the main thread.</li>
 *   <li>{@code getUuidFor(gamertag)} returns a {@code CompletableFuture<UUID>}
 *       backed by an external Geyser HTTP service. It works for offline
 *       Bedrock players who have an Xbox Live account, but must not be
 *       {@code .join()}-ed on the main thread.</li>
 * </ul>
 */
public final class FloodgateHook {
    private static final boolean PLUGIN_LOADED =
            Bukkit.getPluginManager().getPlugin("floodgate") != null;

    private FloodgateHook() {
    }

    /**
     * Whether the Floodgate plugin is present and its API is accessible.
     */
    public static boolean isAvailable() {
        if (!PLUGIN_LOADED) return false;
        try {
            return org.geysermc.floodgate.api.FloodgateApi.getInstance() != null;
        } catch (LinkageError | RuntimeException e) {
            return false;
        }
    }

    /**
     * Whether the player currently online with this UUID is a Bedrock player.
     * Returns false for offline UUIDs (Floodgate's API is online-only here).
     */
    public static boolean isBedrockPlayer(UUID uuid) {
        if (uuid == null || !PLUGIN_LOADED) return false;
        try {
            return org.geysermc.floodgate.api.FloodgateApi.getInstance().isFloodgatePlayer(uuid);
        } catch (LinkageError | RuntimeException e) {
            return false;
        }
    }

    /**
     * Returns the Bedrock username prefix configured in Floodgate. Empty when
     * Floodgate is not installed or the call fails.
     */
    public static Optional<String> getConfiguredPrefix() {
        if (!PLUGIN_LOADED) return Optional.empty();
        try {
            String prefix = org.geysermc.floodgate.api.FloodgateApi.getInstance().getPlayerPrefix();
            return Optional.ofNullable(prefix);
        } catch (LinkageError | RuntimeException e) {
            return Optional.empty();
        }
    }

    /**
     * Asynchronously resolves a Bedrock gamertag (without prefix) to a
     * Java/Floodgate UUID via Floodgate's external XUID service. Performs
     * network I/O. Returns empty when Floodgate is absent or the gamertag
     * cannot be resolved.
     */
    public static CompletableFuture<Optional<UUID>> resolveBedrockUuidAsync(String gamertag) {
        if (gamertag == null || gamertag.isEmpty() || !PLUGIN_LOADED) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        try {
            return org.geysermc.floodgate.api.FloodgateApi.getInstance()
                    .getUuidFor(gamertag)
                    .handle((uuid, ex) -> Optional.ofNullable(ex == null ? uuid : null));
        } catch (LinkageError | RuntimeException e) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }
}
