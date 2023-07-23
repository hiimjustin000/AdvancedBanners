package com.hiimjustin000.advancedbanners.items;

import com.hiimjustin000.advancedbanners.ColorUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedDyeItem extends Item
{
    public AdvancedDyeItem()
    {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
    {
        CompoundTag tag = stack.getTag();
        int color = Mth.clamp(tag != null && tag.contains("Color", 3) ? tag.getInt("Color") : 16777215, 0, 16777215);
        String text = Component.translatable("item.minecraft." + ColorUtilities.roundColor(color).getName() + "_dye").getString();
        components.add(Component.literal(text + " (" + ColorUtilities.toHex(color) + ")").withStyle(Style.EMPTY.withColor(color)));
    }
}
