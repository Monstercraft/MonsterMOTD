package org.monstercraft.deathexplosion.listeners;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.monstercraft.deathexplosion.DeathExplosion;

public class DeathExplosionEntityListener extends EntityListener {

	private Random ran = new Random();
	private DeathExplosion plugin;

	public DeathExplosionEntityListener(DeathExplosion plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player = (Player) event.getEntity();
			if (plugin.map.containsKey(player.getName())) {
				if (ran.nextInt(1000000) <= plugin.map.get(player.getName()))
				try {
					World world = player.getWorld();
					world.createExplosion(player.getLocation(), 5);
					plugin.map.remove(player.getName());
					player.sendMessage("You have exploded.");
				} catch (Exception e) {
				}
			} else {
				player.sendMessage("We are sorry, you didn't explode.");
			}
		}
	}
}
