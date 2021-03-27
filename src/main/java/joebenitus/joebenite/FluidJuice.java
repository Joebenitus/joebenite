package joebenitus.joebenite;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class FluidJuice extends FlowableFluid {

  @Override
  public void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
    final BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
  }

  @Override
  public int getFlowSpeed(WorldView world) {
    return 4;
  }

  @Override
  public Fluid getFlowing() {
    return Joebenite.FLOWING_JUICE;
  }

  @Override
  public int getLevelDecreasePerBlock(WorldView world) {
    return 1;
  }

  @Override
  public Fluid getStill() {
    return Joebenite.STILL_JUICE;
  }

  @Override
  public boolean isInfinite() {
    return false;
  }

  @Override
  public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
    return false;
  }

  @Override
  public float getBlastResistance() {
    return 100.0f;
  }

  @Override
  public Item getBucketItem() {
    return Joebenite.JUICE_BUCKET;
  }

  @Override
  public int getTickRate(WorldView world) {
    return 5;
  }

  @Override
  public boolean isStill(FluidState state) {
    return false;
  }

  @Override
  public BlockState toBlockState(FluidState state) {
		return Joebenite.JUICE.getDefaultState().with(Properties.LEVEL_15, method_15741(state));
  }

  public static class Flowing extends FluidJuice {
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder)
		{
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState fluidState)
		{
			return fluidState.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState fluidState)
		{
			return false;
		}
	}

  public static class Still extends FluidJuice
	{
		@Override
		public int getLevel(FluidState fluidState)
		{
			return 8;
		}

		@Override
		public boolean isStill(FluidState fluidState)
		{
			return true;
		}
	}

  
}
