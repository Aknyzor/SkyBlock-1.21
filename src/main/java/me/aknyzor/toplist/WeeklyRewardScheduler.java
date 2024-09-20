package me.aknyzor.toplist;

import me.aknyzor.SkyBlock;
import net.kyori.adventure.text.Component;
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
    private final TopListHandler fishingTopList;
    private final TopListHandler placeTopList;

    private final Map<Integer, List<String>> placeCommands = new LinkedHashMap<>();

    public WeeklyRewardScheduler(TopListHandler blockBreakTopList, TopListHandler mobKillTopList, TopListHandler harvestTopList, TopListHandler voteTopList, TopListHandler fishingTopList, TopListHandler placeTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;
        this.harvestTopList = harvestTopList;
        this.voteTopList = voteTopList;
        this.fishingTopList = fishingTopList;
        this.placeTopList = placeTopList;

        placeCommands.put(1, Arrays.asList("eco give %player% 1000", "xp set %player% 10 levels"));
        placeCommands.put(2, Arrays.asList("eco give %player% 900", "xp set %player% 9 levels"));
        placeCommands.put(3, Arrays.asList("eco give %player% 800", "xp set %player% 8 levels"));
        placeCommands.put(4, Arrays.asList("eco give %player% 700", "xp set %player% 7 levels"));
        placeCommands.put(5, Arrays.asList("eco give %player% 600", "xp set %player% 6 levels"));
        placeCommands.put(6, Arrays.asList("eco give %player% 500", "xp set %player% 5 levels"));
        placeCommands.put(7, Arrays.asList("eco give %player% 400", "xp set %player% 4 levels"));
        placeCommands.put(8, Arrays.asList("eco give %player% 300", "xp set %player% 3 levels"));
        placeCommands.put(9, Arrays.asList("eco give %player% 200", "xp set %player% 2 levels"));
        placeCommands.put(10, Arrays.asList("eco give %player% 100", "xp set %player% 1 levels"));
    }

    @Override
    public void run() {
        LinkedHashMap<String, Integer> combinedTopList = getCombinedTopList();
        Map<String, List<String>> rewards = new LinkedHashMap<>();

        StringBuilder broadcastMessage = new StringBuilder("Čestitamo svim pobednicima:\n");

        int place = 1;
        for (Map.Entry<String, Integer> entry : combinedTopList.entrySet()) {
            if (place > 10) break;

            String playerName = entry.getKey();
            Player player = Bukkit.getPlayer(playerName);

            broadcastMessage.append(place).append(". mesto: ").append(playerName).append("\n");

            List<String> commands = placeCommands.getOrDefault(place, Collections.emptyList())
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

        Bukkit.getServer().sendMessage(Component.text(broadcastMessage.toString()));

        saveRewards(rewards);

        blockBreakTopList.clearTopList();
        mobKillTopList.clearTopList();
        harvestTopList.clearTopList();
        voteTopList.clearTopList();
        fishingTopList.clearTopList();
        placeTopList.clearTopList();
    }

    private LinkedHashMap<String, Integer> getCombinedTopList() {
        LinkedHashMap<String, Integer> combinedTopList = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> blockBreakTop = blockBreakTopList.getTopPlayers();
        LinkedHashMap<String, Integer> mobKillTop = mobKillTopList.getTopPlayers();
        LinkedHashMap<String, Integer> harvestTop = harvestTopList.getTopPlayers();
        LinkedHashMap<String, Integer> voteTop = voteTopList.getTopPlayers();
        LinkedHashMap<String, Integer> fishingTop = fishingTopList.getTopPlayers();
        LinkedHashMap<String, Integer> placeTop = placeTopList.getTopPlayers();

        addPointsToTopList(blockBreakTop, combinedTopList);
        addPointsToTopList(mobKillTop, combinedTopList);
        addPointsToTopList(harvestTop, combinedTopList);
        addPointsToTopList(voteTop, combinedTopList);
        addPointsToTopList(fishingTop, combinedTopList);
        addPointsToTopList(placeTop, combinedTopList);

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