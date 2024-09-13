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
import java.util.List;
import java.util.Map;

public class RewardListener implements Listener {

    private final String rewardsFileName = "weeklyRewards.yml";

    private Map<String, List<String>> loadRewards() {
        Map<String, List<String>> rewards = new HashMap<>();
        File file = new File(SkyBlock.getInstance().getDataFolder(), rewardsFileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            rewards.put(key, config.getStringList(key));
        }

        return rewards;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();

        Map<String, List<String>> rewards = loadRewards();

        List<String> commands = rewards.get(playerName);
        if (commands != null) {
            for (String command : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
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