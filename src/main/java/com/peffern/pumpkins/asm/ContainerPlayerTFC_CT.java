package com.peffern.pumpkins.asm;

import java.util.ListIterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import net.minecraft.launchwrapper.IClassTransformer;

/**
 * Transform the ContainerPlayerTFC class to call the custom FoodCraftingHandler for updateOutput instead of the normal TFC one
 * @author peffern
 *
 */
public class ContainerPlayerTFC_CT implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(name.equals("com.bioxx.tfc.Containers.ContainerPlayerTFC"))
		{
			return asmify(basicClass);
		}
		else
			return basicClass;
	}
	
	private byte[] asmify(byte[] bytes)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		for(MethodNode m : classNode.methods)
		{
			//find the method
			if((m.name.equals("onCraftMatrixChanged") || m.name.equals("func_75130_a")) && m.desc.equals("(Lnet/minecraft/inventory/IInventory;)V"))
			{
				ListIterator<AbstractInsnNode> it = m.instructions.iterator();
				while(it.hasNext())
				{
					AbstractInsnNode insn = it.next();
					if(insn instanceof MethodInsnNode)
					{
						MethodInsnNode minsn = (MethodInsnNode)insn;
						
						//find the method call
						if(minsn.owner.equals("com/bioxx/tfc/Handlers/FoodCraftingHandler") || minsn.owner.equals("com/bioxx/tfc/Handlers/CraftingHandler"))
						{
							//replace
							MethodInsnNode newMinsn = new MethodInsnNode(minsn.getOpcode(), "com/peffern/pumpkins/CustomFoodCraftingHandler", minsn.name, minsn.desc, minsn.itf);
							m.instructions.insert(minsn, newMinsn);
							m.instructions.remove(minsn);
						}
					}
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
	}
}
