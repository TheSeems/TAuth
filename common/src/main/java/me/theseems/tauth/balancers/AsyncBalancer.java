package me.theseems.tauth.balancers;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class AsyncBalancer extends SimpleBalancer {
    public AsyncBalancer(List<String> servers, long period) {
        super(servers);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, period, period);
    }

    @Override
    public String getServer(UUID player) {
        return super.balanced == null ? super.servers.get(0) : super.balanced;
    }
}
