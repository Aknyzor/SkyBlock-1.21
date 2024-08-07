package me.aknyzor.customitems;

import me.aknyzor.customitems.attor.Attor;
import me.aknyzor.customitems.fragment_of_ataraxia.Fragment_Of_Ataraxia;
import me.aknyzor.customitems.illyrian_blade.Illyrian_Blade;
import me.aknyzor.customitems.rhys_wings.Rhys_Wings;
import me.aknyzor.customitems.shadowsinger.Shadowsinger;
import me.aknyzor.customitems.shield_of_siphons.Shield_of_Siphons;
import me.aknyzor.customitems.staff_of_homa.Staff_of_Homa;
import me.aknyzor.customitems.suriel.Suriel;
import me.aknyzor.customitems.the_bone_carver.The_Bone_Carver;
import me.aknyzor.customitems.the_grass_cutter.The_Grass_Cutter;
import me.aknyzor.customitems.the_grave_digger.The_Grave_Digger;
import me.aknyzor.customitems.the_spine_splitter.The_Spine_Splitter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerManager {

    public ListenerManager(JavaPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new Attor(), plugin); // 1
        pm.registerEvents(new Suriel(), plugin); // 2
        pm.registerEvents(new The_Grave_Digger(), plugin); // 3
        pm.registerEvents(new Illyrian_Blade(), plugin); // 4
        pm.registerEvents(new Shield_of_Siphons(), plugin); // 5
        pm.registerEvents(new Staff_of_Homa(), plugin); // 6
        pm.registerEvents(new Fragment_Of_Ataraxia(), plugin); // 7
        pm.registerEvents(new The_Bone_Carver(), plugin); // 10
        pm.registerEvents(new Shadowsinger(), plugin); // 11
        pm.registerEvents(new The_Grass_Cutter(), plugin); // 12
        pm.registerEvents(new Rhys_Wings(), plugin); // 13
        pm.registerEvents(new The_Spine_Splitter(), plugin); // 24
    }
}