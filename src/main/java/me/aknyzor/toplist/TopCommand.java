package me.aknyzor.toplist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TopCommand implements CommandExecutor {

    /**
     *
     * I dalje je u izradi (Aknyzor)
     *
     */

    private final TopListHandler blockBreakTopList;
    private final TopListHandler mobKillTopList;

    public TopCommand(TopListHandler blockBreakTopList, TopListHandler mobKillTopList) {
        this.blockBreakTopList = blockBreakTopList;
        this.mobKillTopList = mobKillTopList;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /top <blocks|mobs>");
            return true;
        }

        if (args[0].equalsIgnoreCase("blocks")) {
            displayTopList(sender, blockBreakTopList, "Top 10 Players by Blocks Broken:");
            blockBreakTopList.clearTopList();
            return true;
        } else if (args[0].equalsIgnoreCase("mobs")) {
            displayTopList(sender, mobKillTopList, "Top 10 Players by Mobs Killed:");
            return true;
        } else {
            sender.sendMessage("Usage: /top <blocks|mobs>");
            return true;
        }
    }

    private void displayTopList(CommandSender sender, TopListHandler topListHandler, String title) {
        sender.sendMessage(title);
        int rank = 1;
        for (String playerName : topListHandler.getTopPlayers().keySet()) {
            if (rank > 10) break;
            sender.sendMessage(rank + ". " + playerName + ": " + topListHandler.getTopPlayers().get(playerName));
            rank++;
        }
    }
}
