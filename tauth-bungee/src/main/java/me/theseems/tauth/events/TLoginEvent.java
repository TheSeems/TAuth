package me.theseems.tauth.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Event;

public class TLoginEvent extends Event {
  private ProxiedPlayer player;
  private boolean auto;
  private ServerConnectEvent event;

  public TLoginEvent(ProxiedPlayer player, boolean auto, ServerConnectEvent event) {
    this.player = player;
    this.auto = auto;
    this.event = event;
  }

  public ServerConnectEvent getEvent() {
    return event;
  }

  public ProxiedPlayer getPlayer() {
    return player;
  }

  public boolean isAuto() {
    return auto;
  }

  @Override
  public String toString() {
    return "TLoginEvent{" + "player=" + player + ", auto=" + auto + '}';
  }
}
