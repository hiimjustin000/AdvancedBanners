package com.hiimjustin000.advancedbanners;

import com.hiimjustin000.advancedbanners.listeners.BannerPresetReloadListener;
import com.hiimjustin000.advancedbanners.packets.BannerPresetReloadPacket;
import com.hiimjustin000.advancedbanners.registries.*;
import com.hiimjustin000.advancedbanners.renderers.AdvancedBannerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(AdvancedBanners.MODID)
public class AdvancedBanners
{
    public static final String MODID = "advancedbanners";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> "1", "1"::equals, "1"::equals);

    public AdvancedBanners()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);

        BlockRegistry.BLOCKS.register(bus);
        ItemRegistry.ITEMS.register(bus);
        BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register(bus);
        BannerPatternRegistry.BANNER_PATTERNS.register(bus);
        RecipeSerializerRegistry.RECIPES.register(bus);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        CHANNEL.registerMessage(0, BannerPresetReloadPacket.class, BannerPresetReloadPacket::encode,
                BannerPresetReloadPacket::new, BannerPresetReloadPacket::handle);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabRegistry.ADVANCED_BANNERS.getKey())
        {
            event.accept(ItemRegistry.ADVANCED_BANNER.get().getDefaultInstance());
            event.accept(ItemRegistry.ADVANCED_SHIELD.get().getDefaultInstance());
            for (ItemStack banner : BannerPresetReloadListener.PRESETS)
                event.accept(banner);
        }
    }

    @SubscribeEvent
    public void addReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(BannerPresetReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public void registerReloadListeners(RegisterClientReloadListenersEvent event)
    {
        event.registerReloadListener(BannerPresetReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public void syncDatapacks(OnDatapackSyncEvent event)
    {
        ServerPlayer player = event.getPlayer();
        if (player != null && !(player instanceof FakePlayer))
            CHANNEL.sendTo(new BannerPresetReloadPacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> ItemProperties.register(ItemRegistry.ADVANCED_SHIELD.get(), new ResourceLocation("blocking"),
                    (stack, level, entity, id) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1F : 0F));
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
        {
            event.registerBlockEntityRenderer(BlockEntityTypeRegistry.ADVANCED_BANNER.get(), AdvancedBannerRenderer::new);
        }

        @SubscribeEvent
        public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event)
        {
            event.registerReloadListener(IClientItemExtensions.of(ItemRegistry.ADVANCED_SHIELD.get()).getCustomRenderer());
        }
    }
}
