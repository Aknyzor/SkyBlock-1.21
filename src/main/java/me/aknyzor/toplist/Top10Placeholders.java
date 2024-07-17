package me.aknyzor.toplist;

import me.aknyzor.SkyBlock;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

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
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (identifier.startsWith("blockbreak_")) {
            return getTopListPlaceholder(plugin.getBlockBreakTopList().getTopPlayers(), identifier, "blockbreak_");
        } else if (identifier.startsWith("mobkill_")) {
            return getTopListPlaceholder(plugin.getMobKillTopList().getTopPlayers(), identifier, "mobkill_");
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

        if (position > 0 && position <= topList.size()) {
            return (String) topList.keySet().toArray()[position - 1];
        } else {
            return "0";
        }
    }
}