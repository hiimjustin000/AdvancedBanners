package com.hiimjustin000.advancedbanners.atlases;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

public class AdvancedShieldPatternsHolder extends TextureAtlasHolder
{
    public static final ResourceLocation SHEET = new ResourceLocation(AdvancedBanners.MODID, "textures/atlas/advanced_shield_patterns.png");
    public static final ResourceLocation LOCATION = new ResourceLocation(AdvancedBanners.MODID, "advanced_shield_patterns");

    public AdvancedShieldPatternsHolder(TextureManager textureManager)
    {
        super(textureManager, SHEET, LOCATION);
    }
}
