package org.monstercraft.support.plugin.managers;

import net.milkbowl.vault.permission.Permission;

import org.monstercraft.support.MonsterTickets;
import org.monstercraft.support.plugin.hooks.VaultPermissionsHook;

/**
 * This class manages all of the hooks used within the plugin.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class HookManager {

	private VaultPermissionsHook permissions = null;
	private final MonsterTickets plugin;

	/**
	 * Creates an instance of the HookManager class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public HookManager(final MonsterTickets plugin) {
		this.plugin = plugin;
		permissions = new VaultPermissionsHook(plugin);
	}

	/**
	 * Fetches the Permissions hook.
	 * 
	 * @return The hook into Permissions.
	 */
	public Permission getPermissionsHook() {
		return permissions.getHook();
	}

	/**
	 * Creates a new hook into the plugin.
	 * 
	 * @return The new PermissionsHook.
	 */
	public void setPermissionsHook() {
		permissions = new VaultPermissionsHook(plugin);
	}

}