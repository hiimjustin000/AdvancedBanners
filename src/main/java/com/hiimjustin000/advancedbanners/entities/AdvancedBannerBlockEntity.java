package com.hiimjustin000.advancedbanners.entities;

import com.hiimjustin000.advancedbanners.registries.BlockEntityTypeRegistry;
import com.hiimjustin000.advancedbanners.registries.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AdvancedBannerBlockEntity extends BlockEntity implements Nameable
{
    private Component name;
    private int baseColor = 16777215;
    private ListTag patternsTag;
    private List<Pair<Holder<BannerPattern>, Integer>> patterns;

    public AdvancedBannerBlockEntity(BlockPos pos, BlockState state)
    {
        super(BlockEntityTypeRegistry.ADVANCED_BANNER.get(), pos, state);
    }

    @Override
    public Component getName()
    {
        return name != null ? name : Component.translatable("block.advancedbanners.advanced_banner");
    }

    @Nullable
    @Override
    public Component getCustomName()
    {
        return name;
    }

    public void setCustomName(Component component)
    {
        name = component;
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tag.putInt("Base", baseColor);
        if (patternsTag != null)
            tag.put("Patterns", patternsTag);
        if (name != null)
            tag.putString("CustomName", Component.Serializer.toJson(name));
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        if (tag.contains("CustomName", 8))
            name = Component.Serializer.fromJson(tag.getString("CustomName"));
        patternsTag = tag.getList("Patterns", 10).copy();
        baseColor = Mth.clamp(tag.getInt("Base"), 0, 16777215);
        patterns = null;
    }

    public static ListTag getPatternsTag(ItemStack stack)
    {
        ListTag tag = null;
        CompoundTag compoundTag = BlockItem.getBlockEntityData(stack);
        if (compoundTag != null && compoundTag.contains("Patterns", 9))
            tag = compoundTag.getList("Patterns", 10).copy();
        return tag;
    }

    public static int getColorTag(ItemStack stack)
    {
        int color = 16777215;
        CompoundTag compoundTag = BlockItem.getBlockEntityData(stack);
        if (compoundTag != null && compoundTag.contains("Base", 3))
            color = Math.min(16777215, Math.max(0, compoundTag.getInt("Base")));
        return color;
    }

    public void fromItem(ItemStack stack)
    {
        patternsTag = getPatternsTag(stack);
        baseColor = getColorTag(stack);
        name = stack.hasCustomHoverName() ? stack.getHoverName() : null;
        patterns = null;
    }

    public static List<Pair<Holder<BannerPattern>, Integer>> createPatterns(int baseColor, ListTag patternsTag)
    {
        List<Pair<Holder<BannerPattern>, Integer>> list = new ArrayList<>();
        list.add(Pair.of(BuiltInRegistries.BANNER_PATTERN.getHolderOrThrow(BannerPatterns.BASE), baseColor));
        if (patternsTag != null)
        {
            for (int i = 0; i < patternsTag.size(); i++)
            {
                CompoundTag compoundTag = patternsTag.getCompound(i);
                Holder<BannerPattern> pattern = BannerPattern.byHash(compoundTag.getString("Pattern"));
                if (pattern != null)
                {
                    int color = compoundTag.getInt("Color");
                    list.add(Pair.of(pattern, color));
                }
            }
        }

        return list;
    }

    public List<Pair<Holder<BannerPattern>, Integer>> getPatterns()
    {
        if (patterns == null)
            patterns = createPatterns(baseColor, patternsTag);
        return patterns;
    }

    public ItemStack getItem()
    {
        ItemStack stack = new ItemStack(ItemRegistry.ADVANCED_BANNER.get());
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("Base", baseColor);
        if (patternsTag != null)
            compoundTag.put("Patterns", patternsTag);
        if (!compoundTag.isEmpty())
            BlockItem.setBlockEntityData(stack, getType(), compoundTag);
        if (name != null)
            stack.setHoverName(name);
        return stack;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return saveWithoutMetadata();
    }
}
