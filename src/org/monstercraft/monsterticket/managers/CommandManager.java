package org.monstercraft.monsterticket.managers;

import java.util.Enumeration;
import java.util.Hashtable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.monstercraft.monsterticket.command.GameCommand;
import org.monstercraft.monsterticket.command.commands.Check;
import org.monstercraft.monsterticket.command.commands.Claim;
import org.monstercraft.monsterticket.command.commands.Cleanup;
import org.monstercraft.monsterticket.command.commands.Close;
import org.monstercraft.monsterticket.command.commands.Kick;
import org.monstercraft.monsterticket.command.commands.List;
import org.monstercraft.monsterticket.command.commands.Open;

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
			gameCommands.put(6, new Kick());
			gameCommands.put(5, new List());
			gameCommands.put(4, new Cleanup());
			gameCommands.put(3, new Check());
			gameCommands.put(2, new Open());
			gameCommands.put(1, new Close());
			gameCommands.put(0, new Claim());
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
		String[] split = new String[args.length + 1];
		split[0] = label;
		for (int i = 0; i < args.length; i++) {
			split[i + 1] = args[i];
		}
		for (Enumeration<Integer> e = gameCommands.keys(); e.hasMoreElements();) {
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
		return true;
	}
}