package com.hiimjustin000.advancedbanners.recipes;

import com.hiimjustin000.advancedbanners.items.AdvancedBannerItem;
import com.hiimjustin000.advancedbanners.registries.BlockEntityTypeRegistry;
import com.hiimjustin000.advancedbanners.registries.ItemRegistry;
import com.hiimjustin000.advancedbanners.registries.RecipeSerializerRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AdvancedBannerDyeRecipe extends CustomRecipe
{
    public AdvancedBannerDyeRecipe(ResourceLocation id, CraftingBookCategory category)
    {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level)
    {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack dye = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++)
        {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof AdvancedBannerItem)
                {
                    if (!banner.isEmpty())
                        return false;
                    banner = stack;
                }
                else
                {
                    if (!stack.is(ItemRegistry.ADVANCED_DYE.get()) || !dye.isEmpty())
                        return false;
                    dye = stack;
                }
            }
        }

        return !dye.isEmpty() && !banner.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access)
    {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack dye = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); ++i)
        {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof AdvancedBannerItem)
                {
                    CompoundTag entityData = BlockItem.getBlockEntityData(stack);
                    if (entityData == null || !entityData.contains("Patterns", 9) || entityData.getList("Patterns", 10).isEmpty())
                        banner = stack.copy();
                }
                else if (stack.is(ItemRegistry.ADVANCED_DYE.get()))
                    dye = stack.copy();
            }
        }

        if (!banner.isEmpty())
        {
            CompoundTag tag = dye.getTag();
            int color = Mth.clamp(tag != null && tag.contains("Color", 3) ? tag.getInt("Color") : 16777215, 0, 16777215);
            CompoundTag entityData = BlockItem.getBlockEntityData(banner);
            CompoundTag bannerData = entityData != null ? entityData.copy() : new CompoundTag();
            bannerData.putInt("Base", color);
            BlockItem.setBlockEntityData(banner, BlockEntityTypeRegistry.ADVANCED_BANNER.get(), bannerData);
        }

        return banner;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<AdvancedBannerDyeRecipe> getSerializer()
    {
        return RecipeSerializerRegistry.ADVANCED_BANNER_DYE.get();
    }
}
