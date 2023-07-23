package com.hiimjustin000.advancedbanners.registries;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.items.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry
{
    private ItemRegistry() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdvancedBanners.MODID);

    public static final RegistryObject<AdvancedBannerItem> ADVANCED_BANNER = ITEMS.register("advanced_banner", AdvancedBannerItem::new);
    public static final RegistryObject<AdvancedShieldItem> ADVANCED_SHIELD = ITEMS.register("advanced_shield", AdvancedShieldItem::new);
    public static final RegistryObject<AdvancedDyeItem> ADVANCED_DYE = ITEMS.register("advanced_dye", AdvancedDyeItem::new);
    public static final RegistryObject<DyeMachineItem> DYE_MACHINE = ITEMS.register("dye_machine", DyeMachineItem::new);
    public static final RegistryObject<AdvancedLoomItem> ADVANCED_LOOM = ITEMS.register("advanced_loom", AdvancedLoomItem::new);
}
