package com.peffern.pumpkins;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * Easy ASM hack to stop TFC pumpkin gen: replace the TFC pumpkin gen with an empty gen that doesn't do anything
 * @author peffern
 *
 */
public class WorldGenDummyPumpkin extends WorldGenerator
{
	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
	{
		/*
		 * Deliberately do nothing. This is a dummy class to slot into the TFC biome decorator in place of the real pumpkin gen
		 */
		return true;
	}
}
