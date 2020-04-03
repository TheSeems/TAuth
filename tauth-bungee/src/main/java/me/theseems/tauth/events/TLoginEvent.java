package me.theseems.tauth.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class TLoginEvent extends Event implements Cancellable {
  private ProxiedPlayer player;
  private boolean auto;
  private boolean isCancelled;

  public TLoginEvent(ProxiedPlayer player, boolean auto) {
    this.player = player;
    this.auto = auto;
    this.isCancelled = false;
  }

  public ProxiedPlayer getPlayer() {
    return player;
  }

  public boolean isAuto() {
    return auto;
  }

  @Override
  public String toString() {
    return "TLoginEvent{" +
      "player=" + player +
      ", auto=" + auto +
      '}';
  }

  @Override
  public boolean isCancelled() {
    return isCancelled;
  }

  @Override
  public void setCancelled(boolean b) {
    isCancelled = b;
  }
}
