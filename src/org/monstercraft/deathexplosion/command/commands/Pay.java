package org.monstercraft.deathexplosion.command.commands;

import org.bukkit.command.CommandSender;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.command.GameCommand;
import org.monstercraft.deathexplosion.util.Variables;

public class Pay extends GameCommand {

	public Pay(DeathExplosion plugin) {
		super(plugin);
	}

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("boom")
				&& split[1].equalsIgnoreCase("pay");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		try {
			if (!checkIfNumber(split[2])) {
				sender.sendMessage("Make sure to only use numbers!");
				return false;
			}
			if (split[2].length() > String.valueOf(Variables.cost).length()) {
				sender.sendMessage("You are paying more than enough, the limit is "
						+ Variables.cost + ".");
				return false;
			}
			if (split[2].contains("-")) {
				split[2] = split[2].replace("-", "");
			}
			if (plugin.getHookManager().getPermissionsHook().getHook()
					.getBalance(sender.getName()) > Double
					.parseDouble(split[2])) {
				if (!Variables.map.containsKey(sender.getName())) {
					if (Integer.valueOf(split[2]) <= Variables.cost) {
						plugin.getHookManager()
								.getPermissionsHook()
								.getHook()
								.withdrawPlayer(sender.getName(),
										Double.parseDouble(split[2]));
						Variables.map.put(sender.getName(),
								Double.parseDouble(split[2]));
						sender.sendMessage("Thanks for paying: "
								+ Double.valueOf(split[2]) + ".");
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
			return false;
		} catch (Exception e) {
			debug(e);
		}
		return false;
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
