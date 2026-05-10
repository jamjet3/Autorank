package me.armar.plugins.autorank.permissions.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.permissions.PermissionsHandler;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsHandler extends PermissionsHandler {
    private LuckPerms luckPermsApi;

    public LuckPermsHandler(Autorank plugin) {
        super(plugin);
    }

    public boolean addGroup(Player player, String world, String group) {
        if (world != null) {
            this.getPlugin().getServer().dispatchCommand(this.getPlugin().getServer().getConsoleSender(), "lp user " + player.getName() + " parent add " + group + " global " + world);
        } else {
            Server var10000 = this.getPlugin().getServer();
            ConsoleCommandSender var10001 = this.getPlugin().getServer().getConsoleSender();
            String var10002 = player.getName();
            var10000.dispatchCommand(var10001, "lp user " + var10002 + " parent add " + group);
        }

        return true;
    }

    public boolean removeGroup(Player player, String world, String group) {
        if (world != null) {
            this.getPlugin().getServer().dispatchCommand(this.getPlugin().getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove " + group + " global " + world);
        } else {
            Server var10000 = this.getPlugin().getServer();
            ConsoleCommandSender var10001 = this.getPlugin().getServer().getConsoleSender();
            String var10002 = player.getName();
            var10000.dispatchCommand(var10001, "lp user " + var10002 + " parent remove " + group);
        }

        return true;
    }

    public Collection<String> getGroups() {
        return Collections.unmodifiableCollection((Collection)this.luckPermsApi.getGroupManager().getLoadedGroups().stream().map(Group::getName).collect(Collectors.toList()));
    }

    public String getName() {
        return "LuckPerms";
    }

    public Collection<String> getPlayerGroups(Player player) {
        User user = this.luckPermsApi.getUserManager().getUser(player.getUniqueId());
        return (Collection<String>)(user == null ? new ArrayList() : Collections.unmodifiableCollection((Collection)user.getDistinctNodes().parallelStream().filter((node) -> node instanceof InheritanceNode).map((node) -> ((InheritanceNode)node).getGroupName()).collect(Collectors.toList())));
    }

    public Collection<String> getWorldGroups(Player player, String world) {
        return this.getPlayerGroups(player);
    }

    public boolean replaceGroup(Player player, String world, String oldGroup, String newGroup) {
        return this.addGroup(player, world, newGroup) && this.removeGroup(player, world, oldGroup);
    }

    public boolean setupPermissionsHandler() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            this.luckPermsApi = provider.getProvider();
        }

        return this.luckPermsApi != null;
    }
}
