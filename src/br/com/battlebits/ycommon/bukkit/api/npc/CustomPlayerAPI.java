package br.com.battlebits.ycommon.bukkit.api.npc;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.WorldServer;

public class CustomPlayerAPI {

	public static final Cache<GameProfile, Property> Textures = CacheBuilder.newBuilder()
			.expireAfterWrite(30L, TimeUnit.MINUTES).build(new CacheLoader<GameProfile, Property>() {
				@Override
				public Property load(GameProfile key) throws Exception {
					return loadTextures(key);
				}
			});

	private static final Property loadTextures(GameProfile profile) {
		MinecraftServer.getServer().av().fillProfileProperties(profile, true);
		return Iterables.getFirst(profile.getProperties().get("textures"), null);
	}
	
	private static MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
	
	public static MinecraftServer getNmsServer() {
		return nmsServer;
	}
	
	public static WorldServer getNmsWorld(World world){
		return ((CraftWorld) world).getHandle();
	}
	
	public static void setHeadYaw(Entity en, float yaw) {
		if (!(en instanceof EntityLiving))
			return;
		EntityLiving handle = (EntityLiving) en;
		while (yaw < -180.0F) {
			yaw += 360.0F;
		}

		while (yaw >= 180.0F) {
			yaw -= 360.0F;
		}
		handle.aO = yaw;
		if (!(handle instanceof EntityHuman))
			handle.aM = yaw;
		handle.aP = yaw;
	}

}
