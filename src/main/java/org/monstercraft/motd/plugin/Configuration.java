package org.monstercraft.motd.plugin;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.plugin.Plugin;
import org.monstercraft.motd.MonsterMOTD;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class holds all of the configuration data used within the plugin.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class Configuration {

	public static String getCurrentVerison(final Plugin plugin) {
		return plugin.getDescription().getVersion();
	}

	/**
	 * Checks to see if the plugin is the latest version. Thanks to vault for
	 * letting me use their code.
	 * 
	 * @param currentVersion
	 *            The version that is currently running.
	 * @return The latest version
	 */
	public static String checkForUpdates(final Plugin plugin, final String site) {
		String currentVersion = getCurrentVerison(plugin);
		try {
			URL url = new URL(site);
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(url.openConnection().getInputStream());
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("item");
			Node firstNode = nodes.item(0);
			if (firstNode.getNodeType() == 1) {
				Element firstElement = (Element) firstNode;
				NodeList firstElementTagName = firstElement
						.getElementsByTagName("title");
				Element firstNameElement = (Element) firstElementTagName
						.item(0);
				NodeList firstNodes = firstNameElement.getChildNodes();
				return firstNodes.item(0).getNodeValue();
			}
		} catch (Exception e) {
			MonsterMOTD.debug(e);
		}
		return currentVersion;
	}

	public static class URLS {
		public static String UPDATE_URL = "http://dev.bukkit.org/server-mods/monstermotd/files.rss";
	}
	
	public static class Variables {
		public static String defaultMOTD = "A Minecraft server";
	}
}
