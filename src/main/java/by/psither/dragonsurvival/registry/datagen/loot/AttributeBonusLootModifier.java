package by.psither.dragonsurvival.registry.datagen.loot;

import by.psither.dragonsurvival.registry.ADAttributes;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AttributeBonusLootModifier extends LootModifier {
    public static final Supplier<MapCodec<AttributeBonusLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, AttributeBonusLootModifier::new)));

    public AttributeBonusLootModifier(LootItemCondition[] conditionsIn) { super(conditionsIn); }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        Entity attacker = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        if (attacker instanceof LivingEntity living) {
            AttributeInstance bonusLoot = living.getAttribute(ADAttributes.BONUS_LOOT);
            int bonus = bonusLoot != null ? (int) bonusLoot.getValue() : 0;
            if (bonus > 0) {
                for (ItemStack itemStack : generatedLoot) {
                    int bonusLootInStack = context.getRandom().nextInt(bonus);
                    itemStack.setCount(itemStack.getCount() + bonusLootInStack);
                }
            }
        }
        return generatedLoot;
    }


    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}