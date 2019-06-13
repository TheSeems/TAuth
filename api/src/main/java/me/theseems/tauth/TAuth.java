package me.theseems.tauth;

public class TAuth {
    private static AuthDB db;
    private static AuthHasher hasher;
    private static AuthManager authManager;
    private static AuthServer authServer;
    private static Settings settings;

    public TAuth() {
    }

    public static AuthDB getDb() {
        return db;
    }

    public static void setDb(AuthDB db) {
        TAuth.db = db;
    }

    public static AuthHasher getHasher() {
        return hasher;
    }

    public static void setHasher(AuthHasher hasher) {
        TAuth.hasher = hasher;
    }

    public static AuthManager getAuthManager() {
        return authManager;
    }

    public static void setAuthManager(AuthManager authManager) {
        TAuth.authManager = authManager;
    }

    public static AuthServer getAuthServer() {
        return authServer;
    }

    public static void setAuthServer(AuthServer authServer) {
        TAuth.authServer = authServer;
    }

    public static Settings getSettings() {
        return settings;
    }

    public static void setSettings(Settings settings) {
        TAuth.settings = settings;
    }
}
