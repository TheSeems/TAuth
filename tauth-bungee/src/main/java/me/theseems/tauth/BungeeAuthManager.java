package me.theseems.tauth;

import me.theseems.tauth.events.TLoginEvent;

import java.util.UUID;

public class BungeeAuthManager extends TAuthManager {
  @Override
  boolean loginHookup(UUID player) {
    TLoginEvent event = new TLoginEvent(Main.getServer().getPlayer(player), false);

    Main.getServer()
      .getPluginManager()
      .callEvent(event);

    return event.isCancelled();
  }
}
