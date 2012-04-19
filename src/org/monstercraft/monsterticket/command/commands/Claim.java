package org.monstercraft.monsterticket.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.command.GameCommand;
import org.monstercraft.monsterticket.util.Variables;
import org.monstercraft.monsterticket.wrappers.HelpTicket;

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
		} else {
			sender.sendMessage("You must be a player to claim a ticket!");
			return true;
		}
		if (split.length < 2) {
			sender.sendMessage(ChatColor.RED + "Invalid command usage!");
			return true;
		}
		if (!canParse(split[1])) {
			sender.sendMessage(ChatColor.GREEN + "Invalid number!");
			return true;
		}
		for (HelpTicket t : Variables.tickets.keySet()) {
			if (t.getID() == Integer.parseInt(split[1])) {
				if (Bukkit.getPlayer(t.getPlayerName()) == null) {
					sender.sendMessage("Player not online, unable to claim the ticket");
					return true;
				}
				if (Variables.tickets.get(t)) {
					sender.sendMessage("Ticket already claimed");
					return true;
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (Ticket.getHandleManager().getPermissionsHandler()
							.hasModPerm(p)) {
						p.sendMessage(ChatColor.GREEN + sender.getName()
								+ " is now handeling ticket " + t.getID());
					}
				}
				Variables.tickets.put(t, true);
				Player p = Bukkit.getPlayer(t.getPlayerName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manuaddp "
						+ t.getPlayerName() + " towny.chat.support");
				if (p != null) {
					p.sendMessage(ChatColor.GREEN
							+ "******************************************************");
					p.sendMessage(ChatColor.GREEN
							+ "Your support ticket request has been accepted!");
					p.sendMessage(ChatColor.GREEN
							+ "Now moving you to the support channel!"
							+ ChatColor.DARK_RED
							+ " Type: /s Message Here to recieve support!");
					p.sendMessage(ChatColor.GREEN
							+ "******************************************************");
				}
//				Bukkit.getServer()
//						.dispatchCommand(
//								sender,
//								"s Welcome to Support Channel! I am your assigned helper, how many I help you today?");
				Ticket.log(sender.getName() + " ClaimCommand");
				sender.sendMessage(ChatColor.GREEN + "Ticket " + t.getID()
						+ " sucessfully claimed.");
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
