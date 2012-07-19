package org.monstercraft.monsterticket.plugin.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.plugin.command.GameCommand;

public class Reload extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("mtreload");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		if (sender instanceof Player) {
			if (Ticket.getHandleManager().getPermissionsHandler() != null) {
				if (!Ticket.getHandleManager().getPermissionsHandler()
						.hasCommandPerms(((Player) sender), this)) {
					sender.sendMessage("You don't have permission to preform this command.");
					return true;
				}
			} else {
				sender.sendMessage("Permissions not detected, unable to run any ticket commands.");
				return true;
			}
		}
		Ticket.getSettingsManager().load();
		sender.sendMessage(ChatColor.GREEN
				+ "Successfully reloaded plugin settings!");
		return true;
	}

	@Override
	public String getPermission() {
		return "monstertickets.reload";
	}

}
