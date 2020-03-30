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
    pool = new HikariPool(config);
    init();
  }

  private void init() {
    try {
      Connection connection = pool.getConnection();
      PreparedStatement st =
        connection.prepareStatement(
          "CREATE TABLE IF NOT EXISTS TAuth (uuid VARCHAR(40) PRIMARY KEY UNIQUE, hash VARCHAR(512), ip VARCHAR(20), expire timestamp)");
      st.execute();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void initPlayer(UUID player) {
    if (exist(player)) return;
    try {
      Connection connection = getConnection();
      PreparedStatement statement =
        connection.prepareStatement("INSERT INTO tauth VALUES (?, null, null, null)");
      statement.setString(1, player.toString());
      statement.execute();
      connection.close();
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

    try {
      Connection connection = getConnection();
      PreparedStatement statement =
        connection.prepareStatement("SELECT ip, expire FROM tauth WHERE uuid = ?");
      statement.setString(1, player.toString());
      ResultSet set = statement.executeQuery();
      if (!set.next()) {
        connection.close();
        throw new IllegalStateException("Session must be presented if player exists");
      }

      String ip = set.getString("ip");
      LocalDateTime expire = set.getTimestamp("expire").toLocalDateTime();
      connection.close();
      return Optional.of(new TSession(expire, ip));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public Optional<String> getHash(UUID player) {
    if (!exist(player)) return Optional.empty();

    try {
      Connection connection = getConnection();
      PreparedStatement statement =
        connection.prepareStatement("SELECT hash FROM tauth WHERE uuid = ?");
      statement.setString(1, player.toString());
      ResultSet set = statement.executeQuery();
      if (!set.next()) {
        connection.close();
        throw new IllegalStateException("Hash must be presented if player exists");
      }

      String str = set.getString("hash");
      connection.close();
      return Optional.of(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public boolean exist(UUID player) {
    try {
      Connection connection = getConnection();
      PreparedStatement statement =
        connection.prepareStatement("SELECT uuid FROM tauth WHERE uuid = ?");
      statement.setString(1, player.toString());
      ResultSet set = statement.executeQuery();
      if (set.next()) {
        connection.close();
        return true;
      }

      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void setHash(UUID player, String hash) {
    try {
      initPlayer(player);
      Connection connection = getConnection();
      PreparedStatement statement =
        connection.prepareStatement("UPDATE tauth SET hash=? WHERE uuid=?");
      statement.setString(1, hash);
      statement.setString(2, player.toString());
      statement.execute();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setSession(UUID player, Session session) {
    try {
      initPlayer(player);
      Connection connection = getConnection();
      PreparedStatement statement =
        connection.prepareStatement("UPDATE tauth SET ip=?, expire=? WHERE uuid=?");
      statement.setString(1, session.getIp());
      statement.setTimestamp(2, Timestamp.valueOf(session.getExpire()));
      statement.setString(3, player.toString());
      statement.execute();
      connection.close();
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
