package me.armar.plugins.utils.pluginlibrary.hooks;

import me.armar.plugins.utils.pluginlibrary.hooks.afkmanager.AFKManager;

import java.util.UUID;

// Stubbed: CMI plugin not used on Pinecraft Equestrian and CMI-API JAR not available locally.
public class CMIHook extends LibraryHook implements AFKManager {
    public CMIHook() {
    }

    public boolean isHooked() {
        return false;
    }

    public boolean hook() {
        return false;
    }

    public boolean isAFK(UUID uuid) {
        return false;
    }

    public boolean hasAFKData() {
        return true;
    }
}
