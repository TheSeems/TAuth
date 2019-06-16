package me.theseems.tauth.tests;

import me.theseems.tauth.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    RegisterTest() {
        TAuth.setHasher(new SHA512AuthHasher());
        TAuth.setDb(new MemAuthDB());
        TAuth.setManager(new TAuthManager());
        TAuth.setSettings(new Settings() {
            @Override
            public List<String> getAuthServers() {
                return null;
            }

            @Override
            public Integer getExpireSeconds() {
                return 24 * 1000;
            }

            @Override
            public List<String> getNextServers() {
                return null;
            }
        });
    }

    @Test
    void testHash() {
        String a = TAuth.getHasher().hash("hello world");
        assertEquals(a, TAuth.getHasher().hash("hello world"));
        assertNotEquals(a, TAuth.getHasher().hash("hillo world"));
    }

    @Test
    void testRegister() {
        AuthServer one = new AuthServer() {
            @Override
            public String getIp(UUID player) {
                return "one";
            }

            @Override
            public boolean isOnline(UUID player) {
                return true;
            }

            @Override
            public int getOnline(String server) {
                return 0;
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

            @Override
            public int getOnline(String server) {
                return 0;
            }
        };

        TAuth.setServer(one);

        UUID player = UUID.randomUUID();
        assertTrue(TAuth.getServer().isOnline(player));
        assertFalse(TAuth.getDb().exist(player));
        assertEquals(TAuth.getManager().login(player, "some"), LoginResponse.UNREGISTERED);
        assertEquals(TAuth.getManager().autoLogin(player), LoginResponse.UNREGISTERED);

        String hashed = TAuth.getHasher().hash("sdafnbasjdkf mnkads fjk asdjkf asjd fjkas df");
        assertEquals(TAuth.getManager().register(player, hashed), RegisterResponse.OK);
        assertEquals(TAuth.getManager().register(player, hashed), RegisterResponse.REGISTERED);

        assertEquals(TAuth.getManager().autoLogin(player), LoginResponse.OK);
        TAuth.setServer(another);
        assertEquals(TAuth.getManager().autoLogin(player), LoginResponse.EXPIRED);

        TAuth.setServer(one);
        assertEquals(TAuth.getManager().autoLogin(player), LoginResponse.OK);

        TAuth.setServer(another);
        assertEquals(TAuth.getManager().login(player, hashed), LoginResponse.OK);

        TAuth.setServer(another);
        assertEquals(TAuth.getManager().autoLogin(player), LoginResponse.OK);

        TAuth.setServer(one);
        assertEquals(TAuth.getManager().autoLogin(player), LoginResponse.EXPIRED);
    }
}
