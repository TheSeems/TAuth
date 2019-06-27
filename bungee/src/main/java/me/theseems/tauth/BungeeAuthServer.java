package me.theseems.tauth;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BungeeAuthServer implements AuthServer, Runnable {
  private ProxyServer server;
  private Map<String, Integer> online;

  public BungeeAuthServer(Plugin plugin) {
    online = new ConcurrentHashMap<>();
    this.server = plugin.getProxy();
    server
      .getScheduler()
      .schedule(plugin, this, 0, Main.getBungeeSettings().getServerPeriod(), TimeUnit.SECONDS);
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
    return online.computeIfAbsent(server, s -> Integer.MAX_VALUE);
  }

  @Override
  public void run() {
    // Testing each server
    online.forEach(
      (s, integer) -> {
        ServerInfo info = server.getServerInfo(s);
        if (info == null) {
          System.err.println("Got a query on a server '" + s + "' which does not exist");
          online.remove(s);
        } else {
          info.ping(
            (serverPing, throwable) -> {
              if (throwable != null) {
                System.err.println(
                  "Error pinging " + s + " => " + throwable.getLocalizedMessage());
                online.remove(s);
              } else {
                online.put(s, serverPing.getPlayers().getOnline());
              }
                });
        }
        });
  }
}
