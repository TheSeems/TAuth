package me.theseems.tauth.balancers;

import me.theseems.tauth.AuthBalancer;
import me.theseems.tauth.TAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleBalancer implements AuthBalancer {
  public String balanced;
  public List<String> servers;

  public SimpleBalancer() {
    this.servers = new ArrayList<>();
  }

  @Override
  public String getServer(UUID player) {
    update();
    return balanced;
  }

  @Override
  public void init(List<String> serverList) {
    this.servers = serverList;
  }

  public void update() {
    if (servers.size() == 0) return;

    balanced = servers.get(0);
    int lowest = TAuth.getServer().getOnline(servers.get(0));
    for (String auth : servers) {
      int online = TAuth.getServer().getOnline(auth);
      if (online < lowest) {
        balanced = auth;
        lowest = online;
      }
    }
  }
}
