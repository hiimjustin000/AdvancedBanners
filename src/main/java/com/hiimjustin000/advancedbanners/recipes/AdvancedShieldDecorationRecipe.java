package com.hiimjustin000.advancedbanners.recipes;

import com.hiimjustin000.advancedbanners.items.AdvancedBannerItem;
import com.hiimjustin000.advancedbanners.registries.BlockEntityTypeRegistry;
import com.hiimjustin000.advancedbanners.registries.ItemRegistry;
import com.hiimjustin000.advancedbanners.registries.RecipeSerializerRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AdvancedShieldDecorationRecipe extends CustomRecipe
{
    public AdvancedShieldDecorationRecipe(ResourceLocation id, CraftingBookCategory category)
    {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level)
    {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack shield = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); ++i)
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
                    if (!stack.is(ItemRegistry.ADVANCED_SHIELD.get()) || !shield.isEmpty() || BlockItem.getBlockEntityData(stack) != null)
                        return false;
                    shield = stack;
                }
            }
        }

        return !shield.isEmpty() && !banner.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access)
    {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack shield = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++)
        {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof AdvancedBannerItem)
                    banner = stack;
                else if (stack.is(ItemRegistry.ADVANCED_SHIELD.get()))
                    shield = stack.copy();
            }
        }

        if (!shield.isEmpty())
        {
            CompoundTag blockEntityData = BlockItem.getBlockEntityData(banner);
            CompoundTag shieldData = blockEntityData == null ? new CompoundTag() : blockEntityData.copy();
            if (!shieldData.contains("Base", 3))
                shieldData.putInt("Base", 16777215);
            BlockItem.setBlockEntityData(shield, BlockEntityTypeRegistry.ADVANCED_BANNER.get(), shieldData);
        }

        return shield;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<AdvancedShieldDecorationRecipe> getSerializer()
    {
        return RecipeSerializerRegistry.ADVANCED_SHIELD_DECORATION.get();
    }
}
