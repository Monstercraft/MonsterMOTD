package org.monstercraft.deathexplosion;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.monstercraft.deathexplosion.hooks.StatusHook;
import org.monstercraft.deathexplosion.listeners.DeathExplosionListener;
import org.monstercraft.deathexplosion.managers.CommandManager;
import org.monstercraft.deathexplosion.managers.HookManager;
import org.monstercraft.deathexplosion.managers.SettingsManager;
import org.monstercraft.deathexplosion.util.Methods;
import org.monstercraft.deathexplosion.util.Timer;

public class DeathExplosion extends JavaPlugin {

	private DeathExplosionListener listener = null;
	private static Logger logger = Logger.getLogger("Minecraft");
	private static CommandManager commandManager = null;
	private static SettingsManager settings = null;
	private static HookManager hooks = null;
	private DeathExplosion plugin = null;
	public static HashMap<Block, Timer> timedblocks = new HashMap<Block, Timer>();
	private static StatusHook sch;
	public static Object blockLock = new Object();

	public void onEnable() {
		plugin = this;
		settings = new SettingsManager(plugin);
		hooks = new HookManager(plugin);
		sch = new StatusHook();
		commandManager = new CommandManager(plugin);
		listener = new DeathExplosionListener(plugin);
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		Thread t = new Thread(timing);
		t.setPriority(Thread.MAX_PRIORITY);
		t.setDaemon(true);
		t.start();
		log("DeathExplosion has been enabled!");
	}

	private Runnable timing = new Runnable() {
		public void run() {
			while (true) {
				synchronized (blockLock) {
					for (Block b : timedblocks.keySet()) {
						if (timedblocks.get(b).getRemaining() == 0) {
							b.setType(Material.AIR);
							Block b2 = b.getWorld().getBlockAt(
									b.getLocation().getBlockX(),
									b.getLocation().getBlockY() + 1,
									b.getLocation().getBlockZ());
							b2.setType(Material.AIR);
							Methods.removeLWC(b);
							Methods.removeLWC(b2);
							timedblocks.remove(b);
						}
					}
				}
			}
		}
	};

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
	public static void log(String msg) {
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

	public static StatusHook getSCH() {
		return sch;
	}
}