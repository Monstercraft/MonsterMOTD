package org.monstercraft.monsterticket.wrappers;

public class HelpTicket {

	private final int id;

	private final String description;

	private final String name;

	public HelpTicket(final int id, final String description, final String name) {
		this.id = id;
		this.description = description;
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public int getID() {
		return id;
	}

	public String getPlayerName() {
		return name;
	}

}
