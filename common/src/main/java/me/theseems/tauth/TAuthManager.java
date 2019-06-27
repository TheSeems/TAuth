package me.theseems.tauth;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class TAuthManager implements AuthManager {

  @Override
  public LoginResponse login(UUID player, String hash) {
    if (!TAuth.getServer().isOnline(player)) return LoginResponse.INCORRECT;
    if (!TAuth.getDb().exist(player)) return LoginResponse.UNREGISTERED;
    // It should be present, because user exists
    // noinspection OptionalGetWithoutIsPresent
    if (!TAuth.getDb().getHash(player).get().equals(hash)) {
      return LoginResponse.FORBIDDEN;
    } else {
      updateSession(player);
      return LoginResponse.OK;
    }
  }

  @Override
  public RegisterResponse register(UUID player, String hash) {
    if (!TAuth.getServer().isOnline(player)) return RegisterResponse.INCORRECT;
    if (TAuth.getDb().exist(player)) return RegisterResponse.REGISTERED;
    TAuth.getDb().setHash(player, hash);
    updateSession(player);
    return RegisterResponse.OK;
  }

  @Override
  public LoginResponse isAutheticated(UUID player) {
    if (!TAuth.getServer().isOnline(player)) return LoginResponse.INCORRECT;
    if (!TAuth.getDb().exist(player)) return LoginResponse.UNREGISTERED;
    Optional<Session> optional = TAuth.getDb().getSession(player);
    if (optional.isPresent()) {
      if (!optional.get().getIp().equals(TAuth.getServer().getIp(player)))
        return LoginResponse.EXPIRED;
      if (optional.get().getExpire().isAfter(LocalDateTime.now())) return LoginResponse.OK;
      else return LoginResponse.EXPIRED;
    } else {
      return LoginResponse.EXPIRED;
    }
  }

  @Override
  public LoginResponse logout(UUID player) {
    LoginResponse response = isAutheticated(player);
    if (response == LoginResponse.OK) TAuth.getDb().clearSession(player);
    return response;
  }

  @Override
  public void updateSession(UUID player) {
    TAuth.getDb()
      .setSession(
        player,
        new TSession(
                LocalDateTime.now().plusSeconds(TAuth.getSettings().getExpireSeconds()),
          TAuth.getServer().getIp(player)));
  }
}
