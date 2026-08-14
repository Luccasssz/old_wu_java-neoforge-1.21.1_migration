package functionhook.oldwu.item;

import functionhook.oldwu.Old_Wu_java;
import functionhook.oldwu.block.ModBlocks;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** NeoForge item and creative-tab registrations. */
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Old_Wu_java.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Old_Wu_java.MOD_ID);

    public static final DeferredHolder<Item, PaperRollItem> PAPER_ROLL =
            ITEMS.register("paper_roll", () -> new PaperRollItem(new Item.Properties().stacksTo(67)));

    public static final DeferredHolder<Item, Item> DAGOUJIAO =
            ITEMS.register("dagoujiao", () -> new Item(new Item.Properties().stacksTo(64)));

    public static final DeferredHolder<Item, GounaiItem> GOUNAI =
            ITEMS.register("gounai", () -> new GounaiItem(new Item.Properties()
                    .stacksTo(64)
                    .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                    .component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(6).saturationModifier(0.5F).alwaysEdible().build())
                    .component(DataComponents.LORE, new ItemLore(List.of(
                            Component.literal("保质期：永久"),
                            Component.literal("就连时间也惧怕它的存在"))))));

    public static final DeferredHolder<Item, ChunqiuChangItem> CHUNQIU_CHANG =
            ITEMS.register("chunqiu_chang", () -> new ChunqiuChangItem(new Item.Properties()
                    .stacksTo(64)
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5F).alwaysEdible()
                            .effect(new MobEffectInstance(MobEffects.POISON, 60, 0), 1.0F)
                            .effect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 1.0F)
                            .build())
                    .component(DataComponents.LORE, new ItemLore(List.of(
                            Component.literal("生产日期：2018/1/1"),
                            Component.literal("保质期：2008/1/1"))))));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB =
            CREATIVE_MODE_TABS.register("old_wu", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.old_wu_java.old_wu"))
                    .icon(() -> new ItemStack(PAPER_ROLL.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(PAPER_ROLL.get());
                        output.accept(DAGOUJIAO.get());
                        output.accept(GOUNAI.get());
                        output.accept(CHUNQIU_CHANG.get());
                        output.accept(ModBlocks.MIRROR_ITEM.get());
                    })
                    .build());

    private ModItems() {
    }
}
