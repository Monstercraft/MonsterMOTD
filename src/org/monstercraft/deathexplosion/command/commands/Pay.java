package org.monstercraft.deathexplosion.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.deathexplosion.command.GameCommand;
import org.monstercraft.deathexplosion.util.Methods;
import org.monstercraft.deathexplosion.util.Variables;

public class pay extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("boom")
				&& split[1].equalsIgnoreCase("pay");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("You must be a player to execute this command");
			return true;
		}
		try {
			if (split.length < 3) {
				sender.sendMessage("No amount entered please use /boom pay (amount)");
				return true;
			}
			if (!checkIfNumber(split[2])) {
				sender.sendMessage("Make sure to only use numbers!");
				return true;
			}
			if (split[2].length() > String.valueOf(Variables.cost).length()) {
				sender.sendMessage("You are paying more than enough, the limit is "
						+ Variables.cost + ".");
				return true;
			}
			if (split[2].contains("-")) {
				split[2] = split[2].replace("-", "");
			}
			if (getHookManager().getPermissionsHook().getHook()
					.getBalance(sender.getName()) > Double
					.parseDouble(split[2])) {
				if (!Variables.map.containsKey(sender.getName())) {
					if (Integer.valueOf(split[2]) <= Variables.cost) {
						getHookManager()
								.getPermissionsHook()
								.getHook()
								.withdrawPlayer(sender.getName(),
										Double.parseDouble(split[2]));
						Variables.map.put(sender.getName(),
								Double.parseDouble(split[2]));
						sender.sendMessage("Thanks for paying: "
								+ Double.valueOf(split[2]) + ".");
						Methods.attachStatusBar((Player) sender);
						return true;
					} else {
						sender.sendMessage("You are paying more than enough, the limit is "
								+ Variables.cost + ".");
					}
				} else {
					sender.sendMessage("You have already payed. Go die.");
				}
			} else {
				sender.sendMessage("You don't have enough money to pay that much.");
			}
			return true;
		} catch (Exception e) {
			debug(e);
		}
		return true;
	}

	public boolean checkIfNumber(String in) {
		try {

			Double.parseDouble(in);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
