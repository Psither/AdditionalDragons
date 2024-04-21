package by.psither.dragonsurvival.registry;

import java.util.Collections;
import java.util.List;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.effects.BlastDustedEffect;
import by.psither.dragonsurvival.common.effects.BubbleShieldEffect;
import by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active.PyroclasticRoarAbility;
import by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active.UnstoppableAbility;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ADDragonEffects {
	
	public static MobEffect BUBBLE_SHIELD;
	public static MobEffect HIGH_VOLTAGE;
	public static MobEffect BLAST_DUSTED;
	public static MobEffect UNSTOPPABLE;
	public static MobEffect VOLCANIC_RAGE;
	public static MobEffect CONFOUNDED;
	public static MobEffect SEEKING_TALONS;
	public static MobEffect INVIGORATE;

	@SubscribeEvent
	public static void registerEffects(RegisterEvent event){
		if (!event.getRegistryKey().equals(Registry.MOB_EFFECT_REGISTRY))
			return;
		BUBBLE_SHIELD = registerMobEffect(event, "bubble_shield", new BubbleShieldEffect(MobEffectCategory.BENEFICIAL, 0x0));
		HIGH_VOLTAGE = registerMobEffect(event, "high_voltage", new Effect2(MobEffectCategory.BENEFICIAL, 0x0, false));
		BLAST_DUSTED = registerMobEffect(event, "blast_dusted", new BlastDustedEffect(MobEffectCategory.HARMFUL, 0x0));
		VOLCANIC_RAGE = registerMobEffect(event, "volcanic_rage", new Effect2(MobEffectCategory.BENEFICIAL, 0x0, false))
			.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "cc2e6834-8068-45ee-b62e-f85ff588a630", PyroclasticRoarAbility.volcanicRageArmorToughnessValue, Operation.ADDITION)
			.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, "7e51f383-8e38-4aef-b8b3-19a857702da5", PyroclasticRoarAbility.volcanicRageAttackKnockbackValue, Operation.ADDITION)
			.addAttributeModifier(Attributes.ATTACK_DAMAGE, "cc2e6834-8068-45ee-b62e-f85ff588a630", PyroclasticRoarAbility.volcanicRageAttackDamageValue, Operation.ADDITION);
		UNSTOPPABLE = registerMobEffect(event, "unstoppable", new Effect2(MobEffectCategory.BENEFICIAL, 0x0, false))
			.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "11214017-ea47-47dd-8fd8-1c21635d93e6", UnstoppableAbility.unstoppableKnockbackResistValue, Operation.ADDITION);
		CONFOUNDED = registerMobEffect(event, "confounded", new Effect2(MobEffectCategory.HARMFUL, 0x0, false));
		SEEKING_TALONS = registerMobEffect(event, "seeking_talons", new Effect2(MobEffectCategory.BENEFICIAL, 0x0, false));
		INVIGORATE = registerMobEffect(event, "invigorate", new Effect2(MobEffectCategory.BENEFICIAL, 0x0, false));
	}

	protected static MobEffect registerMobEffect(RegisterEvent event, String identity, MobEffect mobEffect)
	{
		event.register(Registry.MOB_EFFECT_REGISTRY, new ResourceLocation(AdditionalDragonsMod.MODID, identity), ()->mobEffect);
		return mobEffect;
	}

	private static class Effect2 extends MobEffect{
		private final boolean uncurable;

		protected Effect2(MobEffectCategory type, int color, boolean uncurable){
			super(type, color);
			this.uncurable = uncurable;
		}

		@Override
		public List<ItemStack> getCurativeItems(){
			return uncurable ? Collections.emptyList() : super.getCurativeItems();
		}
	}

}
