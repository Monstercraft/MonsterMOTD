package org.monstercraft.monsterticket.plugin.command.commands;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.plugin.Configuration.Variables;
import org.monstercraft.monsterticket.plugin.command.GameCommand;
import org.monstercraft.monsterticket.plugin.wrappers.HelpTicket;

public class Check extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("check");
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
		if (split.length == 1) {
			sender.sendMessage(ChatColor.RED
					+ "Listing all open support tickets! ("
					+ Variables.tickets.size() + ")");
			sender.sendMessage(ChatColor.GREEN + "Green" + ChatColor.RED
					+ " are open support tickets!");
			sender.sendMessage(ChatColor.BLUE + "Blue" + ChatColor.RED
					+ " are claimed support tickets that are not closed!");
			sender.sendMessage(ChatColor.RED
					+ "-----------------------------------------------");
			displayPage(1, sender);
		} else if (split.length == 3) {
			if (split[1].equalsIgnoreCase("page")) {
				if (!canParse(split[2])) {
					sender.sendMessage(ChatColor.RED + "Invalid page number!");
					return true;
				}
				if ((int) (Variables.tickets.size() / 15 + 1) < Integer
						.parseInt(split[2])) {
					sender.sendMessage(ChatColor.RED + "Invalid page number!");
					return true;
				}
				if (Integer.parseInt(split[2]) < 1) {
					sender.sendMessage(ChatColor.RED + "Invalid page number!");
					return true;
				}
				int pg = Integer.parseInt(split[2]);
				sender.sendMessage(ChatColor.RED
						+ "Listing all open support tickets! ("
						+ Variables.tickets.size() + ")");
				sender.sendMessage(ChatColor.GREEN + "Green" + ChatColor.RED
						+ " are open support tickets!");
				sender.sendMessage(ChatColor.BLUE + "Blue" + ChatColor.RED
						+ " are claimed support tickets that are not closed!");
				sender.sendMessage(ChatColor.RED
						+ "-----------------------------------------------");
				displayPage(pg, sender);
			}
		} else if (split.length == 2) {
			if (!canParse(split[1])) {
				sender.sendMessage(ChatColor.RED + "Invalid number!");
				return true;
			}
			if (Variables.ticketid < Integer.parseInt(split[1])) {
				sender.sendMessage(ChatColor.RED + "Invalid ticket number!");
				return true;
			}
			if (Integer.parseInt(split[1]) < 0) {
				sender.sendMessage(ChatColor.RED + "Invalid ticket number!");
				return true;
			}
			int ticketID = Integer.parseInt(split[1]);
			Iterator<HelpTicket> i = Variables.tickets.keySet().iterator();
			while (i.hasNext()) {
				HelpTicket h = i.next();
				if (h.getID() == ticketID) {
					if (!Variables.tickets.get(h)) {
						sender.sendMessage(ChatColor.GREEN + "" + h.getID()
								+ ChatColor.RED + " - " + h.getPlayerName()
								+ ChatColor.GREEN + " - " + h.getDescription());
					} else {
						sender.sendMessage(ChatColor.BLUE + "" + h.getID()
								+ ChatColor.RED + " - " + h.getPlayerName()
								+ ChatColor.BLUE + " - " + h.getDescription());
					}
				}
			}
		}
		return true;
	}

	private String getShortDescription(String str) {
		if (str.length() > 20) {
			str = str.substring(0, 20);
			str = str + "...";
		}
		return str;
	}

	private boolean canParse(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	private void displayPage(int page, CommandSender sender) {
		int numPages = Variables.tickets.size() / 15;
		if (numPages == 0) {
			numPages = 1;
		}
		int start = (page - 1) * 15;
		int end = start + 15;
		int c = 0;
		if (sender instanceof Player) {
			if (Ticket.getHandleManager().getPermissionsHandler()
					.hasModPerm((Player) sender)) {
				Iterator<HelpTicket> i = Variables.tickets.keySet().iterator();
				while (i.hasNext()) {
					HelpTicket h = i.next();
					if (c < start) {
						c++;
						continue;
					}
					if (c > end) {
						break;
					}
					if (!Variables.tickets.get(h)) {
						sender.sendMessage(ChatColor.GREEN + "" + h.getID()
								+ " - " + ChatColor.RED + h.getPlayerName()
								+ ChatColor.GREEN + " - "
								+ getShortDescription(h.getDescription()));
					} else {
						sender.sendMessage(ChatColor.BLUE + "" + h.getID()
								+ " - " + ChatColor.RED + h.getPlayerName()
								+ " - " + ChatColor.BLUE
								+ getShortDescription(h.getDescription()));
					}
					c++;
				}
			} else {
				Iterator<HelpTicket> i = Variables.tickets.keySet().iterator();
				while (i.hasNext()) {
					HelpTicket h = i.next();
					if (c < start) {
						c++;
						continue;
					}
					if (c > end) {
						break;
					}
					if (h.getPlayerName() == sender.getName()) {
						if (!Variables.tickets.get(h)) {
							sender.sendMessage(ChatColor.GREEN + "" + h.getID()
									+ " - " + ChatColor.RED + h.getPlayerName()
									+ ChatColor.GREEN + " - "
									+ getShortDescription(h.getDescription()));
						} else {
							sender.sendMessage(ChatColor.BLUE + "" + h.getID()
									+ " - " + ChatColor.RED + h.getPlayerName()
									+ " - " + ChatColor.BLUE
									+ getShortDescription(h.getDescription()));
						}
					}
					c++;
				}
			}
		} else {
			Iterator<HelpTicket> i = Variables.tickets.keySet().iterator();
			while (i.hasNext()) {
				HelpTicket h = i.next();
				if (c < start) {
					c++;
					continue;
				}
				if (c > end) {
					break;
				}
				if (!Variables.tickets.get(h)) {
					sender.sendMessage(ChatColor.GREEN + " -" + h.getID()
							+ " - " + ChatColor.RED + h.getPlayerName()
							+ ChatColor.GREEN + " - "
							+ getShortDescription(h.getDescription()));
				} else {
					sender.sendMessage(ChatColor.BLUE + " -" + h.getID()
							+ " - " + ChatColor.RED + h.getPlayerName() + " - "
							+ ChatColor.BLUE
							+ getShortDescription(h.getDescription()));
				}
				c++;
			}
		}
		sender.sendMessage(ChatColor.RED + "-----------[ " + page + " of "
				+ (int) (Variables.tickets.size() / 15 + 1) + " ]-------------");
	}

	@Override
	public String getPermission() {
		return "monsterticket.check";
	}

}
