package by.psither.dragonsurvival.common.dragon_types.types;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.ServerConfig;
import by.dragonsurvivalteam.dragonsurvival.magic.DragonAbilities;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ActiveDragonAbility;
import by.psither.dragonsurvival.config.ADServerConfig;
import by.psither.dragonsurvival.magic.abilities.Astral.passive.DiffusionAbility;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

public class AstralDragonType extends AbstractDragonType {
    private static final ResourceLocation ASTRAL_FOOD = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/astral_food_icons.png");
    private static final ResourceLocation ASTRAL_MANA = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/astral_magic_icons.png");

    public int ticksSinceFoodGenerated, digestingFoodIntoManaTicks, charges;

    public AstralDragonType() { slotForBonus = 0; }

    public static boolean isCharged(Player player){
        DragonStateHandler handler = DragonStateProvider.getOrGenerateHandler(player);
        if (handler.getType() instanceof AstralDragonType astral) {
            return astral.charges > 0;
        }
        return false;
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ticksSinceFoodGenerated", ticksSinceFoodGenerated);
        tag.putInt("digestingFoodIntoManaTicks", digestingFoodIntoManaTicks);
        tag.putInt("charges", charges);
        return tag;
    }

    @Override
    public void readNBT(CompoundTag compoundTag) {
        ticksSinceFoodGenerated = compoundTag.getInt("ticksSinceFoodGenerated");
        digestingFoodIntoManaTicks = compoundTag.getInt("digestingFoodIntoManaTicks");
        charges = compoundTag.getInt("charges");
    }

    public void eatFood(FoodProperties foodProperties) {
        this.digestingFoodIntoManaTicks += ((int) (foodProperties.nutrition() + foodProperties.saturation())) * 100;
    }

    public float onPlayerDamaged(Player player, float damage) {
        DiffusionAbility diffusion = DragonAbilities.getSelfAbility(player, DiffusionAbility.class);
        if (!DiffusionAbility.diffusion || diffusion.getLevel() == 0) {
            return damage;
        }
        float newDamage = diffusion.getNewDamage(damage);
        int cooldown = diffusion.getCooldownAmount(damage - newDamage);
        for (ActiveDragonAbility ability : DragonAbilities.ACTIVE_ABILITIES.get(this.getTypeName())) {
            if (DragonAbilities.hasAbility(player, ability.getClass())) {
                ActiveDragonAbility abil = DragonAbilities.getAbility(player, ability.getClass());
                abil.setCurrentCooldown(abil.getCurrentCooldown() + cooldown);
            }
        }
        return newDamage;
    }

    @Override
    public void onPlayerUpdate(Player player, DragonStateHandler dragonStateHandler) {
        if (!player.isSpectator()) {
            if (!player.level().isClientSide()) {
                if (ServerConfig.penaltiesEnabled && (!player.isInLiquid() && !player.isInPowderSnow && !player.isInWaterRainOrBubble() && player.isFree(player.getX(), player.getY(), player.getZ()))) {
                    ticksSinceFoodGenerated += player.level().canSeeSky(player.getOnPos()) ? 2 : 1;
                } else {
                    ticksSinceFoodGenerated = 0;
                    player.getFoodData().addExhaustion((float) (double) ADServerConfig.VOID_BODY_DRAIN);
                }
            }

            if (!player.level().isClientSide()) {
                if (digestingFoodIntoManaTicks > 0) {
                    digestingFoodIntoManaTicks -= 1;
                    player.getFoodData().addExhaustion((float) (double) ADServerConfig.VOID_BODY_FOOD_DRAIN);
                } else {
                    digestingFoodIntoManaTicks = 0;
                }
            }

            if (ticksSinceFoodGenerated >= ADServerConfig.ASTRAL_FOOD_TICK_FREQUENCY) {
                if (!player.level().isClientSide()) {
                    ticksSinceFoodGenerated -= ADServerConfig.ASTRAL_FOOD_TICK_FREQUENCY;
                    FoodData foodData = player.getFoodData();
                    foodData.eat(1, foodData.getFoodLevel() >= 20 ? 1f : 0.5f);
                }
            }
        }
    }

    @Override
    public boolean isInManaCondition(Player player, DragonStateHandler dragonStateHandler) {
        return digestingFoodIntoManaTicks > 0;
    }

    @Override
    public void onPlayerDeath() {
        ticksSinceFoodGenerated = 0;
        digestingFoodIntoManaTicks = 0;
    }

    @Override
    public List<Pair<ItemStack, FoodData>> validFoods(Player player, DragonStateHandler dragonStateHandler) {
        return List.of();
    }

    @Override
    public List<TagKey<Block>> mineableBlocks() {
        return List.of(BlockTags.SWORD_EFFICIENT, BlockTags.MINEABLE_WITH_HOE);
    }

    @Override
    public ResourceLocation getFoodIcons() {
        return ASTRAL_FOOD;
    }

    @Override
    public ResourceLocation getManaIcons() {
        return ASTRAL_MANA;
    }

    @Override
    public String getTypeName() {
        return "astral";
    }
}
