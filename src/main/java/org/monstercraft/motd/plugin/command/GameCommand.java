package org.monstercraft.motd.plugin.command;

import org.bukkit.command.CommandSender;
import org.monstercraft.motd.MonsterMOTD;

/**
 * This class is the Abstract Game command.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public abstract class GameCommand extends MonsterMOTD{

	/**
	 * The permission node to check.
	 * 
	 * @return The permission node.
	 */
	public abstract String getPermission();

	/**
	 * Checks if the command can be executed by the certian user.
	 * 
	 * @param sender
	 *            The command sender.
	 * @param split
	 *            The command arguments.
	 * @return True if the user is able to execute the command; otherwise false.
	 */
	public abstract boolean canExecute(final CommandSender sender,
			final String[] split);

	/**
	 * The action to perfrom when executing the command.
	 * 
	 * @param sender
	 *            The command sender.
	 * @param split
	 *            The command arguments.
	 * @return True if the command executed successfully; otherwise false.
	 */
	public abstract boolean execute(final CommandSender sender,
			final String[] split);
}