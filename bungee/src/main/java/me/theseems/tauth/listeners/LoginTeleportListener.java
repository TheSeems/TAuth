package me.theseems.tauth.listeners;

import me.theseems.tauth.Checker;
import me.theseems.tauth.LoginResponse;
import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

import static me.theseems.tauth.Main.debug;

public class LoginTeleportListener implements Listener {

  @EventHandler
  public void onLogin(ServerConnectEvent e) {
    UUID player = e.getPlayer().getUniqueId();
    boolean joinAuth = TAuth.getSettings().getAuthServers().contains(e.getTarget().getName());
    LoginResponse response = TAuth.getManager().isAutheticated(player);

    debug(
        e.getPlayer().getUniqueId()
          + " to "
          + e.getTarget().getName()
          + " with "
          + response
          + " (JA = "
          + joinAuth
          + ")");

    if (response != LoginResponse.OK) {
      Checker.display(player, TAuth.getManager().isAutheticated(player));

      if (e.getPlayer().getServer() == null)
        e.setTarget(Main.getServer().getServerInfo(TAuth.getAuthBalancer().getServer(player)));
      else e.setCancelled(!joinAuth);

    } else if (!(Main.getBungeeSettings().getDebug() && e.getPlayer().hasPermission("tauth.admin"))
      && joinAuth) {

      ServerInfo to = Main.getServer().getServerInfo(TAuth.getNextBalancer().getServer(player));
      debug(
        "Balancer choose as next "
          + e.toString()
          + " for "
          + e.getPlayer().getName()
          + " ("
          + player
          + ")");

      ServerInfo current = e.getPlayer().getServer().getInfo();
      debug("[LTL] connecting to " + to);

      if (current == null || !to.getName().equals(current.getName())) e.setTarget(to);
      else e.setCancelled(true);
    }
  }
}
