package org.monstercraft.deathexplosion.command.commands;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.command.GameCommand;
import org.monstercraft.deathexplosion.util.Variables;

public class warp extends GameCommand {
	Random ran = new Random();

	@Override
	public boolean canExecute(CommandSender sender, String[] split) {
		return split[0].equalsIgnoreCase("boom")
				&& split[1].equalsIgnoreCase("warp");
	}

	public int random(int min, int max) {
		int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : ran.nextInt(n));
	}

	@Override
	public boolean execute(CommandSender sender, String[] split) {
		if (Variables.last != null) {
			ran = new Random();
			int x = random(Variables.last.getX() - (Variables.size * 2),
					Variables.last.getX() + (Variables.size * 2));
			int y = random(Variables.last.getY() - (Variables.size * 2),
					Variables.last.getY() + (Variables.size * 2));
			int z = random(Variables.last.getZ() - (Variables.size * 2),
					Variables.last.getZ() + (Variables.size * 2));
			if (sender instanceof Player) {
				Player p = ((Player) sender);
				DeathExplosion.getTeleportManager().teleport(p,
						new Location(Variables.last.getWorld(), x, y, z));
			}
			return true;
		} else {
			sender.sendMessage("No locations found!");
		}
		return true;
	}
}
