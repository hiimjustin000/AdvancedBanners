package com.hiimjustin000.advancedbanners.registries;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.blocks.AdvancedBannerBlock;
import com.hiimjustin000.advancedbanners.blocks.AdvancedWallBannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry
{
    private BlockRegistry() {}

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdvancedBanners.MODID);

    public static final RegistryObject<AdvancedBannerBlock> ADVANCED_BANNER = BLOCKS.register("advanced_banner", AdvancedBannerBlock::new);
    public static final RegistryObject<AdvancedWallBannerBlock> ADVANCED_WALL_BANNER = BLOCKS.register("advanced_wall_banner", AdvancedWallBannerBlock::new);
}
