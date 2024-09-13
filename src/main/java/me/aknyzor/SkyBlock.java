package me.aknyzor;

import me.aknyzor.customitems.Hearts;
import me.aknyzor.customitems.ListenerManager;
import me.aknyzor.fishing.CustomCaught;
import me.aknyzor.fishing.FishingAFK;
import me.aknyzor.spawner.SpawnerSpawning;
import me.aknyzor.toplist.*;
import me.aknyzor.trader.Trader;
import me.aknyzor.zabrane.Zabrane;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Calendar;

@SuppressWarnings("unused")
public final class SkyBlock extends JavaPlugin {

    private static SkyBlock instance;
    private TopListHandler blockBreakTopList;
    private TopListHandler mobKillTopList;

    private TopListHandler harvestTopList;
    private TopListHandler voteTopList;

    @Override
    public void onEnable() {
        instance = this;
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

        blockBreakTopList = new TopListHandler("blockbreak_toplist.yml", this);
        mobKillTopList = new TopListHandler("mobkill_toplist.yml", this);
        harvestTopList = new TopListHandler("harvest_toplist.yml", this);
        voteTopList = new TopListHandler("vote_toplist.yml", this);

        Bukkit.getServer().getPluginManager().registerEvents(new FishingAFK(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RewardListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Zabrane(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SpawnerSpawning(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CustomCaught(), this);
        new ListenerManager(this);
        Bukkit.getServer().getPluginManager().registerEvents(new Top10Events(blockBreakTopList, mobKillTopList, harvestTopList, voteTopList), this);

        Trader trader = new Trader(this);
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
                    new WeeklyRewardScheduler(blockBreakTopList, mobKillTopList, harvestTopList, voteTopList).run();
                }
            }
        }.runTaskTimer(this, 0L, 20L);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Top10Placeholders(this).register();
        }

        PluginCommand topCommand = getCommand("eventtop");
        if (topCommand != null) {
            topCommand.setExecutor(new TopCommand(blockBreakTopList, mobKillTopList, harvestTopList, voteTopList));
        } else {
            getLogger().warning("Command 'eventtop' is not defined in plugin.yml or failed to load.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("=========================================");
        getLogger().info("SkyBlock dodatak je uspešno ugašen.");
        getLogger().info("Napravljeno od strane Aknyzor");
        getLogger().info("=========================================");

        blockBreakTopList.saveConfig();
        mobKillTopList.saveConfig();
        harvestTopList.saveConfig();
        voteTopList.saveConfig();


        // brisanje hashmapa od custom itema
        Hearts.clearAllAttributes();
    }

    public TopListHandler getBlockBreakTopList() {
        return blockBreakTopList;
    }

    public TopListHandler getMobKillTopList() {
        return mobKillTopList;
    }

    public TopListHandler getHarvestTopList() {
        return harvestTopList;
    }

    public TopListHandler getVoteTopList() {
        return voteTopList;
    }

    public static SkyBlock getInstance() {
        return instance;
    }
}
