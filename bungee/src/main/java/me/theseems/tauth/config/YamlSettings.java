package me.theseems.tauth.config;

import me.theseems.tauth.AuthBalancer;
import me.theseems.tauth.AuthHasher;
import me.theseems.tauth.utils.BungeeTitle;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class YamlSettings implements BungeeSettings {
    private File file;
    private Configuration configuration;

    private AuthHasher hasher;
    private AuthBalancer authBalancer;
    private AuthBalancer nextBalancer;

    public YamlSettings(File file) {
        this.file = file;
        load();
    }

    private <T> T get(Class<T> clazz, String name) {
        Object value = configuration.get(name);
        if (value == null)
            return null;
        if (value.getClass() == clazz)
            return clazz.cast(value);
        else {
            System.out.println("WARNING: Expected class of setting '" + name + "' is " + clazz.getCanonicalName() + " but " + value.getClass().getCanonicalName() + " found");
            return null;
        }
    }

    private <T> T getOrDefault(Class<T> clazz, String name, T def) {
        T value = get(clazz, name);
        if (value == null)
            return def;
        else {
            return value;
        }
    }

    private <T> List<T> getList(Class<T> clazz, String name) {
        Object value = configuration.getList(name);
        if (value == null)
            return null;
        List<?> list = (List<?>) value;
        if (list.size() == 0)
            return new ArrayList<>();
        else if (clazz.isAssignableFrom(list.get(0).getClass()))
            // It should be List<T> because the first element => T
            //noinspection unchecked
            return (List<T>) list;
        return null;
    }

    private AuthHasher loadHasher() {
        String clazz = getOrDefault(String.class, "hasher.class", "me.theseems.tauth.hashers.SHA512AuthHasher");
        List<?> settings = getList(Object.class, "hasher.settings");
        try {
            Class hasherClass = Class.forName(clazz);
            Object object = hasherClass.getConstructors()[0].newInstance(settings.toArray(new Object[0]));
            if (!(object instanceof AuthHasher))
                throw new IllegalStateException(hasherClass + " should implement AuthHasher");
            return (AuthHasher) object;
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find hasher class '" + clazz + "'");
            e.printStackTrace();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println("Error constructing class '" + clazz + "' with args: " + settings.toString());
            e.printStackTrace();
        }
        return null;
    }

    private AuthBalancer loadBalancer(String name) {
        String clazz = getOrDefault(String.class, name + ".class", "me.theseems.tauth.balancers.SimpleBalancer");
        List<?> settings = getList(Object.class, name + ".settings");
        try {
            Class balancerClass = Class.forName(clazz);
            Object object = balancerClass.getConstructors()[0].newInstance(settings.toArray());
            if (!(object instanceof AuthBalancer))
                throw new IllegalStateException(balancerClass + " should implement AuthBalancer");
            return (AuthBalancer) object;
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find balancer class '" + clazz + "'");
            e.printStackTrace();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println("Error constructing class '" + clazz + "' with args: " + settings.toString());
            e.printStackTrace();
        }
        return null;
    }

    private void load() {
        try {
            configuration = ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .load(file);
            hasher = loadHasher();
            authBalancer = loadBalancer("auth_balancer");
            nextBalancer = loadBalancer("next_balancer");
        } catch (IOException e) {
            System.err.println("Error loading config file");
            e.printStackTrace();
        }
    }

    @Override
    public int getCheckerPeriod() {
        return getOrDefault(Integer.class, "checker_period", 5000);
    }

    @Override
    public int getServerPeriod() {
        return getOrDefault(Integer.class, "update_period", 30);
    }

    @Override
    public int getKickPeriod() {
        return getOrDefault(Integer.class, "kick_period", 30);
    }

    @Override
    public Title getTitle(String id) {
        String key = "messages." + id;
        String title = getOrDefault(String.class, key + ".title", id + ".title");
        String subtitle = getOrDefault(String.class, key + ".subtitle", id + ".subtitle");
        int fadeIn = getOrDefault(Integer.class, key + ".fade_in", 10);
        int fadeOut = getOrDefault(Integer.class, key + ".fade_out", 10);
        int stay = getOrDefault(Integer.class, key + ".stay", 30);

        return new BungeeTitle()
                .title(new TextComponent(title))
                .subTitle(new TextComponent(subtitle))
                .fadeIn(fadeIn)
                .fadeOut(fadeOut)
                .stay(stay);
    }

    @Override
    public String getMessage(String id) {
        String key = "messages." + id;
        if (!configuration.contains(key))
            return id;
        return configuration.getString(key);
    }

    @Override
    public String getDbType() {
        return getOrDefault(String.class, "db.type", "memo");
    }

    @Override
    public String getDbUrl() {
        return getOrDefault(String.class, "db.url", null);
    }

    @Override
    public String getDbUser() {
        return getOrDefault(String.class, "db.user", null);
    }

    @Override
    public String getDbPassword() {
        return getOrDefault(String.class, "db.pass", null);
    }

    @Override
    public AuthHasher getHasher() {
        return hasher;
    }

    @Override
    public AuthBalancer getAuthBalancer() {
        return authBalancer;
    }

    @Override
    public AuthBalancer getNextBalancer() {
        return nextBalancer;
    }

    @Override
    public List<String> getAuthServers() {
        return getList(String.class, "auth");
    }

    @Override
    public Integer getExpireSeconds() {
        return getOrDefault(Integer.class, "expire", 2 * 3600);
    }

    @Override
    public List<String> getNextServers() {
        return getList(String.class, "next");
    }

}
