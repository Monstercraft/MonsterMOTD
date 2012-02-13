package org.monstercraft.deathexplosion.listeners;

import java.util.Random;

import net.sacredlabyrinth.Phaed.PreciousStones.FieldFlag;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.hooks.PreciousStonesHook;
import org.monstercraft.deathexplosion.hooks.VaultEconomyHook;
import org.monstercraft.deathexplosion.hooks.WorldGuardHook;
import org.monstercraft.deathexplosion.util.Methods;
import org.monstercraft.deathexplosion.util.Variables;

public class DeathExplosionListener extends DeathExplosion implements Listener {

	private DeathExplosion plugin;
	private Random ran = new Random();
	private Methods methods;

	public DeathExplosionListener(DeathExplosion plugin) {
		this.plugin = plugin;
		methods = new Methods(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPluginEnable(PluginEnableEvent event) {
		String PluginName = event.getPlugin().getDescription().getName();
		if (this.plugin != null) {
			if (PluginName.equals("Vault")) {
				plugin.getHookManager().setPermissionsHook(
						new VaultEconomyHook(plugin));
			}
			if (PluginName.equals("PreciousStones")) {
				plugin.getHookManager().setPSHook(
						new PreciousStonesHook(plugin));
			}
			if (PluginName.equals("WorldGuard")) {
				plugin.getHookManager().setWorldGuardHook(
						new WorldGuardHook(plugin));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPluginDisable(PluginDisableEvent event) {
		String PluginName = event.getPlugin().getDescription().getName();
		if (this.plugin != null) {
			if (PluginName.equals("Vault")) {
				plugin.getServer().getPluginManager()
						.disablePlugin(this.plugin);
				log("Disabling because no economy was found!");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (Variables.map.containsKey(player.getName())) {
			plugin.getHookManager()
					.getPermissionsHook()
					.getHook()
					.depositPlayer(player.getName(),
							Variables.map.get(player.getName()));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event) {
		int i = ran.nextInt(Variables.cost);
		Entity entity = event.getEntity();
		EntityDamageEvent cause = event.getEntity().getLastDamageCause();
		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		Location loc = player.getLocation();
		Block block = player.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY(), loc.getBlockZ());
		if (plugin.getHookManager().getWGHook() != null) {
			if (!plugin.getHookManager().getWGHook().getHook()
					.canBuild(player, block)) {
				return;
			}
		} else {
			System.out.println("Worldguard null");
		}
		if (!methods.canBuild(block, player)) {
			return;
		}
		if (plugin.getHookManager().getPSHook() != null) {
			if (plugin
					.getHookManager()
					.getPSHook()
					.getHook()
					.getForceFieldManager()
					.hasSourceField(block.getLocation(),
							FieldFlag.PREVENT_DESTROY)) {
				return;
			}
		}
		if (Variables.map.containsKey(player.getName())) {
			if (i <= Variables.map.get(player.getName())) {
				World world = player.getWorld();
				world.createExplosion(loc, Variables.size);
				player.sendMessage("You have exploded.");
				methods.saveItems(player, event.getDrops());
				Variables.map.remove(player.getName());
			} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
				methods.saveItems(player, event.getDrops());
				player.sendMessage("You have died from someone elses explosion");
				player.sendMessage("We are sorry, you didn't explode upon death.");
				Variables.map.remove(player.getName());
			} else {
				player.sendMessage("We are sorry, you didn't explode upon death.");
				Variables.map.remove(player.getName());
			}
		} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
			methods.saveItems(player, event.getDrops());
			player.sendMessage("You have died from someone elses explosion");
		}
	}
}