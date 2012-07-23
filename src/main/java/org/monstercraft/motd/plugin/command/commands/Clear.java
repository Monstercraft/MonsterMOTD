package org.monstercraft.motd.plugin.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.motd.MonsterMOTD;
import org.monstercraft.motd.plugin.Configuration.Variables;
import org.monstercraft.motd.plugin.command.GameCommand;
import org.monstercraft.motd.plugin.reflection.Reflect;

public class Clear extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split.length > 1 && split[0].equalsIgnoreCase("motd")
				&& split[1].equalsIgnoreCase("clear");
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
			sender.sendMessage("Permissions not detected, unable to run any ticket commands.");
			return true;
		}
		try {
			Reflect.setMotd(Variables.defaultMOTD);
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED
					+ "There was an error clearing the MOTD.");
			MonsterMOTD.debug(e);
			return true;
		}
		sender.sendMessage(ChatColor.GREEN
				+ "MOTD successfully set back to the default: " + ChatColor.RESET + Variables.defaultMOTD);
		MonsterMOTD.getSettingsManager().save();
		return true;
	}

	@Override
	public String getPermission() {
		return "monstermotd.clear";
	}

}
