package com.peffern.pumpkins;

import java.util.Random;

import com.bioxx.tfc.Blocks.Terrain.BlockFungi;
import com.bioxx.tfc.Food.ItemFoodTFC;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Modifications to the BlockFungi class that points to the custom mushroom items
 * @author peffern
 *
 */
public class BlockCustomFungi extends BlockFungi
{	
	public BlockCustomFungi()
	{
		super();
	}
	
	/**
	 * Prevent the block from dropping when broken
	 */
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
		if(Pumpkins.enableMushrooms)
			return null;
		else
			return super.getItemDropped(p_149650_1_, p_149650_2_, p_149650_3_);
	}
	
	/**
	 * Spawn the mushroom when harvested
	 */
	@Override
	public void onBlockHarvested(World world, int i, int j, int k, int l, EntityPlayer player)
	{
		if(Pumpkins.enableMushrooms)
		{
			if(!world.isRemote)
			{
																	//get the fungi based on damage
				ItemStack is = ItemFoodTFC.createTag(new ItemStack(ItemCustomFungi.FUNGI.get(l)), 4);
				//spawn
				world.spawnEntityInWorld(new EntityItem(world, i, j, k, is));
				
			}
		}
		else
			super.onBlockHarvested(world, i, j, k, l, player);
	}
}
