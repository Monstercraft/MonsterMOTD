package org.monstercraft.monsterticket.plugin.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.plugin.Configuration.Variables;
import org.monstercraft.monsterticket.plugin.command.GameCommand;
import org.monstercraft.monsterticket.plugin.wrappers.HelpTicket;

public class Open extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("request");
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
		} else {
			sender.sendMessage("You must be ingame to open a ticket!");
			return true;
		}
		for (HelpTicket t : Variables.tickets.keySet()) {
			Player p = Bukkit.getPlayer(t.getPlayerName());
			if (p == null) {
				continue;
			}
			if (p.equals(sender)) {
				p.sendMessage(ChatColor.GREEN
						+ "You can only have 1 ticket at a time!");
				return true;
			}
		}
		if (split.length < 2) {
			sender.sendMessage(ChatColor.RED
					+ "Invalid description of problem.");
			return true;
		}
		StringBuffer desc = new StringBuffer();
		for (int i = 1; i < split.length; i++) {
			desc.append(split[i]);
			desc.append(" ");
		}
		HelpTicket t = new HelpTicket(Variables.ticketid, desc.toString()
				.trim().replace("|", "").replace("=", ""), sender.getName());
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Ticket.getHandleManager().getPermissionsHandler().hasModPerm(p)) {
				p.sendMessage(ChatColor.GREEN + sender.getName()
						+ " opened ticket " + t.getID());
			}
		}
		Variables.ticketid++;
		sender.sendMessage(ChatColor.RED + "Help ticket successfully opened!");
		sender.sendMessage(ChatColor.GREEN + "" + t.getID() + " - "
				+ t.getPlayerName() + " - " + t.getDescription());
		Variables.tickets.put(t, false);
		return true;
	}

	@Override
	public String getPermission() {
		return "monstertickets.help";
	}

}
