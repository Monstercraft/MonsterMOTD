package org.monstercraft.monsterticket.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.command.GameCommand;

public class List extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("modlist");
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
		int i = 1;
		sender.sendMessage(ChatColor.RED + "Listing all online Mods");
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Ticket.getHandleManager().getPermissionsHandler().hasModPerm(p)) {
				sender.sendMessage(ChatColor.GREEN + "" + i + ". "
						+ p.getName());
			}
		}
		return true;
	}

	@Override
	public String getPermission() {
		return "monsterticket.list";
	}

}
