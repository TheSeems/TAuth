package me.theseems.tauth;

import me.theseems.tauth.utils.BungeeTitle;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Checker implements Runnable {
    private static Set<UUID> duplicate = new HashSet<>();

    private static void displayLocal(UUID uuid, RegisterResponse response) {
        ProxiedPlayer player = Main.getServer().getPlayer(uuid);
        String title;
        String subtitle = null;

        switch (response) {
            case REGISTERED:
                title = "Already registered";
                break;

            case INCORRECT:
                title = "Contact developer";
                subtitle = "seemsthe@gmail.com";
                break;

            case OK:
                title = "You are registered";
                break;

            default:
                title = response.name();
                break;
        }

        player.sendTitle(
                new BungeeTitle()
                        .title(new TextComponent(title))
                        .subTitle(new TextComponent(subtitle == null ? "" : subtitle))
        );
    }

    private static void displayLocal(UUID uuid, LoginResponse response) {
        duplicate.add(uuid);

        ProxiedPlayer player = Main.getServer().getPlayer(uuid);
        String title = null;
        String subtitle = null;

        switch (response) {
            case UNREGISTERED:
                title = "Please, register";
                subtitle = "/register <pass> <pass>";
                break;

            case INCORRECT:
                title = "Contact developer";
                subtitle = "seemsthe@gmail.com";
                break;

            case OK:
                title = "You are logged in";
                break;

            case EXPIRED:
                title = "Please, log in";
                subtitle = "/login <pass>";
                break;

            case FORBIDDEN:
                subtitle = "Wrong password";
                break;

            default:
                title = response.name();
                break;
        }

        player.sendTitle(
                new BungeeTitle()
                        .title(new TextComponent(title != null ? title : ""))
                        .subTitle(new TextComponent(subtitle != null ? subtitle : ""))
                        .stay(15)
        );
    }

    public static void display(UUID uuid, RegisterResponse response) {
        duplicate.add(uuid);
        displayLocal(uuid, response);
    }

    public static void display(UUID uuid, LoginResponse response) {
        duplicate.add(uuid);
        displayLocal(uuid, response);
    }

    @Override
    public void run() {
        for (ProxiedPlayer player : Main.getServer().getPlayers()) {
            if (duplicate.contains(player.getUniqueId())) {
                duplicate.remove(player.getUniqueId());
                continue;
            }

            LoginResponse response = TAuth.getManager().isAutheticated(player.getUniqueId());
            if (response != LoginResponse.OK) {
                player.connect(Main.getServer().getServerInfo(
                        TAuth.getAuthBalancer().getServer(player.getUniqueId())
                ));
                displayLocal(player.getUniqueId(), response);
            }
        }
    }
}
