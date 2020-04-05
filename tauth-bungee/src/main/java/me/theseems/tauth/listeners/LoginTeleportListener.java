package me.theseems.tauth.listeners;

import me.theseems.tauth.Checker;
import me.theseems.tauth.LoginResponse;
import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static me.theseems.tauth.Main.debug;

public class LoginTeleportListener implements Listener {

  @EventHandler(priority = Byte.MAX_VALUE)
  public void onPermissionEvent(PermissionCheckEvent e) {
    if (!(e.getSender() instanceof ProxiedPlayer)) {
      return;
    }

    LoginResponse response =
      TAuth.getManager().isAutheticated(((ProxiedPlayer) e.getSender()).getUniqueId());
    if (response != LoginResponse.OK) {
      e.setHasPermission(false);
    }
  }

  @EventHandler(priority = Byte.MAX_VALUE)
  public void on(ChatEvent e) {
    List<String> allowed = Arrays.asList("/register", "/login", "/l", "/log", "/reg");
    if (e.getSender() instanceof ProxiedPlayer) {
      LoginResponse response =
        TAuth.getManager().isAutheticated(((ProxiedPlayer) e.getSender()).getUniqueId());
      if (response != LoginResponse.OK) {
        boolean shouldCancel =
          !(e.isCommand() && allowed.stream().anyMatch((a) -> e.getMessage().startsWith(a)));
        if (shouldCancel) {
          ((ProxiedPlayer) e.getSender())
            .sendMessage(
              ChatMessageType.ACTION_BAR,
              new TextComponent(Main.getBungeeSettings().getMessage("on.command")));
          e.setCancelled(true);
        }
      }
    }
  }

  @EventHandler(priority = Byte.MAX_VALUE)
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

      ServerInfo current =
        e.getPlayer().getServer() == null ? null : e.getPlayer().getServer().getInfo();
      debug("[LTL] connecting to " + to);

      if (current == null || !to.getName().equals(current.getName())) e.setTarget(to);
      else e.setCancelled(true);
    }
  }
}
