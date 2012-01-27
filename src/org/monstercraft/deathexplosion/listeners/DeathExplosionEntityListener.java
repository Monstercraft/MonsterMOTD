package org.monstercraft.deathexplosion.listeners;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.sacredlabyrinth.Phaed.PreciousStones.BlockData;
import net.sacredlabyrinth.Phaed.PreciousStones.FieldFlag;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.util.Variables;

public class DeathExplosionEntityListener extends EntityListener {

	private Random ran = new Random();
	private DeathExplosion plugin;
	private HashSet<Location> locations;

	public DeathExplosionEntityListener(DeathExplosion plugin) {
		this.plugin = plugin;
		locations = new HashSet<Location>();
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		int i = ran.nextInt(Variables.cost);
		Entity entity = event.getEntity();
		EntityDamageEvent cause = event.getEntity().getLastDamageCause();
		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		Location loc = player.getLocation();
		Block block = player.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY(), loc.getBlockZ());
		if (plugin.getWorldGuard() != null) {
			if (!plugin.getWorldGuard().canBuild(player, block)) {
				return;
			}
		} else {
			System.out.println("Worldguard null");
		}
		if (!plugin.methods.canBuild(block, player)) {
			return;
		}
		if (plugin.getPStones() != null) {
			if (plugin
					.getPStones()
					.getForceFieldManager()
					.hasSourceField(block.getLocation(),
							FieldFlag.PREVENT_DESTROY)) {
				return;
			}
		}
		if (plugin.map.containsKey(player.getName())) {
			if (i <= plugin.map.get(player.getName())) {
				World world = player.getWorld();
				world.createExplosion(loc, Variables.size);
				locations.add(loc);
				player.sendMessage("You have exploded.");
				plugin.methods.saveItems(player, event.getDrops());
				plugin.map.remove(player.getName());
			} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
				plugin.methods.saveItems(player, event.getDrops());
				player.sendMessage("You have died from someone elses explosion");
				player.sendMessage("We are sorry, you didn't explode upon death.");
				plugin.map.remove(player.getName());
			} else {
				player.sendMessage("We are sorry, you didn't explode upon death.");
				plugin.map.remove(player.getName());
			}
		} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
			plugin.methods.saveItems(player, event.getDrops());
			player.sendMessage("You have died from someone elses explosion");
		}
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (locations.contains(event.getLocation())) {
			final List<BlockData> revert = new LinkedList<BlockData>();
			List<Block> blocks = event.blockList();
			for (Block block : blocks) {
				revert.add(new BlockData(block));
			}
			if (!revert.isEmpty()) {
				for (BlockData db : revert) {
					Block block = db.getLocation().getBlock();
					block.setTypeIdAndData(db.getTypeId(), db.getData(), true);
				}
				revert.clear();
			}
			locations.remove(event.getLocation());
		}
	}
}
