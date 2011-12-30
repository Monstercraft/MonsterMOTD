package org.monstercraft.deathexplosion.listeners;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.util.Variables;
import org.monstercraft.deathexplosion.util.wrappers.TombStone;

public class DeathExplosionEntityListener extends EntityListener {

	private Random ran = new Random();
	private DeathExplosion plugin;

	public DeathExplosionEntityListener(DeathExplosion plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		int i = ran.nextInt(Variables.cost);
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		if (plugin.map.containsKey(player.getName())) {
			if (i <= plugin.map.get(player.getName()))
				try {
					World world = player.getWorld();
					Location l = player.getLocation();
					world.createExplosion(l, Variables.size, false);
					player.sendMessage("You have exploded.");
					if (Variables.chest) {
						Location loc = player.getLocation();
						Block block = player.getWorld().getBlockAt(
								loc.getBlockX(), loc.getBlockY(),
								loc.getBlockZ());
						if (!plugin.methods.canBuild(block, player)) {
							return;
						}
						if (block.getType() == Material.STEP
								|| block.getType() == Material.TORCH
								|| block.getType() == Material.REDSTONE_WIRE
								|| block.getType() == Material.RAILS
								|| block.getType() == Material.STONE_PLATE
								|| block.getType() == Material.WOOD_PLATE
								|| block.getType() == Material.REDSTONE_TORCH_ON
								|| block.getType() == Material.REDSTONE_TORCH_OFF
								|| block.getType() == Material.CAKE_BLOCK) {
							block = player.getWorld().getBlockAt(
									loc.getBlockX(), loc.getBlockY() + 1,
									loc.getBlockZ());
						}
						block = plugin.methods.findPlace(block);
						if (block == null) {
							return;
						}
						if (plugin.methods.checkChest(block)) {
							return;
						}
						Block lBlock = plugin.methods.findLarge(block);
						block.setType(Material.CHEST);
						BlockState state = block.getState();
						if (!(state instanceof Chest)) {
							return;
						}
						Chest sChest = (Chest) state;
						Chest lChest = null;
						int slot = 0;
						int maxSlot = sChest.getInventory().getSize();
						if (event.getDrops().size() > maxSlot) {
							if (lBlock != null) {
								lBlock.setType(Material.CHEST);
								lChest = (Chest) lBlock.getState();
								maxSlot = maxSlot * 2;
							}
						}
						Block sBlock = null;
						sBlock = sChest.getWorld().getBlockAt(sChest.getX(),
								sChest.getY() + 1, sChest.getZ());
						if (plugin.methods.canReplace(sBlock.getType())) {
							plugin.methods.createSign(sBlock, player);
						} else if (lChest != null) {
							sBlock = lChest.getWorld().getBlockAt(
									lChest.getX(), lChest.getY() + 1,
									lChest.getZ());
							if (plugin.methods.canReplace(sBlock.getType())) {
								plugin.methods.createSign(sBlock, player);
							}
						}
						TombStone tBlock = new TombStone(sChest.getBlock(),
								sBlock);
						plugin.methods.registerLWC(player, tBlock);
						if (Variables.announce) {
							System.out.println("announce");
							plugin.methods.advertiseDeath(player.getName(),
									tBlock.getBlock().getWorld().getName(),
									tBlock.getBlock().getX(), tBlock.getBlock()
											.getY(), tBlock.getBlock().getZ());
						}
						for (Iterator<ItemStack> iter = event.getDrops()
								.listIterator(); iter.hasNext();) {
							ItemStack item = iter.next();
							if (item == null)
								continue;
							if (slot < maxSlot) {
								if (slot >= sChest.getInventory().getSize()) {
									if (lChest == null)
										continue;
									lChest.getInventory().setItem(
											slot
													% sChest.getInventory()
															.getSize(), item);
								} else {
									sChest.getInventory().setItem(slot, item);
								}
								iter.remove();
								slot++;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			else {
				player.sendMessage("We are sorry, you didn't explode upon death.");
			}
		}
		plugin.map.remove(player.getName());
	}
}
