package me.theseems.tauth;

import java.util.Date;

public class TSession implements Session {
    private Date expire;
    private String ip;

    public TSession(Date expire, String ip) {
        this.expire = expire;
        this.ip = ip;
    }

    @Override
    public Date getExpire() {
        return expire;
    }

    @Override
    public String getIp() {
        return ip;
    }
}
