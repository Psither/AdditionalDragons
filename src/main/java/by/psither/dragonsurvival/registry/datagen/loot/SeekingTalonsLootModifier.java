package by.psither.dragonsurvival.registry.datagen.loot;

import by.psither.dragonsurvival.registry.ADDragonEffects;
import by.psither.dragonsurvival.registry.ADItems;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SeekingTalonsLootModifier extends LootModifier {
    public static final Supplier<MapCodec<SeekingTalonsLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, SeekingTalonsLootModifier::new)));

    public SeekingTalonsLootModifier(LootItemCondition[] conditionsIn) { super(conditionsIn); }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        Entity attacker = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        if (attacker instanceof LivingEntity living) {
            int talonBonus = 0;
            if (living.hasEffect(ADDragonEffects.SEEKING_TALONS)) {
                talonBonus = living.getEffect(ADDragonEffects.SEEKING_TALONS).getAmplifier() + 1;
            }
            talonBonus = talonBonus < 1 ? 1 : context.getRandom().nextInt(talonBonus);
            for (ItemStack itemStack : generatedLoot) {
                itemStack.setCount(itemStack.getCount() + talonBonus);
            }
        }
        return generatedLoot;
    }


    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
