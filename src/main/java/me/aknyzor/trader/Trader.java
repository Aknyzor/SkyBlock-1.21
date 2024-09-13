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
    private final List<String> cities = Arrays.asList("Valordar", "Nyrondor", "Kragathor");
    private final List<String> villages = Arrays.asList("Selenthia", "Elderstone", "Windermoor");

    public Trader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleNPC() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkAndCreateNPC, 0L, 20L);
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkNPC, 0L, 20L);
    }

    private void checkNPC() {
        if (isNPCCheckTime()) {
            World world = Bukkit.getWorld("world");
            if (world == null) {
                SkyBlock.getInstance().getLogger().info("Svet 'world' ne postoji.");
                return;
            }

            if (isNPCPresent(world)) {
                logNPCAlreadySpawned();
            } else {
                logNPCNotFound();
                createNPC();
            }
        }
    }

    private boolean isNPCCheckTime() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return (day == Calendar.TUESDAY || day == Calendar.FRIDAY)
                && (hour >= 2 && hour < 23) && minute == 0 && second == 0;
    }

    private boolean isNPCPresent(World world) {
        Location minLocation = new Location(world, -117, 72, -19);
        Location maxLocation = new Location(world, -114, 71, -16);

        Chunk chunk = minLocation.getChunk();
        if (!chunk.isLoaded()) {
            chunk.load();
        }

        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof LivingEntity) {
                if (isPigInLocation((LivingEntity) entity, minLocation, maxLocation)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPigInLocation(LivingEntity livingEntity, Location minLocation, Location maxLocation) {
        return livingEntity.getType() == EntityType.PIG &&
                livingEntity.getLocation().getX() >= minLocation.getX() &&
                livingEntity.getLocation().getX() <= maxLocation.getX() &&
                livingEntity.getLocation().getY() >= minLocation.getY() &&
                livingEntity.getLocation().getY() <= maxLocation.getY() &&
                livingEntity.getLocation().getZ() >= minLocation.getZ() &&
                livingEntity.getLocation().getZ() <= maxLocation.getZ();
    }

    private void logNPCAlreadySpawned() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dulog NPC je već spawnovan.");
    }

    private void logNPCNotFound() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dulog Nije bio pronađen Farmer npc, verovatno je bio offline server, ponovo je stvoren npc.");
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
        Random random = new Random();
        String destination = getRandomDestination(random);

        plugin.getLogger().info("Trgovac ide u " + destination);
        spawnNPC(destination);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§6Trgovac je upravo stigao u grad! Pronađite ga na spawnu ako želite da prodate neke iteme.");
        }
    }

    private String getRandomDestination(Random random) {
        return (random.nextBoolean() ? cities : villages).get(random.nextInt(3));
    }

    private void spawnNPC(String destination) {
        String command = switch (destination) {
            case "Valordar" -> "mm mobs spawn npc_farmer_valordar 1 world,-115,71,-17,-45,0";
            case "Nyrondor" -> "mm mobs spawn npc_farmer_nyrondor 1 world,-115,71,-17,-45,0";
            case "Kragathor" -> "mm mobs spawn npc_farmer_kragathor 1 world,-115,71,-17,-45,0";
            case "Selenthia" -> "mm mobs spawn npc_farmer_selenthia 1 world,-115,71,-17,-45,0";
            case "Elderstone" -> "mm mobs spawn npc_farmer_elderstone 1 world,-115,71,-17,-45,0";
            case "Windermoor" -> "mm mobs spawn npc_farmer_windermoor 1 world,-115,71,-17,-45,0";
            default -> throw new IllegalArgumentException("Nepoznata destinacija: " + destination);
        };
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    private void destroyNPC() {
        List<String> destinations = Arrays.asList("valordar", "nyrondor", "kragathor", "selenthia", "elderstone", "windermoor");
        for (String destination : destinations) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_" + destination);
        }
    }
}