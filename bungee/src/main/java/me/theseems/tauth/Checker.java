package me.theseems.tauth;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class Checker implements Runnable {
  private static Set<UUID> duplicate = new HashSet<>();
  private static Map<UUID, Date> kick = new HashMap<>();

  private static void displayLocal(UUID uuid, RegisterResponse response) {
    ProxiedPlayer player = Main.getServer().getPlayer(uuid);
    player.sendTitle(Main.getBungeeSettings().getTitle("register.titles." + response.name()));
  }

  private static void displayLocal(UUID uuid, LoginResponse response) {
    ProxiedPlayer player = Main.getServer().getPlayer(uuid);
    player.sendTitle(Main.getBungeeSettings().getTitle("login.titles." + response.name()));
  }

  public static void display(UUID uuid, RegisterResponse response) {
    duplicate.add(uuid);
    displayLocal(uuid, response);
  }

  public static void display(UUID uuid, LoginResponse response) {
    duplicate.add(uuid);
    displayLocal(uuid, response);
  }

  private boolean checkForKick(ProxiedPlayer player, UUID uuid) {
    if (!kick.containsKey(uuid)) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.SECOND, Main.getBungeeSettings().getKickPeriod());
      kick.put(uuid, calendar.getTime());
    } else {
      Date date = kick.get(uuid);
      if (date.before(new Date())) {
        kick.remove(uuid);
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
        if (!checkForKick(player, uuid)) return;

        if (player.getServer() == null
                || !Main.getBungeeSettings()
                .getAuthServers()
                .contains(player.getServer().getInfo().getName())) {
          String to = TAuth.getAuthBalancer().getServer(uuid);
          player.connect(Main.getServer().getServerInfo(to));
        }

        displayLocal(uuid, response);
      } else {
        kick.remove(uuid);
      }
    }
  }
}
