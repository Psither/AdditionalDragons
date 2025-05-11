package by.psither.dragonsurvival.registry.datagen.loot;

import by.psither.dragonsurvival.registry.ADItems;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class MarrowLootModifier extends LootModifier {
    public static final Supplier<MapCodec<MarrowLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, MarrowLootModifier::new)));

    public MarrowLootModifier(LootItemCondition[] conditionsIn) { super(conditionsIn); }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        Entity attacker = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        if (attacker instanceof LivingEntity living) {
            int lootingLevel = 0;
            if (context.getLevel().registryAccess().registry(Registries.ENCHANTMENT).isPresent()) {
                lootingLevel = EnchantmentHelper.getTagEnchantmentLevel(
                        context.getLevel().registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(Enchantments.LOOTING),
                        living.getWeaponItem()
                );
            }
            int lootingRoll = lootingLevel < 1 ? 1 : context.getRandom().nextInt(lootingLevel);
            generatedLoot.add(new ItemStack(ADItems.CURSED_MARROW, lootingRoll));
            ArrayList<ItemStack> bones = new ArrayList<>();
            for (ItemStack itemStack : generatedLoot) {
                if (itemStack.is(Items.BONE)) {
                    generatedLoot.add(new ItemStack(ADItems.CURSED_MARROW, itemStack.getCount()));
                    bones.add(itemStack);
                }
            }
            generatedLoot.removeAll(bones);
        }
        return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
