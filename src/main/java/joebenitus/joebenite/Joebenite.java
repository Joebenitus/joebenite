package joebenitus.joebenite;

import java.util.function.Function;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

public class Joebenite implements ModInitializer {

	public static final Item JOEBENITE = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

	public static final Block BLOCK_OF_JOEBENITE = new Block(FabricBlockSettings.of(Material.METAL).strength(5, 6).sounds(BlockSoundGroup.METAL).breakByTool(FabricToolTags.PICKAXES).requiresTool());

	public static final Block JOEBENITE_ORE = new JoebeniteOreBlock(FabricBlockSettings.copy(Blocks.STONE));

	public static final ArmorMaterial JOEBENITE_ARMOR = new ArmorMaterialJoebenite();

	public static Block JUICE = null;

	public static FlowableFluid STILL_JUICE = null;

	public static FlowableFluid FLOWING_JUICE = null;

	public static final Item JUICE_BUCKET = new BucketItem(STILL_JUICE, new Item.Settings().group(ItemGroup.MISC).maxCount(1));

	private static ConfiguredFeature<?, ?> JOEBENITE_ORE_OVERWORLD = Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, JOEBENITE_ORE.getDefaultState(), 4)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 20))).spreadHorizontally().repeat(1);

	public static ConfiguredFeature<?, ?> JUICE_LAKE = Feature.LAKE.configure(new SingleStateFeatureConfig(JUICE.getDefaultState()))
	.decorate(Decorator.WATER_LAKE.configure(new ChanceDecoratorConfig(40)));

	@Override
	public void onInitialize() {

		//ITEMS
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite"), JOEBENITE);
		
		//BLOCKS

		Registry.register(Registry.BLOCK, new Identifier("joebenite", "block_of_joebenite"), BLOCK_OF_JOEBENITE);
		Registry.register(Registry.ITEM, new Identifier("joebenite", "block_of_joebenite"), new BlockItem(BLOCK_OF_JOEBENITE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

		Registry.register(Registry.BLOCK, new Identifier("joebenite", "joebenite_ore"), JOEBENITE_ORE);
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_ore"), new BlockItem(JOEBENITE_ORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

		//TOOLS

		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_pickaxe"), new PickaxeBase(new ToolMaterialJoebenite()));
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_sword"), new SwordBase(new ToolMaterialJoebenite()));
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_axe"), new AxeBase(new ToolMaterialJoebenite()));
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_shovel"), new ShovelBase(new ToolMaterialJoebenite()));
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_hoe"), new HoeBase(new ToolMaterialJoebenite()));

		//ARMOR

		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_helmet"), new ArmorBase(JOEBENITE_ARMOR, EquipmentSlot.HEAD));
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_chestplate"), new ArmorBase(JOEBENITE_ARMOR, EquipmentSlot.CHEST));
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_leggings"), new ArmorBase(JOEBENITE_ARMOR, EquipmentSlot.LEGS));
		Registry.register(Registry.ITEM, new Identifier("joebenite", "joebenite_boots"), new ArmorBase(JOEBENITE_ARMOR, EquipmentSlot.FEET ));

		//FLUID

		STILL_JUICE = Registry.register(Registry.FLUID, new Identifier("joebenite", "juice"), new FluidJuice.Still());

		FLOWING_JUICE = Registry.register(Registry.FLUID, new Identifier("joebenite", "flowing_juice"), new FluidJuice.Flowing());

		JUICE = Registry.register(Registry.BLOCK, new Identifier("joebenite", "acid"), new FluidBlock(STILL_JUICE, FabricBlockSettings.copy(Blocks.WATER)){});

		//JUICE_LAKE = Registry.register(Registry.FEATURE, new Identifier("joebenite", "juice_lake"), new LakeFeature(SingleStateFeatureConfig::deserialize));

		//FLUID RENDERING	

		setupFluidRendering(STILL_JUICE, FLOWING_JUICE, new Identifier("minecraft", "water"), 0x4CC248);
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), STILL_JUICE, FLOWING_JUICE);

		//WORLDGEN

		RegistryKey<ConfiguredFeature<?,?>> joebeniteOreOverworld = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("joebenite", "joebenite_ore"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, joebeniteOreOverworld.getValue(), JOEBENITE_ORE_OVERWORLD);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, joebeniteOreOverworld);


		//BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.LOCAL_MODIFICATIONS, JUICE_LAKE
	//);


		System.out.println("Hello Fabric world!");
	}

	public static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier textureFluidId, final int color)
	{
		final Identifier stillSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
		final Identifier flowingSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");

		// If they're not already present, add the sprites to the block atlas
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) ->
		{
			registry.register(stillSpriteId);
			registry.register(flowingSpriteId);
		});

		final Identifier fluidId = Registry.FLUID.getId(still);
		final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");

		final Sprite[] fluidSprites = { null, null };

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener()
		{
			@Override
			public Identifier getFabricId()
			{
				return listenerId;
			}

			/**
			 * Get the sprites from the block atlas when resources are reloaded
			 */
			@Override
			public void apply(ResourceManager resourceManager)
			{
				final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
				fluidSprites[0] = atlas.apply(stillSpriteId);
				fluidSprites[1] = atlas.apply(flowingSpriteId);
			}
		});

		// The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
		final FluidRenderHandler renderHandler = new FluidRenderHandler()
		{
			@Override
			public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state)
			{
				return fluidSprites;
			}

			@Override
			public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state)
			{
				return color;
			}
		};

		FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
		FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
	}

}
