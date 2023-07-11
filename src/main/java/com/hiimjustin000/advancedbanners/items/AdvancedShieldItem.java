package com.hiimjustin000.advancedbanners.items;

import com.hiimjustin000.advancedbanners.renderers.AdvancedBannerWithoutLevelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class AdvancedShieldItem extends Item implements Equipable
{
    private AdvancedBannerWithoutLevelRenderer renderer;

    public AdvancedShieldItem()
    {
        super(new Properties().durability(336));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
    {
        if (BlockItem.getBlockEntityData(stack) != null)
            AdvancedBannerItem.appendHoverTextFromBannerBlockEntityTag(stack, components);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient)
    {
        return ingredient.is(ItemTags.PLANKS) || super.isValidRepairItem(stack, ingredient);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction)
    {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    @Override
    public EquipmentSlot getEquipmentSlot()
    {
        return EquipmentSlot.OFFHAND;
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
