package me.theseems.tauth.listeners;

import me.theseems.tauth.Checker;
import me.theseems.tauth.LoginResponse;
import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import me.theseems.tauth.events.TLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class JoinListener implements Listener {
  @EventHandler
  public void onLogin(ServerConnectEvent e) {
    UUID player = e.getPlayer().getUniqueId();

    if (e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)
      && TAuth.getManager().isAutheticated(player) == LoginResponse.OK) {
      Checker.display(player, LoginResponse.OK);
      Main.getServer().getPluginManager().callEvent(new TLoginEvent(e.getPlayer(), true));
    }
  }
}
