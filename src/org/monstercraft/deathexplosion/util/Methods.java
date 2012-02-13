package org.monstercraft.deathexplosion.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.util.wrappers.TombStone;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.ProtectionTypes;

public class Methods {

	private DeathExplosion plugin;
	public HashMap<Player, TombStone> blocks = new HashMap<Player, TombStone>();

	public Methods(DeathExplosion plugin) {
		this.plugin = plugin;
	}

	public Plugin getPlugin(String name) {
		Plugin plugin = this.plugin.getServer().getPluginManager()
				.getPlugin(name);
		return getPlugin(plugin);
	}

	public Plugin getPlugin(Plugin plugin) {
		if (plugin != null && plugin.isEnabled()) {
			return plugin;
		}
		return null;
	}

	public void advertiseDeath(String name, String world, double x, double y,
			double z) {
		try {
			plugin.getServer().broadcastMessage(
					"§" + Variables.colorCode + "Player: " + name
							+ " has died in " + world + " at x:" + x + " Y:"
							+ y + " Z:" + z);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeLWC(Player player, TombStone tBlock) {
		LWC lwc = ((LWCPlugin) getPlugin("LWC")).getLWC();
		if (lwc != null) {
			Block block = tBlock.getBlock();
			Block sblock = block.getWorld().getBlockAt(block.getX(),
					block.getY() + 1, block.getZ());
			lwc.getPhysicalDatabase().registerProtection(block.getTypeId(),
					ProtectionTypes.PUBLIC, block.getWorld().getName(),
					player.getName(), "", block.getX(), block.getY(),
					block.getZ());
			lwc.getPhysicalDatabase().registerProtection(sblock.getTypeId(),
					ProtectionTypes.PUBLIC, sblock.getWorld().getName(), "",
					"", sblock.getX(), sblock.getY(), sblock.getZ());
			block.setTypeId(0);
			sblock.setTypeId(0);

		}
		blocks.remove(player);
	}

	public void registerLWC(Player player, TombStone tBlock) {
		LWC lwc = ((LWCPlugin) getPlugin("LWC")).getLWC();
		if (lwc != null) {
			Block block = tBlock.getBlock();
			lwc.getPhysicalDatabase().registerProtection(block.getTypeId(),
					ProtectionTypes.PUBLIC, block.getWorld().getName(),
					player.getName(), "", block.getX(), block.getY(),
					block.getZ());
		}
		blocks.put(player, tBlock);
	}

	public double getYawTo(Location from, Location to) {
		final int distX = to.getBlockX() - from.getBlockX();
		final int distZ = to.getBlockZ() - from.getBlockZ();
		double degrees = Math.toDegrees(Math.atan2(-distX, distZ));
		degrees += 180;
		return degrees;
	}

	public void createSign(Block signBlock, Player p) {
		String[] signTemplate = new String[4];
		signTemplate[0] = "{name}";
		signTemplate[1] = "Died on";
		signTemplate[2] = "{date}";
		signTemplate[3] = "{time}";
		HashMap<String, String> vars = new HashMap<String, String>();
		vars.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		vars.put("time", new SimpleDateFormat("hh:mm a").format(new Date()));
		String name = p.getName();
		if (name.length() > 15)
			name = name.substring(0, 15);
		vars.put("name", name);
		signBlock.setType(Material.SIGN_POST);
		final Sign sign = (Sign) signBlock.getState();
		for (int i = 0; i < 4; i++) {
			sign.setLine(i, parseVars(signTemplate[i], vars));
		}
		plugin.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						sign.update();
					}
				});
	}

	public String parseVars(String format, HashMap<String, String> vars) {
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(format);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String var = vars.get(matcher.group(1));
			if (var == null)
				var = "";
			matcher.appendReplacement(sb, Matcher.quoteReplacement(var));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public boolean canBuild(Block b, Player p) {
		int spawnSize = p.getServer().getSpawnRadius();
		Location spawn = p.getWorld().getSpawnLocation();
		if (spawnSize > 0) {
			int distanceFromSpawn = (int) Math.max(
					Math.abs(p.getLocation().getBlockX() - spawn.getBlockX()),
					Math.abs(p.getLocation().getBlockZ() - spawn.getBlockZ()));
			if (distanceFromSpawn <= spawnSize)
				return false;
		}
		BlockPlaceEvent event = new BlockPlaceEvent(b, b.getState(),
				b.getRelative(BlockFace.DOWN), p.getItemInHand(), p, true);
		plugin.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return false;
		}
		return true;
	}

	public Block findPlace(Block base) {
		if (canReplace(base.getType())) {
			return base;
		}
		int x = base.getX();
		int y = base.getY();
		int z = base.getZ();
		World w = base.getWorld();

		for (int i = x - 1; i < x + 1; i++) {
			for (int j = z - 1; j < z + 1; j++) {
				Block b = w.getBlockAt(i, y, j);
				if (canReplace(b.getType()))
					return b;
			}
		}

		return null;
	}

	public Block findLarge(Block base) {
		// Check all 4 sides for air.
		Block exp;
		exp = base.getWorld().getBlockAt(base.getX() - 1, base.getY(),
				base.getZ());
		if (canReplace(exp.getType()) && (!checkChest(exp)))
			return exp;
		exp = base.getWorld().getBlockAt(base.getX(), base.getY(),
				base.getZ() - 1);
		if (canReplace(exp.getType()) && (!checkChest(exp)))
			return exp;
		exp = base.getWorld().getBlockAt(base.getX() + 1, base.getY(),
				base.getZ());
		if (canReplace(exp.getType()) && (!checkChest(exp)))
			return exp;
		exp = base.getWorld().getBlockAt(base.getX(), base.getY(),
				base.getZ() + 1);
		if (canReplace(exp.getType()) && (!checkChest(exp)))
			return exp;
		return null;
	}

	public boolean checkChest(Block base) {
		Block exp = null;
		exp = base.getWorld().getBlockAt(base.getX() - 1, base.getY(),
				base.getZ());
		if (exp.getType() == Material.CHEST)
			return true;
		exp = base.getWorld().getBlockAt(base.getX(), base.getY(),
				base.getZ() - 1);
		if (exp.getType() == Material.CHEST)
			return true;
		exp = base.getWorld().getBlockAt(base.getX() + 1, base.getY(),
				base.getZ());
		if (exp.getType() == Material.CHEST)
			return true;
		exp = base.getWorld().getBlockAt(base.getX(), base.getY(),
				base.getZ() + 1);
		if (exp.getType() == Material.CHEST)
			return true;
		return false;
	}

	public Boolean canReplace(Material mat) {
		return (mat == Material.AIR || mat == Material.SAPLING
				|| mat == Material.WATER || mat == Material.STATIONARY_WATER
				|| mat == Material.LAVA || mat == Material.STATIONARY_LAVA
				|| mat == Material.YELLOW_FLOWER || mat == Material.RED_ROSE
				|| mat == Material.BROWN_MUSHROOM
				|| mat == Material.RED_MUSHROOM || mat == Material.FIRE
				|| mat == Material.CROPS || mat == Material.SNOW || mat == Material.SUGAR_CANE);
	}

	public void saveItems(Player player, List<ItemStack> items) {
		try {
			Location loc = player.getLocation();
			Block block = player.getWorld().getBlockAt(loc.getBlockX(),
					loc.getBlockY(), loc.getBlockZ());
			if (block.getType() == Material.STEP
					|| block.getType() == Material.TORCH
					|| block.getType() == Material.REDSTONE_WIRE
					|| block.getType() == Material.RAILS
					|| block.getType() == Material.STONE_PLATE
					|| block.getType() == Material.WOOD_PLATE
					|| block.getType() == Material.REDSTONE_TORCH_ON
					|| block.getType() == Material.REDSTONE_TORCH_OFF
					|| block.getType() == Material.CAKE_BLOCK) {
				block = player.getWorld().getBlockAt(loc.getBlockX(),
						loc.getBlockY() + 1, loc.getBlockZ());
			}
			block = findPlace(block);
			if (block == null) {
				return;
			}
			if (checkChest(block)) {
				return;
			}
			Block lBlock = findLarge(block);
			block.setType(Material.CHEST);
			BlockState state = block.getState();
			if (!(state instanceof Chest)) {
				return;
			}
			Chest sChest = (Chest) state;
			Chest lChest = null;
			int slot = 0;
			int maxSlot = sChest.getInventory().getSize();
			if (items.size() > maxSlot) {
				if (lBlock != null) {
					lBlock.setType(Material.CHEST);
					lChest = (Chest) lBlock.getState();
					maxSlot = maxSlot * 2;
				}
			}
			Block sBlock = null;
			sBlock = sChest.getWorld().getBlockAt(sChest.getX(),
					sChest.getY() + 1, sChest.getZ());
			if (canReplace(sBlock.getType())) {
				createSign(sBlock, player);
			} else if (lChest != null) {
				sBlock = lChest.getWorld().getBlockAt(lChest.getX(),
						lChest.getY() + 1, lChest.getZ());
				if (canReplace(sBlock.getType())) {
					createSign(sBlock, player);
				}
			}
			TombStone tBlock = new TombStone(sChest.getBlock(), sBlock);
			registerLWC(player, tBlock);
			if (Variables.announce) {
				advertiseDeath(player.getName(), tBlock.getBlock().getWorld()
						.getName(), tBlock.getBlock().getX(), tBlock.getBlock()
						.getY(), tBlock.getBlock().getZ());
			}
			for (Iterator<ItemStack> iter = items.listIterator(); iter
					.hasNext();) {
				ItemStack item = iter.next();
				if (item == null)
					continue;
				if (slot < maxSlot) {
					if (slot >= sChest.getInventory().getSize()) {
						if (lChest == null)
							continue;
						lChest.getInventory().setItem(
								slot % sChest.getInventory().getSize(), item);
					} else {
						sChest.getInventory().setItem(slot, item);
					}
					iter.remove();
					slot++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
