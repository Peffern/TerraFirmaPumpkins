package com.peffern.pumpkins;

import java.lang.reflect.Field;
import java.util.Random;

import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.Food.CropIndex;
import com.bioxx.tfc.Food.CropManager;
import com.bioxx.tfc.TileEntities.TECrop;
import com.bioxx.tfc.WorldGen.Generators.WorldGenGrowCrops;
import com.bioxx.tfc.api.TFCBlocks;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenDebugCrops extends WorldGenGrowCrops
{

	public WorldGenDebugCrops(int par1) 
	{
		super(par1);
	}
	
	@Override
	public void generate(World world, Random rand, int x, int z, int numToGen)
	{
		super.generate(world, rand, x, z, numToGen);
		/* THIS CODE IS TAKEN FROM TFC WorldGenGrowCrops class it's not mine - i used it for debug
		/*try
		{
			Field cropBlockId = WorldGenGrowCrops.class.getDeclaredField("cropBlockId");
			cropBlockId.setAccessible(true);
			int cbi = cropBlockId.getInt(this);
			int i = x, j = 150, k = z;
			CropIndex crop;
			TECrop te;

			for(int c = 0; c < numToGen; c++)
			{
				i = x - 8 + rand.nextInt(16);
				k = z - 8 + rand.nextInt(16);
				j = world.getTopSolidOrLiquidBlock(i, k);
				crop = CropManager.getInstance().getCropFromId(cbi);

				if(crop != null)
				{
					float temp = TFC_Climate.getHeightAdjustedTempSpecificDay(world, TFC_Time.getTotalDays(), i, j, k);
					int month = TFC_Time.getSeasonAdjustedMonth(k);

					if(cbi > 18)
						System.out.println("genning: " + cbi);
					if(cbi > 18)
						System.out.println(i + " " + j + " " + k);
					if(temp > crop.minAliveTemp && month > 0 && month <= 6)
					{
						if(cbi > 18)
							System.out.println("passed climate check");
						Block b = world.getBlock(i, j, k);
						if (TFCBlocks.crops.canBlockStay(world, i, j, k) && (b.isAir(world, i, j, k) || b == TFCBlocks.tallGrass))
						{
							if(cbi > 18)
								System.out.println("passed enviro check");
							if(world.setBlock(i, j, k, TFCBlocks.crops, 0, 0x2))
							{
								if(cbi > 18)
									System.out.println("blck is set");
								te = (TECrop)world.getTileEntity(i, j, k);
								if(te != null)
								{
									
									if(cbi > 18)
										System.out.println("te is set");
									if(cbi > 18)
										System.out.println(i + " " + j + " " + k);
									te.cropId = cbi;
									float gt = Math.max(crop.growthTime / TFC_Time.daysInMonth, 0.01f);
									float mg = Math.min(month / gt, 1.0f) * (0.75f + (rand.nextFloat() * 0.25f));
									float growth = Math.min(crop.numGrowthStages * mg, crop.numGrowthStages);
									te.growth = growth;
								}
							}
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}*/
	}

}
