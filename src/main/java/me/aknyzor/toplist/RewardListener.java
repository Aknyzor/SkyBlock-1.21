package me.aknyzor.toplist;

import me.aknyzor.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RewardListener implements Listener {

    private final String rewardsFileName = "weeklyRewards.yml";

    private Map<String, String> loadRewards() {
        Map<String, String> rewards = new HashMap<>();
        File file = new File(SkyBlock.getInstance().getDataFolder(), rewardsFileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            rewards.put(key, config.getString(key));
        }

        return rewards;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();

        Map<String, String> rewards = loadRewards();

        String reward = rewards.get(playerName);
        if (reward != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
            removePlayerReward(playerName);
        }
    }

    private void removePlayerReward(String playerName) {
        File file = new File(SkyBlock.getInstance().getDataFolder(), rewardsFileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set(playerName, null);

        try {
            config.save(file);
        } catch (IOException e) {
            SkyBlock.getInstance().getLogger().severe("Failed to remove player reward from file " + rewardsFileName + ": " + e.getMessage());
        }
    }
}