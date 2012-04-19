package org.monstercraft.monsterticket.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.command.GameCommand;

public class Kick extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("fkick");
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
		if (split.length < 2) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments.");
			return true;
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manudelp "
				+ split[1] + " towny.chat.support");
		sender.sendMessage(ChatColor.GREEN + "Successfully kicked " + split[1]
				+ "from the support channel!");
		return true;
	}

	@Override
	public String getPermission() {
		return "monsterticket.kick";
	}

}
