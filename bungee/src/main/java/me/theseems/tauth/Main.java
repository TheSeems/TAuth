package me.theseems.tauth;

import me.theseems.tauth.commands.LoginCommand;
import me.theseems.tauth.commands.LogoutCommand;
import me.theseems.tauth.commands.RegisterCommand;
import me.theseems.tauth.config.BungeeSettings;
import me.theseems.tauth.config.YamlSettings;
import me.theseems.tauth.db.JDBCDb;
import me.theseems.tauth.db.MemoDb;
import me.theseems.tauth.listeners.JoinListener;
import me.theseems.tauth.listeners.LoginTeleportListener;
import me.theseems.tauth.listeners.NextServerListener;
import me.theseems.tauth.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Plugin {
  private static ProxyServer server;
  private static BungeeSettings bungeeSettings;
  private static Logger logger;

  public static BungeeSettings getBungeeSettings() {
    return bungeeSettings;
  }

  public static ProxyServer getServer() {
    return server;
  }

  private void loadSettings() {
    try {
      File settings = Utils.loadResource(this, "config.yml");
      File messages = Utils.loadResource(this, "messages.yml");
      bungeeSettings = new YamlSettings(settings, messages);
    } catch (IOException e) {
      getLogger().warning("Error loading yaml settings");
      e.printStackTrace();
    }
  }

  public static void debug(String... msg) {
    if (bungeeSettings.getDebug())
      for (String message : msg) logger.log(Level.INFO, "[DEBUG] " + message);
  }

  @Override
  public void onEnable() {
    loadSettings();
    if (bungeeSettings == null) {
      getLogger().log(Level.OFF, "Failed to load settings");
      return;
    }

    server = this.getProxy();
    switch (bungeeSettings.getDbType()) {
      default:
        getLogger()
          .warning(
            "Unknown db provider given: '"
              + bungeeSettings.getDbType()
              + "'. Using MEMORY db instead");
        TAuth.setDb(new MemoDb());
        break;
      case "memo":
        getLogger()
          .warning(
            "Selecting MEMORY db. This may be bad if you have a lot of players. Consider using postgres or anything else");
        TAuth.setDb(new MemoDb());
        break;
      case "jdbc":
        getLogger().info("Using JDBC as AuthDb");
        TAuth.setDb(
          new JDBCDb(
            bungeeSettings.getDbUrl(),
            bungeeSettings.getDbUser(),
            bungeeSettings.getDbPassword()));
        break;
    }

    logger = getLogger();

    TAuth.setManager(new TAuthManager());
    TAuth.setServer(new BungeeAuthServer(this));
    TAuth.setSettings(bungeeSettings);
    TAuth.setHasher(bungeeSettings.getHasher());
    TAuth.setAuthBalancer(bungeeSettings.getAuthBalancer());
    TAuth.setNextBalancer(bungeeSettings.getNextBalancer());

    getLogger()
      .info(
        "Using "
          + getBungeeSettings().getAuthBalancer().getClass().getName()
          + " as auth pool balancer");
    getLogger()
      .info(
        "Using "
          + getBungeeSettings().getNextBalancer().getClass().getName()
          + " as next pool balancer");
    getLogger()
      .info(
        "Using "
          + getBungeeSettings().getHasher().getClass().getName()
          + " as password hasher");

    if (bungeeSettings.getNextServers() != null)
      TAuth.getNextBalancer().init(bungeeSettings.getNextServers());
    else
      // By default we return the player's current location
      TAuth.setNextBalancer(
        new AuthBalancer() {
          @Override
          public String getServer(UUID player) {
            return server.getPlayer(player).getServer().getInfo().getName();
          }

          @Override
          public void init(List<String> serverList) {
            getLogger().warning("Next pool balancer is not presented!");
          }
        });

    getProxy().getPluginManager().registerListener(this, new LoginTeleportListener());
    getProxy().getPluginManager().registerListener(this, new NextServerListener());
    getProxy().getPluginManager().registerListener(this, new JoinListener());

    getProxy().getPluginManager().registerCommand(this, new LoginCommand());
    getProxy().getPluginManager().registerCommand(this, new RegisterCommand());
    getProxy().getPluginManager().registerCommand(this, new LogoutCommand());

    getServer()
      .getScheduler()
      .schedule(this, new Checker(), 0, bungeeSettings.getCheckerPeriod(), TimeUnit.MILLISECONDS);
  }
}
