package me.theseems.tauth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MemAuthDB implements AuthDB {
    private Map<UUID, String> hashes;
    private Map<UUID, Session> sessions;

    public MemAuthDB() {
        hashes = new HashMap<>();
        sessions = new HashMap<>();
    }

    @Override
    public Optional<Session> getSession(UUID player) {
        return Optional.ofNullable(sessions.get(player));
    }

    @Override
    public Optional<String> getHash(UUID player) {
        return Optional.ofNullable(hashes.get(player));
    }

    @Override
    public boolean exist(UUID player) {
        return hashes.containsKey(player);
    }

    @Override
    public void setHash(UUID player, String hash) {
        hashes.put(player, hash);
    }

    @Override
    public void setSession(UUID player, Session session) {
        sessions.put(player, session);
    }

    @Override
    public void clearSession(UUID player) {
        sessions.remove(player);
    }

}
