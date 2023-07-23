package com.hiimjustin000.advancedbanners.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hiimjustin000.advancedbanners.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class AdvancedWallBannerBlock extends AbstractAdvancedBannerBlock
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(0, 0, 14, 16, 12.5, 16),
            Direction.SOUTH, Block.box(0, 0, 0, 16, 12.5, 2),
            Direction.WEST, Block.box(14, 0, 0, 16, 12.5, 16),
            Direction.EAST, Block.box(0, 0, 0, 2, 12.5, 16)
    ));

    public AdvancedWallBannerBlock()
    {
        super(Properties.of()
                .mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                .noCollission().strength(1).sound(SoundType.WOOD).lootFrom(BlockRegistry.ADVANCED_BANNER).ignitedByLava());
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos)
    {
        return reader.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isSolid();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState,
                                  LevelAccessor accessor, BlockPos pos, BlockPos facingPos)
    {
        return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(accessor, pos) ?
                Blocks.AIR.defaultBlockState() :
                super.updateShape(state, direction, facingState, accessor, pos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = defaultBlockState();
        LevelReader reader = context.getLevel();
        BlockPos pos = context.getClickedPos();

        for (Direction direction : context.getNearestLookingDirections())
        {
            if (direction.getAxis().isHorizontal())
            {
                Direction opposite = direction.getOpposite();
                state = state.setValue(FACING, opposite);
                if (state.canSurvive(reader, pos))
                    return state;
            }
        }

        return null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }
}
