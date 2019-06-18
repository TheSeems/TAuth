package me.theseems.tauth;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BungeeAuthServer implements AuthServer, Runnable {
    private ProxyServer server;
    private Map<String, Integer> online;

    public BungeeAuthServer(Plugin plugin) {
        online = new HashMap<>();
        this.server = plugin.getProxy();
        server.getScheduler().schedule(plugin, this, 0, Main.getBungeeSettings().getServerPeriod(), TimeUnit.SECONDS);
    }

    @Override
    public String getIp(UUID player) {
        return server.getPlayer(player).getAddress().getHostName();
    }

    @Override
    public boolean isOnline(UUID player) {
        return server.getPlayer(player) != null && server.getPlayer(player).isConnected();
    }

    @Override
    public int getOnline(String server) {
        return online.getOrDefault(server, Integer.MAX_VALUE);
    }

    @Override
    public void run() {
        // Testing each server
        server.getConfig().getServers().forEach((s, serverInfo) -> serverInfo.ping((serverPing, throwable) -> {
            if (throwable == null)
                online.put(serverInfo.getName(), serverPing.getPlayers().getOnline());
            else {
                System.err.println("Error pinging server " + serverInfo.getName() + " => " + throwable.getLocalizedMessage());
                online.remove(serverInfo.getName());
            }
        }));
    }
}
