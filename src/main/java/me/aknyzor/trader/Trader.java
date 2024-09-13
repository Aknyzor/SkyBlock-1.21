package me.aknyzor.trader;

import me.aknyzor.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Trader {

    private final JavaPlugin plugin;
    private boolean mobFound = false;
    private final List<String> cities = Arrays.asList("Valordar", "Nyrondor", "Kragathor");
    private final List<String> villages = Arrays.asList("Selenthia", "Elderstone", "Windermoor");
    private final Random random = new Random();

    public Trader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleNPC() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkAndCreateNPC, 0L, 20L);
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkNPC, 0L, 20L);
    }

    private void checkNPC() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        if ((day == Calendar.TUESDAY || day == Calendar.FRIDAY)
                && (hour >= 2 && hour < 23) && minute == 0 && second == 0) {

            World world = Bukkit.getWorld("world");

            if (world == null) {
                SkyBlock.getInstance().getLogger().info("Svet 'world' ne postoji.");
                return;
            }

            Location minLocation = new Location(world, -117, 72, -19);
            Location maxLocation = new Location(world, -114, 71, -16);

            Chunk chunk = minLocation.getChunk();

            if (!chunk.isLoaded()) {
                chunk.load();
            }

            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;

                    if (livingEntity.getType() == EntityType.PIG &&
                            livingEntity.getLocation().getX() >= minLocation.getX() &&
                            livingEntity.getLocation().getX() <= maxLocation.getX() &&
                            livingEntity.getLocation().getY() >= maxLocation.getY() &&
                            livingEntity.getLocation().getY() <= minLocation.getY() &&
                            livingEntity.getLocation().getZ() >= minLocation.getZ() &&
                            livingEntity.getLocation().getZ() <= maxLocation.getZ()) {

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dulog NPC je već spawnovan.");
                        mobFound = true;
                        break;
                    }
                }
            }

            if (!mobFound) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dulog Nije bio pronađen Farmer npc, verovatno je bio offline server, ponovo je stvoren npc.");
                createNPC();
            }
        }
    }

    private void checkAndCreateNPC() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        if ((day == Calendar.TUESDAY || day == Calendar.FRIDAY)
                && hour == 1 && minute == 0 && second == 0) {
            createNPC();
        } else if ((day == Calendar.TUESDAY || day == Calendar.FRIDAY)
                && hour == 23 && minute == 0 && second == 0) {
            destroyNPC();
        }
    }

    private void createNPC() {
        String destination;
        destination = (random.nextBoolean() ? cities : villages).get(random.nextInt(3));

        plugin.getLogger().info("Trgovac ide u " + destination);
        switch (destination) {
            case "Valordar" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs spawn npc_farmer_valordar 1 world,-115,71,-17,-45,0");
            case "Nyrondor" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs spawn npc_farmer_nyrondor 1 world,-115,71,-17,-45,0");
            case "Kragathor" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs spawn npc_farmer_kragathor 1 world,-115,71,-17,-45,0");
            case "Selenthia" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs spawn npc_farmer_selenthia 1 world,-115,71,-17,-45,0");
            case "Elderstone" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs spawn npc_farmer_elderstone 1 world,-115,71,-17,-45,0");
            case "Windermoor" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs spawn npc_farmer_windermoor 1 world,-115,71,-17,-45,0");

            default -> {}
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§6Trgovac je upravo stigao u grad! Pronađite ga na spawnu ako želite da prodate neke iteme.");
        }
    }

    private void destroyNPC() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_valordar");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_nyrondor");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_kragathor");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_selenthia");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_elderstone");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_windermoor");
    }
}