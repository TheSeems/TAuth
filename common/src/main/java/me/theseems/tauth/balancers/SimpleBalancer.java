package me.theseems.tauth.balancers;

import me.theseems.tauth.AuthBalancer;
import me.theseems.tauth.TAuth;

import java.util.List;
import java.util.UUID;

public class SimpleBalancer implements AuthBalancer {
    String balanced;
    List<String> servers;

    public SimpleBalancer(List<String> servers) {
        this.servers = servers;
    }

    @Override
    public String getServer(UUID player) {
        update();
        return balanced;
    }

    void update() {
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
