package org.monstercraft.monsterticket.plugin.wrappers;

import org.bukkit.entity.Player;

public class PrivateChatter {

	private Player mod;

	private Player noob;
	
	private int id;

	public PrivateChatter(Player mod, Player noob, int id) {
		this.mod = mod;
		this.noob = noob;
		this.id = id;
	}

	public Player getMod() {
		return mod;
	}

	public Player getNoob() {
		return noob;
	}
	
	public int getID() {
		return id;
	}

}
