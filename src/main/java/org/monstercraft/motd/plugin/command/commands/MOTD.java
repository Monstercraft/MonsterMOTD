package org.monstercraft.motd.plugin.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.motd.MonsterMOTD;
import org.monstercraft.motd.plugin.command.GameCommand;
import org.monstercraft.motd.plugin.reflection.Reflect;

public class MOTD extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("motd");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		if (MonsterMOTD.getHandleManager().getPermissionsHandler() != null) {
			if (!MonsterMOTD.getHandleManager().getPermissionsHandler()
					.hasCommandPerms(((Player) sender), this)) {
				sender.sendMessage("You don't have permission to preform this command.");
				return true;
			}
		} else {
			sender.sendMessage("Permissions not detected, unable to run this command.");
			return true;
		}
		try {
			sender.sendMessage(ChatColor.GREEN + "Todays MOTD: " + ChatColor.RESET
					+ Reflect.getMotd());
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED
					+ "There was an error checking the MOTD.");
			MonsterMOTD.debug(e);
		}
		return true;
	}

	@Override
	public String getPermission() {
		return "monstermotd.view";
	}

}
