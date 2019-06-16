package me.theseems.tauth;

import java.util.List;

public interface Settings {
    /**
     * Get auth servers
     * @return cluster
     */
    List<String> getAuthServers();

    /**
     * Get seconds before player's session expired
     * @return seconds
     */
    Integer getExpireSeconds();

    /**
     * Get next servers
     * @return next servers
     */
    List<String> getNextServers();
}
