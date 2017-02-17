package com.peffern.pumpkins.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * ASM Class Transformer for the TFC BlockSetup.
 * Replaces the TFC fungi with custom fungi blocks
 * @author peffern
 *
 */
public class BlockSetupCT implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(name.equals("com.bioxx.tfc.BlockSetup"))
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
			if(m.name.equals("loadBlocks") && m.desc.equals("()V"))
			{
				Map<AbstractInsnNode, AbstractInsnNode> replacements = new HashMap<AbstractInsnNode, AbstractInsnNode>();
				ListIterator<AbstractInsnNode> it = m.instructions.iterator();
				while(it.hasNext())
				{
					AbstractInsnNode insn = it.next();
					if(insn instanceof TypeInsnNode)
					{
						TypeInsnNode tinsn = (TypeInsnNode)insn;
						if(tinsn.desc.equals("com/bioxx/tfc/Blocks/Terrain/BlockFungi"))
						{
							//replace
							TypeInsnNode newTinsn = new TypeInsnNode(tinsn.getOpcode(), "com/peffern/pumpkins/BlockCustomFungi");
							replacements.put(tinsn, newTinsn);
						}
					}
					if(insn instanceof MethodInsnNode)
					{
						//find the various method calls
						MethodInsnNode minsn = (MethodInsnNode)insn;
						if(minsn.owner.equals("com/bioxx/tfc/Blocks/Terrain/BlockFungi"))
						{
							//replace
							MethodInsnNode newMinsn = new MethodInsnNode(minsn.getOpcode(), "com/peffern/pumpkins/BlockCustomFungi", minsn.name, minsn.desc, minsn.itf);
							replacements.put(minsn, newMinsn);
						}
					}
				}
				//do replacements
				for(Map.Entry<AbstractInsnNode,AbstractInsnNode> r : replacements.entrySet())
				{
					AbstractInsnNode old = r.getKey();
					AbstractInsnNode noo = r.getValue();
					m.instructions.insert(old,noo);
					m.instructions.remove(old);
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
	}
	

}
