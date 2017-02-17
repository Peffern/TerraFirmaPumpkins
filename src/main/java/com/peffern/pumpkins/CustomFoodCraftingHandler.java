package com.peffern.pumpkins;


import com.bioxx.tfc.Handlers.CraftingHandler;
import com.bioxx.tfc.Handlers.FoodCraftingHandler;
import com.bioxx.tfc.api.Food;
import com.bioxx.tfc.api.TFCBlocks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CustomFoodCraftingHandler extends FoodCraftingHandler
{
	/**
	 * Intercept calls to updateOutput and don't let the TFC Food Crafting Handler get them if it's one of our own recipes.
	 * This is necessary because otherwise the TFC handler converts everything into 0oz of Pickled Salted Dried $whatever
	 * @param player the player doing the crafting
	 * @param craftResult the result of the player's crafting recipe
	 * @param craftingInv the player's crafting grid
	 */
	public static void updateOutput(EntityPlayer player, ItemStack craftResult, IInventory craftingInv)
	{
		//no custom entries as of now
		
		FoodCraftingHandler.updateOutput(player, craftResult, craftingInv);
		
	}
	
	/**
	 * Intercept calls to transferNBT and don't let the TFC Crafting Handler get them if it's one of our own recipes.
	 * Equivalent to updateOutput for non-food recipes
	 * @param player the player doing the crafting
	 * @param craftResult the result of the player's crafting recipe
	 * @param craftingInv the player's crafting grid
	 */
	public static void transferNBT(boolean clearContents, EntityPlayer player, ItemStack itemstack, IInventory iinventory)
	{
		//detect pumpkin/melon hammer recipe
		if((itemstack.getItem() == Item.getItemFromBlock(TFCBlocks.pumpkin) && gridHasItem(iinventory, Pumpkins.pumpkinFood)) || (itemstack.getItem() == Item.getItemFromBlock(Pumpkins.melon) && gridHasItem(iinventory, Pumpkins.melonFood)))
		{
			for(int i = 0, l = iinventory.getSizeInventory(); i < l; i++)
			{
				ItemStack stack = iinventory.getStackInSlot(i);
				//find the food
				if(stack != null && (stack.getItem() == Pumpkins.pumpkinFood || stack.getItem() == Pumpkins.melonFood))
				{
					//make sure enough food to proceed
					float weight = Food.getWeight(stack);
					float decay = Food.getDecay(stack);
					if(decay == -24.0)
						decay = 0;
					float avail = weight - decay;
					//not enough food
					if(avail < Pumpkins.blockWeight(stack.getItem()))
					{
						//remove outputs
						player.inventoryContainer.getSlot(0).decrStackSize(1);
					}
				}
			}
		}
		else
		{
			//default handler
			CraftingHandler.transferNBT(clearContents, player, itemstack, iinventory);
		}
	}
	
	/**
	 * Handle post-craft events (damage tools)
	 */
	@SubscribeEvent
	public void onFoodCrafting(ItemCraftedEvent e)
	{
		ItemStack craftResult = e.crafting;
		IInventory craftingInv = e.craftMatrix;

		if(craftingInv != null)
		{
			//pumpkin or melon hammer recipe
			if((craftResult.getItem() == Item.getItemFromBlock(TFCBlocks.pumpkin) && gridHasItem(craftingInv, Pumpkins.pumpkinFood)) || (craftResult.getItem() == Item.getItemFromBlock(Pumpkins.melon) && gridHasItem(craftingInv, Pumpkins.melonFood)))
			{
				ItemStack hammer = null;
				ItemStack food = null;
				for(int i = 0, l = craftingInv.getSizeInventory(); i < l; i++)
				{
					ItemStack stack = craftingInv.getStackInSlot(i);
					//find the food stack
					if(stack != null && (stack.getItem()==Pumpkins.pumpkinFood || stack.getItem() == Pumpkins.melonFood))
					{
						food = stack;
					}
					else
					{
						//find the hammer stack
						int[] ids = OreDictionary.getOreIDs(stack);
						for(int id : ids)
						{
							if(OreDictionary.getOreName(id).equals("itemHammer"))
							{
								hammer = stack;
								break;
							}
						}
					}
				}
				if(hammer != null && food != null)
				{
					//damage the hammer
					if (hammer.getItemDamage() < hammer.getMaxDamage() - 1)
					{
						hammer.damageItem(1 , e.player);
						hammer.stackSize++;
					}
					
					//subtract weight from the food item
					float weight = Food.getWeight(food);
					float decay = Food.getDecay(food);
					if(decay == -24.0)
						decay = 0;
					float avail = weight - decay;
					avail = avail - Pumpkins.blockWeight(food.getItem());
					weight = avail + decay;
					Food.setWeight(food, weight);
					Food.setDecay(food, decay);
					food.stackSize++;
				}
							
				
			}
		}
	}
}
