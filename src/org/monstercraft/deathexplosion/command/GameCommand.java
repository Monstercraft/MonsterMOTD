package org.monstercraft.deathexplosion.command;

import org.bukkit.command.CommandSender;
import org.monstercraft.deathexplosion.DeathExplosion;

public abstract class GameCommand extends DeathExplosion {
	
	protected DeathExplosion plugin;
	
	public GameCommand(DeathExplosion plugin) {
		this.plugin = plugin;
	}

	public abstract boolean canExecute(CommandSender sender, String[] split);

	public abstract boolean execute(CommandSender sender, String[] split);
}