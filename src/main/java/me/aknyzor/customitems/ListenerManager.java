package me.aknyzor.customitems;

import me.aknyzor.customitems.angel_kiss.Angel_Kiss;
import me.aknyzor.customitems.archeron_wings.Archeron_Wings;
import me.aknyzor.customitems.attor.Attor;
import me.aknyzor.customitems.denier_of_destiny.Denier_of_Destiny;
import me.aknyzor.customitems.hyberns_blade.Hyberns_Blade;
import me.aknyzor.customitems.illyrian_wings.Illyrian_Wings;
import me.aknyzor.customitems.shadowsinger.Shadowsinger;
import me.aknyzor.customitems.shield_of_siphons.Shield_of_Siphons;
import me.aknyzor.customitems.suriel.Suriel;
import me.aknyzor.customitems.the_bone_carver.The_Bone_Carver;
import me.aknyzor.customitems.the_grave_digger.The_Grave_Digger;
import me.aknyzor.customitems.the_oratrices_judgement.The_Oratrices_Judgement;
import me.aknyzor.customitems.the_spine_splitter.The_Spine_Splitter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class ListenerManager {

    private static final List<Object> LISTENERS = Arrays.asList(
            new Attor(),
            new Suriel(),
            new The_Grave_Digger(),
            new Shield_of_Siphons(),
            new The_Bone_Carver(),
            new Shadowsinger(),
            new Illyrian_Wings(),
            new The_Spine_Splitter(),
            new Angel_Kiss(),
            new The_Oratrices_Judgement(),
            new Denier_of_Destiny(),
            new Hyberns_Blade(),
            new Archeron_Wings()
    );

    public ListenerManager(JavaPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        LISTENERS.forEach(listener -> pm.registerEvents((org.bukkit.event.Listener) listener, plugin));
    }
}
