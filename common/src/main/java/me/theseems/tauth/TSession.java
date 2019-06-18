package me.theseems.tauth;

import java.time.LocalDateTime;

public class TSession implements Session {
    private LocalDateTime expire;
    private String ip;

    public TSession(LocalDateTime expire, String ip) {
        this.expire = expire;
        this.ip = ip;
    }

    @Override
    public LocalDateTime getExpire() {
        return expire;
    }

    @Override
    public String getIp() {
        return ip;
    }
}
