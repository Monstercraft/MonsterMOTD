package org.monstercraft.monsterticket.plugin.wrappers;

import org.bukkit.entity.Player;

public class PrivateChatter {

	private Player mod;

	private Player noob;

	public PrivateChatter(Player mod, Player noob) {
		this.mod = mod;
		this.noob = noob;
	}

	public Player getMod() {
		return mod;
	}

	public Player getNoob() {
		return noob;
	}

}
