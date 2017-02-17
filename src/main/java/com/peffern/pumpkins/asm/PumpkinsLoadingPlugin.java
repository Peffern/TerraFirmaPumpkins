package com.peffern.pumpkins.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

/**
 * ASM plugin for TerraFirmaPumpkins. The crop system has moved to TFC Crop Index, so this ASM now exists only for the custom crafting handler, the mushrooms, and the worldgen 
 * @author peffern
 *
 */
@IFMLLoadingPlugin.TransformerExclusions({"com.peffern.pumpkins"})
public class PumpkinsLoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass() 
	{
		return new String[]{BlockSetupCT.class.getName(), ContainerPlayerTFC_CT.class.getName(), BiomeDecoratorTFC_CT.class.getName()};
	}

	@Override
	public String getModContainerClass() 
	{
		return null;
	}

	@Override
	public String getSetupClass() 
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) 
	{
		
	}

	@Override
	public String getAccessTransformerClass() 
	{
		return null;
	}
}
