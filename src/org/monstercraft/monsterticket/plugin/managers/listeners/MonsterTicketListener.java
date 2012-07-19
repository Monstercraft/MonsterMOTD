package org.monstercraft.monsterticket.plugin.managers.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.monstercraft.monsterticket.plugin.Configuration.Variables;
import org.monstercraft.monsterticket.plugin.command.commands.Close;
import org.monstercraft.monsterticket.plugin.wrappers.HelpTicket;
import org.monstercraft.monsterticket.plugin.wrappers.PrivateChatter;

/**
 * This class listens for chat ingame to pass to the IRC.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class MonsterTicketListener implements Listener {

	/**
	 * 
	 * @param plugin
	 *            The parent plugin for the listener.
	 */
	public MonsterTicketListener() {
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(PlayerChatEvent event) {
		for (PrivateChatter pc : Variables.priv) {
			if (pc.getNoob().equals(event.getPlayer())
					|| pc.getMod().equals(event.getPlayer())) {
				pc.getNoob().sendMessage(
						ChatColor.RED + "[Support] "
								+ event.getPlayer().getDisplayName() + ": "
								+ ChatColor.WHITE + event.getMessage());
				pc.getMod().sendMessage(
						ChatColor.RED + "[Support] "
								+ event.getPlayer().getDisplayName() + ": "
								+ ChatColor.WHITE + event.getMessage());
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(
				ChatColor.RED + "To open a support request type:"
						+ ChatColor.GREEN + "/request (issue)");
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		for (PrivateChatter pc : Variables.priv) {
			if (pc.getMod().equals(event.getPlayer())
					|| pc.getNoob().equals(event.getPlayer())) {
				Variables.priv.remove(pc);
				int id = -1;
				for (HelpTicket t : Variables.tickets.keySet()) {
					if (t.getPlayerName().equalsIgnoreCase(
							pc.getNoob().getName())) {
						id = t.getID();
					}
				}
				if (id != -1) {
					Close.close(pc.getMod(), String.valueOf(id));
				}
				return;
			}
		}
	}
}