package org.monstercraft.deathexplosion.hooks;

import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;

import org.bukkit.plugin.Plugin;
import org.monstercraft.deathexplosion.DeathExplosion;

/**
 * This class listens for chat ingame to pass to the IRC.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class PreciousStonesHook extends DeathExplosion {

	private PreciousStones pStonesHook;
	private DeathExplosion plugin;

	public PreciousStonesHook(final DeathExplosion plugin) {
		this.plugin = plugin;
		initmcMMOHook();
	}

	private void initmcMMOHook() {
		if (pStonesHook != null) {
			return;
		}
		Plugin psplugin = plugin.getServer().getPluginManager()
				.getPlugin("PreciousStones");

		if (psplugin == null) {
			log("PStones not detected.");
			pStonesHook = null;
			return;
		}

		pStonesHook = ((PreciousStones) psplugin);
		log("Pstones detected; hooking: "
				+ ((PreciousStones) psplugin).getDescription().getFullName());
	}

	/**
	 * Fetches the hook into mcMMO.
	 * 
	 * @return the hook into mcMMO.
	 */
	public PreciousStones getHook() {
		return pStonesHook;
	}

}
