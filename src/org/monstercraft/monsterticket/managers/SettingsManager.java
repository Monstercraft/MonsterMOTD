package org.monstercraft.monsterticket.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.util.Variables;
import org.monstercraft.monsterticket.wrappers.HelpTicket;

/**
 * This class contains all of the plugins settings.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class SettingsManager extends Ticket {
	private Ticket plugin = null;

	public static String SETTINGS_PATH = "plugins/MonsterTicket/";

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
		List<String> tickets = new ArrayList<String>();
		Iterator<HelpTicket> i = Variables.tickets.keySet().iterator();
		while (i.hasNext()) {
			HelpTicket h = i.next();
			tickets.add(h.getID() + "|" + h.getPlayerName() + "="
					+ h.getDescription());
		}
		try {
			config.set("ID", Variables.ticketid);
			if (!tickets.isEmpty()) {
				config.set("TICKETS", tickets);
			}
			save(config, CONFIGURATION_FILE);
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
				config.options().copyDefaults(true);
				config.load(CONFIGURATION_FILE);
			} catch (Exception e) {
				debug(e);
			}
		} else {
			log("Loading default settings!");
			config.options().copyDefaults(true);
		}
		try {
			List<String> tickets = new ArrayList<String>();
			Variables.ticketid = config.getInt("ID", Variables.ticketid);
			tickets = config.getStringList("TICKETS");
			if (!tickets.isEmpty()) {
				for (String str : tickets) {
					if (str.contains("|") && str.contains("=")) {
						int id = Integer.parseInt(str.substring(0,
								str.indexOf("|")));
						String player = str.substring(str.indexOf("|") + 1,
								str.indexOf("="));
						String description = str
								.substring(str.indexOf("=") + 1);
						Variables.tickets.put(new HelpTicket(id, description,
								player), false);
					}
				}
			}
			save(config, CONFIGURATION_FILE);
		} catch (Exception e) {
			debug(e);
		}
	}
}
