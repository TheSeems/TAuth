package me.theseems.tauth.listeners;

import me.theseems.tauth.Checker;
import me.theseems.tauth.LoginResponse;
import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class LoginTeleportListener implements Listener {

    @EventHandler
    public void onLogin(ServerConnectEvent e) {
        UUID player = e.getPlayer().getUniqueId();

        if (TAuth.getManager().isAutheticated(player) != LoginResponse.OK) {
            Checker.display(player, TAuth.getManager().isAutheticated(player));

            if (e.getPlayer().getServer() == null)
                e.setTarget(
                        Main.getServer()
                                .getServerInfo(TAuth.getAuthBalancer().getServer(player))
                );
            else
                e.setCancelled(true);
        }
    }
}
