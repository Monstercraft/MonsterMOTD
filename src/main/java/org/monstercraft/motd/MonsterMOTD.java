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
 * This class represents the main plugin. All actions related to the plugin are forwarded by this class
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class MonsterMOTD extends JavaPlugin implements Listener {

    private class Update implements Runnable {
        private final String version;

        public Update(final String version) {
            this.version = version;
        }

        @Override
        public void run() {
            try {
                final URL url = new URL(
                        "http://dev.bukkit.org/server-mods/monstermotd/files.rss");
                final Document doc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(url.openConnection().getInputStream());
                doc.getDocumentElement().normalize();
                final NodeList nodes = doc.getElementsByTagName("item");
                final Node firstNode = nodes.item(0);
                if (firstNode.getNodeType() == 1) {
                    final Element firstElement = (Element) firstNode;
                    final NodeList firstElementTagName = firstElement
                            .getElementsByTagName("title");
                    final Element firstNameElement = (Element) firstElementTagName
                            .item(0);
                    final NodeList firstNodes = firstNameElement
                            .getChildNodes();
                    final String cur = firstNodes.item(0).getNodeValue();
                    if (cur.contains(version)) {
                        log("You are using the latest version of MonsterMOTD");
                    } else {
                        log(cur + " is out! You are running " + version);
                        log("Update MonsterMOTD at: http://dev.bukkit.org/server-mods/monstermotd");
                    }
                }
            } catch (final Exception e) {
                debug(e);
            }

        }
    }

    private final String DENIED = "You do not have permission for this command";
    private String currentMOTD;

    private String defaultMOTD;

    /**
     * Logs debugging messages to the console.
     * 
     * @param error
     *            The message to print.
     */
    public void debug(final Exception error) {
        getLogger().log(Level.SEVERE, "Critical error detected!", error);
    }

    /**
     * Logs a message to the console.
     * 
     * @param msg
     *            The message to print.
     */
    public void log(final String msg) {
        getLogger().info(msg);
    }

    /**
     * Handles commands.
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
            final String label, final String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("clear")) {
            if (!sender.hasPermission("monstermotd.clear")) {
                sender.sendMessage(DENIED);
                return true;
            }
            setMOTD(null);
            sender.sendMessage(ChatColor.GREEN
                    + "MOTD successfully set back to the default: "
                    + ChatColor.RESET + ColorUtils.format(defaultMOTD));
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("monstermotd.set")) {
                sender.sendMessage(DENIED);
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED
                        + "You must put a MOTD to set!");
                return true;
            }
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]);
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1);
            final String motd = sb.toString();
            setMOTD(motd);
            sender.sendMessage(ChatColor.GREEN + "MOTD successfully set to: "
                    + ChatColor.RESET + ColorUtils.format(motd));
            return true;
        }
        if (sender.hasPermission("monstermotd.view")) {
            sender.sendMessage(ChatColor.GREEN + "Todays MOTD: "
                    + ChatColor.RESET + ColorUtils.format(currentMOTD));
            return true;
        }
        sender.sendMessage(DENIED);
        return true;
    }

    @Override
    public void onEnable() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        currentMOTD = getConfig().getString("CURRENT-MOTD");
        defaultMOTD = getConfig().getString("DEFAULT-MOTD");
        if (defaultMOTD == null) {
            defaultMOTD = "A Minecraft server";
        }
        if (currentMOTD == null) {
            currentMOTD = "A Minecraft server";
        }
        try {
            new Metrics(this).start();
        } catch (final IOException e) {
            debug(e);
        }
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().scheduleAsyncDelayedTask(this,
                new Update(getDescription().getVersion()));
    }

    @EventHandler
    public void onPing(final ServerListPingEvent event) {
        event.setMotd(ColorUtils.format(currentMOTD));
    }

    private void setMOTD(final String motd) {
        currentMOTD = motd == null ? defaultMOTD : motd;
        getConfig().set("CURRENT-MOTD", currentMOTD);
        saveConfig();
    }

    public enum ColorUtils {
        BLACK("&0", ChatColor.BLACK.toString()),
        DARK_BLUE("&1", ChatColor.DARK_BLUE.toString()),
        DARK_GREEN("&2", ChatColor.DARK_GREEN.toString()),
        DARK_AQUA("&3", ChatColor.DARK_AQUA.toString()),
        DARK_RED("&4", ChatColor.DARK_RED.toString()),
        DARK_PURPLE("&5", ChatColor.DARK_PURPLE.toString()),
        GOLD("&6", ChatColor.GOLD.toString()),
        GRAY("&7", ChatColor.GRAY.toString()),
        DARK_GRAY("&8", ChatColor.DARK_GRAY.toString()),
        BLUE("&9", ChatColor.BLUE.toString()),
        GREEN("&a", ChatColor.GREEN.toString()),
        AQUA("&b", ChatColor.AQUA.toString()),
        RED("&c", ChatColor.RED.toString()),
        LIGHT_PURPLE("&d", ChatColor.LIGHT_PURPLE.toString()),
        YELLOW("&e", ChatColor.YELLOW.toString()),
        WHITE("&f", ChatColor.WHITE.toString()),
        MAGIC("&k", ChatColor.MAGIC.toString()),
        BOLD("&l", ChatColor.BOLD.toString()),
        STRIKETHROUGH("&m", ChatColor.STRIKETHROUGH.toString()),
        UNDERLINE("&n", ChatColor.UNDERLINE.toString()),
        ITALIC("&o", ChatColor.ITALIC.toString()),
        RESET("&r", ChatColor.RESET.toString());

        /**
         * Colors in minecraft and IRC.
         * 
         * @param IRCColor
         *            The color code in IRC.
         * @param MinecraftColor
         *            The color code in Minecraft.
         */
        ColorUtils(final String input, final String MinecraftColor) {
            this.input = input;
            this.MinecraftColor = MinecraftColor;
        }

        /**
         * Fetches the color in minecraft.
         * 
         * @return The minecraft color code.
         */
        public String getMinecraftColor() {
            return MinecraftColor;
        }

        /**
         * Fetches the color in IRC.
         * 
         * @return The IRC color code.
         */
        public String getInput() {
            return input;
        }

        /**
         * Creates a formatted message with proper colors.
         * 
         * @param message
         *            The inital message to format.
         * @return The formatted message.
         */
        public static String format(final String message) {
            String msg = message;
            for (final ColorUtils c : ColorUtils.values()) {
                msg = msg.replace(c.getInput(), c.getMinecraftColor());
            }
            return msg;
        }

        private final String input;

        private final String MinecraftColor;

    }

}
