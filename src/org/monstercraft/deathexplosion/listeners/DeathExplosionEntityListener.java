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
	private HashSet<Location> explosions = new HashSet<Location>();

	public DeathExplosionEntityListener(DeathExplosion plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		int i = ran.nextInt(Variables.cost);
		int i2 = ran.nextInt(Variables.pcost);
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
		} else {
			System.out.println("pstones null");
		}
		if (plugin.map.containsKey(player.getName())) {
			if (plugin.pchest.containsKey(player)) {
				if (i <= plugin.map.get(player.getName())) {
					World world = player.getWorld();
					world.createExplosion(loc, Variables.size, false);
					explosions.add(loc);
					player.sendMessage("You have exploded.");
					if (i2 <= plugin.pchest.get(player.getName())) {
						plugin.methods
								.saveItems(player, event.getDrops(), true);
						plugin.pchest.remove(player.getName());
						plugin.map.remove(player.getName());
						player.sendMessage("Your items have been stored in a private chest where you died!");
					} else {
						plugin.methods.saveItems(player, event.getDrops(),
								false);
						plugin.pchest.remove(player.getName());
						plugin.map.remove(player.getName());
						player.sendMessage("Your items were not stored in a private chest.");
					}
				} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
					if (i2 <= plugin.pchest.get(player.getName())) {
						plugin.methods
								.saveItems(player, event.getDrops(), true);
						plugin.pchest.remove(player.getName());
						plugin.map.remove(player.getName());
						player.sendMessage("Your items have been stored in a private chest where you died!");
					} else {
						plugin.methods.saveItems(player, event.getDrops(),
								false);
						plugin.pchest.remove(player.getName());
						plugin.map.remove(player.getName());
						player.sendMessage("Your items were not stored in a private chest.");
					}
					player.sendMessage("You have died from someone elses explosion");
					player.sendMessage("We are sorry, you didn't explode upon death.");
				} else {
					plugin.map.remove(player.getName());
					player.sendMessage("We are sorry, you didn't explode upon death.");
				}
			} else {
				if (i <= plugin.map.get(player.getName())) {
					World world = player.getWorld();
					world.createExplosion(loc, Variables.size, false);
					explosions.add(loc);
					player.sendMessage("You have exploded.");
					plugin.methods.saveItems(player, event.getDrops(), false);
					plugin.map.remove(player.getName());
				} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
					plugin.methods.saveItems(player, event.getDrops(), false);
					player.sendMessage("You have died from someone elses explosion");
					player.sendMessage("We are sorry, you didn't explode upon death.");
					plugin.map.remove(player.getName());
				} else {
					player.sendMessage("We are sorry, you didn't explode upon death.");
					plugin.map.remove(player.getName());
				}
			}
		} else if (plugin.pchest.containsKey(player)) {
			if (i2 <= plugin.pchest.get(player.getName())) {
				plugin.methods.saveItems(player, event.getDrops(), true);
				plugin.pchest.remove(player.getName());
				player.sendMessage("Your items have been stored in a private chest where you died!");
			} else {
				plugin.methods.saveItems(player, event.getDrops(), false);
				plugin.pchest.remove(player.getName());
				player.sendMessage("Your items were not stored in a private chest.");
			}
			player.sendMessage("We are sorry, you didn't explode upon death.");
		} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
			plugin.methods.saveItems(player, event.getDrops(), false);
			player.sendMessage("You have died from someone elses explosion");
		}
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (!explosions.contains(event.getLocation())) {
			return;
		}
		final List<BlockData> revert = new LinkedList<BlockData>();
		List<Block> blocks = event.blockList();
		for (Block block : blocks) {
			revert.add(new BlockData(block));
			continue;
		}
		if (!revert.isEmpty()) {
			plugin.getServer().getScheduler()
					.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							for (BlockData db : revert) {
								Block block = db.getLocation().getBlock();
								block.setTypeIdAndData(db.getTypeId(),
										db.getData(), true);
							}
							revert.clear();
						}
					}, 3);
		}
		explosions.remove(event.getLocation());

	}
}
