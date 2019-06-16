package me.theseems.tauth;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.UUID;

public class BungeeAuthServer implements AuthServer {
    private ProxyServer server;

    public BungeeAuthServer(ProxyServer server) {
        this.server = server;
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
        ServerInfo info = this.server.getServerInfo(server);
        if (info == null)
            return Integer.MAX_VALUE;
        else
            return info.getPlayers().size();
    }
}
