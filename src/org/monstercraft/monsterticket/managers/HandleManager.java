package org.monstercraft.monsterticket.managers;

import net.milkbowl.vault.permission.Permission;

import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.managers.handlers.PermissionsHandler;

/**
 * This class contains all of the plugins handles.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class HandleManager extends Ticket {

	private PermissionsHandler perms = null;

	/**
	 * Creates an instance of the Handle class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public HandleManager() {
		perms = new PermissionsHandler(getHookManager().getPermissionsHook());
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
