package org.monstercraft.motd.plugin.managers;

import java.util.LinkedList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.monstercraft.motd.MonsterMOTD;
import org.monstercraft.motd.plugin.command.GameCommand;
import org.monstercraft.motd.plugin.command.commands.Clear;
import org.monstercraft.motd.plugin.command.commands.MOTD;
import org.monstercraft.motd.plugin.command.commands.Set;

/**
 * This class manages all of the plugins commands.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class CommandManager {

	private LinkedList<GameCommand> gameCommands = new LinkedList<GameCommand>();

	/**
	 * Creates an instance
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public CommandManager() {
		gameCommands.add(new Set());
		gameCommands.add(new Clear());
		gameCommands.add(new MOTD());
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
		String[] split;
		if (args.length > 0) {
			split = new String[args.length + 1];
			split[0] = label;
			for (int a = 0; a < args.length; a++) {
				split[a + 1] = args[a];
			}
		} else {
			split = new String[] {label};
		}
		for (GameCommand c : gameCommands) {
			if (c.canExecute(sender, split)) {
				try {
					return c.execute(sender, split);
				} catch (Exception ex) {
					MonsterMOTD.debug(ex);
				}
			}
		}
		return true;
	}
}