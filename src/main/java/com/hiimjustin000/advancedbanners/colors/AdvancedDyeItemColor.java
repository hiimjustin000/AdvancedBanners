package com.hiimjustin000.advancedbanners.colors;

import com.hiimjustin000.advancedbanners.items.AdvancedDyeItem;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class AdvancedDyeItemColor implements ItemColor
{
    @Override
    public int getColor(ItemStack stack, int tint)
    {
        if (stack.getItem() instanceof AdvancedDyeItem)
        {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("Color", 3))
                return tag.getInt("Color");
        }

        return 16777215;
    }
}
