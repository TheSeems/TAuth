package me.theseems.tauth;

public class TAuth {
  private static AuthDB db;
  private static AuthHasher hasher;
  private static AuthManager manager;
  private static AuthServer server;
  private static Settings settings;

  private static AuthBalancer authBalancer;
  private static AuthBalancer nextBalancer;

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

  public static AuthManager getManager() {
    return manager;
  }

  public static void setManager(AuthManager manager) {
    TAuth.manager = manager;
  }

  public static AuthServer getServer() {
    return server;
  }

  public static void setServer(AuthServer server) {
    TAuth.server = server;
  }

  public static Settings getSettings() {
    return settings;
  }

  public static void setSettings(Settings settings) {
    TAuth.settings = settings;
  }

  public static AuthBalancer getAuthBalancer() {
    return authBalancer;
  }

  public static void setAuthBalancer(AuthBalancer authBalancer) {
    TAuth.authBalancer = authBalancer;
    authBalancer.init(settings.getAuthServers());
  }

  public static AuthBalancer getNextBalancer() {
    return nextBalancer;
  }

  public static void setNextBalancer(AuthBalancer nextBalancer) {
    TAuth.nextBalancer = nextBalancer;
    nextBalancer.init(settings.getNextServers());
  }
}
