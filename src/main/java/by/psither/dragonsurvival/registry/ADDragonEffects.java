package by.psither.dragonsurvival.registry;

import java.util.Set;

import by.psither.dragonsurvival.common.effects.BlastDustedEffect;
import by.psither.dragonsurvival.common.effects.BubbleShieldEffect;
import by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active.PyroclasticRoarAbility;
import by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active.UnstoppableAbility;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

public class ADDragonEffects {

	public static final DeferredRegister<MobEffect> AD_MOB_EFFECTS = DeferredRegister.create(
			BuiltInRegistries.MOB_EFFECT,
			MODID
	);
	
	public static Holder<MobEffect> BUBBLE_SHIELD = AD_MOB_EFFECTS.register(
			"bubble_shield",
			() -> new BubbleShieldEffect(MobEffectCategory.BENEFICIAL, 0x0)
	);

	public static Holder<MobEffect> HIGH_VOLTAGE = AD_MOB_EFFECTS.register(
			"high_voltage",
			() -> new ModifiableMobEffect(MobEffectCategory.BENEFICIAL, 0x0, false)
	);
	public static Holder<MobEffect> BLAST_DUSTED = AD_MOB_EFFECTS.register(
			"blast_dusted",
			() -> new BlastDustedEffect(MobEffectCategory.HARMFUL, 0x0)
	);
	public static Holder<MobEffect> UNSTOPPABLE = AD_MOB_EFFECTS.register(
			"unstoppable",
			() -> new ModifiableMobEffect(MobEffectCategory.BENEFICIAL, 0x0, false)
					.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(MODID, "unstoppable"), UnstoppableAbility.unstoppableKnockbackResistValue, Operation.ADD_VALUE)
	);
	public static Holder<MobEffect> VOLCANIC_RAGE = AD_MOB_EFFECTS.register(
			"volcanic_rage",
			() -> new ModifiableMobEffect(MobEffectCategory.BENEFICIAL, 0x0, false)
					.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, ResourceLocation.fromNamespaceAndPath(MODID, "volcanic_toughness"), PyroclasticRoarAbility.volcanicRageArmorToughnessValue, Operation.ADD_VALUE)
					.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, ResourceLocation.fromNamespaceAndPath(MODID, "volcanic_knockback"), PyroclasticRoarAbility.volcanicRageAttackKnockbackValue, Operation.ADD_VALUE)
					.addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(MODID, "volcanic_strength"), PyroclasticRoarAbility.volcanicRageAttackDamageValue, Operation.ADD_VALUE)
	);
	public static Holder<MobEffect> CONFOUNDED = AD_MOB_EFFECTS.register(
			"confounded",
			() -> new ModifiableMobEffect(MobEffectCategory.HARMFUL, 0x0, false)
	);
	public static Holder<MobEffect> SEEKING_TALONS = AD_MOB_EFFECTS.register(
			"seeking_talons",
			() -> new ModifiableMobEffect(MobEffectCategory.BENEFICIAL, 0x0, false)
	);
	public static Holder<MobEffect> INVIGORATE = AD_MOB_EFFECTS.register(
			"invigorate",
			() -> new ModifiableMobEffect(MobEffectCategory.BENEFICIAL, 0x0, false)
	);

	public static Holder<MobEffect> PHASE_OUT = AD_MOB_EFFECTS.register(
			"phase_out",
			() -> new ModifiableMobEffect(MobEffectCategory.NEUTRAL, 0x0, false)
	);
	public static Holder<MobEffect> UNREALITY = AD_MOB_EFFECTS.register(
			"unreality",
			() -> new ModifiableMobEffect(MobEffectCategory.NEUTRAL, 0x0, false)
	);

	private static class ModifiableMobEffect extends MobEffect{
		private final boolean uncurable;

		protected ModifiableMobEffect(MobEffectCategory type, int color, boolean uncurable){
			super(type, color);
			this.uncurable = uncurable;
		}

		@Override
		public void fillEffectCures(@NotNull Set<EffectCure> cures, @NotNull MobEffectInstance effectInstance) {
			if (uncurable) {
				cures.clear();
			} else {
				super.fillEffectCures(cures, effectInstance);
			}
		}
	}
}
