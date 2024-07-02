package me.aknyzor.toplist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map;

public class WeeklyRewardScheduler extends BukkitRunnable {

    /**
     *
     * I dalje je u izradi (Aknyzor)
     *
     */

    private final TopListHandler blockBreakTopList;
    private final TopListHandler mobKillTopList;

    // Definicija komandi za svako mesto
    private final Map<Integer, String> placeCommands = new LinkedHashMap<>();

    public WeeklyRewardScheduler(TopListHandler blockBreakTopList, TopListHandler mobKillTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;

        // Primer definicije komandi
        placeCommands.put(1, "eco give %player% 1000");
        placeCommands.put(2, "eco give %player% 1000");
        placeCommands.put(3, "eco give %player% 1000");
        placeCommands.put(4, "eco give %player% 1000");
        placeCommands.put(5, "eco give %player% 1000");
        placeCommands.put(6, "eco give %player% 1000");
        placeCommands.put(7, "eco give %player% 1000");
        placeCommands.put(8, "eco give %player% 1000");
        placeCommands.put(9, "eco give %player% 1000");
        placeCommands.put(10, "eco give %player% 1000");
    }

    @Override
    public void run() {
        LinkedHashMap<String, Integer> combinedTopList = getCombinedTopList();

        // Sortiranje kombinovane liste i dodela nagrada
        int place = 1;
        for (Map.Entry<String, Integer> entry : combinedTopList.entrySet()) {
            if (place > 10) break;

            String playerName = entry.getKey();
            Player player = Bukkit.getPlayer(playerName);
            if (player != null) {
                // Dodela definisane komande za svako mesto
                String command = placeCommands.getOrDefault(place, "say Congratulations to %player%!");
                command = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            place++;
        }

        // Brisanje temp poena i top lista
        blockBreakTopList.clearTopList();
        mobKillTopList.clearTopList();
        getCombinedTopList().clear();
    }

    private LinkedHashMap<String, Integer> getCombinedTopList() {
        LinkedHashMap<String, Integer> combinedTopList = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> blockBreakTop = blockBreakTopList.getTopPlayers();
        LinkedHashMap<String, Integer> mobKillTop = mobKillTopList.getTopPlayers();

        // Dodavanje temp poena
        int tempPoints = 10;
        for (String playerName : blockBreakTop.keySet()) {
            combinedTopList.put(playerName, combinedTopList.getOrDefault(playerName, 0) + tempPoints);
            tempPoints--;
        }
        tempPoints = 10;
        for (String playerName : mobKillTop.keySet()) {
            combinedTopList.put(playerName, combinedTopList.getOrDefault(playerName, 0) + tempPoints);
            tempPoints--;
        }

        return combinedTopList;
    }
}