package me.aknyzor;

import me.aknyzor.fishing.CustomCaught;
import me.aknyzor.fishing.FishingArea;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("=========================================");
        getLogger().info("SkyBlock dodatak je uspešno učitan.");
        getLogger().info("Napravljeno od strane Aknyzor");
        getLogger().info("=========================================");


        Bukkit.getServer().getPluginManager().registerEvents(new FishingArea(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CustomCaught(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("=========================================");
        getLogger().info("SkyBlock dodatak je uspešno ugašen.");
        getLogger().info("Napravljeno od strane Aknyzor");
        getLogger().info("=========================================");
    }
}
