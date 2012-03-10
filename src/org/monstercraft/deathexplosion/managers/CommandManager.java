package org.monstercraft.deathexplosion.managers;

import java.util.Enumeration;
import java.util.Hashtable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.monstercraft.deathexplosion.command.GameCommand;
import org.monstercraft.deathexplosion.command.commands.off;
import org.monstercraft.deathexplosion.command.commands.on;
import org.monstercraft.deathexplosion.command.commands.pay;

/**
 * This class manages all of the plugins commands.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class CommandManager {

	private Hashtable<Integer, GameCommand> gameCommands = new Hashtable<Integer, GameCommand>();

	public CommandManager() {
		try {
			gameCommands.put(2, new pay());
			gameCommands.put(1, new on());
			gameCommands.put(0, new off());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			for (Enumeration<Integer> e = gameCommands.keys(); e
					.hasMoreElements();) {
				int key = e.nextElement();
				GameCommand c = gameCommands.get(key);
				if (c.canExecute(sender, split)) {
					try {
						c.execute(sender, split);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return true;
	}
}