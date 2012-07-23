package org.monstercraft.motd;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.monstercraft.motd.plugin.util.Metrics;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * This class represents the main plugin. All actions related to the plugin are
 * forwarded by this class
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class MonsterMOTD extends JavaPlugin implements Listener {

    private final String DENIED = "You do not have permission for this command";
    
    private String currentMOTD;
    private String defaultMOTD;

	public void onEnable() {
	    if(!new File(this.getDataFolder(),"config.yml").exists()){
	        this.saveDefaultConfig();
	    }
		this.currentMOTD = this.getConfig().getString("CURRENT-MOTD");
		this.defaultMOTD = this.getConfig().getString("DEFAULT-MOTD");
		if(this.defaultMOTD == null) {
		    this.defaultMOTD = "A Minecraft server";
		}
		if(this.currentMOTD == null) {
		    this.currentMOTD = "A Minecraft server";
		}
		try {
            new Metrics(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getScheduler().scheduleAsyncDelayedTask(this, new Update(this.getDescription().getVersion()));
	}

	/**
	 * Logs a message to the console.
	 * 
	 * @param msg
	 *            The message to print.
	 */
	public void log(String msg) {
		this.getLogger().info(msg);
	}

	/**
	 * Logs debugging messages to the console.
	 * 
	 * @param error
	 *            The message to print.
	 */
	public void debug(final Exception error) {
	    this.getLogger().log(Level.SEVERE, "Critical error detected!", error);
	}
	
	private void setMOTD(String motd) {
	    this.currentMOTD = motd == null ? this.defaultMOTD : motd;
	    this.getConfig().set("CURRENT-MOTD", this.currentMOTD);
	    this.saveConfig();
	}

	/**
	 * Handles commands.
	 */
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
	    if(args.length > 1 && args[0].equalsIgnoreCase("clear")){
	        if(!sender.hasPermission("monstermotd.view")){
	            sender.sendMessage(DENIED);
	            return true;
	        }
	        this.setMOTD(null);
	        sender.sendMessage(ChatColor.GREEN
	                + "MOTD successfully set back to the default: " + ChatColor.RESET + this.defaultMOTD);
	        return true;
	    }
	    if(args.length>1 && args[0].equalsIgnoreCase("set")){
	        if(!sender.hasPermission("monstermotd.set")){
	            sender.sendMessage(DENIED);
	            return true;
	        }
	        if (args.length < 2) {
	            sender.sendMessage(ChatColor.RED + "You must put a MOTD to set!");
	            return true;
	        }
	        StringBuilder sb = new StringBuilder();
	        for (int i = 1; i < args.length; i++) {
	            sb.append(args[i]);
	            sb.append(" ");
	        }
	        sb.setLength(sb.length()-1);
	        String motd = sb.toString();
	        this.setMOTD(motd);
	        sender.sendMessage(ChatColor.GREEN + "MOTD successfully set to: "
	                + ChatColor.RESET + motd);
	        return true;
	    }
	    // Else, display motd.
	    if(sender.hasPermission("monstermotd.view")){
	            sender.sendMessage(ChatColor.GREEN + "Todays MOTD: " + ChatColor.RESET
                    + this.currentMOTD);
	    }
	    sender.sendMessage(DENIED);
        return true;
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
	    event.setMotd(this.currentMOTD);
	}

	private class Update implements Runnable {
	    private final String version;

        public Update(String version) {
	        this.version = version;
	    }
        
        public void run() {
                try {
                    URL url = new URL("http://dev.bukkit.org/server-mods/monstermotd/files.rss");
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
                        final String cur = firstNodes.item(0).getNodeValue();
                        if(cur.contains(version)){
                            log("You are using the latest version of MonsterMOTD");
                        } else {
                            log(cur + " is out! You are running " +version);
                            log("Update MonsterMOTD at: http://dev.bukkit.org/server-mods/monstermotd");
                        }
                    }
                } catch (Exception e) {
                    debug(e);
                }
                
        }
	}
	
}
