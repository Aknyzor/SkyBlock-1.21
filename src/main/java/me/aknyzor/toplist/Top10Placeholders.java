package me.aknyzor.toplist;

import me.aknyzor.SkyBlock;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Top10Placeholders extends PlaceholderExpansion {

    private final SkyBlock plugin;

    public Top10Placeholders(SkyBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    @Deprecated
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "top10";
    }

    @Override
    @Deprecated
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (identifier.startsWith("blockbreak_")) {
            return getTopListPlaceholder(plugin.getBlockBreakTopList().getTopPlayers(), identifier, "blockbreak_");
        } else if (identifier.startsWith("mobkill_")) {
            return getTopListPlaceholder(plugin.getMobKillTopList().getTopPlayers(), identifier, "mobkill_");
        } else if (identifier.startsWith("harvest_")) {
            return getTopListPlaceholder(plugin.getHarvestTopList().getTopPlayers(), identifier, "harvest_");
        } else if (identifier.startsWith("vote_")) {
            return getTopListPlaceholder(plugin.getVoteTopList().getTopPlayers(), identifier, "vote_");
        }
        return null;
    }

    private String getTopListPlaceholder(LinkedHashMap<String, Integer> topList, String identifier, String prefix) {
        int position;
        try {
            position = Integer.parseInt(identifier.replace(prefix, ""));
        } catch (NumberFormatException e) {
            return "0";
        }

        LinkedHashMap<String, Integer> sortedTopList = topList.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        if (position > 0 && position <= sortedTopList.size()) {
            return (String) sortedTopList.keySet().toArray()[position - 1];
        } else {
            return "0";
        }
    }
}