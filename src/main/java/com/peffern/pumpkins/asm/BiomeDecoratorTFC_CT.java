package com.peffern.pumpkins.asm;

import static org.objectweb.asm.Opcodes.*;

import java.util.ListIterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;
/**
 * Modifies TFC's BiomeDecorator by replacing references to the pumpkin generator with the dummy pumpkin generator.
 * Debug code: insert debug printouts into TFC's world gen when crops are generated (disabled currently)
 * @author peffern
 *
 */
@SuppressWarnings("unused")
public class BiomeDecoratorTFC_CT implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(name.equals("com.bioxx.tfc.WorldGen.BiomeDecoratorTFC"))
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
			//find the render method
			if((m.name.equals("genDecorations") || m.name.equals("func_150513_a")) && m.desc.equals("(Lnet/minecraft/world/biome/BiomeGenBase;)V"))
			{

				ListIterator<AbstractInsnNode> it = m.instructions.iterator();
				//iterate over the bytecode instructions
				while(it.hasNext())
				{
					AbstractInsnNode insn = it.next();
					//find the pumpkin gen variable declaration
					if(insn instanceof TypeInsnNode)
					{
						TypeInsnNode tinsn = (TypeInsnNode)insn;
						if(tinsn.desc.equals("com/bioxx/tfc/WorldGen/Generators/WorldGenCustomPumpkin"))
						{
							//replace with dummy
							TypeInsnNode newTinsn = new TypeInsnNode(tinsn.getOpcode(),"com/peffern/pumpkins/WorldGenDummyPumpkin");
							m.instructions.insert(tinsn,newTinsn);
							m.instructions.remove(tinsn);

						}
						else if(tinsn.desc.equals("com/bioxx/tfc/WorldGen/Generators/WorldGenGrowCrops"))
						{
							TypeInsnNode newTinsn = new TypeInsnNode(tinsn.getOpcode(),"com/peffern/pumpkins/WorldGenDebugCrops");
							m.instructions.insert(tinsn,newTinsn);
							m.instructions.remove(tinsn);
						}
					}
					//find the pumpkin gen <init> and call to generate
					else if(insn instanceof MethodInsnNode)
					{
						MethodInsnNode minsn = (MethodInsnNode)insn;
						if(minsn.owner.equals("com/bioxx/tfc/WorldGen/Generators/WorldGenCustomPumpkin"))
						{
							//replace with dummy
							MethodInsnNode newMinsn = new MethodInsnNode(minsn.getOpcode(), "com/peffern/pumpkins/WorldGenDummyPumpkin", minsn.name, minsn.desc, minsn.itf);
							m.instructions.insert(minsn, newMinsn);
							m.instructions.remove(minsn);
						}
						else if(minsn.owner.equals("com/bioxx/tfc/WorldGen/Generators/WorldGenGrowCrops"))
						{
							//replace with dummy
							MethodInsnNode newMinsn = new MethodInsnNode(minsn.getOpcode(), "com/peffern/pumpkins/WorldGenDebugCrops", minsn.name, minsn.desc, minsn.itf);
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
