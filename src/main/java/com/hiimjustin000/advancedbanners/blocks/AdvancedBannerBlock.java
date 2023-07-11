package com.hiimjustin000.advancedbanners.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AdvancedBannerBlock extends AbstractAdvancedBannerBlock
{
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 16, 12);

    public AdvancedBannerBlock()
    {
        super(Properties.of()
                .mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                .noCollission().strength(1).sound(SoundType.WOOD).ignitedByLava());
        registerDefaultState(stateDefinition.any().setValue(ROTATION, 0));
    }

    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos)
    {
        return reader.getBlockState(pos.below()).isSolid();
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation() + 180));
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState,
                                  LevelAccessor accessor, BlockPos pos, BlockPos facingPos)
    {
        return direction == Direction.DOWN && !state.canSurvive(accessor, pos) ?
                Blocks.AIR.defaultBlockState() :
                super.updateShape(state, direction, facingState, accessor, pos, facingPos);
    }

    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), 16));
    }

    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), 16));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(ROTATION);
    }
}
