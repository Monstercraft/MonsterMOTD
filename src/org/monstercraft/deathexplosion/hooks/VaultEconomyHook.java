package org.monstercraft.deathexplosion.hooks;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.monstercraft.deathexplosion.DeathExplosion;

/**
 * This class listens for chat ingame to pass to the IRC.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class VaultEconomyHook extends DeathExplosion {

	private Economy economyHook;
	private DeathExplosion plugin;

	/**
	 * Creates an instance of the PermissionsHook class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public VaultEconomyHook(final DeathExplosion plugin) {
		this.plugin = plugin;
		boolean b = setupPermissions();
		if (b) {
			Plugin permsPlugin = plugin.getServer().getPluginManager()
					.getPlugin(economyHook.getName());
			if (economyHook != null) {
				if (permsPlugin != null) {
					log("Vault permissions detected; hooking: "
							+ permsPlugin.getDescription().getFullName());
				} else {
					log("Permissions found!");
				}
			}
		} else {
			log("Could not hook into permissions using vault! (Permissions not found?)");
		}
	}

	private Boolean setupPermissions() {
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer()
				.getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economyHook = economyProvider.getProvider();
		}
		return (economyHook != null);
	}

	/**
	 * Fetches the hook into Permissions.
	 * 
	 * @return The hook into Permissions.
	 */
	public Economy getHook() {
		return economyHook;
	}

}
