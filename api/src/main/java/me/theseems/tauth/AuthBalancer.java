package me.theseems.tauth;

import java.util.List;
import java.util.UUID;

public interface AuthBalancer {
  /**
   * Get auth server for player
   *
   * @param player to get server for
   * @return server name
   */
  String getServer(UUID player);

  /**
   * Init balancer with server list
   *
   * @param serverList of servers
   */
  void init(List<String> serverList);
}
