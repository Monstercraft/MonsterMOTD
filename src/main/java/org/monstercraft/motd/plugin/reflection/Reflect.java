package org.monstercraft.motd.plugin.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.server.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

public class Reflect {

	public static void setMotd(final Object motd) throws Exception {
		// Fetch the motd field
		final Field field = MinecraftServer.class.getDeclaredField("motd");
		// Fetch the instance of the server
		final Field server = CraftServer.class.getDeclaredField("console");
		// Fetch the modifiers field
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		// set all fields accessable
		modifiersField.setAccessible(true);
		field.setAccessible(true);
		server.setAccessible(true);

		// remove the final modifier
		modifiersField.setInt(server, server.getModifiers() & ~Modifier.FINAL);
		// fetch the instance of the server
		MinecraftServer instance = (MinecraftServer) server.get(Bukkit
				.getServer());
		// set the new field to the instance of the MinecraftServer
		field.set(instance, motd);
	}

	public static String getMotd() throws Exception {
		// Fetch the motd field
		final Field field = MinecraftServer.class.getDeclaredField("motd");
		// Fetch the instance of the server
		final Field server = CraftServer.class.getDeclaredField("console");
		// set all fields accessable
		field.setAccessible(true);
		server.setAccessible(true);
		// fetch the instance of the server
		MinecraftServer instance = (MinecraftServer) server.get(Bukkit
				.getServer());
		// retrieve the motd
		return (String) field.get(instance);
	}
}
