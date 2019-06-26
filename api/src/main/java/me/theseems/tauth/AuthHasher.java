package me.theseems.tauth;

public interface AuthHasher {
    /**
     * Hash string
     *
     * @param raw to hash
     * @return hash of raw
     */
    String hash(String raw);
}
