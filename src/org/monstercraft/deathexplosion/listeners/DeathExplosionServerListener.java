package org.monstercraft.deathexplosion.listeners;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.monstercraft.deathexplosion.DeathExplosion;

public class DeathExplosionServerListener extends ServerListener {

	private DeathExplosion plugin;

	public DeathExplosionServerListener(DeathExplosion plugin) {
		this.plugin = plugin;
	}

	public void onPluginEnable(PluginEnableEvent event) {
		String PluginName = event.getPlugin().getDescription().getName();
		if (this.plugin != null) {
			if (PluginName.equals("iConomy")) {
				plugin.getPlugin(event.getPlugin());
			}
		}
	}

	public void onPluginDisable(PluginDisableEvent event) {
		String PluginName = event.getPlugin().getDescription().getName();
		if (this.plugin != null) {
			if (PluginName.equals("iConomy")) {
				plugin.getServer().getPluginManager()
						.disablePlugin(this.plugin);
			}
		}
	}
}