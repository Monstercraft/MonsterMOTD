package org.monstercraft.monsterticket.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.monstercraft.monsterticket.wrappers.HelpTicket;
import org.monstercraft.monsterticket.wrappers.PrivateChatter;

public class Variables {

	public static LinkedHashMap<HelpTicket, Boolean> tickets = new LinkedHashMap<HelpTicket, Boolean>();
	public static int ticketid = 1;
	public static ArrayList<PrivateChatter> priv = new ArrayList<PrivateChatter>();

}
