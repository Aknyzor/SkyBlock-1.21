package me.aknyzor;

import me.aknyzor.fishing.CustomCaught;
import me.aknyzor.fishing.FishingArea;
import me.aknyzor.toplist.*;
import me.aknyzor.trader.Trader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;

@SuppressWarnings("unused")
public final class SkyBlock extends JavaPlugin {
    private Trader trader;
    private TopListHandler blockBreakTopList;
    private TopListHandler mobKillTopList;
    @Override
    public void onEnable() {
        getLogger().info("=========================================");
        getLogger().info("SkyBlock dodatak je uspešno učitan.");
        getLogger().info("Napravljeno od strane Aknyzor");
        getLogger().info("=========================================");

        File dataFolder = this.getDataFolder();
        if (!dataFolder.exists()) {
            boolean created = dataFolder.mkdirs();
            if (!created) {
                this.getLogger().warning("Nije moguće kreirati folder za podatke.");
            }
        }

        blockBreakTopList = new TopListHandler(this, new File(getDataFolder(), "blockbreak_toplist.yml"));
        mobKillTopList = new TopListHandler(this, new File(getDataFolder(), "mobkill_toplist.yml"));

        Bukkit.getServer().getPluginManager().registerEvents(new FishingArea(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CustomCaught(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Top10Events(blockBreakTopList, mobKillTopList), this);

        trader = new Trader(this);
        trader.scheduleNPC();

        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                if (day == Calendar.SUNDAY && hour == 23 && minute == 0 && second == 0) {
                    new WeeklyRewardScheduler(blockBreakTopList, mobKillTopList).run();
                }
            }
        }.runTaskTimer(this, 0L, 20L);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Top10Placeholders(this).register();
        }

        Objects.requireNonNull(getCommand("top")).setExecutor(new TopCommand(blockBreakTopList, mobKillTopList));
    }

    @Override
    public void onDisable() {
        getLogger().info("=========================================");
        getLogger().info("SkyBlock dodatak je uspešno ugašen.");
        getLogger().info("Napravljeno od strane Aknyzor");
        getLogger().info("=========================================");

        trader.onDisable();
    }

    public TopListHandler getBlockBreakTopList() {
        return blockBreakTopList;
    }

    public TopListHandler getMobKillTopList() {
        return mobKillTopList;
    }
}
