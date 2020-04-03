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

import static me.theseems.tauth.Main.debug;

public class JoinListener implements Listener {
  @EventHandler(priority = 0b1111111)
  public void onLogin(ServerConnectEvent e) {
    UUID player = e.getPlayer().getUniqueId();
    if (e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)
      && TAuth.getManager().isAutheticated(player) == LoginResponse.OK) {
      TLoginEvent event = new TLoginEvent(e.getPlayer(), true);
      Main.getServer().getPluginManager().callEvent(event);

      if (!event.isCancelled()) {
        Checker.display(player, LoginResponse.OK);
        debug("Auto logined " + player);
      } else {
        TAuth.getDb().clearSession(e.getPlayer().getUniqueId());
      }
    }
  }
}
