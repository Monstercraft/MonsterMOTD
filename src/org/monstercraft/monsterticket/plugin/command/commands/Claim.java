package org.monstercraft.monsterticket.plugin.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.plugin.Configuration.Variables;
import org.monstercraft.monsterticket.plugin.command.GameCommand;
import org.monstercraft.monsterticket.plugin.wrappers.HelpTicket;
import org.monstercraft.monsterticket.plugin.wrappers.PrivateChatter;

public class Claim extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("claim");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		if (sender instanceof Player) {
			if (Ticket.getHandleManager().getPermissionsHandler() != null) {
				if (!Ticket.getHandleManager().getPermissionsHandler()
						.hasCommandPerms(((Player) sender), this)) {
					sender.sendMessage("You don't have permission to preform that command.");
					return true;
				}
			} else {
				sender.sendMessage("Permissions not detected, unable to run any ticket commands.");
				return true;
			}
		}
		if (split.length < 2) {
			sender.sendMessage(ChatColor.RED + "Invalid command usage!");
			return true;
		}
		if (!canParse(split[1])) {
			sender.sendMessage(ChatColor.GREEN + "Invalid number!");
			return true;
		}
		for (PrivateChatter pc : Variables.priv) {
			if (pc.getMod().equals((Player) sender)) {
				sender.sendMessage(ChatColor.GREEN
						+ "You are already supporting someone!");
				return true;
			}
		}
		for (HelpTicket t : Variables.tickets.keySet()) {
			if (t.getID() == Integer.parseInt(split[1])) {
				Player playa = Bukkit.getPlayer(t.getPlayerName());
				if (playa == null) {
					sender.sendMessage(ChatColor.RED
							+ "Player not online, unable to claim the ticket");
					Variables.tickets.remove(t);
					return true;
				}
				if (Variables.tickets.get(t)) {
					sender.sendMessage(ChatColor.RED
							+ "Ticket already claimed.");
					return true;
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (Ticket.getHandleManager().getPermissionsHandler()
							.hasModPerm(p)) {
						if (sender instanceof Player) {
							p.sendMessage(ChatColor.GREEN + sender.getName()
									+ " is now handeling ticket " + t.getID());
						} else {
							p.sendMessage(ChatColor.GREEN
									+ "A mod in IRC is now handeling ticket "
									+ t.getID());
						}
					}
				}
				Variables.tickets.put(t, true);
				Variables.priv.add(new PrivateChatter((Player) sender, playa));
				Player p = Bukkit.getPlayer(t.getPlayerName());
				sender.sendMessage(ChatColor.GREEN + "Ticket " + t.getID()
						+ " sucessfully claimed.");
				if (p != null) {
					p.sendMessage(ChatColor.GREEN
							+ "******************************************************");
					p.sendMessage(ChatColor.RED
							+ "Your support ticket request has been accepted!");
					p.sendMessage(ChatColor.RED
							+ "Start chatting with the mod assisting you.");
					p.sendMessage(ChatColor.GREEN
							+ "******************************************************");
					p.sendMessage(ChatColor.GREEN
							+ "******************************************************");
					p.sendMessage(ChatColor.RED + "[Support]"
							+ sender.getName() + ": " + ChatColor.WHITE
							+ " Hello, my name is " + ChatColor.GOLD
							+ sender.getName() + ChatColor.WHITE
							+ " how can I help you?");
					sender.sendMessage(ChatColor.RED + "[Support]"
							+ sender.getName() + ": " + ChatColor.WHITE
							+ " Hello, my name is " + ChatColor.GOLD
							+ sender.getName() + ChatColor.WHITE
							+ " how can I help you?");
				}
				break;
			}
		}
		return true;
	}

	private boolean canParse(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	@Override
	public String getPermission() {
		return "monsterticket.claim";
	}

}
