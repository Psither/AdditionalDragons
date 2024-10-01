package by.psither.dragonsurvival.magic.abilities.Astral.active;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.InstantCastAbility;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;
import static by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType.isCharged;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class WyrmholeAbility extends InstantCastAbility {
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "wyrmhole"}, key = "wyrmhole", comment = "Whether the wyrmhole ability should be enabled" )
    public static Boolean wyrmhole = true;

    @ConfigRange( min = 0.05, max = 10000 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "wyrmhole"}, key = "wyrmholeCooldown", comment = "The cooldown in seconds of the wyrmhole ability" )
    public static Double wyrmholeCooldown = 1.0;

    @ConfigRange( min = 0, max = 100 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "wyrmhole"}, key = "wyrmholeManaCost", comment = "The mana cost for using the wyrmhole ability" )
    public static Integer wyrmholeManaCost = 0;

    @ConfigRange ( min = 0, max = 10000.0 )
    @ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "wyrmhole"}, key = "wyrmholeRange", comment = "How far, in meters the wyrmhole is able to teleport per cast." )
    public static Double wyrmholeRange = 15.0;

    @ConfigRange ( min = 0, max = 10000.0 )
    @ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "wyrmhole"}, key = "wyrmholePullRange", comment = "How close, in meters, an entity has to be to be teleported with you." )
    public static Double wyrmholePullRange = 1.0;

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getTitle() {
        return Component.translatable("ds.skill." + this.getName() + (isCharged(player) ? ".enhanced" : ""));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getDescription(){
        return Component.translatable("ds.skill.description." + getName() + (isCharged(player) ? ".enhanced" : ""));
    }

    @Override
    public int getSortOrder(){
        return 2;
    }

    public void onKeyPressed(Player player, Runnable onFinish, long castStartTime, long clientTime) {
        if (canCast(player)) {
            super.onKeyPressed(player, onFinish, castStartTime, clientTime);
        }
    }

    public boolean canCast(Player player) {
        return getTeleportDestination(player) != null;
    }

    @Override
    public void onCast(Player player) {
        DragonStateHandler handler = DragonStateProvider.getOrGenerateHandler(player);
        Vec3 teleportDestination = getTeleportDestination(player);

        if (this.player.level().isClientSide()) {
            doClientStuff(player.getEyePosition(), teleportDestination, player);
        }
        if (handler.getType() instanceof AstralDragonType astral && astral.charges > 0) {
            astral.charges--;
            for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(wyrmholePullRange * this.level))) {
                Vec3 offset = player.getEyePosition().subtract(entity.getEyePosition());
                if (entity.level().noCollision(entity.getBoundingBox().move(new Vec3(teleportDestination.x, teleportDestination.y, teleportDestination.z).subtract(offset)))) {
                    entity.moveTo(new Vec3(teleportDestination.x, teleportDestination.y, teleportDestination.z).subtract(offset));
                } else if (entity.level().noCollision(entity.getBoundingBox().move(new Vec3(teleportDestination.x, teleportDestination.y, teleportDestination.z).subtract(offset.multiply(0.5, 0.5, 0.5))))) {
                    entity.moveTo(new Vec3(teleportDestination.x, teleportDestination.y, teleportDestination.z).subtract(offset.multiply(0.5, 0.5, 0.5)));
                } else {
                    entity.moveTo(teleportDestination);
                }
            }
        }
        player.moveTo(teleportDestination.subtract(player.getDeltaMovement()));
    }

    public static void doClientStuff(Vec3 clientEye, Vec3 destination, Player player) {
        player.level().playSound(player, BlockPos.containing(clientEye), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.1f, 1.1f);
        player.level().playSound(player, BlockPos.containing(destination), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.1f, 1.1f);
        for (int i = 0; i < 5; i++) {
            player.level().addParticle(ParticleTypes.PORTAL, destination.x + Math.random() - 0.5, destination.y + Math.random() - 0.5, destination.z + Math.random() - 0.5, 0.0, 0.0, 0.0);
            player.level().addParticle(ParticleTypes.PORTAL, clientEye.x + Math.random() - 0.5, clientEye.y + Math.random() - 0.5, clientEye.z + Math.random() - 0.5, 0.0, 0.0, 0.0);
        }
    }

    public Vec3 getTeleportDestination(Player player) {
        for (double dist = wyrmholeRange * this.getLevel(); dist > 1; dist-= 0.5) {
            for (double offsetHeight = -player.getBbHeight(); offsetHeight < player.getBbHeight(); offsetHeight += player.getBbHeight() * 0.2) {
                Vec3 lookAngle = player.getLookAngle().multiply(dist, dist, dist);
                BlockHitResult res = player.level().clip(new ClipContext(player.getEyePosition(), lookAngle.add(player.getEyePosition()), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player));
                if (res.getType().equals(HitResult.Type.MISS)) {
                    if (player.level().noCollision(player.getBoundingBox().move(player.getEyePosition().subtract(res.getLocation()).add(0, offsetHeight, 0)))) {
                        return res.getLocation().add(0, offsetHeight, 0);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getManaCost() {
        return wyrmholeManaCost;
    }

    @Override
    public Integer[] getRequiredLevels() {
        return new Integer[]{0, 3, 19, 31, 47};
    }

    @Override
    public int getSkillCooldown() {
        return Functions.secondsToTicks(wyrmholeCooldown);
    }

    @Override
    public String getName() {
        return "wyrmhole";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        if (isCharged(this.player)) {
            return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/personal_wyrmhole_0.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/personal_wyrmhole_1.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/personal_wyrmhole_2.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/personal_wyrmhole_3.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/personal_wyrmhole_4.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/personal_wyrmhole_5.png")
            };
        }
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/wyrmhole_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/wyrmhole_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/wyrmhole_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/wyrmhole_3.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/wyrmhole_4.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/wyrmhole_5.png")
        };
    }

    @Override
    public int getMinLevel() {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || !wyrmhole;
    }
}
