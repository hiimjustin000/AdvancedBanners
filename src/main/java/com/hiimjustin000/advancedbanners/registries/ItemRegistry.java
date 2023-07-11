package com.hiimjustin000.advancedbanners.registries;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.items.AdvancedBannerItem;
import com.hiimjustin000.advancedbanners.items.AdvancedShieldItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry
{
    private ItemRegistry() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdvancedBanners.MODID);

    public static final RegistryObject<AdvancedBannerItem> ADVANCED_BANNER = ITEMS.register("advanced_banner", AdvancedBannerItem::new);
    public static final RegistryObject<Item> ADVANCED_SHIELD = ITEMS.register("advanced_shield", AdvancedShieldItem::new);
}
