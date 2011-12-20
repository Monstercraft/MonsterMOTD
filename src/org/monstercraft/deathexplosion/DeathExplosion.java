package org.monstercraft.deathexplosion;

import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathExplosion extends JavaPlugin {
    
    private final DeathExplosionPlayerListener playerListener = new DeathExplosionPlayerListener(this);
    Logger log = Logger.getLogger("Minecraft");
 
	public void onEnable(){
            
		log.info("DeathExplosion has been enabled!");
                PluginManager pm = this.getServer().getPluginManager();
                pm.registerEvent(Event.Type.ENTITY_DEATH, playerListener, Event.Priority.Normal, this);
	}
 
	public void onDisable(){
		log.info("DeathExplosion has been disabled.");
	}
}