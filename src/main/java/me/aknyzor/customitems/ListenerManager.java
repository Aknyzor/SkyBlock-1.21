package me.aknyzor.customitems;

import me.aknyzor.customitems.attor.Attor;
import me.aknyzor.customitems.illyrian_blade.Illyrian_Blade;
import me.aknyzor.customitems.shield_of_siphons.Shield_of_Siphons;
import me.aknyzor.customitems.staff_of_homa.Staff_of_Homa;
import me.aknyzor.customitems.suriel.Suriel;
import me.aknyzor.customitems.the_grave_digger.The_Grave_Digger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerManager {

    public ListenerManager(JavaPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new Attor(), plugin);
        pm.registerEvents(new Suriel(), plugin);
        pm.registerEvents(new The_Grave_Digger(), plugin);
        pm.registerEvents(new Illyrian_Blade(), plugin);
        pm.registerEvents(new Shield_of_Siphons(), plugin);
        pm.registerEvents(new Staff_of_Homa(), plugin);
    }
}
