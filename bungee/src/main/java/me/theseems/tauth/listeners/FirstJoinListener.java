package me.theseems.tauth.listeners;

import me.theseems.tauth.Checker;
import me.theseems.tauth.LoginResponse;
import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import me.theseems.tauth.events.TLoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FirstJoinListener implements Listener {
    private static Set<UUID> players = new HashSet<>();

    static void add(UUID player) {
        players.add(player);
    }

    @EventHandler
    public void onExit(PlayerDisconnectEvent e) {
        players.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLogin(ServerConnectEvent e) {
        UUID player = e.getPlayer().getUniqueId();

        if (!players.contains(player) && TAuth.getManager().isAutheticated(player) == LoginResponse.OK) {
            players.add(player);
            Checker.display(player, LoginResponse.OK);
            Main.getServer().getPluginManager().callEvent(new TLoginEvent(e.getPlayer(), true));
        }
    }
}
