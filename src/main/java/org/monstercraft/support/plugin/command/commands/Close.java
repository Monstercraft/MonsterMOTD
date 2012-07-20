package org.monstercraft.support.plugin.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.support.MonsterTickets;
import org.monstercraft.support.plugin.Configuration.Variables;
import org.monstercraft.support.plugin.command.GameCommand;
import org.monstercraft.support.plugin.wrappers.HelpTicket;
import org.monstercraft.support.plugin.wrappers.PrivateChatter;

public class Close extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("close");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		if (sender instanceof Player) {
			if (MonsterTickets.getHandleManager().getPermissionsHandler() != null) {
				if (!MonsterTickets.getHandleManager().getPermissionsHandler()
						.hasCommandPerms(((Player) sender), this)) {
					sender.sendMessage("You don't have permission to preform this command.");
					return true;
				}
			} else {
				sender.sendMessage("Permissions not detected, unable to run any ticket commands.");
				return true;
			}
		}
		if (split.length == 2) {
			if (split[1].equalsIgnoreCase("all")) {
				closeAll(sender);
				sender.sendMessage(ChatColor.GREEN
						+ "Successfully closed all open and claimed tickets!");
				return true;
			} else if (canParse(split[1])) {
				close(sender, Integer.parseInt(split[1]));
				return true;
			} else {
				sender.sendMessage(ChatColor.GREEN + "Invalid command usage.");
				return true;
			}
		}
		if (sender instanceof Player) {
			close((Player) sender);
			return true;
		}
		sender.sendMessage(ChatColor.GREEN
				+ "You must be ingame and supporting a ticket to close a ticket.");
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
		return "monstertickets.close";
	}

	public static void close(Player mod) {
		int id = -1;
		for (PrivateChatter pc : Variables.priv) {
			if (pc.getMod().equals(mod)) {
				id = pc.getID();
				Variables.priv.remove(pc);
				break;
			}
		}
		for (HelpTicket t : Variables.tickets.keySet()) {
			if (t.getID() == id) {
				Variables.tickets.remove(t);
				Player p = Bukkit.getPlayer(t.getPlayerName());
				if (p != null) {
					p.sendMessage(ChatColor.GREEN
							+ "Your support ticket has been closed.");
				}
				mod.sendMessage(ChatColor.GREEN + "Ticket " + t.getID()
						+ " sucessfully closed.");
				for (Player pl : Bukkit.getOnlinePlayers()) {
					if (MonsterTickets.getHandleManager()
							.getPermissionsHandler().hasModPerm(pl)) {
						pl.sendMessage(ChatColor.GREEN + mod.getName()
								+ " closed ticket " + t.getID());
					}
				}
				return;
			}
		}
		mod.sendMessage(ChatColor.GREEN
				+ "You are not currently supporting a ticket!");
	}

	public static void close(CommandSender mod, int id) {
		for (PrivateChatter pc : Variables.priv) {
			if (pc.getID() == id) {
				Variables.priv.remove(pc);
				break;
			}
		}
		for (HelpTicket t : Variables.tickets.keySet()) {
			if (t.getID() == id) {
				Variables.tickets.remove(t);
				Player p = Bukkit.getPlayer(t.getPlayerName());
				if (p != null) {
					p.sendMessage(ChatColor.GREEN
							+ "Your support ticket has been closed.");
				}
				mod.sendMessage(ChatColor.GREEN + "Ticket " + t.getID()
						+ " sucessfully closed.");
				for (Player pl : Bukkit.getOnlinePlayers()) {
					if (MonsterTickets.getHandleManager()
							.getPermissionsHandler().hasModPerm(pl)) {
						pl.sendMessage(ChatColor.GREEN + mod.getName()
								+ " closed ticket " + t.getID());
					}
				}
				return;
			}
		}
		mod.sendMessage(ChatColor.GREEN
				+ "You are not currently supporting a ticket!");
	}

	public static void closeAll(CommandSender sender) {
		Variables.priv.clear();
		for (HelpTicket t : Variables.tickets.keySet()) {
			Variables.tickets.remove(t);
			Player p = Bukkit.getPlayer(t.getPlayerName());
			if (p != null) {
				p.sendMessage(ChatColor.GREEN
						+ "Your support ticket has been forced closed, if this was a mistake please create a new ticket.");
			}
		}
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (MonsterTickets.getHandleManager().getPermissionsHandler()
					.hasModPerm(pl)) {
				pl.sendMessage(ChatColor.GREEN
						+ "All support tickets have been closed by "
						+ sender.getName() + ".");
			}
		}
	}

}
