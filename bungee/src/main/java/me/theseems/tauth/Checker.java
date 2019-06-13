package me.theseems.tauth;

import me.theseems.tauth.utils.BungeeTitle;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class Checker implements Runnable {
    public static void display(UUID uuid, RegisterResponse response) {
        ProxiedPlayer player = Main.getServer().getPlayer(uuid);
        switch (response) {
            case REGISTERED:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent("Already registered"))
                                .subTitle(new TextComponent()));
                break;

            case INCORRECT:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent("How that even happened?"))
                                .subTitle(new TextComponent()));
                break;

            case OK:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent("Registered :)"))
                                .subTitle(new TextComponent()));
                break;

            default:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent(response.name()))
                                .subTitle(new TextComponent()));
                break;
        }
    }

    public static void display(UUID uuid, LoginResponse response) {
        ProxiedPlayer player = Main.getServer().getPlayer(uuid);
        switch (response) {
            case UNREGISTERED:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent("Register"))
                                .subTitle(new TextComponent("/register <pass> <pass>")));
                break;

            case INCORRECT:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent("How that even happened?"))
                                .subTitle(new TextComponent()));
                break;

            case OK:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent("Logined :)"))
                                .subTitle(new TextComponent())
                                .stay(20));
                break;

            case EXPIRED:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent("Please, log in"))
                                .subTitle(new TextComponent("/login <pass>")));
                break;

            case FORBIDDEN:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent("Wrong password"))
                                .subTitle(new TextComponent()));
                break;

            default:
                player.sendTitle(
                        new BungeeTitle()
                                .title(new TextComponent(response.name()))
                                .subTitle(new TextComponent()));
                break;
        }
    }

    @Override
    public void run() {
        for (ProxiedPlayer player : Main.getServer().getPlayers()) {
            LoginResponse response = TAuth.getAuthManager().isAutheticated(player.getUniqueId());
            if (response != LoginResponse.OK) {
                if (!TAuth.getSettings().getAuthServers().contains(player.getServer().getInfo().getName()))
                    player.connect(Main.getServer().getServerInfo(
                            TAuth.getSettings().getAuthServers().get(0)
                    ));

                display(player.getUniqueId(), response);
            }
        }
    }
}
