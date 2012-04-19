package org.monstercraft.monsterticket.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.command.GameCommand;
import org.monstercraft.monsterticket.util.Variables;
import org.monstercraft.monsterticket.wrappers.HelpTicket;

public class Close extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("done");
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
			sender.sendMessage("You must be a player to close a support ticket!");
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
				Variables.tickets.remove(t);
				Player p = Bukkit.getPlayer(t.getPlayerName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manudelp "
						+ t.getPlayerName() + " towny.chat.support");
				if (p != null) {
					p.sendMessage(ChatColor.GREEN
							+ "Your support ticket has been closed.");
				}
				sender.sendMessage(ChatColor.GREEN + "Ticket " + t.getID()
						+ " sucessfully closed.");
				for (Player pl : Bukkit.getOnlinePlayers()) {
					if (Ticket.getHandleManager().getPermissionsHandler()
							.hasModPerm(pl)) {
						pl.sendMessage(ChatColor.GREEN + sender.getName()
								+ " closed ticket " + t.getID());
					}
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
		return "monsterticket.complete";
	}

}
