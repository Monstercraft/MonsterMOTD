package me.dqtreats.DeathExplosion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerListener;

public class DeathExplosionPlayerListener extends PlayerListener{
    
    public DeathExplosion plugin;
    
    public DeathExplosionPlayerListener(DeathExplosion instance) {
    plugin = instance;
}

    public void PlayerDeath(PlayerDeathEvent event) {
            Entity entity = event.getEntity();

        if (entity instanceof TNTPrimed){
            TNTPrimed tnt = (TNTPrimed) entity;

            
            event.getEntity().getWorld().createExplosion(tnt.getLocation(), 0);
        }
    }
}
