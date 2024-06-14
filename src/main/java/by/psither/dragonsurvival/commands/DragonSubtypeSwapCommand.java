package by.psither.dragonsurvival.commands;

import static net.minecraft.commands.Commands.literal;

import java.util.ArrayList;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.config.ServerConfig;
import by.dragonsurvivalteam.dragonsurvival.network.NetworkHandler;
import by.dragonsurvivalteam.dragonsurvival.network.RequestClientData;
import by.dragonsurvivalteam.dragonsurvival.network.flight.SyncSpinStatus;
import by.dragonsurvivalteam.dragonsurvival.network.player.SyncSize;
import by.dragonsurvivalteam.dragonsurvival.network.player.SynchronizeDragonCap;
import by.dragonsurvivalteam.dragonsurvival.network.status.SyncAltarCooldown;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class DragonSubtypeSwapCommand {
	public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher){
		LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = literal("dragon-subtype").then(
			literal("change").then(
				argument("subtype", StringArgumentType.string()).executes(context -> {return changeSubtype(context.getSource(), context.getArgument("subtype", String.class));})
			)
		).then(
			literal("register").executes(context -> {return registerSubtypes(context.getSource());})
		);

		commandDispatcher.register(literalargumentbuilder);
	}
	
	public static int registerSubtypes(CommandSourceStack pSource) {
		System.out.println(DragonTypes.staticSubtypes);
		ADDragonTypes.registerSubtypes();
		System.out.println(DragonTypes.staticSubtypes);
		System.out.println(ADDragonTypes.PRIMORDIAL.getSubtypeName());
		return 0;
	}

	public static int changeSubtype(CommandSourceStack pSource, String subtype) {
		ServerPlayer serverplayer = pSource.getPlayer();
		DragonStateHandler cap = DragonUtils.getHandler(serverplayer);

		System.out.println("subtypes of type: " + DragonTypes.getSubtypesOfType(cap.getTypeName()));
		System.out.println("static subtype: " + DragonTypes.getStaticSubtype(DragonUtils.getDragonType(serverplayer).getTypeName()));
		System.out.println("current dragon type: " + DragonUtils.getDragonType(serverplayer));

		if (DragonTypes.getSubtypesOfType(cap.getTypeName()).contains(DragonTypes.getStaticSubtype(subtype))) {
			System.out.println("changing to: " + DragonTypes.getStaticSubtype(subtype));
			cap.setType(DragonTypes.getStaticSubtype(subtype));

			NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverplayer),new SyncAltarCooldown(serverplayer.getId(), Functions.secondsToTicks(ServerConfig.altarUsageCooldown)));
			NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverplayer),new SynchronizeDragonCap(serverplayer.getId(), cap.isHiding(), cap.getType(), cap.getBody(), cap.getSize(), cap.hasFlight(), 0));
			NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverplayer),new SyncSpinStatus(serverplayer.getId(), cap.getMovementData().spinAttack, cap.getMovementData().spinCooldown, cap.getMovementData().spinLearned));
			NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverplayer), new SyncSize(serverplayer.getId(), cap.getSize()));
			NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverplayer), new RequestClientData(cap.getType(), cap.getBody(), cap.getLevel()));
			serverplayer.refreshDimensions();

			//pSource.sendSuccess(Component.translatable("ds.commands.ability_swap.success"), true);
			System.out.println("new dragon type: " + DragonUtils.getDragonType(serverplayer).getSubtypeName());
			return 1;
		}
		pSource.sendFailure(Component.translatable("ds.commands.ability_swap.failure"));
		return 0;
	}
}
