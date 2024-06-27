package me.aknyzor.trader;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.CommandTrait;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Trader {

    /**
     *
     * I dalje je u izradi (Aknyzor)
     *
     */

    private final JavaPlugin plugin;
    private NPC npc;
    private final List<String> cities = Arrays.asList("Grad1", "Grad2", "Grad3");
    private final List<String> villages = Arrays.asList("Selo1", "Selo2", "Selo3");
    private final Random random = new Random();

    public Trader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleNPC() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkAndCreateNPC, 0L, 1200L);
    }

    private void checkAndCreateNPC() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if ((dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.FRIDAY) && hourOfDay == 1) {
            if (npc == null || !npc.isSpawned()) {
                createNPC();
            }
        } else if ((dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.FRIDAY) && hourOfDay == 23) {
            if (npc != null && npc.isSpawned()) {
                npc.destroy();
                npc = null;
            }
        }
    }

    private void createNPC() {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        npc = registry.createNPC(EntityType.PLAYER, "Trgovac");

        Location location = new Location(Bukkit.getWorld("world"), 0, 64, 0);
        npc.spawn(location);
        npc.getOrAddTrait(LookClose.class).lookClose(true);

        String destination = (random.nextBoolean() ? cities : villages).get(random.nextInt(3));
        plugin.getLogger().info("NPC ide u " + destination);

        CommandTrait commandTrait = npc.getOrAddTrait(CommandTrait.class);

        switch (destination) {
            case "Grad1" -> {
                CommandTrait.NPCCommandBuilder commandBuilder = new CommandTrait.NPCCommandBuilder("grad1", CommandTrait.Hand.RIGHT);
                commandTrait.addCommand(commandBuilder);
            }
            case "Grad2" -> {
                CommandTrait.NPCCommandBuilder commandBuilder = new CommandTrait.NPCCommandBuilder("grad2", CommandTrait.Hand.RIGHT);
                commandTrait.addCommand(commandBuilder);
            }
            case "Grad3" -> {
                CommandTrait.NPCCommandBuilder commandBuilder = new CommandTrait.NPCCommandBuilder("grad3", CommandTrait.Hand.RIGHT);
                commandTrait.addCommand(commandBuilder);
            }
            case "Selo1" -> {
                CommandTrait.NPCCommandBuilder commandBuilder = new CommandTrait.NPCCommandBuilder("selo1", CommandTrait.Hand.RIGHT);
                commandTrait.addCommand(commandBuilder);
            }
            case "Selo2" -> {
                CommandTrait.NPCCommandBuilder commandBuilder = new CommandTrait.NPCCommandBuilder("selo2", CommandTrait.Hand.RIGHT);
                commandTrait.addCommand(commandBuilder);
            }
            case "Selo3" -> {
                CommandTrait.NPCCommandBuilder commandBuilder = new CommandTrait.NPCCommandBuilder("selo3", CommandTrait.Hand.RIGHT);
                commandTrait.addCommand(commandBuilder);
            }
        }
    }

    public void onDisable() {
        if (npc != null && npc.isSpawned()) {
            npc.destroy();
        }
    }
}