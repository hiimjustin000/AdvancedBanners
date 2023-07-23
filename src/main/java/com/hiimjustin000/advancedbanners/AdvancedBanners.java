package com.hiimjustin000.advancedbanners;

import com.hiimjustin000.advancedbanners.colors.AdvancedDyeItemColor;
import com.hiimjustin000.advancedbanners.listeners.BannerPresetReloadListener;
import com.hiimjustin000.advancedbanners.listeners.DyePresetReloadListener;
import com.hiimjustin000.advancedbanners.packets.BannerPresetReloadPacket;
import com.hiimjustin000.advancedbanners.packets.ChangeItemColorPacket;
import com.hiimjustin000.advancedbanners.packets.DyePresetReloadPacket;
import com.hiimjustin000.advancedbanners.registries.*;
import com.hiimjustin000.advancedbanners.renderers.AdvancedBannerRenderer;
import com.hiimjustin000.advancedbanners.screens.AdvancedLoomScreen;
import com.hiimjustin000.advancedbanners.screens.DyeMachineScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
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
        bus.addListener(this::registerColorHandlers);

        BlockRegistry.BLOCKS.register(bus);
        ItemRegistry.ITEMS.register(bus);
        BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register(bus);
        MenuTypeRegistry.MENU_TYPES.register(bus);
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
        CHANNEL.registerMessage(1, ChangeItemColorPacket.class, ChangeItemColorPacket::encode,
                ChangeItemColorPacket::new, ChangeItemColorPacket::handle);
        CHANNEL.registerMessage(2, DyePresetReloadPacket.class, DyePresetReloadPacket::encode,
                DyePresetReloadPacket::new, DyePresetReloadPacket::handle);
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
        else if (event.getTabKey() == CreativeModeTabRegistry.ADVANCED_DYE.getKey())
        {
            event.accept(ItemRegistry.ADVANCED_DYE.get().getDefaultInstance());
            event.accept(ItemRegistry.DYE_MACHINE.get().getDefaultInstance());
            event.accept(ItemRegistry.ADVANCED_LOOM.get().getDefaultInstance());
            for (ItemStack dye : DyePresetReloadListener.PRESETS)
                event.accept(dye);
        }
    }

    private void registerColorHandlers(RegisterColorHandlersEvent.Item event)
    {
        event.register(new AdvancedDyeItemColor(), ItemRegistry.ADVANCED_DYE.get());
    }

    @SubscribeEvent
    public void addReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(BannerPresetReloadListener.INSTANCE);
        event.addListener(DyePresetReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public void syncDatapacks(OnDatapackSyncEvent event)
    {
        ServerPlayer player = event.getPlayer();
        if (player != null && !(player instanceof FakePlayer))
        {
            CHANNEL.sendTo(new BannerPresetReloadPacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            CHANNEL.sendTo(new DyePresetReloadPacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> ItemProperties.register(ItemRegistry.ADVANCED_SHIELD.get(), new ResourceLocation("blocking"),
                    (stack, level, entity, id) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1F : 0F));
            event.enqueueWork(() -> MenuScreens.register(MenuTypeRegistry.DYE_MACHINE_MENU_TYPE.get(), DyeMachineScreen::new));
            event.enqueueWork(() -> MenuScreens.register(MenuTypeRegistry.ADVANCED_LOOM_MENU_TYPE.get(), AdvancedLoomScreen::new));
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
