package by.psither.dragonsurvival.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active.BubbleShieldAbility;

public class BubbleShieldEffect extends MobEffect {
	private Double health;

	public BubbleShieldEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
		this.health = BubbleShieldAbility.bubbleShieldStrength;
	}

	public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
		pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount() - (float)(BubbleShieldAbility.bubbleShieldStrength * (pAmplifier + 1)));
		super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
	}

	public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
		pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount() + (float)(BubbleShieldAbility.bubbleShieldStrength * (pAmplifier + 1)));
		super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
	}
	
	public double getHealth() {
		return this.health;
	}

	public void takeDamage(float damage) {
		this.health -= damage;
	}
}
