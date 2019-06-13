package me.theseems.tauth;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class TAuthManager implements AuthManager {

    @Override
    public LoginResponse login(UUID player, String hash) {
        if (!TAuth.getAuthServer().isOnline(player))
            return LoginResponse.INCORRECT;
        if (!TAuth.getDb().exist(player))
            return LoginResponse.UNREGISTERED;
        if (!TAuth.getDb().getHash(player).get().equals(hash)) {
            return LoginResponse.FORBIDDEN;
        } else {
            updateSession(player);
            return LoginResponse.OK;
        }
    }

    @Override
    public LoginResponse autoLogin(UUID player) {
        return isAutheticated(player);
    }

    @Override
    public RegisterResponse register(UUID player, String hash) {
        if (!TAuth.getAuthServer().isOnline(player))
            return RegisterResponse.INCORRECT;
        if (TAuth.getDb().exist(player))
            return RegisterResponse.REGISTERED;
        TAuth.getDb().setHash(player, hash);
        updateSession(player);
        return RegisterResponse.OK;
    }

    @Override
    public LoginResponse isAutheticated(UUID player) {
        if (!TAuth.getAuthServer().isOnline(player))
            return LoginResponse.INCORRECT;
        if (!TAuth.getDb().exist(player))
            return LoginResponse.UNREGISTERED;
        Optional<Session> optional = TAuth.getDb().getSession(player);
        if (optional.isPresent()) {
            if (!optional.get().getIp().equals(TAuth.getAuthServer().getIp(player)))
                return LoginResponse.EXPIRED;
            if (optional.get().getExpire().after(new Date()))
                return LoginResponse.OK;
            else
                return LoginResponse.EXPIRED;
        } else {
            return LoginResponse.EXPIRED;
        }
    }

    @Override
    public LoginResponse logout(UUID player) {
        LoginResponse response = isAutheticated(player);
        if (response == LoginResponse.OK)
            TAuth.getDb().clearSession(player);
        return response;
    }

    @Override
    public void updateSession(UUID player) {
        TAuth.getDb().setSession(player, new TSession(
                new Date(System.currentTimeMillis() + TAuth.getSettings().getExpireMils()),
                TAuth.getAuthServer().getIp(player)
        ));
    }
}
