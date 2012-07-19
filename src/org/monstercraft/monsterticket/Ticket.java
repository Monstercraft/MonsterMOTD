package org.monstercraft.monsterticket;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.monstercraft.monsterticket.listeners.MonsterTicketListener;
import org.monstercraft.monsterticket.managers.CommandManager;
import org.monstercraft.monsterticket.managers.HandleManager;
import org.monstercraft.monsterticket.managers.HookManager;
import org.monstercraft.monsterticket.managers.SettingsManager;

public class Ticket extends JavaPlugin {

	private static Logger logger = Logger.getLogger("Minecraft");
	private CommandManager commandManager;
	private static HookManager hooks = null;
	private static HandleManager handles = null;
	private static SettingsManager settings = null;
	private static MonsterTicketListener listener = null;

	public void onEnable() {
		settings = new SettingsManager(this);
		Ticket.hooks = new HookManager(this);
		Ticket.handles = new HandleManager();
		listener = new MonsterTicketListener();
		getServer().getPluginManager().registerEvents(listener, this);
		this.commandManager = new CommandManager();
		log("MonsterTickets has been enabled!");
	}

	public void onDisable() {
		settings.save();
		log("MonsterTickets has been disabled.");
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return commandManager.onGameCommand(sender, command, label, args);
	}

	/**
	 * Logs a message to the console.
	 * 
	 * @param msg
	 *            The message to print.
	 */
	public static void log(String msg) {
		logger.log(Level.INFO, "[MonsterTickets] " + msg);
	}

	/**
	 * Logs debugging messages to the console.
	 * 
	 * @param error
	 *            The message to print.
	 */
	public static void debug(Exception error) {
		logger.log(Level.WARNING, "[MonsterTickets - Critical error detected!]");
		error.printStackTrace();
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

	public static SettingsManager getSettingsManager() {
		return settings;
	}

}