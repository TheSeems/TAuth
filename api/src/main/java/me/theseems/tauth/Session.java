package me.theseems.tauth;

import java.time.LocalDateTime;

public interface Session {
    /**
     * Get expire of session
     *
     * @return expire
     */
    LocalDateTime getExpire();

    /**
     * Get ip of session
     *
     * @return ip
     */
    String getIp();
}
