package me.armar.plugins.autorank.util.uuid;

import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Soft-dependency wrapper around the Floodgate API.
 *
 * All references to {@code org.geysermc.floodgate.api.*} classes are confined
 * to method bodies and guarded by a plugin-presence check, so the JVM never
 * loads Floodgate classes when the Floodgate plugin is absent. This keeps
 * Autorank functional on servers without Floodgate installed.
 */
public final class FloodgateHook {
    private static final boolean PLUGIN_LOADED =
            Bukkit.getPluginManager().getPlugin("floodgate") != null;

    private FloodgateHook() {
    }

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
     * Returns false for offline UUIDs even if they belong to a Bedrock account.
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
     * Returns the Bedrock username prefix configured in Floodgate, if available.
     * Floodgate's default is a single dot. Returns empty when Floodgate is absent.
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
     * Asynchronously resolves a Bedrock gamertag to a Java/Floodgate UUID via
     * the Geyser global XUID service. Performs network I/O — never join() this
     * on the main thread.
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
