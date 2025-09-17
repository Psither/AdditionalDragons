package by.psither.dragonsurvival.registry;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvival;
import by.dragonsurvivalteam.dragonsurvival.common.codecs.DragonAbilityHolder;
import by.dragonsurvivalteam.dragonsurvival.registry.data_components.DSDataComponents;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.DragonSpecies;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbility;
import by.psither.dragonsurvival.items.AncientCatalystItem;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static by.psither.dragonsurvival.AdditionalDragonsMod.*;


public class ADItems {
	public static DeferredRegister<Item> REGISTRY = DeferredRegister.create(
			BuiltInRegistries.ITEM,
			MODID
	);

	public static final Holder<Item> SLIPPERY_SUSHI = REGISTRY.register(
			"slippery_sushi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.75f).effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 400), 1f).build())) {
		@Override
		public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag){
			super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
			pTooltipComponents.add(Component.translatable("item.additionaldragons.slippery_sushi.desc"));
		}
	});
	public static final Holder<Item> CURSED_MARROW = REGISTRY.register(
			"cursed_marrow", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.8f).fast().effect(() -> new MobEffectInstance(ADEffects.CONFOUNDED, 400, 1), 1).build())) {
				@Override
				public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag){
					super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
					pTooltipComponents.add(Component.translatable("item.additionaldragons.cursed_marrow.desc"));
				}
			});

	public static final Holder<Item> ANCIENT_CATALYST_EMPTY = REGISTRY.register("ancient_catalyst_empty", () -> new AncientCatalystItem(
			new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)
	) {
		@Override
		public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag){
			super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
			pTooltipComponents.add(Component.translatable("item.additionaldragons.ancient_catalyst_empty.desc"));
		}
	});

	public static final Holder<Item> ANCIENT_CATALYST_HUMAN = REGISTRY.register("ancient_catalyst_human", () -> new AncientCatalystItem(
			new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)
	) {
		@Override
		public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag){
			super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
			pTooltipComponents.add(Component.translatable("item.additionaldragons.ancient_catalyst_human.desc"));
		}
	});

	public static final Holder<Item> ANCIENT_CATALYST_DEEPWOODS = REGISTRY.register("ancient_catalyst_deepwoods", () -> new AncientCatalystItem(
			new Item.Properties().component(
					DSDataComponents.DRAGON_ABILITIES,
					new DragonAbilityHolder(
							HolderSet.direct(
									DeferredHolder.create(DragonAbility.REGISTRY, res("misty_barb")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("confounding_breath")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("invigorate")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("seeking_talons"))
							),
							Optional.of(HolderSet.direct(DeferredHolder.create(ResourceKey.create(DragonSpecies.REGISTRY, DragonSurvival.res("forest_dragon"))))),
					false)
			).stacksTo(1).rarity(Rarity.EPIC)
	) {
		@Override
		public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag){
			super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
			pTooltipComponents.add(Component.translatable("item.additionaldragons.ancient_catalyst_deepwoods.desc"));
		}
	});

	public static final Holder<Item> ANCIENT_CATALYST_PRIMORDIAL = REGISTRY.register("ancient_catalyst_primordial", () -> new AncientCatalystItem(
			new Item.Properties().component(
					DSDataComponents.DRAGON_ABILITIES,
					new DragonAbilityHolder(
							HolderSet.direct(
									DeferredHolder.create(DragonAbility.REGISTRY, res("luminous_breath")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("group_heal")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("high_voltage")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("bubble_shield"))
							),
							Optional.of(HolderSet.direct(DeferredHolder.create(ResourceKey.create(DragonSpecies.REGISTRY, DragonSurvival.res("sea_dragon"))))),
							false)
			).stacksTo(1).rarity(Rarity.EPIC)
	) {
		@Override
		public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag){
			super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
			pTooltipComponents.add(Component.translatable("item.additionaldragons.ancient_catalyst_primordial.desc"));
		}
	});

	public static final Holder<Item> ANCIENT_CATALYST_TECTONIC = REGISTRY.register("ancient_catalyst_tectonic", () -> new AncientCatalystItem(
			new Item.Properties().component(
					DSDataComponents.DRAGON_ABILITIES,
					new DragonAbilityHolder(
							HolderSet.direct(
									DeferredHolder.create(DragonAbility.REGISTRY, res("blast_breath")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("blast_dust_immunity")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("fault_line")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("pyroclastic_roar")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("unstoppable"))
							),
							Optional.of(HolderSet.direct(DeferredHolder.create(ResourceKey.create(DragonSpecies.REGISTRY, DragonSurvival.res("cave_dragon"))))),
							false)
			).stacksTo(1).rarity(Rarity.EPIC)
	) {
		@Override
		public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag){
			super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
			pTooltipComponents.add(Component.translatable("item.additionaldragons.ancient_catalyst_tectonic.desc"));
		}
	});

	public static final Holder<Item> ANCIENT_CATALYST_ASTRAL = REGISTRY.register("ancient_catalyst_astral", () -> new AncientCatalystItem(
			new Item.Properties().component(
					DSDataComponents.DRAGON_ABILITIES,
					new DragonAbilityHolder(
							HolderSet.direct(
									DeferredHolder.create(DragonAbility.REGISTRY, res("wyrmgate_overworld")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("wyrmgate_nether")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("wyrmgate_end")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("gravity_control")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("phase_out")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("personal_wyrmhole"))
							),
							Optional.of(HolderSet.direct(DeferredHolder.create(ResourceKey.create(DragonSpecies.REGISTRY, DragonSurvival.res("astral_dragon"))))),
							false
					)
			)
	) {
		@Override
		public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag){
			super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
			pTooltipComponents.add(Component.translatable("item.additionaldragons.ancient_catalyst_astral.desc"));
		}
	});
}
