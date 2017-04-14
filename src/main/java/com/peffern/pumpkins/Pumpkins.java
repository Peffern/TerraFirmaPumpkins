package com.peffern.pumpkins;


import com.bioxx.tfc.Core.Recipes;
import com.bioxx.tfc.Food.ItemFoodTFC;
import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.Render.Item.FoodItemRenderer;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.Enums.EnumFoodGroup;
import com.peffern.crops.BaseCrop;
import com.peffern.crops.CropsRegistry;
import com.peffern.crops.ICrop;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapelessOreRecipe;


/**
 * Mod to make Pumpkins and Melons TFC crops - also mushrooms
 * @author peffern
 *
 */
@Mod(modid = Pumpkins.MODID, name = Pumpkins.MODNAME, version = Pumpkins.VERSION, dependencies = "required-after:" + "terrafirmacraft" + ";" + "required-after:" + "tfccrops" + ";")
public class Pumpkins 
{
	
	/** pumpkin seeds */
	static Item pumpkinSeeds;
	
	/** melon seeds */
	static Item melonSeeds;
	
	/** pumpkin vegetable item */
	static Item pumpkinFood;
	
	/** melon fruit item */
	static Item melonFood;
	
	/** pumpkin cooked item */
	static Item pumpkinFoodCooked;
	
	/**melon block*/
	static Block melon;
		
	/**mushroom*/
	static Item fungi1;
	
	/**mushroom*/
	static Item fungi2;
	
	/** Mod ID String */
	public static final String MODID = "TerraFirmaPumpkins";
	
	/** Mod Name */
	public static final String MODNAME = "TerraFirmaPumpkins";
	
	/** Mod Version */
	public static final String VERSION = "2.1";
	
	static Configuration config;
	
	static boolean enableMushrooms = true;
	
	/**
	 * Configuration
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();
		enableMushrooms = config.get(Configuration.CATEGORY_GENERAL, "enableMushrooms", true, "Set to false to use the normal TFC mushrooms").getBoolean();
		if(config.hasChanged())
			config.save();
	}
	
	/**
	 * Do all the main mod setup
	 */
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		//register the food item
		pumpkinFood = new ItemFoodTFC(EnumFoodGroup.Vegetable, 20, 0, 0, 0, 20, false, false)
		{
			//override the icon registerer to use the pumpkin food sprite in TFC-unused
			@Override
			public void registerIcons(IIconRegister registerer)
			{
				this.itemIcon = registerer.registerIcon(com.bioxx.tfc.Reference.MOD_ID + ":" + "food/unused/" + "img73");
				MinecraftForgeClient.registerItemRenderer(this, new FoodItemRenderer());
			}
		};
		//register item
		pumpkinFood.setUnlocalizedName("Raw Pumpkin");
		GameRegistry.registerItem(pumpkinFood, pumpkinFood.getUnlocalizedName());
		
		//register the food item
		melonFood = new ItemFoodTFC(EnumFoodGroup.Fruit, 20, 20, 0, 0, 0, true)
		{
			//override the icon registerer to use the melon food sprite in TFC-unused
			@Override
			public void registerIcons(IIconRegister registerer)
			{
				this.itemIcon = registerer.registerIcon(com.bioxx.tfc.Reference.MOD_ID + ":" + "food/unused/" + "img74");
				MinecraftForgeClient.registerItemRenderer(this, new FoodItemRenderer());
			}
		};
		//register item
		melonFood.setUnlocalizedName("Melon");
		GameRegistry.registerItem(melonFood, melonFood.getUnlocalizedName());
		
		//register the cooked food item
		pumpkinFoodCooked = new ItemFoodTFC(EnumFoodGroup.Vegetable, 20, 0, 0, 0, 20, true)
		{
			//override the icon registerer
			@Override
			public void registerIcons(IIconRegister registerer)
			{
				this.itemIcon = registerer.registerIcon(MODID + ":" + "food/Cooked Pumpkin");
				MinecraftForgeClient.registerItemRenderer(this, new FoodItemRenderer());
			}
		};
		//register item
		pumpkinFoodCooked.setUnlocalizedName("Cooked Pumpkin");
		GameRegistry.registerItem(pumpkinFoodCooked, pumpkinFoodCooked.getUnlocalizedName());
				
	
		//register the melon block	
		melon = new BlockCustomMelon().setHardness(1.0F).setStepSound(Block.soundTypeWood).setBlockName("Melon").setBlockTextureName("melon");
		GameRegistry.registerBlock(melon, ItemTerraBlock.class, "Melon");
		
		//fungi general
		ItemCustomFungi.setSuperName("Raw Mushroom");
		
		//brown mushroom
		fungi1 = ItemCustomFungi.create().setUnlocalizedName("mushroom_brown");
		GameRegistry.registerItem(fungi1, fungi1.getUnlocalizedName());
		
		//red mushroom
		fungi2 = ItemCustomFungi.create().setUnlocalizedName("mushroom_red");
		GameRegistry.registerItem(fungi2, fungi2.getUnlocalizedName());
		
		String[] iconNames = new String[7];
		for(int i = 1; i < iconNames.length + 1; i++)
		{
			iconNames[i-1] = Pumpkins.MODID + ":" + "plants/crops/Pumpkin (" + i + ")";
		}
		ICrop p = new BaseCrop("pumpkin", 2, 36, iconNames, 8, 0, 0.9f, pumpkinFood, (int)blockWeight(pumpkinFood), "seedsPumpkin", com.bioxx.tfc.Reference.MOD_ID + ":" + "food/unused/" + "img131", "Seeds Pumpkin");
		pumpkinSeeds = CropsRegistry.addCrop(p);
		
		String[] icons2 = new String[5];
		for(int i = 1; i < icons2.length + 1; i++)
		{
			icons2[i-1] = Pumpkins.MODID + ":" + "plants/crops/Melon (" + i + ")";
		}
		ICrop m = new BaseCrop("melon", 2, 45, icons2, 16, 10, 0.7f, melonFood, (int)blockWeight(melonFood), "seedsMelon", com.bioxx.tfc.Reference.MOD_ID + ":" + "food/unused/" + "img133", "Seeds Melon");
		melonSeeds = CropsRegistry.addCrop(m);
		
		//carving recipes
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(melon), ItemFoodTFC.createTag(new ItemStack(melonFood)), "itemHammer"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCBlocks.pumpkin), ItemFoodTFC.createTag(new ItemStack(pumpkinFood)), "itemHammer"));
		
		//receive crafting events on the crafting handler
		FMLCommonHandler.instance().bus().register(new CustomFoodCraftingHandler());
		
		//allow heating food, and cooking pumpkins
		HeatRegistry.getInstance().addIndex(new HeatIndex(new ItemStack(pumpkinFood, 1), 1, 600, new ItemStack(pumpkinFoodCooked, 1)).setKeepNBT(true));
		HeatRegistry.getInstance().addIndex(new HeatIndex(new ItemStack(pumpkinFoodCooked, 1), 1, 1200, null));
		HeatRegistry.getInstance().addIndex(new HeatIndex(new ItemStack(melonFood, 1), 1, 1200, null));
		
		
		/*
		 * TFC normally does this for all food, if I did this in preInit. However, when I ported this to TechNode, it stopped working due to changes in the timing of the TFC foodList.
		 * So I had to move everything to init, which means the TFC foodList has already been processed and I had to do this myself.
		 * This makes duplicate recipes in regular TFC, but oh well, it doesn't hurt anything, and if TechNode ever updates to fix this I can take it out.
		 */
		Recipes.addFoodMergeRecipe(pumpkinFood);
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemFoodTFC.createTag(new ItemStack(pumpkinFood, 1)), ItemFoodTFC.createTag(new ItemStack(pumpkinFood, 1)), "itemKnife"));
		GameRegistry.addShapelessRecipe(ItemFoodTFC.createTag(new ItemStack(pumpkinFood, 1)), ItemFoodTFC.createTag(new ItemStack(pumpkinFood, 1)));
		
		Recipes.addFoodMergeRecipe(melonFood);
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemFoodTFC.createTag(new ItemStack(melonFood, 1)), ItemFoodTFC.createTag(new ItemStack(melonFood, 1)), "itemKnife"));
		GameRegistry.addShapelessRecipe(ItemFoodTFC.createTag(new ItemStack(melonFood, 1)), ItemFoodTFC.createTag(new ItemStack(melonFood, 1)));
		
		
		Recipes.addFoodMergeRecipe(pumpkinFoodCooked);
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemFoodTFC.createTag(new ItemStack(pumpkinFoodCooked, 1)), ItemFoodTFC.createTag(new ItemStack(pumpkinFoodCooked, 1)), "itemKnife"));
		GameRegistry.addShapelessRecipe(ItemFoodTFC.createTag(new ItemStack(pumpkinFoodCooked, 1)), ItemFoodTFC.createTag(new ItemStack(pumpkinFoodCooked, 1)));

		Recipes.addFoodMergeRecipe(fungi1);
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemFoodTFC.createTag(new ItemStack(fungi1, 1)), ItemFoodTFC.createTag(new ItemStack(fungi1, 1)), "itemKnife"));
		GameRegistry.addShapelessRecipe(ItemFoodTFC.createTag(new ItemStack(fungi1, 1)), ItemFoodTFC.createTag(new ItemStack(fungi1, 1)));

		Recipes.addFoodMergeRecipe(fungi2);
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemFoodTFC.createTag(new ItemStack(fungi2, 1)), ItemFoodTFC.createTag(new ItemStack(fungi2, 1)), "itemKnife"));
		GameRegistry.addShapelessRecipe(ItemFoodTFC.createTag(new ItemStack(fungi2, 1)), ItemFoodTFC.createTag(new ItemStack(fungi2, 1)));
	
		
	
		
	}
	
	/**
	 * Gets the block-to-food conversion ratio (oz/block) for pumpkins/melons
	 * @param item the item to convert
	 * @return the weight of a block in food oz
	 */
	public static float blockWeight(Item item)
	{
		if(item == pumpkinFood)
			return 16;
		else if(item == melonFood)
			return 12;
		else
			return 0;
	}
	
	/*
	static JTextArea ta = null;
	
	public static void display(int c, int i, int j, int k)
	{
		System.out.println("received");
		if(ta == null)
		{
			JFrame f = new JFrame();
			f.setSize(300,300);
			JPanel panel = new JPanel();
			f.setContentPane(panel);
			ta = new JTextArea(15,15);
			panel.add(new JScrollPane(ta));
			f.setVisible(true);
		}
		if(c > 18)
		{
			String s = c + " found at: " + i + "," + j + "," + k;
			ta.setText(ta.getText() + "\n" + s);
			ta.repaint();
			ta.revalidate();
		}
	}*/
}
