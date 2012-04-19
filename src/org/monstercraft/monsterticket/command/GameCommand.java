package org.monstercraft.monsterticket.command;

import org.bukkit.command.CommandSender;
import org.monstercraft.monsterticket.Ticket;

public abstract class GameCommand extends Ticket {

	public abstract boolean canExecute(CommandSender sender, String[] split);

	public abstract boolean execute(CommandSender sender, String[] split);

	public abstract String getPermission();
}