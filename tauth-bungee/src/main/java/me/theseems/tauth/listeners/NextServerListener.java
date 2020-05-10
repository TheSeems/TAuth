package me.theseems.tauth.listeners;

import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import me.theseems.tauth.events.TLoginEvent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

import static me.theseems.tauth.Main.debug;

public class NextServerListener implements Listener {
  @EventHandler
  public void onLogin(TLoginEvent e) {
    debug("TLoginEvent => " + e.toString());
    UUID uuid = e.getPlayer().getUniqueId();
    String current =
      e.getPlayer().getServer() != null ? e.getPlayer().getServer().getInfo().getName() : null;
    // Force next check
    if (!Main.getBungeeSettings().getForceNext()) {
      debug("Passing...");
      return;
    }

    debug("Force connecting... (current = " + current + ")");

    ServerInfo info = Main.getServer().getServerInfo(TAuth.getNextBalancer().getServer(uuid));
    debug(
      "Balancer choose as next "
        + e.toString()
        + " for "
        + e.getPlayer().getName()
        + " ("
        + uuid
        + ") => "
        + info.getName());

    debug("Current = " + (e.getPlayer().getServer() == null ? "null" : e.getPlayer().getServer().getInfo().getName()) +  " need = " + info.getName());

    if (e.getEvent() == null) {
      System.out.println("Null event");
      e.getPlayer().connect(info);
    } else {
      System.out.println("Setting " + info);
      e.getEvent().setCancelled(false);
      e.getEvent().setTarget(info);
    }
  }
}
