package org.monstercraft.support.plugin.command;

import org.bukkit.command.CommandSender;
import org.monstercraft.support.MonsterTickets;

public abstract class GameCommand extends MonsterTickets {

	public abstract boolean canExecute(CommandSender sender, String[] split);

	public abstract boolean execute(CommandSender sender, String[] split);

	public abstract String getPermission();
}