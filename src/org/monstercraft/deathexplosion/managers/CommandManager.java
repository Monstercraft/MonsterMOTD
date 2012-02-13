package org.monstercraft.deathexplosion.managers;

import java.util.HashSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.command.GameCommand;
import org.monstercraft.deathexplosion.command.commands.Pay;

/**
 * This class manages all of the plugins commands.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class CommandManager extends DeathExplosion {

	private HashSet<GameCommand> gameCommands = new HashSet<GameCommand>();;

	/**
	 * Creates an instance
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public CommandManager(DeathExplosion plugin) {
		gameCommands.add(new Pay(plugin));
	}

	/**
	 * Executes a command that was ran in game or through the console.
	 * 
	 * @param sender
	 *            The command sender.
	 * @param command
	 *            The command.
	 * @param label
	 *            The commands label.
	 * @param args
	 *            The arguments in the command.
	 * @return True if the command executed successfully; Otherwise false.
	 */
	public boolean onGameCommand(final CommandSender sender,
			final Command command, final String label, final String[] args) {
		if (args.length > 0) {
			String[] split = new String[args.length + 1];
			split[0] = label;
			for (int a = 0; a < args.length; a++) {
				split[a + 1] = args[a];
			}
			for (GameCommand c : gameCommands) {
				if (c.canExecute(sender, split)) {
					try {
						c.execute(sender, split);
					} catch (Exception e) {
						debug(e);
					}
					return true;
				}
			}
		}
		return false;
	}
}