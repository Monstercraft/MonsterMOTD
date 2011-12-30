package org.monstercraft.deathexplosion.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.monstercraft.deathexplosion.DeathExplosion;

import com.iCo6.system.Account;

public class DeathExplosionPlayerListener extends PlayerListener {
	private DeathExplosion plugin;

	public DeathExplosionPlayerListener(DeathExplosion plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (plugin.map.containsKey(player.getName())) {
			Account a = new Account(player.getName());
			a.getHoldings().add(plugin.map.get(player.getName()));
		}
	}
}
