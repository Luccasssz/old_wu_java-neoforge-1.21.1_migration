package functionhook.oldwu.block;

import functionhook.oldwu.Old_Wu_java;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 方块注册。目前包含“镜子”方块（硬度 1、可空手挖掘）。
 */
public final class ModBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Old_Wu_java.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Old_Wu_java.MOD_ID);

	public static final DeferredBlock<MirrorBlock> MIRROR = BLOCKS.register(
		"mirror",
		() -> new MirrorBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_LIGHT_GRAY)
			.strength(1.0F)
			.sound(SoundType.GLASS)
			.noCollission()
			.noOcclusion())
	);

	public static final DeferredItem<BlockItem> MIRROR_ITEM = ITEMS.registerSimpleBlockItem("mirror", MIRROR);

	private ModBlocks() {
	}
}
