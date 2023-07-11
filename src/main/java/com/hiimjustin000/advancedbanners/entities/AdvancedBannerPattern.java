package com.hiimjustin000.advancedbanners.entities;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;

public class AdvancedBannerPattern
{
    public static ResourceLocation location(ResourceKey<BannerPattern> key, boolean banner)
    {
        return new ResourceLocation(AdvancedBanners.MODID, "entity/advanced_" + (banner ? "banner" : "shield") + "/" + key.location().getPath());
    }
}
