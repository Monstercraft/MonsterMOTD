package org.monstercraft.monsterticket.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.command.GameCommand;
import org.monstercraft.monsterticket.util.Variables;
import org.monstercraft.monsterticket.wrappers.HelpTicket;

public class Cleanup extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("cleanup");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		try {
			if (sender instanceof Player) {
				sender.sendMessage("This is a console only command!");
				return true;
			}
			sender.sendMessage("Closing and resetting all tickets!");
			Variables.ticketid = 1;
			for (HelpTicket t : Variables.tickets.keySet()) {
				Variables.tickets.remove(t);
				Player p = Bukkit.getPlayer(t.getPlayerName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manudelp "
						+ t.getPlayerName() + " towny.chat.support");
				if (p != null) {
					p.sendMessage("Your support ticket has been closed by the console.");
				}
				break;
			}
			Variables.tickets.clear();
			Ticket.getSettingsManager().save();
			return true;
		} catch (Exception e) {
			Ticket.debug(e);
		}
		return true;
	}

	@Override
	public String getPermission() {
		return "monsterticket.cleanup";
	}

}
