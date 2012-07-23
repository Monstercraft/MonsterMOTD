package org.monstercraft.motd.plugin.managers;

import org.monstercraft.motd.MonsterMOTD;
import org.monstercraft.motd.plugin.managers.handlers.PermissionsHandler;

import net.milkbowl.vault.permission.Permission;

/**
 * This class contains all of the plugins handles.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class HandleManager {

	private PermissionsHandler perms = null;

	/**
	 * Creates an instance of the Handle class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public HandleManager() {
		perms = new PermissionsHandler(MonsterMOTD.getHookManager()
				.getPermissionsHook());
	}

	/**
	 * Fetches the permission handler.
	 * 
	 * @return The PermissionsHandler.
	 */
	public PermissionsHandler getPermissionsHandler() {
		return perms;
	}

	/**
	 * Sets the PermissionsHandler.
	 * 
	 * @param hook
	 *            The hook into Permissions.
	 * @return The new permissions Handler.
	 */
	public PermissionsHandler setPermissionsHandler(final Permission hook) {
		if (hook != null) {
			return perms = new PermissionsHandler(hook);
		}
		return perms;
	}

}
