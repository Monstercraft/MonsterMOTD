package org.monstercraft.motd.plugin.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.monstercraft.motd.MonsterMOTD;
import org.monstercraft.motd.plugin.Configuration.Variables;
import org.monstercraft.motd.plugin.reflection.Reflect;

/**
 * This class contains all of the plugins settings.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class SettingsManager {

	private MonsterMOTD plugin = null;

	/**
	 * Creates an instance of the Settings class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public SettingsManager(final MonsterMOTD plugin) {
		this.plugin = plugin;
		load();
	}

	/**
	 * Saves the config file.
	 * 
	 * @param config
	 *            The config to save.
	 * @param file
	 *            The file to save it to.
	 */
	public void save() {
		try {
			final FileConfiguration config = this.plugin.getConfig();
			final File CONFIGURATION_FILE = new File(plugin.getDataFolder(),
					"Config.yml");
			try {
				config.set("MOTD", Reflect.getMotd());
			} catch (Exception e) {
				config.set("MOTD", Variables.defaultMOTD);
			}
			config.save(CONFIGURATION_FILE);
		} catch (IOException e) {
			MonsterMOTD.debug(e);
		}
	}

	/**
	 * This method loads the plugins configuration file.
	 */
	public void load() {
		final FileConfiguration config = this.plugin.getConfig();
		final File CONFIGURATION_FILE = new File(plugin.getDataFolder(),
				"Settings.yml");
		boolean exists = CONFIGURATION_FILE.exists();
		MonsterMOTD.log("Loading Config.yml file");
		if (exists) {
			try {
				MonsterMOTD.log("Loading settings!");
				config.options().copyDefaults(true);
				config.load(CONFIGURATION_FILE);
			} catch (Exception e) {
				MonsterMOTD.debug(e);
			}
		} else {
			MonsterMOTD.log("Loading default settings!");
			config.options().copyDefaults(true);
		}
		try {
			String motd = config.getString("MOTD", Variables.defaultMOTD);
			if (motd.length() > 1 && !Reflect.getMotd().equalsIgnoreCase(motd)) {
				Reflect.setMotd(motd);
			}
			save();
		} catch (Exception e) {
			MonsterMOTD.debug(e);
		}
	}

}
