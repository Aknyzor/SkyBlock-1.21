package me.aknyzor.trader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 *
 * I dalje je u izradi (Aknyzor)
 * 27.6.2024. Dodati da proverava da li je npc spawnovan, ako nije da ga spawnuje(u slučaju pucanja servera), i odraditi malo bolje kod.
 * 27.6.2024. Dodati da budu drugačije komande za svaku destinaciju
 * 29.6.2024. Dodati poruke za npca i još neka podešavanja
 * 29.6.2024. Dodati poruku kad se trgovac spawna
 */

public class Trader {
    private final JavaPlugin plugin;
    private final String npcName = "Trgovac";
    private final List<String> cities = Arrays.asList("Grad1", "Grad2", "Grad3");
    private final List<String> villages = Arrays.asList("Selo1", "Selo2", "Selo3");
    private final Random random = new Random();

    public Trader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleNPC() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkAndCreateNPC, 0L, 20L);
    }

    private void checkAndCreateNPC() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        if ((day == Calendar.THURSDAY || day == Calendar.FRIDAY)
                && hour == 18 && minute == 10 && second == 0) {
            createNPC();
        }else if ((day == Calendar.THURSDAY || day == Calendar.FRIDAY)
                && hour == 23 && minute == 0 && second == 0) {
            destroyNPC();
        }
    }

    private void createNPC() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc create " + npcName + " --type player --at 11,63,0,world");

        String destination;
        destination = (random.nextBoolean() ? cities : villages).get(random.nextInt(3));

        plugin.getLogger().info("NPC ide u " + destination);

        if (destination != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npcName);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc command add --hand right " + destination);
        }
    }

    private void destroyNPC() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npcName);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc remove");
    }

    public void onDisable() {
        destroyNPC();
    }
}