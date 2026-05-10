package me.armar.plugins.autorank.permissions.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.permissions.PermissionsHandler;
import me.armar.plugins.utils.pluginlibrary.Library;
import me.armar.plugins.utils.pluginlibrary.hooks.LibraryHook;
import me.armar.plugins.utils.pluginlibrary.hooks.VaultHook;
import org.bukkit.entity.Player;

public class VaultPermissionsHandler extends PermissionsHandler {
    public VaultPermissionsHandler(Autorank plugin) {
        super(plugin);
    }

    public boolean addGroup(Player player, String world, String group) {
        LibraryHook hook = this.getPlugin().getDependencyManager().getLibraryHook(Library.VAULT).orElse(null);
        if (hook != null && hook.isHooked()) {
            return VaultHook.getPermissions() != null && VaultHook.getPermissions().playerAddGroup(world, player, group);
        } else {
            return false;
        }
    }

    public boolean demotePlayer(Player player, String world, String groupFrom, String groupTo) {
        LibraryHook hook = this.getPlugin().getDependencyManager().getLibraryHook(Library.VAULT).orElse(null);
        if (hook != null && hook.isHooked()) {
            if (VaultHook.getPermissions() == null) {
                return false;
            } else {
                if (world == null && VaultHook.getPermissions().getName().toLowerCase().contains("bpermissions")) {
                    world = player.getWorld().getName();
                }

                for(String group : this.getPlayerGroups(player)) {
                    Autorank var10000 = this.getPlugin();
                    String var10001 = player.getName();
                    var10000.debugMessage("Group of " + var10001 + " before removing: " + group);
                }

                boolean worked1 = this.removeGroup(player, world, groupFrom);
                boolean worked2 = false;
                if (worked1) {
                    for(String group : this.getPlayerGroups(player)) {
                        Autorank var14 = this.getPlugin();
                        String var15 = player.getName();
                        var14.debugMessage("Group of " + var15 + " after removing: " + group);
                    }

                    worked2 = this.addGroup(player, world, groupTo);
                }

                return worked1 && worked2;
            }
        } else {
            return false;
        }
    }

    public Collection<String> getGroups() {
        List<String> groups = new ArrayList();
        LibraryHook hook = this.getPlugin().getDependencyManager().getLibraryHook(Library.VAULT).orElse(null);
        if (hook != null && hook.isHooked()) {
            if (VaultHook.getPermissions() == null) {
                return Collections.unmodifiableCollection(groups);
            } else {
                groups.addAll(Arrays.asList(VaultHook.getPermissions().getGroups()));
                return Collections.unmodifiableCollection(groups);
            }
        } else {
            return Collections.unmodifiableCollection(groups);
        }
    }

    public String getName() {
        return VaultHook.getPermissions().getName();
    }

    public Collection<String> getPlayerGroups(Player player) {
        List<String> groups = new ArrayList();
        LibraryHook hook = this.getPlugin().getDependencyManager().getLibraryHook(Library.VAULT).orElse(null);
        if (hook != null && hook.isHooked()) {
            if (VaultHook.getPermissions() == null) {
                return Collections.unmodifiableCollection(groups);
            } else {
                if (this.getPlugin().getSettingsConfig().onlyUsePrimaryGroupVault()) {
                    groups.add(VaultHook.getPermissions().getPrimaryGroup(player));
                } else {
                    groups.addAll(Arrays.asList(VaultHook.getPermissions().getPlayerGroups(player)));
                }

                return Collections.unmodifiableCollection(groups);
            }
        } else {
            return Collections.unmodifiableCollection(groups);
        }
    }

    public Collection<String> getWorldGroups(Player player, String world) {
        List<String> groups = new ArrayList();
        LibraryHook hook = this.getPlugin().getDependencyManager().getLibraryHook(Library.VAULT).orElse(null);
        if (hook != null && hook.isHooked()) {
            if (VaultHook.getPermissions() == null) {
                return Collections.unmodifiableCollection(groups);
            } else {
                groups.addAll(Arrays.asList(VaultHook.getPermissions().getPlayerGroups(world, player.getName())));
                return Collections.unmodifiableCollection(groups);
            }
        } else {
            return Collections.unmodifiableCollection(groups);
        }
    }

    public boolean removeGroup(Player player, String world, String group) {
        LibraryHook hook = this.getPlugin().getDependencyManager().getLibraryHook(Library.VAULT).orElse(null);
        if (hook != null && hook.isHooked()) {
            return VaultHook.getPermissions() != null && VaultHook.getPermissions().playerRemoveGroup(world, player, group);
        } else {
            return false;
        }
    }

    public boolean replaceGroup(Player player, String world, String oldGroup, String newGroup) {
        LibraryHook hook = this.getPlugin().getDependencyManager().getLibraryHook(Library.VAULT).orElse(null);
        if (hook != null && hook.isHooked()) {
            if (VaultHook.getPermissions() == null) {
                return false;
            } else {
                if (world == null && VaultHook.getPermissions().getName().toLowerCase().contains("bpermissions")) {
                    world = player.getWorld().getName();
                }

                Collection<String> groupsBeforeAdd = this.getPlayerGroups(player);

                for(String group : groupsBeforeAdd) {
                    Autorank var10000 = this.getPlugin();
                    String var10001 = player.getName();
                    var10000.debugMessage("Group of " + var10001 + " before adding: " + group);
                }

                boolean worked1 = this.addGroup(player, world, newGroup);
                boolean worked2 = false;
                if (worked1) {
                    Collection<String> groupsAfterAdd = this.getPlayerGroups(player);

                    for(String group : groupsAfterAdd) {
                        Autorank var16 = this.getPlugin();
                        String var17 = player.getName();
                        var16.debugMessage("Group of " + var17 + " after adding: " + group);
                    }

                    if (VaultHook.getPermissions().getName().toLowerCase().contains("permissionsex")) {
                        if (groupsAfterAdd.size() >= groupsBeforeAdd.size() + 1) {
                            worked2 = this.removeGroup(player, world, oldGroup);
                        } else if (groupsAfterAdd.size() == 1) {
                            for(String group : groupsBeforeAdd) {
                                if (!group.equalsIgnoreCase(oldGroup)) {
                                    this.addGroup(player, world, group);
                                }
                            }

                            worked2 = true;
                        } else {
                            worked2 = true;
                        }
                    } else {
                        worked2 = this.removeGroup(player, world, oldGroup);
                    }
                }

                return worked1 && worked2;
            }
        } else {
            return false;
        }
    }

    public boolean setupPermissionsHandler() {
        return this.getPlugin().getDependencyManager().getLibraryHook(Library.VAULT).map(LibraryHook::isHooked).orElse(false);
    }
}
