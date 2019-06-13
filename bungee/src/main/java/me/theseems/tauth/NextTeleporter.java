package me.theseems.tauth;

import me.theseems.tauth.events.TLoginEvent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class NextTeleporter implements Listener {
    @EventHandler(priority = 0xf)
    public void onLogin(TLoginEvent e) {
        FirstJoinAuth.add(e.getPlayer().getUniqueId());
        ServerInfo info = Main.getServer().getServerInfo(
                TAuth.getSettings().getNextServer()
        );

        if (e.getPlayer().getServer() != null
                && !e.getPlayer().getServer().getInfo().getName().equals(info.getName()))
            e.getPlayer().connect(info);
    }
}
