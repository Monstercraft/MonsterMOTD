package org.monstercraft.deathexplosion.command.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.deathexplosion.command.GameCommand;
import org.monstercraft.deathexplosion.util.Variables;

public class warp extends GameCommand {

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("boom")
				&& split[1].equalsIgnoreCase("warp");
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		if (Variables.last != null) {
			if (sender instanceof Player) {
				Player p = ((Player) sender);
				p.teleport(new Location(Variables.last.getWorld(),
						Variables.last.getX(), Variables.last.getY() + 1,
						Variables.last.getZ()));
			}
			return true;
		} else {
			sender.sendMessage("No locations found!");
		}
		return true;
	}
}
