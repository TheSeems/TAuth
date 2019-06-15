package me.theseems.tauth;

import java.util.Date;

public interface Session {
    /**
     * Get expire of session
     * @return expire
     */
    Date getExpire();

    /**
     * Get ip of session
     * @return ip
     */
    String getIp();
}
