package me.theseems.tauth;

import me.theseems.tauth.balancers.SimpleBalancer;
import me.theseems.tauth.commands.LoginCommand;
import me.theseems.tauth.commands.LogoutCommand;
import me.theseems.tauth.commands.RegisterCommand;
import me.theseems.tauth.config.BungeeSettings;
import me.theseems.tauth.config.YamlSettings;
import me.theseems.tauth.listeners.FirstJoinListener;
import me.theseems.tauth.listeners.LoginTeleportListener;
import me.theseems.tauth.listeners.NextServerListener;
import me.theseems.tauth.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Main extends Plugin {
    private static ProxyServer server;
    private static BungeeSettings bungeeSettings;

    public static BungeeSettings getBungeeSettings() {
        return bungeeSettings;
    }

    public static ProxyServer getServer() {
        return server;
    }

    private void loadSettings() {
        try {
            File file = Utils.loadResource(this, "config.yml");
            bungeeSettings = new YamlSettings(file);
        } catch (IOException e) {
            getLogger().warning("Error loading yaml settings");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        loadSettings();
        if (bungeeSettings == null) {
            getLogger().log(Level.OFF, "Failed to load settings");
            return;
        }

        server = this.getProxy();
        TAuth.setDb(new MemAuthDB());
        TAuth.setHasher(new SHA512AuthHasher());
        TAuth.setManager(new TAuthManager());
        TAuth.setServer(new BungeeAuthServer(server));
        TAuth.setSettings(bungeeSettings);

        TAuth.setAuthBalancer(new SimpleBalancer(bungeeSettings.getAuthServers()));

        if (bungeeSettings.getNextServers() != null)
            TAuth.setNextBalancer(new SimpleBalancer(bungeeSettings.getNextServers()));
        else
            // By default we return the player's current location
            TAuth.setNextBalancer(player -> server.getPlayer(player).getServer().getInfo().getName());

        getProxy().getPluginManager().registerListener(this, new LoginTeleportListener());
        getProxy().getPluginManager().registerListener(this, new NextServerListener());
        getProxy().getPluginManager().registerListener(this, new FirstJoinListener());

        getProxy().getPluginManager().registerCommand(this, new LoginCommand());
        getProxy().getPluginManager().registerCommand(this, new RegisterCommand());
        getProxy().getPluginManager().registerCommand(this, new LogoutCommand());

        getServer().getScheduler().schedule(this, new Checker(), 0, bungeeSettings.getCheckerPeriod(), TimeUnit.MILLISECONDS);
    }
}
