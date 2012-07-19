package org.monstercraft.monsterticket.plugin.managers.handlers;

import java.util.List;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.monstercraft.monsterticket.Ticket;
import org.monstercraft.monsterticket.plugin.command.GameCommand;

/**
 * This handles all of the plugins permissions.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class PermissionsHandler extends Ticket {

	private final Permission perms;

	/**
	 * Creates an instance of the PermissionsHandler class.
	 * 
	 * @param perms
	 *            The Permissions hooks handler.
	 */
	public PermissionsHandler(final Permission perms) {
		this.perms = perms;
	}

	/**
	 * Checks if the player has access to the command.
	 * 
	 * @param player
	 *            The player executing the command.
	 * @param command
	 *            The command being executed.
	 * @return True if the player has permission; otherwise false.
	 */
	public boolean hasCommandPerms(final Player player,
			final GameCommand command) {
		if (Ticket.getHookManager().getPermissionsHook() != null) {
			if (perms != null) {
				return perms.has(player, "monsterticket.mod")
						|| perms.has(player, command.getPermission())
						|| player.isOp();
			} else {
				return player.isOp();
			}
		} else {
			return player.isOp();
		}
	}

	public boolean hasModPerm(final Player player) {
		if (Ticket.getHookManager().getPermissionsHook() != null) {
			if (perms != null) {
				return perms.has(player, "monsterticket.mod") || player.isOp();
			} else {
				return player.isOp();
			}
		} else {
			return player.isOp();
		}
	}

	/**
	 * Checks if a player is in a list.
	 * 
	 * @param player
	 *            The player to check.
	 * @param list
	 *            The list to check.
	 * @return True if the list contains their name; otherwise false.
	 */
	public boolean anyGroupsInList(final Player player, final List<String> list) {
		String[] groups = getGroups(player);
		for (int i = 0; i < groups.length; i++) {
			if (list.contains(groups[i]))
				return true;
		}
		return false;
	}

	/**
	 * Sets the players groups.
	 * 
	 * @param player
	 *            The player to set groups for.
	 * @return The groups the player is in.
	 */
	public String[] getGroups(final Player player) {
		if (perms != null) {
			try {
				String world = player.getWorld().getName();
				String name = player.getName();
				return perms.getPlayerGroups(world, name);
			} catch (Exception e) {
				Ticket.debug(e);
			}
		}
		return new String[0];
	}
}
