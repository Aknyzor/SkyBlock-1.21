package me.aknyzor.toplist;

import com.bencodez.votingplugin.events.PlayerVoteEvent;
import me.aknyzor.SkyBlock;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.EnumSet;

public class Top10Events implements Listener {

    private final TopListHandler blockBreakTopList;
    private final TopListHandler mobKillTopList;
    private final TopListHandler harvestTopList;
    private final TopListHandler voteTopList;
    private final TopListHandler fishingTopList;
    private final TopListHandler placeTopList;

    private static final String BLOCK_PLACED_BY_PLAYER = "placedByPlayer";

    public Top10Events(TopListHandler blockBreakTopList, TopListHandler mobKillTopList, TopListHandler harvestTopList, TopListHandler voteTopList, TopListHandler fishingTopList, TopListHandler placeTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;
        this.harvestTopList = harvestTopList;
        this.voteTopList = voteTopList;
        this.fishingTopList = fishingTopList;
        this.placeTopList = placeTopList;
    }

    @EventHandler
    public void onFishing(PlayerFishEvent event) {
        if (fishingTopList == null) {
            return;
        }

        if (event.getCaught() != null) {
            fishingTopList.addPlayer(event.getPlayer().getName(), 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        if (blockBreakTopList == null) {
            return;
        }

        if (event.getBlock().hasMetadata(BLOCK_PLACED_BY_PLAYER)) {
            return;
        }

        blockBreakTopList.addPlayer(event.getPlayer().getName(), 1);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        event.getBlock().setMetadata(BLOCK_PLACED_BY_PLAYER, new FixedMetadataValue(SkyBlock.getInstance(), true));

        if (placeTopList == null) {
            return;
        }

        placeTopList.addPlayer(event.getPlayer().getName(), 1);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHarvest(BlockBreakEvent event) {
        if (harvestTopList == null) {
            return;
        }

        if(event.getBlock().getBlockData() instanceof Ageable) {
            if (isCropsBlock(event.getBlock().getType()) || isCropsMaterial(event.getBlock().getType())) {
                Ageable ab = (Ageable) event.getBlock().getBlockData();
                if (ab.getAge() == ab.getMaximumAge()) {
                    harvestTopList.addPlayer(event.getPlayer().getName(), 1);
                }
            }
        }
    }

    @EventHandler
    public void onVote(PlayerVoteEvent event) {
        if (voteTopList == null) {
            return;
        }

        voteTopList.addPlayer(event.getPlayer(), 1);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (mobKillTopList == null) {
            return;
        }
        if (event.getEntity().getKiller() != null) {
            String playerName = event.getEntity().getKiller().getName();
            mobKillTopList.addPlayer(playerName, 1);
        }
    }

    private static final EnumSet<Material> CROPS_MATERIALS = EnumSet.of(
            Material.WHEAT,
            Material.BEETROOTS,
            Material.CARROTS,
            Material.POTATOES,
            Material.COCOA,
            Material.NETHER_WART
    );

    private static final EnumSet<Material> CROPS_BLOCKS = EnumSet.of(
            Material.MELON,
            Material.PUMPKIN,
            Material.CACTUS,
            Material.CHORUS_PLANT,
            Material.SUGAR_CANE,
            Material.BAMBOO
    );

    private boolean isCropsMaterial(Material material) {
        return CROPS_MATERIALS.contains(material);
    }

    private boolean isCropsBlock(Material material) {
        return CROPS_BLOCKS.contains(material);
    }

}