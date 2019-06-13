package me.theseems.tauth;

import java.util.UUID;

public interface AuthServer {
    /**
     * Get ip of player
     * @param player to get ip of
     * @return ip
     */
    String getIp(UUID player);

    /**
     * Check whether player is online
     * @param player to check
     * @return result
     */
    boolean isOnline(UUID player);
}
