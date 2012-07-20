package org.monstercraft.support.plugin.hooks;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.monstercraft.support.MonsterTickets;

/**
 * This class listens for chat ingame to pass to the IRC.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class VaultPermissionsHook {

	private Permission PermissionsHook;
	private MonsterTickets plugin;

	/**
	 * Creates an instance of the PermissionsHook class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public VaultPermissionsHook(final MonsterTickets plugin) {
		this.plugin = plugin;
		boolean b = setupPermissions();
		if (b) {
			Plugin permsPlugin = plugin.getServer().getPluginManager()
					.getPlugin(PermissionsHook.getName());
			if (PermissionsHook != null) {
				if (permsPlugin != null) {
					MonsterTickets.log("Vault permissions detected; hooking: "
							+ permsPlugin.getDescription().getFullName());
				} else {
					MonsterTickets.log("Permissions found!");
				}
			}
		} else {
			MonsterTickets
					.log("Could not hook into permissions using vault! (Permissions not found?)");
		}
	}

	private Boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = plugin
				.getServer()
				.getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			PermissionsHook = permissionProvider.getProvider();
		}
		return (PermissionsHook != null);
	}

	/**
	 * Fetches the hook into Permissions.
	 * 
	 * @return The hook into Permissions.
	 */
	public Permission getHook() {
		return PermissionsHook;
	}

}
