package me.theseems.tauth;

import java.util.Date;
import java.util.UUID;

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
