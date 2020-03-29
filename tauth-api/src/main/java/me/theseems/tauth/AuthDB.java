package me.theseems.tauth;

import java.util.Optional;
import java.util.UUID;

public interface AuthDB {
  /**
   * Get session by ip
   *
   * @param player to get session of
   * @return session
   */
  Optional<Session> getSession(UUID player);

  /**
   * Get hash of player
   *
   * @param player to get hash of
   * @return hash
   */
  Optional<String> getHash(UUID player);

  /**
   * Do player exist (registered)
   *
   * @param player to check
   * @return exist or not
   */
  boolean exist(UUID player);

  /**
   * Set hash to player
   *
   * @param player to set hash
   */
  void setHash(UUID player, String hash);

  /**
   * Set session
   *
   * @param session to set
   */
  void setSession(UUID player, Session session);

  /**
   * Clear session of player
   *
   * @param player to clear
   */
  void clearSession(UUID player);
}
