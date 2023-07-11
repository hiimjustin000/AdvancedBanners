package com.hiimjustin000.advancedbanners.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hiimjustin000.advancedbanners.entities.AdvancedBannerBlockEntity;
import com.hiimjustin000.advancedbanners.registries.BlockEntityTypeRegistry;
import com.hiimjustin000.advancedbanners.registries.ItemRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;

import java.util.*;

public class BannerPresetReloadListener extends SimpleJsonResourceReloadListener
{
    public static final BannerPresetReloadListener INSTANCE = new BannerPresetReloadListener(new Gson());
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final List<ItemStack> PRESETS = new ArrayList<>();
    public static final BlockEntityType<AdvancedBannerBlockEntity> ADVANCED_BANNER_BLOCK_ENTITY_TYPE = BlockEntityTypeRegistry.ADVANCED_BANNER.get();
    public static final BlockItem ADVANCED_BANNER_ITEM = ItemRegistry.ADVANCED_BANNER.get();

    private static Gson GSON;

    public BannerPresetReloadListener(Gson gson)
    {
        super(gson, "banner_presets");
        GSON = gson;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager manager, ProfilerFiller profiler)
    {
        addPresets(object);
    }

    public static void addPresets(Map<ResourceLocation, JsonElement> object)
    {
        PRESETS.clear();
        SortedMap<ResourceLocation, JsonElement> sortedMap = new TreeMap<>(object);
        for (Map.Entry<ResourceLocation, JsonElement> entry : sortedMap.entrySet())
        {
            try
            {
                ItemStack stack = new ItemStack(ADVANCED_BANNER_ITEM);
                BlockItem.setBlockEntityData(stack, ADVANCED_BANNER_BLOCK_ENTITY_TYPE, TagParser.parseTag(GSON.toJson(entry.getValue())));
                PRESETS.add(stack);
                ResourceLocation id = entry.getKey();
                LOGGER.info("Added banner preset: {}:{}", id.getNamespace(), id.getPath());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
