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
    private final Random random = new Random();

    public Trader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleNPC() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkAndCreateNPC, 0L, 20L);
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkNPC, 0L, 20L);
    }

    private void checkNPC() {
        if (shouldCheckNPC()) {
            World world = Bukkit.getWorld("world");
            if (world == null) {
                SkyBlock.getInstance().getLogger().info("Svet 'world' ne postoji.");
                return;
            }

            Location minLocation = new Location(world, -117, 72, -19);
            Location maxLocation = new Location(world, -114, 71, -16);
            Chunk chunk = minLocation.getChunk();

            loadChunkIfNeeded(chunk);

            if (isMobFoundInChunk(chunk, minLocation, maxLocation)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dulog NPC je već spawnovan.");
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dulog Nije bio pronađen Farmer npc, verovatno je bio offline server, ponovo je stvoren npc.");
                createNPC();
            }
        }
    }

    private boolean shouldCheckNPC() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return (day == Calendar.TUESDAY || day == Calendar.FRIDAY) &&
                (hour >= 2 && hour < 23) && minute == 15 && second == 0;
    }

    private void loadChunkIfNeeded(Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
    }

    private boolean isMobFoundInChunk(Chunk chunk, Location minLocation, Location maxLocation) {
        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof LivingEntity livingEntity && livingEntity.getType() == EntityType.PIG &&
                    isEntityWithinBounds(livingEntity, minLocation, maxLocation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEntityWithinBounds(LivingEntity entity, Location minLocation, Location maxLocation) {
        Location loc = entity.getLocation();
        return loc.getX() >= minLocation.getX() && loc.getX() <= maxLocation.getX() &&
                loc.getY() >= maxLocation.getY() && loc.getY() <= minLocation.getY() &&
                loc.getZ() >= minLocation.getZ() && loc.getZ() <= maxLocation.getZ();
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
            p.sendMessage("§6Trgovac je upravo stigao u naš grad i doneo sa sobom razne mogućnosti za zaradu! " +
                    "Ako imate vredne predmete koje želite da prodate ili trampite, sada je pravo vreme da ga potražite. " +
                    "Nalazi se na spawnu, spreman da pregovara i ponudi vam najbolje cene. " +
                    "Ne propustite ovu priliku da unovčite svoje resurse!");
        }
    }

    private void destroyNPC() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_valordar");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_nyrondor");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_kragathor");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_selenthia");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_elderstone");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm mobs kill npc_farmer_windermoor");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§6Trgovac je napustio grad i krenuo na svoju sledeću destinaciju! " +
                    "Ako niste stigli da prodate svoje predmete, ne brinite – vratiće se uskoro sa novim prilikama za trgovinu. " +
                    "Do tada, nastavite da prikupljate vredne resurse i budite spremni za njegov povratak!");
        }
    }
}