package org.monstercraft.deathexplosion.managers;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.monstercraft.deathexplosion.DeathExplosion;
import org.monstercraft.deathexplosion.util.Constants;
import org.monstercraft.deathexplosion.util.Variables;

/**
 * This class contains all of the plugins settings.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class SettingsManager extends DeathExplosion {
	private DeathExplosion plugin = null;

	/**
	 * Creates an instance of the Settings class.
	 * 
	 * @param plugin
	 *            The parent plugin.
	 */
	public SettingsManager(DeathExplosion plugin) {
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
	public void load() {
		final FileConfiguration config = this.plugin.getConfig();
		final File CONFIGURATION_FILE = new File(Constants.SETTINGS_PATH
				+ File.separator + Constants.SETTINGS_FILE);
		boolean exists = CONFIGURATION_FILE.exists();
		log("Loading settings.yml file");
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
			Variables.announce = config.getBoolean("BOOM.EXPLOSION.ANNOUNCE",
					Variables.announce);
			Variables.colorCode = config.getString("BOOM.EXPLOSION.COLOR",
					Variables.colorCode);
			Variables.cost = config.getInt("BOOM.EXPLOSION.COST",
					Variables.cost);
			Variables.size = config.getInt("BOOM.EXPLOSION.SIZE",
					Variables.size);
			Variables.time = config.getInt("BOOM.EXPLOSION.TIME_TO_REMOVE",
					Variables.time);
			save(config, CONFIGURATION_FILE);
		} catch (Exception e) {
			debug(e);
		}
	}
}
