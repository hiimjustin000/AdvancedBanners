package com.hiimjustin000.advancedbanners.registries;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.entities.AdvancedBannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityTypeRegistry
{
    private BlockEntityTypeRegistry() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AdvancedBanners.MODID);

    public static final RegistryObject<BlockEntityType<AdvancedBannerBlockEntity>> ADVANCED_BANNER =
            BLOCK_ENTITY_TYPES.register("advanced_banner", () -> BlockEntityType.Builder
                    .of(AdvancedBannerBlockEntity::new, BlockRegistry.ADVANCED_BANNER.get(), BlockRegistry.ADVANCED_WALL_BANNER.get()).build(null));
}
