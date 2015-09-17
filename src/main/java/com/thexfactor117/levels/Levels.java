package com.thexfactor117.levels;

import com.thexfactor117.levels.handlers.ConfigHandler;
import com.thexfactor117.levels.handlers.VersionChecker;
import com.thexfactor117.levels.helpers.LogHelper;
import com.thexfactor117.levels.init.ModEvents;
import com.thexfactor117.levels.network.PacketRarities;
import com.thexfactor117.levels.proxies.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * To-Do List
 * 
 * View the planned_features.txt file
 */

/**
 * 
 * @author TheXFactor117
 *
 */
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Levels 
{	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
	public static CommonProxy proxy;
	@Instance(Reference.MODID)
	public static Levels instance;
	public static VersionChecker versionChecker;
	public static SimpleNetworkWrapper network;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LogHelper.info("Beginning initialization phases...");
		
		ConfigHandler.registerConfig(event.getSuggestedConfigurationFile());
		ModEvents.registerEvents();
		network = NetworkRegistry.INSTANCE.newSimpleChannel("RarityChannel");
		network.registerMessage(PacketRarities.HandlerRarities.class, PacketRarities.class, 0, Side.CLIENT);
		
		LogHelper.info("Levels has finished initializing.");
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		LogHelper.info("Checking if latest version...");
		
		proxy.postInit();
		
		LogHelper.info("VersionChecker complete.");
	}
}
