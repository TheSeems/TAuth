package me.theseems.tauth.config;

import me.theseems.tauth.Settings;
import net.md_5.bungee.api.Title;

public interface BungeeSettings extends Settings {
    /**
     * Get bungee checker delay
     *
     * @return delay
     */
    int getCheckerPeriod();

    /**
     * Get period retrieving server infos
     *
     * @return info
     */
    int getServerPeriod();

    /**
     * Get period in seconds after
     * When this period is expired, player will be kicked
     *
     * @return kick period
     */
    int getKickPeriod();

    /**
     * Get title from config
     *
     * @param id of title
     * @return title
     */
    Title getTitle(String id);

    /**
     * Get message from config
     *
     * @param id of message
     * @return message or empty array
     */
    String getMessage(String id);
}
