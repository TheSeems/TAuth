package me.theseems.tauth.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class TLoginEvent extends Event {
    private ProxiedPlayer player;
    private boolean auto;

    public TLoginEvent(ProxiedPlayer player, boolean auto) {
        this.player = player;
        this.auto = auto;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public boolean isAuto() {
        return auto;
    }
}
