package com.hiimjustin000.advancedbanners.blocks;

import com.hiimjustin000.advancedbanners.entities.AdvancedBannerBlockEntity;
import com.hiimjustin000.advancedbanners.registries.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class AbstractAdvancedBannerBlock extends BaseEntityBlock
{
    protected AbstractAdvancedBannerBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState state)
    {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new AdvancedBannerBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack)
    {
        if (level.isClientSide)
            level.getBlockEntity(pos, BlockEntityTypeRegistry.ADVANCED_BANNER.get()).ifPresent(blockEntity ->
                    blockEntity.fromItem(stack));
        else if (stack.hasCustomHoverName())
            level.getBlockEntity(pos, BlockEntityTypeRegistry.ADVANCED_BANNER.get()).ifPresent(blockEntity ->
                    blockEntity.setCustomName(stack.getHoverName()));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state)
    {
        return getter.getBlockEntity(pos) instanceof AdvancedBannerBlockEntity entity ? entity.getItem() : super.getCloneItemStack(getter, pos, state);
    }
}
