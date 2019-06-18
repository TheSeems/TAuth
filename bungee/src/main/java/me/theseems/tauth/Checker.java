package me.theseems.tauth;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class Checker implements Runnable {
    private static Set<UUID> duplicate = new HashSet<>();
    private static Map<UUID, Date> joined = new HashMap<>();

    private static void displayLocal(UUID uuid, RegisterResponse response) {
        ProxiedPlayer player = Main.getServer().getPlayer(uuid);
        player.sendTitle(
                Main.getBungeeSettings().getTitle("register.titles." + response.name())
        );
    }

    private static void displayLocal(UUID uuid, LoginResponse response) {
        ProxiedPlayer player = Main.getServer().getPlayer(uuid);
        player.sendTitle(
                Main.getBungeeSettings().getTitle("login.titles." + response.name())
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

    private boolean checkJoined(ProxiedPlayer player, UUID uuid) {
        if (!joined.containsKey(uuid)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, Main.getBungeeSettings().getKickPeriod());
            joined.put(uuid, calendar.getTime());
        } else {
            Date date = joined.get(uuid);
            if (date.before(new Date())) {
                joined.remove(uuid);
                player.disconnect(new TextComponent(Main.getBungeeSettings().getMessage("kick.timed_out")));
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        for (ProxiedPlayer player : Main.getServer().getPlayers()) {
            UUID uuid = player.getUniqueId();

            if (duplicate.contains(uuid)) {
                duplicate.remove(uuid);
                continue;
            }

            LoginResponse response = TAuth.getManager().isAutheticated(uuid);
            if (response != LoginResponse.OK) {
                if (!checkJoined(player, uuid))
                    return;

                if (!Main.getBungeeSettings().getAuthServers().contains(player.getServer().getInfo().getName()))
                    player.connect(Main.getServer().getServerInfo(
                            TAuth.getAuthBalancer().getServer(uuid)
                    ));
                displayLocal(uuid, response);
            } else {
                joined.remove(uuid);
            }
        }
    }
}
