package org.monstercraft.deathexplosion.hooks;

import org.bukkit.plugin.Plugin;
import org.monstercraft.deathexplosion.DeathExplosion;

import com.iCo6.iConomy;

public class iConomyHook {
	
	public iConomy iConomyHook;
	private DeathExplosion plugin;
	
	public iConomyHook(DeathExplosion plugin) {
		this.plugin = plugin;
		initiConomyHook();
	}
	
	private void initiConomyHook() {
		if (iConomyHook != null) {
			return;
		}
		Plugin iComomyPlugin = plugin.getServer().getPluginManager()
				.getPlugin("iConomy");

		if (iComomyPlugin == null) {
			System.out.println("[DeathExplosion] iComomy not detected.");
			return;
		}

		iConomyHook = ((iConomy) iComomyPlugin);
		System.out.println("[DeathExplosion] iComomy detected; hooking: "
				+ ((iConomy) iComomyPlugin).getDescription().getFullName());
	}

}
