package me.theseems.tauth;

import me.theseems.tauth.commands.LoginCommand;
import me.theseems.tauth.commands.LogoutCommand;
import me.theseems.tauth.commands.RegisterCommand;
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
        TAuth.setAuthManager(new TAuthManager());
        TAuth.setAuthServer(new AuthServer() {
            @Override
            public String getIp(UUID player) {
                return server.getPlayer(player).getAddress().getHostName();
            }

            @Override
            public boolean isOnline(UUID player) {
                return server.getPlayer(player) == null || server.getPlayer(player).isConnected();
            }
        });

        TAuth.setSettings(new Settings() {
            @Override
            public List<String> getAuthServers() {
                return Collections.singletonList("limbo");
            }

            @Override
            public long getExpireMils() {
                // 2 hours
                return 1000 * 3600 * 2;
            }

            @Override
            public String getNextServer() {
                return "instance";
            }
        });

        getProxy().getPluginManager().registerListener(this, new LoginTeleporter());
        getProxy().getPluginManager().registerListener(this, new NextTeleporter());
        getProxy().getPluginManager().registerListener(this, new FirstJoinAuth());

        getProxy().getPluginManager().registerCommand(this, new LoginCommand());
        getProxy().getPluginManager().registerCommand(this, new RegisterCommand());
        getProxy().getPluginManager().registerCommand(this, new LogoutCommand());

        getServer().getScheduler().schedule(this, new Checker(), 0, 4000, TimeUnit.MILLISECONDS);
    }

    public static ProxyServer getServer() {
        return server;
    }
}
