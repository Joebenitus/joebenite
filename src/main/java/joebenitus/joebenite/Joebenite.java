package joebenitus.joebenite;

import joebenitus.joebenite.PickaxeBase;
import joebenitus.joebenite.ToolMaterialJoebenite;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class Joebenite implements ModInitializer {

	public static final Item JOEBENITE = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

	public static final Block BLOCK_OF_JOEBENITE = new Block(FabricBlockSettings.of(Material.METAL).strength(5, 6).sounds(BlockSoundGroup.METAL).breakByTool(FabricToolTags.PICKAXES).requiresTool());

	public static final Block JOEBENITE_ORE = new JoebeniteOreBlock(FabricBlockSettings.copy(Blocks.STONE));

	public static final Item JOEBENITE_PICKAXE = new Item(new Item.Settings().group(ItemGroup.TOOLS));

	private static ConfiguredFeature<?, ?> JOEBENITE_ORE_OVERWORLD = Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, JOEBENITE_ORE.getDefaultState(), 4)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 20))).spreadHorizontally().repeat(1);

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite"), JOEBENITE);
		Registry.register(Registry.BLOCK, new Identifier("joebenite", "block_of_joebenite"), BLOCK_OF_JOEBENITE);
		Registry.register(Registry.ITEM, new Identifier("joebenite", "block_of_joebenite"), new BlockItem(BLOCK_OF_JOEBENITE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

		Registry.register(Registry.BLOCK, new Identifier("joebenite", "joebenite_ore"), JOEBENITE_ORE);
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_ore"), new BlockItem(JOEBENITE_ORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

		//TOOLS

		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_pickaxe"), new PickaxeBase(new ToolMaterialJoebenite()));


		RegistryKey<ConfiguredFeature<?,?>> joebeniteOreOverworld = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("joebenite", "joebenite_ore"));

		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, joebeniteOreOverworld.getValue(), JOEBENITE_ORE_OVERWORLD);

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, joebeniteOreOverworld);

		System.out.println("Hello Fabric world!");
	}
}
