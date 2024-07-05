package by.psither.dragonsurvival.registry.datagen.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarrowLootModifier extends LootModifier {
    public static final MapCodec<MarrowLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(glm -> glm.conditions),
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("table").forGetter(MarrowLootModifier::table),
            ResourceKey.codec(Registries.LOOT_TABLE).listOf().fieldOf("tables_to_apply").forGetter(MarrowLootModifier::tablesToApply))
                .apply(instance, MarrowLootModifier::new));

    private final ResourceKey<LootTable> table;
    private final List<ResourceKey<LootTable>> tablesToApply;

    public MarrowLootModifier(LootItemCondition[] conditionsIn, ResourceKey<LootTable> table, List<ResourceKey<LootTable>> lootTables) {
        super(conditionsIn);
        this.table = table;
        this.tablesToApply = lootTables;
    }

    public ResourceKey<LootTable> table() {
        return this.table;
    }

    public List<ResourceKey<LootTable>> tablesToApply() {
        return this.tablesToApply;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        /*AtomicBoolean shouldApply = new AtomicBoolean(false);
        for(ResourceKey<LootTable> table : this.tablesToApply) {
            context.getResolver().get(Registries.LOOT_TABLE, table).flatMap(Holder.Reference::unwrapKey).ifPresent(key -> {
                if (key.location().equals(context.getQueriedLootTableId())) {
                    shouldApply.set(true);
                }
            });
        }

        if(!shouldApply.get()) {
            return generatedLoot;
        }*/

        context.getResolver().get(Registries.LOOT_TABLE, this.table).ifPresent(extraTable -> {
            // Don't run loot modifiers for subtables;
            // the added loot will be modifiable by downstream loot modifiers modifying the target table,
            // so if we modify it here then it could get modified twice.
            extraTable.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
        });
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
