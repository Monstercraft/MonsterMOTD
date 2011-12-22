package org.monstercraft.deathexplosion.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.command.Command;

import com.iCo6.system.Account;

public class Pay extends Command {

	public Pay(DeathExplosion plugin) {
		super(plugin);
	}

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return true;
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		Account a = new Account(sender.getName());
		if (a.getHoldings().hasOver(Integer.valueOf(split[2]))
				&& Integer.valueOf(split[2]) <= 1000000
				&& !plugin.map.containsKey((Player) sender)) {
			a.getHoldings().subtract(Integer.valueOf(split[2]));
			plugin.map.put(sender.getName(), Integer.valueOf(split[2]));
		} else {

		}
		return false;
	}

}
