package by.psither.dragonsurvival.common.blocks;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class GlowSlimeBlock extends MultifaceBlock implements SimpleWaterloggedBlock {
	public static final MapCodec<GlowSlimeBlock> CODEC = simpleCodec(GlowSlimeBlock::new);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private final MultifaceSpreader spreader = new MultifaceSpreader(this);

	public GlowSlimeBlock(BlockBehaviour.Properties pProperties) {
		super(pProperties);
	    this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.FALSE));
	}

	protected @NotNull MapCodec<? extends MultifaceBlock> codec() {
		return CODEC;
	}

	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(WATERLOGGED);
	}

	@Override
	public @NotNull BlockState updateShape(BlockState pState, @NotNull Direction pDirection, @NotNull BlockState pNeighborState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pNeighborPos) {
		if (pState.getValue(WATERLOGGED)) {
		   pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}

	    return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTootipComponents, @NotNull TooltipFlag pTooltipFlag){
		super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
		pTootipComponents.add(Component.translatable("ad.description.glow_slime"));
	}
	
	public static int getLightLevel(BlockState pState) {
		// Underwater: 10, In air: 4, Empty: 0
		return MultifaceBlock.hasAnyFace(pState) ? pState.getValue(WATERLOGGED) ? 10 : 4 : 0;
	}

	public @NotNull FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return true;
	}
	
	@Override
	public @NotNull MultifaceSpreader getSpreader() {
		return spreader;
	}
}
