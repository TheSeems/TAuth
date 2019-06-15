package me.theseems.tauth.listeners;

import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import me.theseems.tauth.events.TLoginEvent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class NextServerListener implements Listener {
    @EventHandler(priority = 0xf)
    public void onLogin(TLoginEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        FirstJoinListener.add(uuid);

        if (e.getPlayer().getServer() == null)
            return;

        ServerInfo info = Main.getServer()
                .getServerInfo(TAuth.getNextBalancer().getServer(uuid));

        if (!e.getPlayer().getServer().getInfo().getName().equals(info.getName()))
            e.getPlayer().connect(info);
    }
}
