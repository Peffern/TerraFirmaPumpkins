package com.peffern.pumpkins;

import java.util.ArrayList;
import java.util.List;

import com.bioxx.tfc.Food.ItemFoodTFC;
import com.bioxx.tfc.Render.Item.FoodItemRenderer;
import com.bioxx.tfc.api.Food;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.Enums.EnumFoodGroup;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Custom Food representing the mushroom types
 * @author peffern
 *
 */
public class ItemCustomFungi extends ItemFoodTFC
{	
	/** Basically the unlocal name for all of the mushrooms */
	static String superName;
	/** Makes sure that each fungi created matches a specific damage value for fungi blocks */
	static int damageCounter = 0;
	/** List of created items, indexed by damage number */
	public static List<ItemCustomFungi> FUNGI = new ArrayList<ItemCustomFungi>();
	/** Damage number for this item */
	int blockDamage;
	/** Second part of unlocal name (mushroom specific) */
	String subName;
	
	/**
	 * Generates an ItemCustomFungi representing the next value of the damage counter
	 * @return
	 */
	static ItemCustomFungi create()
	{
		ItemCustomFungi ret = new ItemCustomFungi(damageCounter);
		damageCounter++;
		FUNGI.add(ret);
		return ret;
	}
	
	/**
	 * Constructs an ItemCustomFungi from a a damage parameter.
	 * @param d
	 */
	private ItemCustomFungi(int d)
	{
		super(EnumFoodGroup.Protein, 0, 0, 0, 20, 20, false, true);
		blockDamage = d;
	}
	
	/**
	 * Sets the general (first half) of name
	 * @param sn name
	 */
	static void setSuperName(String sn)
	{
		superName = sn;
	}
	
	/**
	 * Combine super and regular names
	 */
	@Override
	public Item setUnlocalizedName(String name)
	{
		subName = name;
		return super.setUnlocalizedName(superName + "." + name);
		
	}
	
	/**
	 * Use regular name to register icons
	 */
	@Override
	public void registerIcons(IIconRegister register)
	{
		this.itemIcon = register.registerIcon(Pumpkins.MODID + ":" + "food/" + subName);
		MinecraftForgeClient.registerItemRenderer(this, new FoodItemRenderer());
	}
	
	

	
	/**
	 * Right-Click with item
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		
		
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

		if (movingobjectposition == null)
		{
			return par1ItemStack;
		}
		else
		{
			//hit block
			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK)
			{
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
					return par1ItemStack;

				if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
					return par1ItemStack;

				//TFC verify
				if (TFCBlocks.fungi.canBlockStay(par2World, i, j + 1, k) && par2World.isAirBlock(i, j + 1, k))
				{
					//make sure food has enough weight to plant mushroom
					float weight = Food.getWeight(par1ItemStack);
					float decay = Food.getDecay(par1ItemStack);
					if (decay < 0)
						decay = 0;
					float avail =  weight-decay; 
					//sufficient weight
					if(avail >= 4 || !par1ItemStack.hasTagCompound())
					{
						//create block
						par2World.setBlock(i, j + 1, k, TFCBlocks.fungi, blockDamage, 0x3);
						par2World.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, TFCBlocks.fungi.stepSound.func_150496_b(), (TFCBlocks.fungi.stepSound.getVolume() + 1.0F) / 2.0F, TFCBlocks.fungi.stepSound.getPitch() * 0.8F);
						//edge case: 0 oz remaining
						if(avail == 4 || !par1ItemStack.hasTagCompound())
							par1ItemStack.stackSize--;
						else //excess weight
						{
							//reduce weight
							avail = avail - 4;
							weight = avail + decay;
							Food.setWeight(par1ItemStack, weight);
							Food.setDecay(par1ItemStack, decay);
						}
						
					}
					
					
				}
			}
			return par1ItemStack;
		}
	}
	
}
