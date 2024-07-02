package me.aknyzor.toplist;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Top10Events implements Listener {

    /**
     *
     * I dalje je u izradi (Aknyzor)
     *
     */

    private final TopListHandler blockBreakTopList;
    private final TopListHandler mobKillTopList;

    public Top10Events(TopListHandler blockBreakTopList, TopListHandler mobKillTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (blockBreakTopList == null) {
            return;
        }
        String playerName = event.getPlayer().getName();
        blockBreakTopList.addPlayer(playerName, 1);
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
}