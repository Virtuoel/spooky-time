package com.fabriccommunity.thehallow.block;

import com.fabriccommunity.thehallow.registry.HallowedBlocks;
import com.fabriccommunity.thehallow.registry.HallowedTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HallowedGateBlock extends Block {
	public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

	public HallowedGateBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateFactory().getDefaultState().with(FACING, Direction.NORTH));
	}

	public static boolean isValid(World world, BlockPos pos, BlockState state) {
		if (state.getBlock() != HallowedBlocks.HALLOWED_GATE) return false;
		for (Direction dir : Direction.values()) {
			if (dir == Direction.UP || dir == Direction.DOWN) continue;
			BlockState newState = world.getBlockState(pos.offset(dir));
			if (!HallowedTags.GATE_CIRCLE.contains(newState.getBlock())) return false;
			if (dir == Direction.NORTH || dir == Direction.SOUTH) {
				BlockState eastState = world.getBlockState(pos.offset(Direction.EAST));
				BlockState westState = world.getBlockState(pos.offset(Direction.WEST));
				if (!HallowedTags.GATE_CIRCLE.contains(eastState.getBlock()) || !HallowedTags.GATE_CIRCLE.contains(westState.getBlock())) return false;
			}
		}
		return true;
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaque(BlockState state) {
		return false;
	}

	@Override
	public int getLuminance(BlockState state) {
		return 6;
	}
}
