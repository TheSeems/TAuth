package me.theseems.tauth.tests;

import me.theseems.tauth.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    public RegisterTest() {
        TAuth.setHasher(new SHA512AuthHasher());
        TAuth.setDb(new MemAuthDB());
        TAuth.setAuthManager(new TAuthManager());
        TAuth.setSettings(new Settings() {
            @Override
            public List<String> getAuthServers() {
                return Collections.singletonList("limbo");
            }

            @Override
            public long getExpireMils() {
                return 24 * 1000;
            }

            @Override
            public String getNextServer() {
                return null;
            }
        });
    }

    @Test
    public void testHash() {
        String a = TAuth.getHasher().hash("hello world");
        assertEquals(a, TAuth.getHasher().hash("hello world"));
        assertNotEquals(a, TAuth.getHasher().hash("hillo world"));
    }

    @Test
    public void testRegister() {
        AuthServer one = new AuthServer() {
            @Override
            public String getIp(UUID player) {
                return "one";
            }

            @Override
            public boolean isOnline(UUID player) {
                return true;
            }
        };
        AuthServer another = new AuthServer() {
            @Override
            public String getIp(UUID player) {
                return "another";
            }

            @Override
            public boolean isOnline(UUID player) {
                return true;
            }
        };

        TAuth.setAuthServer(one);

        UUID player = UUID.randomUUID();
        assertTrue(TAuth.getAuthServer().isOnline(player));
        assertFalse(TAuth.getDb().exist(player));
        assertEquals(TAuth.getAuthManager().login(player, "some"), LoginResponse.UNREGISTERED);
        assertEquals(TAuth.getAuthManager().autoLogin(player), LoginResponse.UNREGISTERED);

        String hashed = TAuth.getHasher().hash("sdafnbasjdkf mnkads fjk asdjkf asjd fjkas df");
        assertEquals(TAuth.getAuthManager().register(player, hashed), RegisterResponse.OK);
        assertEquals(TAuth.getAuthManager().register(player, hashed), RegisterResponse.REGISTERED);

        assertEquals(TAuth.getAuthManager().autoLogin(player), LoginResponse.OK);
        TAuth.setAuthServer(another);
        assertEquals(TAuth.getAuthManager().autoLogin(player), LoginResponse.EXPIRED);

        TAuth.setAuthServer(one);
        assertEquals(TAuth.getAuthManager().autoLogin(player), LoginResponse.OK);

        TAuth.setAuthServer(another);
        assertEquals(TAuth.getAuthManager().login(player, hashed), LoginResponse.OK);

        TAuth.setAuthServer(another);
        assertEquals(TAuth.getAuthManager().autoLogin(player), LoginResponse.OK);

        TAuth.setAuthServer(one);
        assertEquals(TAuth.getAuthManager().autoLogin(player), LoginResponse.EXPIRED);
    }
}
