package com.hiimjustin000.advancedbanners.entities;

import com.google.common.collect.Lists;
import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

public class AdvancedBannerPattern
{
    public static ResourceLocation location(ResourceKey<BannerPattern> key, boolean banner)
    {
        return new ResourceLocation(AdvancedBanners.MODID, "entity/advanced_" + (banner ? "banner" : "shield") + "/" + key.location().getPath());
    }

    public static class Builder
    {
        private final List<Pair<Holder<BannerPattern>, Integer>> patterns = Lists.newArrayList();

        public AdvancedBannerPattern.Builder addPattern(Holder<BannerPattern> pattern, int color)
        {
            return addPattern(Pair.of(pattern, color));
        }

        public AdvancedBannerPattern.Builder addPattern(Pair<Holder<BannerPattern>, Integer> color)
        {
            patterns.add(color);
            return this;
        }

        public ListTag toListTag()
        {
            ListTag patternsTag = new ListTag();

            for (Pair<Holder<BannerPattern>, Integer> pair : patterns)
            {
                CompoundTag patternTag = new CompoundTag();
                patternTag.putString("Pattern", (pair.getFirst().value()).hashname);
                patternTag.putInt("Color", pair.getSecond());
                patternsTag.add(patternTag);
            }

            return patternsTag;
        }
    }
}
