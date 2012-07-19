package org.monstercraft.monsterticket.plugin.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.plugin.Configuration.Variables;
import org.monstercraft.monsterticket.plugin.wrappers.HelpTicket;

/**
 * This class contains all of the plugins settings.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class SettingsManager extends Ticket {
	private Ticket plugin = null;

	public static String SETTINGS_PATH = "plugins/MonsterTickets/";

	public static String SETTINGS_FILE = "Config.yml";

	/**
	 * Creates an instance of the Settings class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public SettingsManager(Ticket plugin) {
		this.plugin = plugin;
		load();
	}

	private void save(final FileConfiguration config, final File file) {
		try {
			config.save(file);
		} catch (IOException e) {
			debug(e);
		}
	}

	/**
	 * This method loads the plugins configuration file.
	 */
	public void save() {
		final FileConfiguration config = this.plugin.getConfig();
		final File CONFIGURATION_FILE = new File(SETTINGS_PATH + File.separator
				+ SETTINGS_FILE);
		try {
			config.set("MONSTERTICKETS.OPTIONS.OVERRIDE_HELP_COMMAND",
					Variables.overridehelp);
			save(config, CONFIGURATION_FILE);
			saveTicketsConfig();
		} catch (Exception e) {
			debug(e);
		}
	}

	/**
	 * This method loads the plugins configuration file.
	 */
	public void load() {
		final FileConfiguration config = this.plugin.getConfig();
		final File CONFIGURATION_FILE = new File(SETTINGS_PATH + File.separator
				+ SETTINGS_FILE);
		boolean exists = CONFIGURATION_FILE.exists();
		if (exists) {
			try {
				log("Loading settings!");
				config.options()
						.header("MonsterTickets configuration file, refer to our DBO page for help.");
				config.load(CONFIGURATION_FILE);
			} catch (Exception e) {
				debug(e);
			}
		} else {
			log("Loading default settings!");
			config.options()
					.header("MonsterTickets configuration file, refer to our DBO page for help.");
			config.options().copyDefaults(true);
		}
		try {
			Variables.overridehelp = config.getBoolean(
					"MONSTERTICKETS.OPTIONS.OVERRIDE_HELP_COMMAND",
					Variables.overridehelp);
			save(config, CONFIGURATION_FILE);
			loadTicketsConfig();
		} catch (Exception e) {
			debug(e);
		}
	}

	private void loadTicketsConfig() throws FileNotFoundException, IOException,
			InvalidConfigurationException {
		File TICKETS_FILE = new File(SETTINGS_PATH + File.separator
				+ "Tickets.dat");
		if (!TICKETS_FILE.exists()) {
			return;
		}
		List<String> tickets = new ArrayList<String>();
		FileConfiguration config = new YamlConfiguration();
		config.load(TICKETS_FILE);
		config.options().header("DO NOT MODIFY");
		Variables.ticketid = config.getInt("ID", Variables.ticketid);
		tickets = config.getStringList("TICKETS");
		if (!tickets.isEmpty()) {
			for (String str : tickets) {
				if (str.contains("|") && str.contains("=")) {
					int id = Integer
							.parseInt(str.substring(0, str.indexOf("|")));
					String player = str.substring(str.indexOf("|") + 1,
							str.indexOf("="));
					String description = str.substring(str.indexOf("=") + 1);
					Variables.tickets.put(new HelpTicket(id, description,
							player), false);
				}
			}
		}
	}

	private void saveTicketsConfig() {
		File TICKETS_FILE = new File(SETTINGS_PATH + File.separator
				+ "Tickets.dat");
		List<String> tickets = new ArrayList<String>();
		FileConfiguration config = new YamlConfiguration();
		config.options().header("DO NOT MODIFY");
		Iterator<HelpTicket> i = Variables.tickets.keySet().iterator();
		while (i.hasNext()) {
			HelpTicket h = i.next();
			tickets.add(h.getID() + "|" + h.getPlayerName() + "="
					+ h.getDescription());
		}
		config.set("ID", Variables.ticketid);
		if (!tickets.isEmpty()) {
			config.set("TICKETS", tickets);
		}
		save(config, TICKETS_FILE);
	}
}
