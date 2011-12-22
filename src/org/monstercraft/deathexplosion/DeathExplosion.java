package org.monstercraft.deathexplosion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.monstercraft.deathexplosion.command.commands.Pay;
import org.monstercraft.deathexplosion.hooks.iConomyHook;
import org.monstercraft.deathexplosion.listeners.DeathExplosionEntityListener;

public class DeathExplosion extends JavaPlugin {

	private DeathExplosionEntityListener entityListener;
	Logger log = Logger.getLogger("Minecraft");
	public iConomyHook iConomy;
	public List<org.monstercraft.deathexplosion.command.Command> commands;
	public HashMap<String, Integer> map;

	@Override
	public void onEnable() {
		commands = new ArrayList<org.monstercraft.deathexplosion.command.Command>();
		map = new HashMap<String, Integer>();
		registerHooks();
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
	}
	
	private void registerHooks() {
		iConomy = new iConomyHook(this);
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
		PluginManager pm = this.getServer().getPluginManager();
		entityListener = new DeathExplosionEntityListener(this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener,
				Event.Priority.Normal, this);
	}
}