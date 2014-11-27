package mattparks.mods.MattparksCore;

import java.io.File;

import mattparks.mods.MattparksCore.event.EventCapeRender;
import mattparks.mods.MattparksCore.proxy.CommonProxy;
import mattparks.mods.MattparksCore.util.ConfigManager;
import mattparks.mods.MattparksCore.util.MCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(name = MattCore.NAME, version = MattCore.LOCALMAJVERSION + "." + MattCore.LOCALMINVERSION + "." + MattCore.LOCALBUILDVERSION, useMetadata = true, modid = MattCore.MODID)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MattCore
{
	public static final int LOCALMAJVERSION = 3;
	public static final int LOCALMINVERSION = 0;
	public static final int LOCALBUILDVERSION = 1;
	public static int remoteMajVer;
	public static int remoteMinVer;
	public static int remoteBuildVer;
	
	public static final String NAME = "Mattparks Core";
	public static final String MODID = "MattCore";
	public static final String CHANNEL = "MattCore";
	public static final String CHANNELENTITIES = "MattCoreEntities";

	@SidedProxy(clientSide = "mattparks.mods.MattparksCore.proxy.ClientProxy", serverSide = "mattparks.mods.MattparksCore.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(MattCore.MODID)
	public static MattCore instance;

	@EventHandler
    public void init (FMLInitializationEvent event)
    {
        if (event.getSide() == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(new EventCapeRender());
        }
    }

    @EventHandler
	public void load(FMLInitializationEvent event)
	{
		MattCore.proxy.init(event);
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		MattCore.proxy.postInit(event);
		MattCore.proxy.registerRenderInformation();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		new ConfigManager(new File(event.getModConfigurationDirectory(), "MattparksCore.conf"));
		
		MattCore.proxy.preInit(event);
	}

	public void registerGalacticraftCreature(Class<? extends Entity> var0, String var1, int id, int back, int fore)
	{
		EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
		EntityRegistry.registerModEntity(var0, var1, id, MattCore.instance, 80, 3, true);
	}
	
	public void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
	{
		EntityList.addMapping(var0, var1, id);
		EntityRegistry.registerModEntity(var0, var1, id, this, trackingDistance, updateFreq, sendVel);
	}
	
	@EventHandler
	public void serverInit(FMLServerStartedEvent event)
	{
		MCUtil.checkVersion(Side.SERVER);
	}
}
