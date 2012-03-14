package org.monstercraft.deathexplosion.listeners;

import java.util.Map;
import java.util.Random;

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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.hooks.VaultEconomyHook;
import org.monstercraft.deathexplosion.hooks.WorldGuardHook;
import org.monstercraft.deathexplosion.util.Methods;
import org.monstercraft.deathexplosion.util.Variables;
import org.monstercraft.deathexplosion.util.wrappers.Timer;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class DeathExplosionListener extends DeathExplosion implements Listener {

	private final DeathExplosion plugin;
	private final Random ran = new Random();
	private final Methods methods;

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
			if (PluginName.equalsIgnoreCase("WorldGuard")) {
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
		if (Variables.off) {
			player.sendMessage("The plugin is currently disabled");
			return;
		}
		if (plugin.getHookManager().getWGHook() != null) {
			if (plugin.getHookManager().getWGHook().getHook()
					.getGlobalRegionManager().get(loc.getWorld()).getRegions() != null) {
				Map<String, ProtectedRegion> r = plugin.getHookManager()
						.getWGHook().getHook().getGlobalRegionManager()
						.get(loc.getWorld()).getRegions();
				for (String s : r.keySet()) {
					if (r.get(s).contains(loc.getBlockX(), loc.getBlockY(),
							loc.getBlockZ())) {
						player.sendMessage("You can not explode here!");
						return;
					}
				}
			}
		}
		if (Variables.map.containsKey(player.getName())) {
			if (i <= Variables.map.get(player.getName())) {
				World world = player.getWorld();
				world.createExplosion(loc, Variables.size);
				player.sendMessage("You have exploded.");
				Block b = methods.saveItems(player, event.getDrops());
				new Timer(Variables.time, b);
				methods.removeStatusBar(player);
				Variables.map.remove(player.getName());
			} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
				Block b = methods.saveItems(player, event.getDrops());
				new Timer(Variables.time, b);
				player.sendMessage("You have died from someone elses explosion");
				player.sendMessage("We are sorry, you didn't explode upon death.");
				methods.removeStatusBar(player);
				Variables.map.remove(player.getName());
			} else {
				player.sendMessage("We are sorry, you didn't explode upon death.");
				methods.removeStatusBar(player);
				Variables.map.remove(player.getName());
			}
		} else if (cause.getCause() == DamageCause.BLOCK_EXPLOSION) {
			Block b = methods.saveItems(player, event.getDrops());
			new Timer(Variables.time, b);
			player.sendMessage("You have died from someone elses explosion");
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		if (Variables.map.containsKey(p.getName())) {
			Methods.attachStatusBar(p);
		}
	}
}