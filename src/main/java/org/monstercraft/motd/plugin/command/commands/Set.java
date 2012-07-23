package org.monstercraft.motd.plugin.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.motd.MonsterMOTD;
import org.monstercraft.motd.plugin.command.GameCommand;
import org.monstercraft.motd.plugin.reflection.Reflect;

public class Set extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split.length > 1 && split[0].equalsIgnoreCase("motd")
				&& split[1].equalsIgnoreCase("set");
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
		if (split.length < 3) {
			sender.sendMessage(ChatColor.RED + "You must put a MOTD to set!");
			return true;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < split.length; i++) {
			sb.append(split[i]);
			sb.append(" ");
		}
		try {
			Reflect.setMotd(sb.toString().trim());
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED
					+ "There was an error setting the MOTD.");
			MonsterMOTD.debug(e);
			return true;
		}
		sender.sendMessage(ChatColor.GREEN + "MOTD successfully set to: "
				+ ChatColor.RESET + sb.toString().trim());
		MonsterMOTD.getSettingsManager().save();
		return true;
	}

	@Override
	public String getPermission() {
		return "monstermotd.set";
	}

}
