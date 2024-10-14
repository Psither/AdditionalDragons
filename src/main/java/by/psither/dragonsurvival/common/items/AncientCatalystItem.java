package by.psither.dragonsurvival.common.items;

import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.CaveDragon.LargeFireParticle;
import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.ForestDragon.SmallPoisonParticle;
import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.SeaDragon.LargeLightningParticle;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.capability.subcapabilities.MagicCap;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.magic.common.passive.PassiveDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.network.syncing.SyncComplete;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.utils.MathUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class AncientCatalystItem extends Item {
	private String descriptionId = "item.additionaldragons.ancient_catalyst";

	public AncientCatalystItem(Properties properties) {
		super(properties);
		properties.stacksTo(1);
		properties.rarity(Rarity.EPIC);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext pTooltip, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag){
		super.appendHoverText(stack, pTooltip, list, tooltipFlag);
		list.add(Component.translatable("ad.description.ancient_catalyst"));
	}

	protected String getOrCreateDescriptionId() {
		if (this.descriptionId == null) {
			this.descriptionId = Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this));
		}

		return this.descriptionId;
	}

	public String getDescriptionId() {
		return this.getOrCreateDescriptionId();
	}

	public String getDescriptionId(ItemStack p_41455_) {
		return this.getDescriptionId();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		DragonStateHandler handler = DragonStateProvider.getOrGenerateHandler(player);

		if(handler.altarCooldown > 0){
			if(player.level().isClientSide){
				//Show the current cooldown in minutes and seconds in cases where the cooldown is set high in the config
				int mins = (int) (Functions.ticksToMinutes(handler.altarCooldown));
				int secs = (int) (Functions.ticksToSeconds(handler.altarCooldown - Functions.minutesToTicks(mins)));
				player.sendSystemMessage(Component.translatable("ds.cooldown.active", (mins > 0 ? mins + "m" : "") + secs + (mins > 0 ? "s" : "")));
			}
			return InteractionResultHolder.fail(itemstack);
		} else {
			if(!transformToAncientType(player))
				return InteractionResultHolder.fail(itemstack);

			if (!player.getAbilities().instabuild) {
				itemstack.shrink(1);
			}
			return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
		}
	}

	public static AbstractDragonType getNewType(Player player) {
		AbstractDragonType type = DragonUtils.getDragonType(player);
		if (type == null) return null;
        return switch (type.getSubtypeName()) {
            case "sea" -> ADDragonTypes.PRIMORDIAL;
            case "cave" -> ADDragonTypes.TECTONIC;
            case "forest" -> ADDragonTypes.DEEPWOODS;
            case "primordial" -> DragonTypes.SEA;
            case "tectonic" -> DragonTypes.CAVE;
            case "deepwoods" -> DragonTypes.FOREST;
            default -> null;
        };
	}
	
	private static void showParticles(Player player, ParticleOptions particle) {
		RandomSource random = player.getRandom();
		for (int i = 0; i < 40; i++) {
			Vector3f vec = MathUtils.randomPointInSphere(2F, random);
			player.level().addAlwaysVisibleParticle(particle, player.getX() + vec.x(), player.getY() + vec.y(), player.getZ() + vec.z(), 0, 0, 0);
		}
	}

	private static boolean transformToAncientType(Player player) {
		if (player == null) return false;
		AbstractDragonType type = getNewType(player);
		if (type == null) return false;
		DragonStateHandler cap = DragonStateProvider.getOrGenerateHandler(player);
		MagicCap mc = cap.getMagicData();
		List<PassiveDragonAbility> passives = new ArrayList<PassiveDragonAbility>();
		for (int i = 0; i < MagicCap.passiveAbilitySlots; i++) {
			passives.add(mc.getPassiveAbilityFromSlot(i));
		}

		cap.setType(type);
		for (int i = 0; i < MagicCap.passiveAbilitySlots; i++) {
			mc.getPassiveAbilityFromSlot(i).setLevel(passives.get(i).getLevel());
		}

		if (player.level().isClientSide()) {
			player.sendSystemMessage(Component.translatable("ds." + type.getSubtypeName().toLowerCase() + "_dragon_choice"));
			switch (type.getTypeName().toLowerCase()) {
			case "sea":
				//player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 1.2F, false);
				player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.PLAYERS, 0.8F, 0.9F, false);
				showParticles(player, new LargeLightningParticle.Data(37, false));
				break;
			case "cave":
				//player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 1.2F, false);
				player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 4.0F, 1.2F, false);
				showParticles(player, new LargeFireParticle.Data(37, false));
				break;
			case "forest":
				player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 1.2F, false);
				showParticles(player, new SmallPoisonParticle.Data(37, false));
				break;
			}
		}
		else {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new SyncComplete.Data(player.getId(), cap.serializeNBT(player.registryAccess())));
			player.refreshDimensions();
		}
		return true;
	}
}
