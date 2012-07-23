package org.monstercraft.motd.plugin.managers.handlers;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.monstercraft.motd.MonsterMOTD;
import org.monstercraft.motd.plugin.command.GameCommand;

/**
 * This handles all of the plugins permissions.
 * 
 * @author fletch_to_99 <fletchto99@hotmail.com>
 * 
 */
public class PermissionsHandler {

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
		if (MonsterMOTD.getHookManager().getPermissionsHook() != null) {
			if (perms != null) {
				return perms.has(player, "monstermotd.admin")
						|| perms.has(player, command.getPermission())
						|| player.isOp();
			} else {
				return player.isOp();
			}
		} else {
			return player.isOp();
		}
	}
}
