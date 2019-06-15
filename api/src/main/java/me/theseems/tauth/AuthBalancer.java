package me.theseems.tauth;

import java.util.UUID;

public interface AuthBalancer {
    /**
     * Get auth server for player
     *
     * @param player to get server for
     * @return server name
     */
    String getServer(UUID player);
}
