package by.psither.dragonsurvival.commands;

import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.subcapabilities.MagicCap;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.magic.DragonAbilities;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ActiveDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.network.NetworkHandler;
import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncMagicCap;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class DragonAbilitySwapCommand {
	static String[] primordial = {"luminous_breath", "group_heal", "bubble_shield", "high_voltage"};
	static String[] sea = {"storm_breath", "ball_lightning", "revealing_the_soul", "sea_eyes"};
	static String[] forest = {"forest_breath", "spike", "inspiration", "hunter"};
	static String[] cave = {"nether_breath", "fireball", "strong_leather", "lava_vision"};
	static String[] abilityGroups = {"primordial", "sea", "forest", "cave"};

	public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher){
		LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = literal("dragon-ability")
		.then(literal("one").then(
					argument("abilityName", StringArgumentType.string()).executes(context -> {
						return changeAbility(context.getSource(), context.getArgument("abilityName", String.class));
					})
				)
		).then(literal("all").then(
					argument("abilitySet", StringArgumentType.string()).executes(context -> {
						return changeAbilitySet(context.getSource(), context.getArgument("abilitySet", String.class));
					})
				)
		).then(literal("test").executes(context -> {
											return displayInfo(context.getSource());
										})
		);

		commandDispatcher.register(literalargumentbuilder.requires(context -> { return context.hasPermission(2); }));
	}
	
	private static int displayInfo(CommandSourceStack pSource) {
		ServerPlayer serverPlayer = pSource.getPlayer();
		DragonStateHandler cap = DragonUtils.getHandler(serverPlayer);

		System.out.println(DragonAbilities.ABILITIES);
		MagicCap mc = cap.getMagicData();
		System.out.println(mc.abilities);
		return 0;
	}

	private static int changeAbilitySet(CommandSourceStack pSource, String abilitySetName) {
		String[] abilities = {};
		switch (abilitySetName) {
			case "sea":
				abilities = sea;
				break;
			case "primordial":
				abilities = primordial;
				break;
			case "forest":
				abilities = forest;
				break;
			case "cave":
				abilities = cave;
				break;
		}
		DragonUtils.getHandler(pSource.getPlayer()).getType();
		int c = 0;
		for (int i = 0; i < abilities.length; i++) {
			c += changeAbility(pSource, abilities[i]);
		}
		return c;
	}

	private static int changeAbility(CommandSourceStack pSource, String abilityName) {
		ServerPlayer serverPlayer = pSource.getPlayer();
		DragonStateHandler cap = DragonUtils.getHandler(serverPlayer);
		MagicCap mc = cap.getMagicData();

		if (mc.abilities.get(abilityName) instanceof ActiveDragonAbility) {
			boolean res = replaceAbility((Player) serverPlayer, mc, (ActiveDragonAbility) mc.abilities.get(abilityName));
			if (!res) {
				//pSource.sendSuccess(Component.translatable("ds.commands.ability_swap.success"), true);
			} else {
				pSource.sendFailure(Component.translatable("ds.commands.ability_swap.same"));
			}
	        return 0;
		}
		else {
			pSource.sendFailure(Component.translatable("ds.commands.ability_swap.failure"));
			return 1;
		}
	}
	
	private static boolean replaceAbility(Player player, MagicCap mc, ActiveDragonAbility ability) {
		int slot = ability.getSortOrder() - 1;
		if (mc.getAbilityFromSlot(slot) == ability) {
			return false;
		}
		if (!mc.abilities.containsKey(ability.getName())) {
			mc.abilities.put(ability.getName(), ability);
		}
		mc.activeDragonAbilities.put(slot, ability.getName());
		
		if(player.level().isClientSide){
			NetworkHandler.CHANNEL.sendToServer(new SyncMagicCap(player.getId(), mc));
		}else{
			NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncMagicCap(player.getId(), mc));
		}
		return true;
	}
}
