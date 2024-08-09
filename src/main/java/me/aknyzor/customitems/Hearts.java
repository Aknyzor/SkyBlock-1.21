package me.aknyzor.customitems;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hearts {

    private static final File FILE = new File("plugins/SkyBlock/attributes.yml");
    private static final YamlConfiguration CONFIG = YamlConfiguration.loadConfiguration(FILE);
    private static final Logger LOGGER = Logger.getLogger(Hearts.class.getName());
    private static final String ATTRIBUTE_COMMAND_TEMPLATE = "attribute %s minecraft:generic.max_health modifier %s %s %s %s";

    public static void addAttribute(Player player, double value, String attributeId) {
        Set<String> attributes = getAttributesFromFile(player);
        if (attributes.add(attributeId)) {
            saveAttributesToFile(player, attributes);
            executeCommand(String.format(ATTRIBUTE_COMMAND_TEMPLATE, player.getName(), "add", attributeId, value, "add_value"));
        }
    }

    public static void removeAttribute(Player player, String attributeId) {
        Set<String> attributes = getAttributesFromFile(player);
        if (attributes.remove(attributeId)) {
            saveAttributesToFile(player, attributes);
            executeCommand(String.format(ATTRIBUTE_COMMAND_TEMPLATE, player.getName(), "remove", attributeId, "", ""));
        }
    }

    public static void clearAttributes(Player player) {
        Set<String> attributes = getAttributesFromFile(player);
        if (!attributes.isEmpty()) {
            for (String attributeId : attributes) {
                executeCommand(String.format(ATTRIBUTE_COMMAND_TEMPLATE, player.getName(), "remove", attributeId, "", ""));
            }
            saveAttributesToFile(player, new HashSet<>());
        } else {
            logNoAttributesFound(player);
        }
    }

    public static void clearAllAttributes() {
        for (String playerName : CONFIG.getKeys(false)) {
            Player player = Bukkit.getPlayer(playerName);
            if (player != null) {
                clearAttributes(player);
            }
        }
    }

    public static boolean hasAttributes(Player player) {
        return !getAttributesFromFile(player).isEmpty();
    }

    private static Set<String> getAttributesFromFile(Player player) {
        return new HashSet<>(CONFIG.getStringList(player.getName()));
    }

    private static void saveAttributesToFile(Player player, Set<String> attributes) {
        CONFIG.set(player.getName(), new ArrayList<>(attributes));
        try {
            CONFIG.save(FILE);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not save attributes for player " + player.getName(), e);
        }
    }

    private static void executeCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }

    private static void logNoAttributesFound(Player player) {
        Bukkit.getServer().getConsoleSender().sendMessage("No attributes found for player " + player.getName());
    }
}