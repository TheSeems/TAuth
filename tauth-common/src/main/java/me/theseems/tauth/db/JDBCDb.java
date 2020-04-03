package me.theseems.tauth.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import me.theseems.tauth.AuthDB;
import me.theseems.tauth.Session;
import me.theseems.tauth.TSession;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class JDBCDb implements AuthDB {

  private HikariPool pool;

  public JDBCDb(String host, String user, String password) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(host);
    config.setUsername(user);
    config.setPassword(password);
    config.setLeakDetectionThreshold(60 * 1000);
    pool = new HikariPool(config);
    init();
  }

  private void init() {
    try {
      Connection connection = pool.getConnection();
      PreparedStatement st =
        connection.prepareStatement(
          "CREATE TABLE IF NOT EXISTS TAuth (uuid VARCHAR(140) PRIMARY KEY UNIQUE, hash VARCHAR(512), ip VARCHAR(60), expire timestamp)");
      st.execute();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void initPlayer(UUID player) {
    if (exist(player)) return;
    try (Connection connection = getConnection()) {
      PreparedStatement statement =
        connection.prepareStatement("INSERT INTO TAuth VALUES (?, null, null, null)");
      statement.setString(1, player.toString());
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private Connection getConnection() {
    try {
      return pool.getConnection();
    } catch (SQLException e) {
      System.err.println("Error getting connection");
      e.printStackTrace();
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Optional<Session> getSession(UUID player) {
    if (!exist(player)) return Optional.empty();

    try (Connection connection = getConnection()) {
      Statement statement = connection.createStatement();
      ResultSet set =
        statement.executeQuery(
          "SELECT ip, expire FROM TAuth WHERE uuid = '" + player.toString() + "'");

      if (set.next()) {
        if (set.getString("ip") == null || set.getTimestamp("expire") == null) {
          clearSession(player);
          return Optional.empty();
        }

        String ip = set.getString("ip");
        LocalDateTime expire = set.getTimestamp("expire").toLocalDateTime();
        return Optional.of(new TSession(expire, ip));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  @Override
  public Optional<String> getHash(UUID player) {
    if (!exist(player)) return Optional.empty();

    try (Connection connection = getConnection()) {
      Statement statement = connection.createStatement();
      ResultSet set =
        statement.executeQuery("SELECT hash FROM TAuth WHERE uuid = '" + player.toString() + "'");

      if (set.next()) {
        String str = set.getString("hash");
        set.close();
        connection.close();
        return Optional.of(str);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public boolean exist(UUID player) {
    try (Connection connection = getConnection()) {
      Statement statement = connection.createStatement();
      ResultSet set =
        statement.executeQuery("SELECT uuid FROM TAuth WHERE uuid = '" + player.toString() + "'");
      if (set.next()) {
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void setHash(UUID player, String hash) {
    initPlayer(player);
    try (Connection connection = getConnection()) {
      PreparedStatement statement =
        connection.prepareStatement("UPDATE TAuth SET hash=? WHERE uuid=?");
      statement.setString(1, hash);
      statement.setString(2, player.toString());
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setSession(UUID player, Session session) {
    initPlayer(player);
    try (Connection connection = getConnection()) {
      PreparedStatement statement =
        connection.prepareStatement("UPDATE TAuth SET ip=?, expire=? WHERE uuid=?");
      statement.setString(1, session.getIp());
      statement.setTimestamp(2, Timestamp.valueOf(session.getExpire()));
      statement.setString(3, player.toString());
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void clearSession(UUID player) {
    if (!exist(player)) return;
    setSession(player, new TSession(LocalDateTime.now(), ""));
  }
}
