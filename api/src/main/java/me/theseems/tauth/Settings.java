package me.theseems.tauth;

import java.util.List;

public interface Settings {
    /**
     * Get auth servers
     * @return cluster
     */
    List<String> getAuthServers();

    /**
     * Get mils before player's session expired
     * @return mils
     */
    long getExpireMils();

    /**
     * Get next servers
     * @return next servers
     */
    List<String> getNextServers();
}
