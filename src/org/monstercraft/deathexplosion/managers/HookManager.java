package org.monstercraft.deathexplosion.managers;

import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.hooks.PreciousStonesHook;
import org.monstercraft.deathexplosion.hooks.VaultEconomyHook;
import org.monstercraft.deathexplosion.hooks.WorldGuardHook;

/**
 * This class manages all of the hooks used within the plugin.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class HookManager {

	private VaultEconomyHook economy = null;
	private WorldGuardHook wg = null;
	private PreciousStonesHook ps = null;

	/**
	 * Creates an instance of the HookManager class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public HookManager(final DeathExplosion plugin) {
		economy = new VaultEconomyHook(plugin);
		wg = new WorldGuardHook(plugin);
		ps = new PreciousStonesHook(plugin);
	}

	/**
	 * Fetches the Permissions hook.
	 * 
	 * @return The hook into Permissions.
	 */
	public VaultEconomyHook getPermissionsHook() {
		return economy;
	}
	
	/**
	 * Fetches the Permissions hook.
	 * 
	 * @return The hook into Permissions.
	 */
	public PreciousStonesHook getPSHook() {
		return ps;
	}
	
	/**
	 * Fetches the Permissions hook.
	 * 
	 * @return The hook into Permissions.
	 */
	public WorldGuardHook getWGHook() {
		return wg;
	}

	/**
	 * Creates a new hook into the plugin.
	 * 
	 * @param hook
	 *            A hook into Permissions.
	 * @return The new PermissionsHook.
	 */
	public VaultEconomyHook setPermissionsHook(final VaultEconomyHook hook) {
		return economy = hook;
	}
	
	/**
	 * Creates a new hook into the plugin.
	 * 
	 * @param hook
	 *            A hook into Permissions.
	 * @return The new PermissionsHook.
	 */
	public PreciousStonesHook setPSHook(final PreciousStonesHook hook) {
		return ps = hook;
	}
	
	/**
	 * Creates a new hook into the plugin.
	 * 
	 * @param hook
	 *            A hook into Permissions.
	 * @return The new PermissionsHook.
	 */
	public WorldGuardHook setWorldGuardHook(final WorldGuardHook hook) {
		return wg = hook;
	}

}
