package org.monstercraft.deathexplosion;

import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.monstercraft.deathexplosion.listeners.DeathExplosionPlayerListener;

public class DeathExplosion extends JavaPlugin {

	private DeathExplosionPlayerListener playerListener;
	Logger log = Logger.getLogger("Minecraft");
@Override
	public void onEnable() {
		playerListener = new DeathExplosionPlayerListener(
					this);
		log.info("DeathExplosion has been enabled!");
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DEATH, playerListener,
				Event.Priority.Normal, this);
	}
@Override
	public void onDisable() {
		log.info("DeathExplosion has been disabled.");
	}
}