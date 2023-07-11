package com.hiimjustin000.advancedbanners.registries;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabRegistry
{
    private CreativeModeTabRegistry() {}

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AdvancedBanners.MODID);

    public static final RegistryObject<CreativeModeTab> ADVANCED_BANNERS = CREATIVE_MODE_TABS.register("advanced_banners", () ->
            CreativeModeTab.builder().icon(ItemRegistry.ADVANCED_BANNER.get()::getDefaultInstance)
                    .title(Component.translatable("itemGroup.advanced_banners")).build());
}
