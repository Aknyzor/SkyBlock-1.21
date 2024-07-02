package me.aknyzor.toplist;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class TopListHandler {

    /**
     *
     * I dalje je u izradi (Aknyzor)
     *
     */

    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration config;
    private final Map<String, Integer> topPlayers = new LinkedHashMap<>();

    public TopListHandler(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;

        if (!file.exists()) {
            try {
                InputStream inputStream = plugin.getResource("blockbreak_toplist.yml");
                if (inputStream != null) {
                    Files.copy(inputStream, file.toPath());
                } else {
                    plugin.getLogger().warning("Resurs 'blockbreak_toplist.yml' nije pronađen.");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Greška prilikom kopiranja resursa 'blockbreak_toplist.yml' iz JAR fajla.");
                plugin.getLogger().severe(e.getMessage());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void addPlayer(String playerName, int count) {
        topPlayers.put(playerName, topPlayers.getOrDefault(playerName, 0) + count);
        saveTopList();
    }

    public LinkedHashMap<String, Integer> getTopPlayers() {
        return new LinkedHashMap<>(topPlayers);
    }

    public void clearTopList() {
        topPlayers.clear();
        saveTopList();
    }

    //private void loadTopList() {
    //    for (String key : config.getKeys(false)) {
    //        topPlayers.put(key, config.getInt(key));
    //    }
    //}

    private void saveTopList() {
        for (Map.Entry<String, Integer> entry : topPlayers.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save top list to " + file.getName() + ": " + e.getMessage());
        }
    }
}