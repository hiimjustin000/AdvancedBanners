package com.hiimjustin000.advancedbanners.blocks;

import com.hiimjustin000.advancedbanners.menus.DyeMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DyeMachineBlock extends Block
{
    public static final Component TITLE = Component.translatable("container.dye_machine");

    public DyeMachineBlock()
    {
        super(Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F));
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        return new SimpleMenuProvider((id, inventory, player) ->
                new DyeMachineMenu(id, inventory, ContainerLevelAccess.create(level, pos)), TITLE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if (!level.isClientSide)
        {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }
}
