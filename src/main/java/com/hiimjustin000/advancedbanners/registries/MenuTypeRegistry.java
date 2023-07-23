package com.hiimjustin000.advancedbanners.registries;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.menus.AdvancedLoomMenu;
import com.hiimjustin000.advancedbanners.menus.DyeMachineMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeRegistry
{
    private MenuTypeRegistry() {}

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AdvancedBanners.MODID);

    public static final RegistryObject<MenuType<DyeMachineMenu>> DYE_MACHINE_MENU_TYPE = MENU_TYPES.register("dye_machine", () ->
            IForgeMenuType.create((id, inventory, buffer) -> new DyeMachineMenu(id, inventory)));
    public static final RegistryObject<MenuType<AdvancedLoomMenu>> ADVANCED_LOOM_MENU_TYPE = MENU_TYPES.register("advanced_loom", () ->
            IForgeMenuType.create((id, inventory, buffer) -> new AdvancedLoomMenu(id, inventory)));
}
