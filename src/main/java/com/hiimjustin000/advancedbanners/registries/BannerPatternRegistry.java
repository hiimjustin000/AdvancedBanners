package com.hiimjustin000.advancedbanners.registries;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BannerPatternRegistry
{
    private BannerPatternRegistry() {}

    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, AdvancedBanners.MODID);

    public static final RegistryObject<BannerPattern> QUAD_TOP = BANNER_PATTERNS.register("quad_top", () -> new BannerPattern("4t"));
    public static final RegistryObject<BannerPattern> QUAD_BOTTOM = BANNER_PATTERNS.register("quad_bottom", () -> new BannerPattern("4b"));
    public static final RegistryObject<BannerPattern> QUAD_LEFT = BANNER_PATTERNS.register("quad_left", () -> new BannerPattern("4l"));
    public static final RegistryObject<BannerPattern> QUAD_RIGHT = BANNER_PATTERNS.register("quad_right", () -> new BannerPattern("4r"));
    public static final RegistryObject<BannerPattern> QUIN_TOP = BANNER_PATTERNS.register("quin_top", () -> new BannerPattern("5t"));
    public static final RegistryObject<BannerPattern> QUIN_BOTTOM = BANNER_PATTERNS.register("quin_bottom", () -> new BannerPattern("5b"));
    public static final RegistryObject<BannerPattern> QUIN_LEFT = BANNER_PATTERNS.register("quin_left", () -> new BannerPattern("5l"));
    public static final RegistryObject<BannerPattern> QUIN_RIGHT = BANNER_PATTERNS.register("quin_right", () -> new BannerPattern("5r"));
    public static final RegistryObject<BannerPattern> QUIN_TOP_STRIPE = BANNER_PATTERNS.register("quin_top_stripe", () -> new BannerPattern("5st"));
    public static final RegistryObject<BannerPattern> QUIN_BOTTOM_STRIPE = BANNER_PATTERNS.register("quin_bottom_stripe", () -> new BannerPattern("5sb"));
    public static final RegistryObject<BannerPattern> QUIN_LEFT_STRIPE = BANNER_PATTERNS.register("quin_left_stripe", () -> new BannerPattern("5sl"));
    public static final RegistryObject<BannerPattern> QUIN_RIGHT_STRIPE = BANNER_PATTERNS.register("quin_right_stripe", () -> new BannerPattern("5sr"));
    public static final RegistryObject<BannerPattern> SIX_TOP = BANNER_PATTERNS.register("six_top", () -> new BannerPattern("6t"));
    public static final RegistryObject<BannerPattern> SIX_BOTTOM = BANNER_PATTERNS.register("six_bottom", () -> new BannerPattern("6b"));
    public static final RegistryObject<BannerPattern> SEP_TOP = BANNER_PATTERNS.register("sep_top", () -> new BannerPattern("7t"));
    public static final RegistryObject<BannerPattern> SEP_BOTTOM = BANNER_PATTERNS.register("sep_bottom", () -> new BannerPattern("7b"));
    public static final RegistryObject<BannerPattern> SEP_TOP_STRIPE = BANNER_PATTERNS.register("sep_top_stripe", () -> new BannerPattern("7st"));
    public static final RegistryObject<BannerPattern> SEP_BOTTOM_STRIPE = BANNER_PATTERNS.register("sep_bottom_stripe", () -> new BannerPattern("7sb"));
    public static final RegistryObject<BannerPattern> SEP_TOP_MIDDLE = BANNER_PATTERNS.register("sep_top_middle", () -> new BannerPattern("7mt"));
    public static final RegistryObject<BannerPattern> SEP_BOTTOM_MIDDLE = BANNER_PATTERNS.register("sep_bottom_middle", () -> new BannerPattern("7mb"));
    public static final RegistryObject<BannerPattern> QUIN_MIDDLE = BANNER_PATTERNS.register("quin_middle", () -> new BannerPattern("5m"));
    public static final RegistryObject<BannerPattern> QUIN_CENTER = BANNER_PATTERNS.register("quin_center", () -> new BannerPattern("5c"));
    public static final RegistryObject<BannerPattern> OPEN_CIRCLE = BANNER_PATTERNS.register("open_circle", () -> new BannerPattern("oc"));
}
