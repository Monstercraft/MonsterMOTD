package org.monstercraft.deathexplosion.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.util.wrappers.TombStone;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.ProtectionTypes;

public class Methods {

	private DeathExplosion plugin;

	public Methods(DeathExplosion plugin) {
		this.plugin = plugin;
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

	public void registerLWC(Player player, TombStone tBlock) {
		LWC lwc = ((LWCPlugin) plugin.getPlugin("LWC")).getLWC();
		if (lwc != null) {
			Block block = tBlock.getBlock();
			lwc.getPhysicalDatabase().registerProtection(block.getTypeId(),
					ProtectionTypes.PUBLIC, block.getWorld().getName(),
					player.getName(), "", block.getX(), block.getY(),
					block.getZ());
		}
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
}
