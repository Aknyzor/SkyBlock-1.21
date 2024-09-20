package me.aknyzor.toplist;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TopListHandler {

    private final File file;
    private final FileConfiguration config;
    private final JavaPlugin plugin;
    private final LinkedHashMap<String, Integer> topPlayers;

    public TopListHandler(String fileName, JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            boolean dirsCreated = file.getParentFile().mkdirs();
            if (dirsCreated) {
                plugin.getLogger().info("Kreirani direktorijumi za " + file.getName());
            }
            try {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    plugin.getLogger().info("Kreiran fajl " + file.getName());
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Greška prilikom kreiranja fajla " + file.getName());
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        this.topPlayers = new LinkedHashMap<>();

        loadConfig();
    }

    private void loadConfig() {
        topPlayers.clear();
        for (String key : config.getKeys(false)) {
            topPlayers.put(key, config.getInt(key));
        }
    }

    public void addPlayer(String playerName, int amount) {
        topPlayers.put(playerName, topPlayers.getOrDefault(playerName, 0) + amount);
        saveConfig();
    }

    public void clearTopList() {
        topPlayers.clear();
        for (String key : config.getKeys(false)) {
            config.set(key, null);
        }
        saveConfig();
    }

    @SuppressWarnings("unused")
    private void loadTopList() {
        for (String key : config.getKeys(false)) {
            topPlayers.put(key, config.getInt(key));
        }
    }

    public LinkedHashMap<String, Integer> getTopPlayers() {
        return topPlayers.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public void saveConfig() {
        for (Map.Entry<String, Integer> entry : topPlayers.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Greška prilikom čuvanja podataka u fajl " + file.getName());
        }
    }
}