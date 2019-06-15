package me.theseems.tauth;

import me.theseems.tauth.balancers.SimpleBalancer;
import me.theseems.tauth.commands.LoginCommand;
import me.theseems.tauth.commands.LogoutCommand;
import me.theseems.tauth.commands.RegisterCommand;
import me.theseems.tauth.listeners.FirstJoinListener;
import me.theseems.tauth.listeners.LoginTeleportListener;
import me.theseems.tauth.listeners.NextServerListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin {
    private static ProxyServer server;

    @Override
    public void onEnable() {
        server = this.getProxy();
        TAuth.setDb(new MemAuthDB());
        TAuth.setHasher(new SHA512AuthHasher());
        TAuth.setManager(new TAuthManager());
        TAuth.setServer(new AuthServer() {
            @Override
            public String getIp(UUID player) {
                return server.getPlayer(player).getAddress().getHostName();
            }

            @Override
            public boolean isOnline(UUID player) {
                return server.getPlayer(player) == null || server.getPlayer(player).isConnected();
            }

            @Override
            public int getOnline(String server) {
                return getServer().getServerInfo(server).getPlayers().size();
            }
        });

        // TODO: make config file to parse
        // TODO: make configuration for custom settings such as checker delay

        TAuth.setSettings(new Settings() {
            @Override
            public List<String> getAuthServers() {
                return Collections.singletonList("limbo");
            }

            @Override
            public long getExpireMils() {
                // 2 hours
                return 2 * 3600 * 1000;
            }

            @Override
            public List<String> getNextServers() {
                return Collections.singletonList("instance");
            }
        });

        TAuth.setAuthBalancer(new SimpleBalancer(TAuth.getSettings().getAuthServers()));
        TAuth.setNextBalancer(new SimpleBalancer(TAuth.getSettings().getNextServers()));

        getProxy().getPluginManager().registerListener(this, new LoginTeleportListener());
        getProxy().getPluginManager().registerListener(this, new NextServerListener());
        getProxy().getPluginManager().registerListener(this, new FirstJoinListener());

        getProxy().getPluginManager().registerCommand(this, new LoginCommand());
        getProxy().getPluginManager().registerCommand(this, new RegisterCommand());
        getProxy().getPluginManager().registerCommand(this, new LogoutCommand());

        getServer().getScheduler().schedule(this, new Checker(), 0, 5000, TimeUnit.MILLISECONDS);
    }

    public static ProxyServer getServer() {
        return server;
    }
}
