package org.monstercraft.deathexplosion.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Variables {

	public static boolean announce = true;

	public static String colorCode = "d";

	public static int cost = 1000000;

	public static int size = 15;

	public static int time = 300000;

	public static Map<String, Double> map = new HashMap<String, Double>();

	public static boolean off = false;

	public static Block last = null;

	public static Map<String, ProtectedCuboidRegion> regions = new HashMap<String, ProtectedCuboidRegion>();

}
