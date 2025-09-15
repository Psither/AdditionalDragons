package by.psither.dragonsurvival.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class WyrmgateBlock extends BaseEntityBlock {
    public static final MapCodec<WyrmgateBlock> CODEC = simpleCodec(WyrmgateBlock::new);

    private int lifespan;
    public WyrmgateBlock(Properties properties) {
        super(properties);
        lifespan = 2;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        lifespan--;
        if (lifespan <= 0) {
            this.destroy(level, pos, state);
        }
    }

    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
