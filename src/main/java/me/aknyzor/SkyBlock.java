package me.aknyzor;

import me.aknyzor.fishing.CustomCaught;
import me.aknyzor.fishing.FishingArea;
import me.aknyzor.trader.Trader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class SkyBlock extends JavaPlugin {

    private Trader trader;
    @Override
    public void onEnable() {
        getLogger().info("=========================================");
        getLogger().info("SkyBlock dodatak je uspešno učitan.");
        getLogger().info("Napravljeno od strane Aknyzor");
        getLogger().info("=========================================");


        Bukkit.getServer().getPluginManager().registerEvents(new FishingArea(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CustomCaught(), this);
        trader = new Trader(this);
        trader.scheduleNPC();
    }

    @Override
    public void onDisable() {
        getLogger().info("=========================================");
        getLogger().info("SkyBlock dodatak je uspešno ugašen.");
        getLogger().info("Napravljeno od strane Aknyzor");
        getLogger().info("=========================================");

        trader.onDisable();
    }
}
