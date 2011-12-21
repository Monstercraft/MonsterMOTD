package org.monstercraft.deathexplosion.listeners;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.monstercraft.deathexplosion.DeathExplosion;

public class DeathExplosionPlayerListener extends EntityListener {
	
	private Random ran = new Random();

	public DeathExplosionPlayerListener(DeathExplosion instance) {
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			int r = ran.nextInt(2);
			if (r == 1) {
				Player player = (Player) event.getEntity();
				try {
					World world = player.getWorld();
					world.createExplosion(player.getLocation(),
					5 + ran.nextInt(6));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
