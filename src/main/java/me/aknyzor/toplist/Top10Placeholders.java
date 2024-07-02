package me.aknyzor.toplist;

import me.aknyzor.SkyBlock;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class Top10Placeholders extends PlaceholderExpansion {

    /**
     *
     * I dalje je u izradi (Aknyzor)
     *
     */

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
    public String onPlaceholderRequest(Player player, String identifier) {

        if (identifier.startsWith("blockbreak_")) {
            return getTopListPlaceholder(plugin.getBlockBreakTopList().getTopPlayers(), identifier, "blockbreak_");
        } else if (identifier.startsWith("mobkill_")) {
            return getTopListPlaceholder(plugin.getMobKillTopList().getTopPlayers(), identifier, "mobkill_");
        }

        return null;
    }

    private String getTopListPlaceholder(LinkedHashMap<String, Integer> topList, String identifier, String prefix) {
        int rank = Integer.parseInt(identifier.replace(prefix, ""));
        int index = 0;
        for (Map.Entry<String, Integer> entry : topList.entrySet()) {
            if (++index == rank) {
                return entry.getKey() + " - " + entry.getValue();
            }
        }
        return "N/A";
    }
}