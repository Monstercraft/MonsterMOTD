package org.monstercraft.motd;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.monstercraft.motd.plugin.Configuration;
import org.monstercraft.motd.plugin.Configuration.Variables;
import org.monstercraft.motd.plugin.managers.CommandManager;
import org.monstercraft.motd.plugin.managers.HandleManager;
import org.monstercraft.motd.plugin.managers.HookManager;
import org.monstercraft.motd.plugin.managers.SettingsManager;
import org.monstercraft.motd.plugin.reflection.Reflect;
import org.monstercraft.motd.plugin.util.Metrics;
/**
 * This class represents the main plugin. All actions related to the plugin are
 * forwarded by this class
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class MonsterMOTD extends JavaPlugin implements Runnable {

	/**
	 * Enables the plugin.
	 */

	private static SettingsManager settings = null;

	private static CommandManager command = null;

	private static HookManager hooks = null;

	private static HandleManager handles = null;

	public void onEnable() {
		try {
			Variables.defaultMOTD = Reflect.getMotd();
		} catch (Exception e) {
			log("Error setting up!");
			debug(e);
		}
		hooks = new HookManager(this);
		handles = new HandleManager();
		command = new CommandManager();
		settings = new SettingsManager(this);
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

	/**
	 * Disables the plugin.
	 */

	public void onDisable() {
		log("Disabling MonsterMOTD....");
		log("Setting back the default MOTD....");
		try {
			Reflect.setMotd(Variables.defaultMOTD);
		} catch (Exception e) {
		}
		log("Successfully disabled MonsterMOTD");
	}

	private final static Logger logger = Logger.getLogger(MonsterMOTD.class
			.getSimpleName());

	/**
	 * Logs a message to the console.
	 * 
	 * @param msg
	 *            The message to print.
	 */
	public static void log(final String msg) {
		logger.log(Level.INFO, "[MonsterMOTD] " + msg);
	}

	/**
	 * Logs debugging messages to the console.
	 * 
	 * @param error
	 *            The message to print.
	 */
	public static void debug(final String error, final boolean console) {
		if (console) {
			logger.log(Level.WARNING, "[MonsterMOTD - Debug] " + error);
		}
	}

	/**
	 * Logs debugging messages to the console.
	 * 
	 * @param error
	 *            The message to print.
	 */
	public static void debug(final Exception error) {
		logger.log(Level.SEVERE, "[MonsterIRC - Critical error detected!]");
		error.printStackTrace();
	}

	/**
	 * Handles commands.
	 */

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return getCommandManager().onGameCommand(sender, command, label, args);
	}

	/**
	 * The manager that creates the hooks with other plugins.
	 * 
	 * @return The hooks.
	 */
	public static HookManager getHookManager() {
		return hooks;
	}

	/**
	 * The manager that holds the handlers.
	 * 
	 * @return The handlers.
	 */
	public static HandleManager getHandleManager() {
		return handles;
	}

	/**
	 * The plugins settings.
	 * 
	 * @return The settings.
	 */
	protected static SettingsManager getSettingsManager() {
		return settings;
	}

	/**
	 * The CommandManager that Assigns all the commands.
	 * 
	 * @return The plugins command manager.
	 */
	protected static CommandManager getCommandManager() {
		return command;
	}

	public void run() {
		try {
			log("Setting up metrics!");
			Metrics metrics = new Metrics(this);
			metrics.start();
			String newVersion = Configuration.checkForUpdates(this,
					Configuration.URLS.UPDATE_URL);
			if (!newVersion.contains(Configuration.getCurrentVerison(this))) {
				log(newVersion + " is out! You are running "
						+ Configuration.getCurrentVerison(this));
				log("Update MonsterMOTD at: http://dev.bukkit.org/server-mods/monstermotd");
			} else {
				log("You are using the latest version of MonsterMOTD");
			}
		} catch (Exception e) {
			debug(e);
		}
	}
}
