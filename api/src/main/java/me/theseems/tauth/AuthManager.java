package me.theseems.tauth;

import java.util.UUID;

public interface AuthManager {
    /**
     * Login call
     * @param player to login
     * @param hash of password
     * @return response
     */
    LoginResponse login(UUID player, String hash);

    /**
     * Register call
     * @param player to register
     * @param hash of password
     * @return response
     */
    RegisterResponse register(UUID player, String hash);

    /**
     * Check whether player is authenticated
     * @param player to check
     * @return result
     */
    LoginResponse isAutheticated(UUID player);

    /**
     * Logout from player
     * @param player to logout from
     */
    LoginResponse logout(UUID player);

    /**
     * Update session for player
     * @param player to update for
     */
    void updateSession(UUID player);
}
