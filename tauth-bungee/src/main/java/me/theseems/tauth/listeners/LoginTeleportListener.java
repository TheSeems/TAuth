package me.theseems.tauth.listeners;

import me.theseems.tauth.Checker;
import me.theseems.tauth.LoginResponse;
import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import me.theseems.tauth.events.TLoginEvent;
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

  private boolean checkNick(String nick) {
    return nick.matches("^[a-zA-Z]\\w*$");
  }

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

  @EventHandler
  public void onLogin(ServerConnectEvent e) {
    if (e.isCancelled())
      return;

    UUID player = e.getPlayer().getUniqueId();
    if (!checkNick(e.getPlayer().getName())) {
      e.setCancelled(true);
      e.getPlayer().disconnect(new TextComponent("§7Недопустимый ник"));
      return;
    }

    if (e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)
            && TAuth.getManager().isAutheticated(player) == LoginResponse.OK) {
      debug("Auto logined " + player);
      Main.getServer().getPluginManager().callEvent(new TLoginEvent(e.getPlayer(), true, e));
      return;
    }

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
      ServerInfo info = Main.getServer().getServerInfo(TAuth.getAuthBalancer().getServer(player));
      System.out.println("Connecting to the auth " + info.getName());
      if (!info.equals(e.getTarget()))
        e.setTarget(info);
    }
  }
}
