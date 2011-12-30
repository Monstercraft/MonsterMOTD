package org.monstercraft.deathexplosion.command.commands;

import org.bukkit.command.CommandSender;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.command.Command;
import org.monstercraft.deathexplosion.util.Variables;

import com.iCo6.system.Account;

public class Pay extends Command {

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
		Account a = new Account(sender.getName());
		if (a.getHoldings().hasOver(Integer.valueOf(split[2]))) {
			if (!plugin.map.containsKey(sender.getName())) {
				if (Integer.valueOf(split[2]) <= Variables.cost) {
					a.getHoldings().subtract(Integer.valueOf(split[2]));
					plugin.map.put(sender.getName(), Integer.valueOf(split[2]));
					sender.sendMessage("Thanks for paying: "
							+ Integer.valueOf(split[2]) + ".");
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
	}
}
