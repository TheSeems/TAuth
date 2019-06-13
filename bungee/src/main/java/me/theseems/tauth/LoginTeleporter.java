package me.theseems.tauth;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class LoginTeleporter implements Listener {

    @EventHandler
    public void onLogin(ServerConnectEvent e) {
        UUID player = e.getPlayer().getUniqueId();

        if (TAuth.getAuthManager().isAutheticated(player) != LoginResponse.OK) {
            ServerInfo info = Main.getServer().getServerInfo(
                    TAuth.getSettings().getAuthServers().get(0)
            );
            if (info != e.getTarget())
                e.setTarget(info);
        }
    }
}
