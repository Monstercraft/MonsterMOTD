package org.monstercraft.deathexplosion;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.monstercraft.deathexplosion.listeners.DeathExplosionListener;
import org.monstercraft.deathexplosion.managers.CommandManager;
import org.monstercraft.deathexplosion.managers.HookManager;
import org.monstercraft.deathexplosion.managers.SettingsManager;

public class DeathExplosion extends JavaPlugin {

	private DeathExplosionListener listener = null;
	private static Logger logger = Logger.getLogger("Minecraft");
	private static CommandManager commandManager = null;
	private static SettingsManager settings = null;
	private static HookManager hooks = null;
	private DeathExplosion plugin = null;

	public void onEnable() {
		plugin = this;
		settings = new SettingsManager(plugin);
		hooks = new HookManager(plugin);
		commandManager = new CommandManager(plugin);
		listener = new DeathExplosionListener(plugin);
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		log("DeathExplosion has been enabled!");
	}

	public void onDisable() {
		log("DeathExplosion has been disabled.");
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return getCommandManager().onGameCommand(sender, command, label, args);
	}

	/**
	 * Logs a message to the console.
	 * 
	 * @param msg
	 *            The message to print.
	 */
	protected static void log(String msg) {
		logger.log(Level.INFO, "[Death Explosions] " + msg);
	}

	/**
	 * Logs debugging messages to the console.
	 * 
	 * @param error
	 *            The message to print.
	 */
	protected static void debug(Exception error) {
		logger.log(Level.WARNING,
				"[Death Explosions - Critical error detected!]");
		error.printStackTrace();
	}

	public SettingsManager getSettingsManager() {
		return settings;
	}

	public HookManager getHookManager() {
		return hooks;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
}