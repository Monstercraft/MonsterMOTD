package org.monstercraft.deathexplosion.command.commands;

import org.bukkit.command.CommandSender;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.command.Command;
import org.monstercraft.deathexplosion.util.Variables;

import com.iCo6.system.Account;

public class PrivateChest extends Command {

	public PrivateChest(DeathExplosion plugin) {
		super(plugin);
	}

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("tomb")
				&& split[1].equalsIgnoreCase("pay");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		Account a = new Account(sender.getName());
		if (split[2].length() > String.valueOf(Variables.pcost).length()) {
			sender.sendMessage("You are paying more than enough, the limit is "
					+ Variables.pcost + ".");
			return false;
		}
		if (!checkIfNumber(split[2])) {
			sender.sendMessage("Make sure to only use numbers!");
			return false;
		}
		if (split[2].contains("-")) {
			split[2] = split[2].replace("-", "");
		}
		if (a.getHoldings().hasOver(Integer.valueOf(split[2]))) {
			if (!plugin.pchest.containsKey(sender.getName())) {
				if (Integer.valueOf(split[2]) <= Variables.pcost) {
					a.getHoldings().subtract(Integer.valueOf(split[2]));
					plugin.pchest.put(sender.getName(), Integer.valueOf(split[2]));
					sender.sendMessage("Thanks for paying: "
							+ Integer.valueOf(split[2]) + ".");
					return true;
				} else {
					sender.sendMessage("You are paying more than enough, the limit is "
							+ Variables.pcost + ".");
				}
			} else {
				sender.sendMessage("You have already payed. Go die.");
			}
		} else {
			sender.sendMessage("You don't have enough money to pay that much.");
		}
		return false;
	}

	public boolean checkIfNumber(String in) {
		try {

			Integer.parseInt(in);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
