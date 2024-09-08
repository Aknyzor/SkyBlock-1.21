package me.aknyzor.toplist;

import com.bencodez.votingplugin.events.PlayerVoteEvent;
import me.aknyzor.SkyBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.EnumSet;

public class Top10Events implements Listener {

    private final TopListHandler blockBreakTopList;
    private final TopListHandler mobKillTopList;
    private final TopListHandler harvestTopList;

    private final TopListHandler voteTopList;

    public Top10Events(TopListHandler blockBreakTopList, TopListHandler mobKillTopList, TopListHandler harvestTopList, TopListHandler voteTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;
        this.harvestTopList = harvestTopList;
        this.voteTopList = voteTopList;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (blockBreakTopList == null) {
            return;
        }

        if (event.getBlock().hasMetadata("PLACED")) {
            return;
        }

        String playerName = event.getPlayer().getName();
        blockBreakTopList.addPlayer(playerName, 1);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
        voteTopList.addPlayer(event.getPlayer(), 1);
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlock();
        if (!b.hasMetadata("PLACED")) {
            b.setMetadata("PLACED", new FixedMetadataValue(SkyBlock.getInstance(), "PLACED"));
        }
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