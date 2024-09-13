package me.aknyzor.toplist;

import me.aknyzor.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class WeeklyRewardScheduler extends BukkitRunnable {
    private final TopListHandler blockBreakTopList;
    private final TopListHandler mobKillTopList;
    private final TopListHandler harvestTopList;
    private final TopListHandler voteTopList;

    private final Map<Integer, List<String>> placeCommands = new LinkedHashMap<>();

    public WeeklyRewardScheduler(TopListHandler blockBreakTopList, TopListHandler mobKillTopList, TopListHandler harvestTopList, TopListHandler voteTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;
        this.harvestTopList = harvestTopList;
        this.voteTopList = voteTopList;

        // Definišemo komande za različita mesta
        placeCommands.put(1, Arrays.asList("eco give %player% 1000", "xp set %player% 10 levels"));
        placeCommands.put(2, Arrays.asList("eco give %player% 900", "xp set %player% 9 levels"));
        placeCommands.put(3, Arrays.asList("eco give %player% 800", "xp set %player% 8 levels"));
        // Dodajte ostale komande za preostala mesta po potrebi
        placeCommands.put(10, Arrays.asList("eco give %player% 100", "xp set %player% 1 levels"));
    }

    @Override
    public void run() {
        LinkedHashMap<String, Integer> combinedTopList = getCombinedTopList();
        Map<String, List<String>> rewards = new LinkedHashMap<>();

        int place = 1;
        for (Map.Entry<String, Integer> entry : combinedTopList.entrySet()) {
            if (place > 10) break;

            String playerName = entry.getKey();
            Player player = Bukkit.getPlayer(playerName);
            List<String> commands = placeCommands.getOrDefault(place, Collections.singletonList("broadcast Congratulations to %player%!"))
                    .stream()
                    .map(cmd -> cmd.replace("%player%", playerName))
                    .collect(Collectors.toList());

            if (player != null) {
                Bukkit.getScheduler().runTask(SkyBlock.getInstance(), () -> {
                    for (String command : commands) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                });
            } else {
                rewards.put(playerName, commands);
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

    private void saveRewards(Map<String, List<String>> rewards) {
        String fileName = "weeklyRewards.yml";
        File file = new File(SkyBlock.getInstance().getDataFolder(), fileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Map.Entry<String, List<String>> entry : rewards.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            SkyBlock.getInstance().getLogger().severe("Failed to save rewards to file " + fileName + ": " + e.getMessage());
        }
    }
}