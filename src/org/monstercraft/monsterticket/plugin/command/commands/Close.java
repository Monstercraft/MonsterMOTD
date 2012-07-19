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
		close((Player) sender);
		return true;
	}

	@Override
	public String getPermission() {
		return "monsterticket.close";
	}

	public static void close(Player mod) {
		int id = -1;
		for (PrivateChatter pc : Variables.priv) {
			if (pc.getMod().equals((Player) mod)) {
				Variables.priv.remove(pc);
				id = pc.getID();
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
					if (Ticket.getHandleManager().getPermissionsHandler()
							.hasModPerm(pl)) {
						pl.sendMessage(ChatColor.GREEN + mod.getName()
								+ " closed ticket " + t.getID());
					}
				}
				break;
			}
		}
	}

}
