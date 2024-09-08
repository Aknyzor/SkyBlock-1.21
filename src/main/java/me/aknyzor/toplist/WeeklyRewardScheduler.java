package me.aknyzor.toplist;

import me.aknyzor.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeeklyRewardScheduler extends BukkitRunnable {
    private final TopListHandler blockBreakTopList;
    private final TopListHandler mobKillTopList;
    private final TopListHandler harvestTopList;
    private final TopListHandler voteTopList;

    private final Map<Integer, String> placeCommands = new LinkedHashMap<>();

    public WeeklyRewardScheduler(TopListHandler blockBreakTopList, TopListHandler mobKillTopList, TopListHandler harvestTopList, TopListHandler voteTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;
        this.harvestTopList = harvestTopList;
        this.voteTopList = voteTopList;

        for (int i = 1; i <= 10; i++) {
            placeCommands.put(i, "eco give %player% 1000");
        }
    }

    @Override
    public void run() {
        LinkedHashMap<String, Integer> combinedTopList = getCombinedTopList();
        Map<String, String> rewards = new LinkedHashMap<>();

        int place = 1;
        for (Map.Entry<String, Integer> entry : combinedTopList.entrySet()) {
            if (place > 10) break;

            String playerName = entry.getKey();
            Player player = Bukkit.getPlayer(playerName);
            final String command = placeCommands.getOrDefault(place, "broadcast Congratulations to %player%!")
                    .replace("%player%", playerName);

            if (player != null) {
                Bukkit.getScheduler().runTask(SkyBlock.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
            } else {
                rewards.put(playerName, command);
            }
            place++;
        }

        // Čuvanje nagrada u jedan fajl
        saveRewards(rewards);

        // Čišćenje top lista
        blockBreakTopList.clearTopList();
        mobKillTopList.clearTopList();
        harvestTopList.clearTopList();
        voteTopList.clearTopList();
    }

    private LinkedHashMap<String, Integer> getCombinedTopList() {
        LinkedHashMap<String, Integer> combinedTopList = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> blockBreakTop = blockBreakTopList.getTopPlayers();
        LinkedHashMap<String, Integer> mobKillTop = mobKillTopList.getTopPlayers();
        LinkedHashMap<String, Integer> harvestTop = harvestTopList.getTopPlayers();
        LinkedHashMap<String, Integer> voteTop = voteTopList.getTopPlayers();

        addPointsToTopList(blockBreakTop, combinedTopList);
        addPointsToTopList(mobKillTop, combinedTopList);
        addPointsToTopList(harvestTop, combinedTopList);
        addPointsToTopList(voteTop, combinedTopList);

        return combinedTopList;
    }

    private void addPointsToTopList(LinkedHashMap<String, Integer> sourceTopList, LinkedHashMap<String, Integer> targetTopList) {
        int tempPoints = 10;
        for (String playerName : sourceTopList.keySet()) {
            targetTopList.put(playerName, targetTopList.getOrDefault(playerName, 0) + tempPoints);
            tempPoints--;
        }
    }

    private void saveRewards(Map<String, String> rewards) {
        String fileName = "weeklyRewards.yml";
        File file = new File(SkyBlock.getInstance().getDataFolder(), fileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Map.Entry<String, String> entry : rewards.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            SkyBlock.getInstance().getLogger().severe("Failed to save rewards to file " + fileName + ": " + e.getMessage());
        }
    }
}