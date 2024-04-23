package by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.particles.SeaDragon.LargeGlowSlimeParticleData;
import by.psither.dragonsurvival.client.sounds.LuminousBreathSound;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.client.sounds.ADSoundRegistry;
import by.psither.dragonsurvival.registry.ADBlocks;

import java.util.ArrayList;
import java.util.Locale;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.BreathAbility;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.dragonsurvivalteam.dragonsurvival.util.TargetingFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;

@RegisterDragonAbility
public class LuminousBreathAbility extends BreathAbility {

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreath", comment = "Whether the luminous breath ability should be enabled" )
	public static Boolean luminousBreath = true;

	@ConfigRange( min = 0.0, max = 100.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreathDamage", comment = "The amount of damage the luminous breath ability deals. This value is multiplied by the skill level." )
	public static Double luminousBreathDamage = 0.5;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreathInitialMana", comment = "The mana cost for starting the luminous breath ability" )
	public static Integer luminousBreathInitialMana = 1;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreathCooldown", comment = "The cooldown in seconds of the luminous breath ability" )
	public static Double luminousBreathCooldown = 10.0;

	@ConfigRange( min = 0.05, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreathCasttime", comment = "The cast time in seconds of the luminous breath ability" )
	public static Double luminousBreathCasttime = 1.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreathOvertimeMana", comment = "The mana cost of sustaining the luminous breath ability" )
	public static Integer luminousBreathOvertimeMana = 1;

	@ConfigRange( min = 0.0, max = 100.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreathManaTicks", comment = "How often in seconds, mana is consumed while using luminous breath" )
	public static Double luminousBreathManaTicks = 3.0;

	@ConfigRange ( min = 0.0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreathEffectDuration", comment = "How long the glowing and slowdown effects will remain when applied to an entity" )
	public static Double luminousBreathEffectDuration = 10.0;

	@ConfigOption (side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "luminous_breath"}, key = "luminousBreathLightOutOfWater", comment = "Whether luminous breath's glow slime can produce light outside of water" )
	public static boolean luminousBreathLightOutOfWater = true;
	
	public boolean clipsWater = false;

	public static boolean getLightOutOfWater() {
		return luminousBreathLightOutOfWater;
	}
	
	public static double getEffectDuration() {
		return luminousBreathEffectDuration;
	}
	
	@Override
	public boolean canHitEntity(LivingEntity entity){
		return !(entity instanceof Player) || player.canHarmPlayer((Player)entity);
	}
	
	@Override
	public void onEntityHit(LivingEntity entity) {
		if (!DragonUtils.isDragonType(entity, DragonTypes.SEA)) {
			super.onEntityHit(entity);
		}
	}

	@Override
	public void onDamage(LivingEntity entity){
		if (!entity.level().isClientSide()) {
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Functions.secondsToTicks(luminousBreathEffectDuration), 1));
			entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, Functions.secondsToTicks(luminousBreathEffectDuration), 0, false, true));
		}
	}

	@OnlyIn( Dist.CLIENT ) // FIXME :: dist
	public void sound(){
		Vec3 pos = player.getEyePosition(1.0F);
		SimpleSoundInstance startingSound = new SimpleSoundInstance(
				ADSoundRegistry.luminousBreathStart,
				SoundSource.PLAYERS,
				1.0F,1.0F,
				SoundInstance.createUnseededRandom(),
				pos.x,pos.y,pos.z
		);
		Minecraft.getInstance().getSoundManager().playDelayed(startingSound, 0);
		Minecraft.getInstance().getSoundManager().stop(new ResourceLocation(AdditionalDragonsMod.MODID, "luminous_breath_loop"), SoundSource.PLAYERS);
		Minecraft.getInstance().getSoundManager().queueTickingSound(new LuminousBreathSound(this));
	}
	
	@OnlyIn( Dist.CLIENT )
	public void stopSound(){
		if(ADSoundRegistry.luminousBreathEnd != null){
			Vec3 pos = player.getEyePosition(1.0F);
			SimpleSoundInstance endSound = new SimpleSoundInstance(
					ADSoundRegistry.luminousBreathEnd,
					SoundSource.PLAYERS,
					1.0F,1.0F,
					SoundInstance.createUnseededRandom(),
					pos.x, pos.y, pos.z
			);
			Minecraft.getInstance().getSoundManager().playDelayed(endSound, 0);
		}

		Minecraft.getInstance().getSoundManager().stop(new ResourceLocation(AdditionalDragonsMod.MODID, "luminous_breath_loop"), SoundSource.PLAYERS);
	}
	
	@Override
	public void onChanneling(Player player, int castDuration){
		super.onChanneling(player, castDuration);

		if(player.level().isClientSide && castDuration <= 0){
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> (SafeRunnable)this::sound);
		}

		if(player.level().isClientSide){
			for(int i = 0; i < 6; i++){
				double xSpeed = speed * 1f * xComp + (spread * 1.7 * (player.getRandom().nextFloat() * 2 - 1) * Math.sqrt(1 - xComp * xComp));
				double ySpeed = speed * 1f * yComp + (spread * 0.7 * (player.getRandom().nextFloat() * 2 - 1) * Math.sqrt(1 - yComp * yComp));
				double zSpeed = speed * 1f * zComp + (spread * 1.7 * (player.getRandom().nextFloat() * 2 - 1) * Math.sqrt(1 - zComp * zComp));
				int dur = (int) (player.getRandom().nextFloat() * 64 + 16);
				player.level().addParticle(new LargeGlowSlimeParticleData(dur, false), dx, dy, dz, xSpeed, ySpeed, zSpeed);
			}

			for(int i = 0; i < 2; i++){
				  double xSpeed = speed * xComp;
				  double ySpeed = speed * yComp;
				  double zSpeed = speed * zComp;
				  player.level().addParticle(new LargeGlowSlimeParticleData(37, true), dx, dy, dz, xSpeed, ySpeed, zSpeed);
			}
		}

		hitEntities();

		if(player.tickCount % 5 == 0){
			hitBlocks();
		}
	}

	public static float getDamage(int level) {
		return (float) (luminousBreathDamage * level);
	}
	
	@Override
	public void onBlock(BlockPos pos, BlockState blockState, Direction direction) {
		if (!(player.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		
		boolean wl = false;
		int creationChance = 30;
		if (blockState.hasProperty(BlockStateProperties.WATERLOGGED))
			wl = blockState.getValue(BlockStateProperties.WATERLOGGED);
		if (wl || blockState.getBlock().equals(Blocks.WATER))
			creationChance = 45;
		BlockHitResult bhr = new BlockHitResult(player.position(), direction, pos, false);
		ItemStack gs = new ItemStack(ADBlocks.glowSlime, 1);
		UseOnContext uc = new UseOnContext(player.level(), player, InteractionHand.MAIN_HAND, gs, bhr);
		// Give closer blocks an increased chance to apply
		double mathDist = player.position().distanceToSqr(new Vec3(pos.getX(), pos.getY(), pos.getZ())) / calculateCurrentBreathRange(DragonUtils.getDragonLevel(player));
		creationChance /= mathDist;
		if (player.getRandom().nextInt(100) < creationChance) {
			InteractionResult ir = ADBlocks.glowSlime.asItem().useOn(uc);
			if (ir.consumesAction()) {
				player.level().playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 2F, 1F, false);
			}
		}
	}

	@Override
	public int blockBreakChance() {
		return 0;
	}

	@Override
	public float getDamage(){
		return getDamage(getLevel());
	}

	@Override
	public int getSkillChargeTime() {
		return Functions.secondsToTicks(luminousBreathCasttime);
	}

	@Override
	public int getContinuousManaCostTime() {
		return Functions.secondsToTicks(luminousBreathManaTicks);
	}

	@Override
	public int getInitManaCost() {
		return luminousBreathInitialMana;
	}

	@Override
	public void castComplete(Player player) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> (SafeRunnable)this::stopSound); // FIXME :: dist
	}

	@Override
	public int getManaCost() {
		return luminousBreathOvertimeMana;
	}

	@Override
	public Integer[] getRequiredLevels(){
		return new Integer[]{0, 10, 30, 50};
	}

	@Override
	public int getSkillCooldown() {
		 return Functions.secondsToTicks(luminousBreathCooldown);
	}

	@Override
	public String getName() {
		return "luminous_breath";
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.duration.seconds", (int) getEffectDuration()));
		return components;
	}
	
	@Override
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.damage", "+" + luminousBreathDamage));
		return list;
	}

	@Override
	public Fluid clipContext() {
		return ClipContext.Fluid.NONE;
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.PRIMORDIAL;
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/luminous_breath_0.png"),
									  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/luminous_breath_1.png"),
									  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/luminous_breath_2.png"),
									  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/luminous_breath_3.png"),
									  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/luminous_breath_4.png")};
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int getMinLevel() {
		return 0;
	}
	
	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !luminousBreath;
	}

}
