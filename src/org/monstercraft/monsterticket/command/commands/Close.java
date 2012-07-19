package org.monstercraft.monsterticket.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.command.GameCommand;
import org.monstercraft.monsterticket.util.Variables;
import org.monstercraft.monsterticket.wrappers.HelpTicket;
import org.monstercraft.monsterticket.wrappers.PrivateChatter;

public class Close extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("close");
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
			sender.sendMessage(ChatColor.RED + "Invalid command usage!");
			return true;
		}
		if (!canParse(split[1])) {
			sender.sendMessage(ChatColor.GREEN + "Invalid number!");
			return true;
		}
		close((Player) sender, split[1]);
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

	public static void close(Player player, String ticket) {
		for (HelpTicket t : Variables.tickets.keySet()) {
			if (t.getID() == Integer.parseInt(ticket)) {
				Variables.tickets.remove(t);
				Player p = Bukkit.getPlayer(t.getPlayerName());
				for (PrivateChatter pc : Variables.priv) {
					if (pc.getMod().equals((Player) player)) {
						Variables.priv.remove(pc);
						break;
					}
				}
				if (p != null) {
					p.sendMessage(ChatColor.GREEN
							+ "Your support ticket has been closed.");
				}
				player.sendMessage(ChatColor.GREEN + "Ticket " + t.getID()
						+ " sucessfully closed.");
				for (Player pl : Bukkit.getOnlinePlayers()) {
					if (Ticket.getHandleManager().getPermissionsHandler()
							.hasModPerm(pl)) {
						pl.sendMessage(ChatColor.GREEN + player.getName()
								+ " closed ticket " + t.getID());
					}
				}
				break;
			}
		}
	}

}
