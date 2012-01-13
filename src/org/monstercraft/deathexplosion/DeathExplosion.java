package org.monstercraft.deathexplosion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.monstercraft.deathexplosion.command.commands.Pay;
import org.monstercraft.deathexplosion.command.commands.PrivateChest;
import org.monstercraft.deathexplosion.listeners.DeathExplosionEntityListener;
import org.monstercraft.deathexplosion.listeners.DeathExplosionPlayerListener;
import org.monstercraft.deathexplosion.listeners.DeathExplosionServerListener;
import org.monstercraft.deathexplosion.util.Configuration;
import org.monstercraft.deathexplosion.util.Methods;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class DeathExplosion extends JavaPlugin {

	private DeathExplosionEntityListener entityListener;
	private DeathExplosionPlayerListener playerListener;
	private DeathExplosionServerListener serverListener;
	Logger log = Logger.getLogger("Minecraft");
	public List<org.monstercraft.deathexplosion.command.Command> commands;
	public HashMap<String, Integer> map;
	public HashMap<String, Integer> pchest;
	public Configuration config;
	public Methods methods;

	@Override
	public void onEnable() {
		commands = new ArrayList<org.monstercraft.deathexplosion.command.Command>();
		config = new Configuration(this);
		methods = new Methods(this);
		map = new HashMap<String, Integer>();
		pchest = new HashMap<String, Integer>();
		registerListeners();
		registerCommands();
		log.info("DeathExplosion has been enabled!");
	}

	@Override
	public void onDisable() {
		log.info("DeathExplosion has been disabled.");
	}

	private void registerCommands() {
		commands.add(new Pay(this));
		commands.add(new PrivateChest(this));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length > 0) {
			String[] split = new String[args.length + 1];
			split[0] = label;
			for (int a = 0; a < args.length; a++) {
				split[a + 1] = args[a];
			}
			for (org.monstercraft.deathexplosion.command.Command c : commands) {
				if (c.canExecute(sender, split)) {
					c.execute(sender, split);
					return true;
				}
			}
			return false;
		}
		return false;
	}

	private void registerListeners() {
		serverListener = new DeathExplosionServerListener(this);
		entityListener = new DeathExplosionEntityListener(this);
		playerListener = new DeathExplosionPlayerListener(this);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener,
				Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener,
				Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Type.PLUGIN_ENABLE, serverListener, Priority.Normal,
				this);
		pm.registerEvent(Type.PLUGIN_DISABLE, serverListener, Priority.Normal,
				this);
	}

	public Plugin getPlugin(String name) {
		Plugin plugin = this.getServer().getPluginManager().getPlugin(name);
		return getPlugin(plugin);
	}

	public Plugin getPlugin(Plugin plugin) {
		if (plugin != null && plugin.isEnabled()) {
			return plugin;
		}
		return null;
	}

	public WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null; 
		}
		if (!plugin.isEnabled()) {
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}

	public PreciousStones getPStones() {
		Plugin plugin = getServer().getPluginManager().getPlugin(
				"PreciousStones");
		if (plugin == null || !(plugin instanceof PreciousStones)) {
			return null;
		}
		if (!plugin.isEnabled()) {
			return null;
		}
		return (PreciousStones) plugin;
	}
}