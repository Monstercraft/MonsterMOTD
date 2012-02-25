package org.monstercraft.deathexplosion.hooks;

import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.Packet41MobEffect;
import net.minecraft.server.Packet42RemoveMobEffect;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class StatusHook {

	public void attachStatusBar(Player player) {
		final CraftPlayer cplr = ((CraftPlayer) player);
		cplr.getHandle().netServerHandler
				.sendPacket(new Packet41MobEffect(cplr.getEntityId(),
						new MobEffect(MobEffectList.HARM.getId(), 0, 0)));
	}

	public void removeStatusBar(Player player) {
		final CraftPlayer cplr = ((CraftPlayer) player);
		cplr.getHandle().netServerHandler
				.sendPacket(new Packet42RemoveMobEffect(cplr.getEntityId(),
						new MobEffect(MobEffectList.HARM.getId(), 0, 0)));
	}

}