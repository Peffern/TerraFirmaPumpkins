package com.peffern.pumpkins.asm;

import static org.objectweb.asm.Opcodes.*;

import java.util.ListIterator;

import javax.swing.JOptionPane;

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
						else if(minsn.owner.equals("com/bioxx/tfc/WorldGen/Generators/WorldGenGrowCrops") && minsn.name.equals("generate"))
						{
							//insert debugging printouts. used this to debug in technode
							/*InsnNode insn1 = new InsnNode(ACONST_NULL);
							VarInsnNode var1 = new VarInsnNode(ILOAD,7);
							MethodInsnNode method1 = new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
							MethodInsnNode method2 = new MethodInsnNode(INVOKESTATIC, "com/peffern/pumpkins/Pumpkins", "cropid", "(Ljava/awt/Component;Ljava/lang/Object;)V", false);
							InsnNode insn2 = new InsnNode(ACONST_NULL);
							VarInsnNode var2 = new VarInsnNode(ILOAD,3);
							MethodInsnNode method3 = new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
							MethodInsnNode method4 = new MethodInsnNode(INVOKESTATIC, "com/peffern/pumpkins/Pumpkins", "xcoord", "(Ljava/awt/Component;Ljava/lang/Object;)V", false);
							InsnNode insn3 = new InsnNode(ACONST_NULL);
							VarInsnNode var3 = new VarInsnNode(ILOAD,5);
							MethodInsnNode method5 = new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
							MethodInsnNode method6 = new MethodInsnNode(INVOKESTATIC, "com/peffern/pumpkins/Pumpkins", "xcoord", "(Ljava/awt/Component;Ljava/lang/Object;)V", false);
							
							m.instructions.insert(minsn,insn1);
							m.instructions.insert(insn1,var1);
							m.instructions.insert(var1,method1);
							m.instructions.insert(method1,method2);
							m.instructions.insert(method2,insn2);
							m.instructions.insert(insn2,var2);
							m.instructions.insert(var2,method3);
							m.instructions.insert(method3,method4);
							m.instructions.insert(method4,insn3);
							m.instructions.insert(insn3,var3);
							m.instructions.insert(var3,method5);
							m.instructions.insert(method5,method6);*/

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
