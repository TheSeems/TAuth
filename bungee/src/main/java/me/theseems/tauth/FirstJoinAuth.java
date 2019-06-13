package me.theseems.tauth;

import me.theseems.tauth.events.TLoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FirstJoinAuth implements Listener {

    private static Set<UUID> players;

    public FirstJoinAuth() {
        players = new HashSet<>();
    }

    @EventHandler
    public void onLogin(ServerConnectEvent e) {
        UUID player = e.getPlayer().getUniqueId();

        if (!players.contains(e.getPlayer().getUniqueId())
                && TAuth.getAuthManager().isAutheticated(player) == LoginResponse.OK) {
            players.add(e.getPlayer().getUniqueId());
            Main.getServer().getPluginManager().callEvent(new TLoginEvent(e.getPlayer()));
        }
    }

    @EventHandler
    public void onExit(PlayerDisconnectEvent e) {
        players.remove(e.getPlayer().getUniqueId());
    }

    public static void add(UUID player) {
        players.add(player);
    }
}
