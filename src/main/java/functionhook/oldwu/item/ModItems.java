package functionhook.oldwu.item;

import functionhook.oldwu.Old_Wu_java;
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

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB =
            CREATIVE_MODE_TABS.register("old_wu", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.old_wu_java.old_wu"))
                    .icon(() -> new ItemStack(PAPER_ROLL.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(PAPER_ROLL.get());
                        output.accept(DAGOUJIAO.get());
                    })
                    .build());

    private ModItems() {
    }
}
