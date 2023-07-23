package com.hiimjustin000.advancedbanners.items;

import com.hiimjustin000.advancedbanners.ColorUtilities;
import com.hiimjustin000.advancedbanners.registries.BlockRegistry;
import com.hiimjustin000.advancedbanners.renderers.AdvancedBannerWithoutLevelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class AdvancedBannerItem extends StandingAndWallBlockItem
{
    private AdvancedBannerWithoutLevelRenderer renderer;

    public AdvancedBannerItem()
    {
        super(BlockRegistry.ADVANCED_BANNER.get(), BlockRegistry.ADVANCED_WALL_BANNER.get(), new Properties().stacksTo(16), Direction.DOWN);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
    {
        appendHoverTextFromBannerBlockEntityTag(stack, components);
    }

    public static void appendHoverTextFromBannerBlockEntityTag(ItemStack stack, List<Component> components)
    {
        CompoundTag blockEntityTag = BlockItem.getBlockEntityData(stack);
        int baseColor = blockEntityTag != null && blockEntityTag.contains("Base", 3) ? Mth.clamp(blockEntityTag.getInt("Base"), 0, 16777215) : 16777215;
        String baseText = Component.translatable("block.minecraft." + ColorUtilities.roundColor(baseColor).getName() + "_banner").getString();
        components.add(Component.literal(baseText + " (#" + ColorUtilities.toHex(baseColor) + ")").withStyle(Style.EMPTY.withColor(baseColor)));
        if (blockEntityTag != null && blockEntityTag.contains("Patterns", 9))
        {
            ListTag patterns = blockEntityTag.getList("Patterns", 10);
            for (int i = 0; i < patterns.size(); i++)
            {
                CompoundTag pattern = patterns.getCompound(i);
                int color = Mth.clamp(pattern.getInt("Color"), 0, 16777215);
                DyeColor rounded = ColorUtilities.roundColor(color);
                Holder<BannerPattern> holder = BannerPattern.byHash(pattern.getString("Pattern"));
                if (holder != null)
                {
                    holder.unwrapKey().map(x -> x.location().toShortLanguageKey()).ifPresent(x ->
                    {
                        ResourceLocation id = new ResourceLocation(x);
                        String text = Component.translatable("block." + id.getNamespace() + ".banner." + id.getPath() + "." + rounded.getName()).getString();
                        components.add(Component.literal(text + " (#" + ColorUtilities.toHex(color) + ")").withStyle(Style.EMPTY.withColor(color)));
                    });
                }
            }
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions()
        {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                if (renderer == null)
                {
                    Minecraft mc = Minecraft.getInstance();
                    renderer = new AdvancedBannerWithoutLevelRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
                }

                return renderer;
            }
        });
    }
}
