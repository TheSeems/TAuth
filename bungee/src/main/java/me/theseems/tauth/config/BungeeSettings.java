package me.theseems.tauth.config;

import me.theseems.tauth.Settings;

public interface BungeeSettings extends Settings {
    /**
     * Get bungee checker delay
     *
     * @return delay
     */
    int getCheckerPeriod();
}
