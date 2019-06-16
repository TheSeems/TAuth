package me.theseems.tauth.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YamlSettings implements BungeeSettings {
    private File file;
    private Configuration configuration;

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
        else
            return value;
    }

    private <T> List<T> getList(Class<T> clazz, String name) {
        Object value = configuration.getList(name);
        if (value == null)
            return null;
        List<?> list = (List<?>) value;
        if (list.size() == 0)
            return new ArrayList<>();
        else if (list.get(0).getClass() == clazz)
            // It should be List<T> because the first element is T
            //noinspection unchecked
            return (List<T>) list;
        return null;
    }

    private void load() {
        try {
            configuration = ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .load(file);
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
    public List<String> getAuthServers() {
        return getList(String.class, "auth");
    }

    @Override
    public Integer getExpireMils() {
        return getOrDefault(Integer.class, "expire", 2 * 3600 * 1000);
    }

    @Override
    public List<String> getNextServers() {
        return getList(String.class, "next");
    }

}
