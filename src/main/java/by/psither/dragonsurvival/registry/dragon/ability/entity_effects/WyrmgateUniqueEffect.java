package by.psither.dragonsurvival.registry.dragon.ability.entity_effects;

import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbilityInstance;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.entity_effects.AbilityEntityEffect;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;

import java.util.ArrayList;
import java.util.List;

public record WyrmgateUniqueEffect(ResourceKey<Level> dimension, ResourceKey<Block> block) implements AbilityEntityEffect {
    public static final MapCodec<WyrmgateUniqueEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(WyrmgateUniqueEffect::dimension),
            ResourceKey.codec(Registries.BLOCK).fieldOf("block").forGetter(WyrmgateUniqueEffect::block)
    ).apply(instance, WyrmgateUniqueEffect::new));

    public void apply(ServerPlayer dragon, DragonAbilityInstance ability, Entity target) {
        if (!dimension.equals(target.level().dimension())) {
            ServerLevel serverLevel = (ServerLevel) dragon.level();
            WorldBorder worldborder = serverLevel.getWorldBorder();
            ServerLevel dimensionLevel = serverLevel.getServer().getLevel(dimension);
            if (dimensionLevel != null) {
                double scale = DimensionType.getTeleportationScale(target.level().dimensionType(), dimensionLevel.dimensionType());
                BlockPos startPos = worldborder.clampToBounds(target.getX() * scale, target.getY(), target.getZ() * scale);
                int targetHeight = (int) target.getBbHeight() + 1;
                serverLevel.getPoiManager().ensureLoadedAndValid(dimensionLevel, startPos, 1);
                if (startPos.getY() < dimensionLevel.dimensionType().minY()) {
                    startPos = startPos.atY(dimensionLevel.dimensionType().minY());
                }
                if (startPos.getY() > dimensionLevel.getLogicalHeight()) {
                    startPos = startPos.atY(dimensionLevel.dimensionType().logicalHeight() - targetHeight);
                }
                for (int i = startPos.getY(); i >= dimensionLevel.dimensionType().minY(); i--) {
                    if (i > dimensionLevel.dimensionType().logicalHeight()) {
                        break;
                    }
                    BlockPos pos = new BlockPos(startPos.getX(), i, startPos.getZ());
                    if (dimensionLevel.getBlockState(pos).isSolid() || dimensionLevel.getBlockState(pos).isEmpty()) {
                        boolean flag = true;
                        for (int j = 1; j <= targetHeight; j++) {
                            if (!dimensionLevel.getBlockState(pos.above(j)).isEmpty()) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            Registry<Block> registry = serverLevel.registryAccess().registry(Registries.BLOCK).orElse(null);
                            if (registry != null) {
                                Block block2 = registry.get(block);
                                if (block2 != null) {
                                    int radius = (int) target.getBbWidth() + 2;
                                    for (int x = -radius; x <= radius; x++) {
                                        for (int y = -radius; y <= radius; y++) {
                                            if (dimensionLevel.getBlockState(pos.east(x).north(y)).isEmpty()) {
                                                if (dimensionLevel.getBlockState(pos.east(x).north(y)).canBeReplaced()) {
                                                    dimensionLevel.setBlock(pos.east(x).north(y), block2.defaultBlockState(), 2);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            target.changeDimension(new DimensionTransition(dimensionLevel, pos.above().getCenter(), target.getDeltaMovement(), target.getYRot(), target.getXRot(), DimensionTransition.DO_NOTHING));
                            return;
                        }
                    }
                }
                for (int i = startPos.getY(); i < dimensionLevel.dimensionType().logicalHeight() - targetHeight; i++) {
                    BlockPos pos = new BlockPos(startPos.getX(), i, startPos.getZ());
                    if (dimensionLevel.getBlockState(pos).isSolid() || dimensionLevel.getBlockState(pos).isEmpty()) {
                        boolean flag = true;
                        for (int j = 1; j <= targetHeight; j++) {
                            if (!dimensionLevel.getBlockState(pos.above(j)).isEmpty()) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            Registry<Block> registry = serverLevel.registryAccess().registry(Registries.BLOCK).orElse(null);
                            if (registry != null) {
                                Block block2 = registry.get(block);
                                if (block2 != null) {
                                    int radius = (int) target.getBbWidth() + 2;
                                    for (int x = -radius; x <= radius; x++) {
                                        for (int y = -radius; y <= radius; y++) {
                                            if (dimensionLevel.getBlockState(pos.east(x).north(y)).canBeReplaced()) {
                                                dimensionLevel.setBlock(pos.east(x).north(y), block2.defaultBlockState(), 2);
                                            }
                                        }
                                    }
                                }
                            }
                            target.changeDimension(new DimensionTransition(dimensionLevel, pos.above().getCenter(), target.getDeltaMovement(), target.getYRot(), target.getXRot(), DimensionTransition.DO_NOTHING));
                            return;
                        }
                    }
                }
            }
        }
    }

    public List<MutableComponent> getDescription(final Player dragon, final DragonAbilityInstance ability) {
        List<MutableComponent> components = new ArrayList<>();

        components.add(Component.translatable("dragonsurvival.gui.ability.wyrmgate_dimension", dimension.location().getNamespace(), dimension.location().getPath()));
        return components;
    }

    @Override
    public MapCodec<? extends AbilityEntityEffect> entityCodec() {
        return CODEC;
    }
}
