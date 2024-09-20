package me.aknyzor.toplist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TopCommand implements CommandExecutor {

    private final TopListHandler blockBreakTopList;
    private final TopListHandler mobKillTopList;
    private final TopListHandler harvestTopList;
    private final TopListHandler voteTopList;
    private final TopListHandler fishingTopList;
    private final TopListHandler placeTopList;

    public TopCommand(TopListHandler blockBreakTopList, TopListHandler mobKillTopList, TopListHandler harvestTopList, TopListHandler voteTopList, TopListHandler fishingTopList, TopListHandler placeTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;
        this.harvestTopList = harvestTopList;
        this.voteTopList = voteTopList;
        this.fishingTopList = fishingTopList;
        this.placeTopList = placeTopList;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /eventtop <break|mobs|harvest|vote|fishing|place>");
            return true;
        }

        if (args[0].equalsIgnoreCase("break")) {
            displayTopList(sender, blockBreakTopList, "Top 10 igrača po uništenim blokovima:");
            return true;
        } else if (args[0].equalsIgnoreCase("mobs")) {
            displayTopList(sender, mobKillTopList, "Top 10 igrača po ubijenim mobovima:");
            return true;
        } else if (args[0].equalsIgnoreCase("harvest")) {
            displayTopList(sender, harvestTopList, "Top 10 igrača po ubranom bilju:");
            return true;
        } else if (args[0].equalsIgnoreCase("vote")) {
            displayTopList(sender, voteTopList, "Top 10 igrača po glasovima:");
            return true;
        } else if (args[0].equalsIgnoreCase("fishing")) {
            displayTopList(sender, fishingTopList, "Top 10 igrača po ulovljenim ribama:");
            return true;
        } else if (args[0].equalsIgnoreCase("place")) {
            displayTopList(sender, placeTopList, "Top 10 igrača po postavljenim blokovima:");
            return true;
        } else {
            sender.sendMessage("Usage: /eventtop <break|mobs|harvest|vote|fishing|place>");
            return true;
        }
    }

    private void displayTopList(CommandSender sender, TopListHandler topListHandler, String title) {
        sender.sendMessage(title);

        LinkedHashMap<String, Integer> topPlayers = topListHandler.getTopPlayers();
        LinkedHashMap<String, Integer> sortedTopPlayers = topPlayers.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        int rank = 1;
        for (Map.Entry<String, Integer> entry : sortedTopPlayers.entrySet()) {
            if (rank > 10) break;
            sender.sendMessage(rank + ". " + entry.getKey() + ": " + entry.getValue());
            rank++;
        }
    }
}
