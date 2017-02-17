package com.peffern.pumpkins;

import java.util.Random;

import com.bioxx.tfc.Core.TFCTabs;

import net.minecraft.block.BlockMelon;
import net.minecraft.item.Item;

/**
 * Override the minecraft Melon block so it doesnt break into slices when you break it, and it instead behaves like a TFC block
 * @author peffern
 *
 */
public class BlockCustomMelon extends BlockMelon
{
	public BlockCustomMelon()
	{
		super();
		this.setCreativeTab(TFCTabs.TFC_BUILDING);
	}
	
	/**
	 * Drop the block when broken
	 */
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(this);
    }
	
	/**
	 * Drop exactly one of the block when broken
	 */
	@Override
	public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }
	
	/**
	 * Ignore "fortune" enchantment
	 */
	@Override
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_)
    {
       return quantityDropped(p_149679_2_);
    }
}
