package org.monstercraft.deathexplosion.hooks;

import org.bukkit.plugin.Plugin;
import org.monstercraft.deathexplosion.DeathExplosion;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * This class listens for chat ingame to pass to the IRC.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class WorldGuardHook extends DeathExplosion {

	private WorldGuardPlugin worldGuardHook;
	private DeathExplosion plugin;

	public WorldGuardHook(final DeathExplosion plugin) {
		this.plugin = plugin;
		initmcMMOHook();
	}

	private void initmcMMOHook() {
		if (worldGuardHook != null) {
			return;
		}
		Plugin wgplugin = plugin.getServer().getPluginManager()
				.getPlugin("WorldGuard");

		if (wgplugin == null) {
			log("World Guard not detected.");
			worldGuardHook = null;
			return;
		}

		worldGuardHook = ((WorldGuardPlugin) wgplugin);
		log("World Guard detected; hooking: "
				+ ((WorldGuardPlugin) wgplugin).getDescription().getFullName());
	}

	/**
	 * Fetches the hook into mcMMO.
	 * 
	 * @return the hook into mcMMO.
	 */
	public WorldGuardPlugin getHook() {
		return worldGuardHook;
	}

}
