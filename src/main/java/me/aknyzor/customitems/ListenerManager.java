package me.aknyzor.customitems;

import me.aknyzor.customitems.dracula.Axe;
import me.aknyzor.customitems.dracula.Pickaxe;
import me.aknyzor.customitems.dracula.Shovel;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerManager {

    public ListenerManager(JavaPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new Axe(), plugin);
        pm.registerEvents(new Pickaxe(), plugin);
        pm.registerEvents(new Shovel(), plugin);
    }
}
