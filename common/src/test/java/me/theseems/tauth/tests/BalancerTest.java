package me.theseems.tauth.tests;

import me.theseems.tauth.AuthBalancer;
import me.theseems.tauth.AuthServer;
import me.theseems.tauth.TAuth;
import me.theseems.tauth.balancers.AsyncBalancer;
import me.theseems.tauth.balancers.SimpleBalancer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BalancerTest {
    private AuthBalancer balancer;
    private Map<String, Integer> servers;

    private BalancerTest() {
        servers = new HashMap<>();
        TAuth.setServer(new AuthServer() {
            @Override
            public String getIp(UUID player) {
                return null;
            }

            @Override
            public boolean isOnline(UUID player) {
                return false;
            }

            @Override
            public int getOnline(String server) {
                return servers.getOrDefault(server, 0);
            }
        });
    }

    @Test
    void caseOne() {
        if (balancer == null)
            return;
        servers.put("first", 1213);
        servers.put("second", 312);
        servers.put("third", 32);
        servers.put("unknown", 0);
        assertEquals("third", balancer.getServer(null));
    }

    @Test
    void caseTwo() {
        if (balancer == null)
            return;
        servers.put("third", 500);
        assertEquals("second", balancer.getServer(null));
        servers.clear();
    }

    @Test
    void caseThree() {
        if (balancer == null)
            return;
        assertNotNull(balancer.getServer(null));
    }

    @Test
    void simpleBalancerTest() {
        balancer = new SimpleBalancer(Arrays.asList("first", "second", "third"));
        caseOne();
        caseTwo();
        caseThree();
    }

    @Test
    void threadBalancerTest() throws InterruptedException {
        servers.put("first", 1213);
        servers.put("second", 312);
        servers.put("third", 32);
        servers.put("unknown", 0);
        balancer = new AsyncBalancer(Arrays.asList("first", "second", "third"), 2);
        Thread.sleep(10);
        assertEquals("third", balancer.getServer(null));
        servers.put("second", 500);
        servers.put("third", 501);
        Thread.sleep(10);
        assertEquals("second", balancer.getServer(null));

        servers.clear();

        // Takes too long to update, so we can only get the first server with that period (50000)
        balancer = new AsyncBalancer(Arrays.asList("first", "second", "third"), 50000);
        servers.put("first", 1213);
        servers.put("second", 312);
        servers.put("third", 32);
        servers.put("unknown", 0);
        assertEquals("first", balancer.getServer(null));
        servers.put("second", 500);
        servers.put("third", 501);
        assertEquals("first", balancer.getServer(null));
    }
}
