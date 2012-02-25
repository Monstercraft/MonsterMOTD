package org.monstercraft.deathexplosion.command.commands;

import org.bukkit.command.CommandSender;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.command.GameCommand;
import org.monstercraft.deathexplosion.util.Variables;

public class off extends GameCommand {

	public off(DeathExplosion plugin) {
		super(plugin);
	}

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return sender.isOp() && split[1].equalsIgnoreCase("off");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		if (sender.isOp()) {
			Variables.off = true;
			sender.sendMessage("Successfully disabled");
		}
		return true;
	}

}
