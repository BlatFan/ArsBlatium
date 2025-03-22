package ru.blatfan.ars_blatium;

import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.ars_blatium.config.Config;
import ru.blatfan.ars_blatium.entities.client.BlatiumHoleRenderer;
import ru.blatfan.ars_blatium.init.ABRegistry;
import ru.blatfan.ars_blatium.init.ArsNouveauRegistry;
import ru.blatfan.ars_blatium.network.BlatiumHolePacket;
import ru.blatfan.ars_blatium.network.PurpleLightPacket;
import ru.blatfan.blatapi.fluffy_fur.client.gui.screen.FluffyFurMod;
import ru.blatfan.blatapi.fluffy_fur.client.gui.screen.FluffyFurModsHandler;
import ru.blatfan.blatapi.fluffy_fur.common.network.PacketHandler;
import software.bernie.geckolib.GeckoLib;

import java.awt.*;

@Mod(ArsBlatium.MODID)
public class ArsBlatium {
    public static final String MODID = "ars_blatium";
    
    public static final SpellTier BLATIUM = SpellTier.createTier(prefix("blatium"), 10);
    public static final SpellTier NLIUM = SpellTier.createTier(prefix("nlium"), 20);
    
    public ArsBlatium() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ABRegistry.R.register(bus);
        ArsNouveauRegistry.spells();
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "blatfan/ars_blatium.toml");
        
        bus.addListener(this::setup);
        bus.addListener(ClientModEvents::onClientSetup);
        GeckoLib.initialize();
    }
    
    public static class PacketsHandler extends PacketHandler {
        public static final String PROTOCOL = "10";
        public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(prefix("network"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
        
        public static void init() {
            int id = 0;
            BlatiumHolePacket.register(HANDLER, id++);
            PurpleLightPacket.register(HANDLER, id++);
        }
        
        public static SimpleChannel getHandler() {
            return HANDLER;
        }
        
        public static void sendTo(ServerPlayer playerMP, Object toSend) {
            sendTo(getHandler(), playerMP, toSend);
        }
        
        public static void sendNonLocal(ServerPlayer playerMP, Object toSend) {
            sendNonLocal(getHandler(), playerMP, toSend);
        }
        
        public static void sendToTracking(Level level, BlockPos pos, Object msg) {
            sendToTracking(getHandler(), level, pos, msg);
        }
        
        public static void sendTo(Player entity, Object msg) {
            sendTo(getHandler(), entity, msg);
        }
        
        public static void sendToServer(Object msg) {
            sendToServer(getHandler(), msg);
        }
    }
    
    public static class ClientModEvents {
        public static final FluffyFurMod INS = new FluffyFurMod(MODID, "Ars Blatium", "0.1").setDev("BlatFan")
            .setNameColor(new Color(142, 95, 239)).setVersionColor(new Color(65, 36, 138))
            .setItem(ABRegistry.BLATIUM.getChest().getDefaultInstance()).setDescription(Component.translatable("mod_desc.ars_blatium"))
            .addGithubLink("")
            .addCurseForgeLink("")
            .addModrinthLink("https://modrinth.com/project/ars-blatium")
            .addDiscordLink("https://discord.gg/eHJChH9mqH")
            ;
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ABRegistry.HOLE.get(), BlatiumHoleRenderer::new);
            FluffyFurModsHandler.registerMod(INS);
        }
    }
    
    public static ResourceLocation prefix(String path){
        return new ResourceLocation(MODID, path);
    }
    
    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ArsNouveauRegistry::postInit);
        PacketsHandler.init();
    }
}