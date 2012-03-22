package org.monstercraft.deathexplosion.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.monstercraft.deathexplosion.DeathExplosion;

public class TeleportManager {

	List<Integer> throughBlocks = new ArrayList<Integer>(
			Arrays.asList(new Integer[] { 0, 6, 8, 9, 10, 11, 37, 38, 39, 40,
					50, 51, 55, 59, 69, 76 }));

	public TeleportManager(DeathExplosion plugin) {
	}

	public boolean teleport(Entity entity, Location destination) {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		entities.add(entity);

		return teleport(entities, destination);
	}

	public boolean teleport(ArrayList<Entity> entities, Location destination) {
		World world = destination.getWorld();
		double x = destination.getBlockX();
		double y = destination.getBlockY();
		double z = destination.getBlockZ();

		x = x + .5D;
		z = z + .5D;

		if (y < 1.0D) {
			y = 1.0D;
		}

		if (!world.isChunkLoaded(destination.getBlockX() >> 4,
				destination.getBlockZ() >> 4)) {
			world.loadChunk(destination.getBlockX() >> 4,
					destination.getBlockZ() >> 4);
		}

		while (blockIsAboveAir(world, x, y, z)) {
			y -= 1.0D;

			if (y < -512) {
				return false;
			}
		}

		while (!blockIsSafe(world, x, y, z)) {
			y += 1.0D;

			if (y > 512) {
				return false;
			}
		}

		for (Entity entity : entities) {
			Location targetLocation = new Location(world, x, y, z,
					destination.getYaw(), destination.getPitch());

			entity.teleport(targetLocation);
		}

		return true;
	}

	private boolean blockIsAboveAir(World world, double x, double y, double z) {
		Material mat = world.getBlockAt((int) Math.floor(x),
				(int) Math.floor(y - 1.0D), (int) Math.floor(z)).getType();

		return throughBlocks.contains(mat.getId());
	}

	public boolean blockIsSafe(Block block) {
		return blockIsSafe(block.getWorld(), block.getX(), block.getY(),
				block.getZ());
	}

	public boolean blockIsSafe(World world, double x, double y, double z) {
		Material mat1 = world.getBlockAt((int) Math.floor(x),
				(int) Math.floor(y), (int) Math.floor(z)).getType();
		Material mat2 = world.getBlockAt((int) Math.floor(x),
				(int) Math.floor(y + 1.0D), (int) Math.floor(z)).getType();

		return (throughBlocks.contains(mat1.getId()))
				&& (throughBlocks.contains(mat2.getId()));
	}

	public Location calculateSmartLocation(Player player) {
		return player.getLocation();
	}
}