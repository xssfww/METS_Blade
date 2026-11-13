package net.METS_Blade.CoreMod.Transformer.Destroy;

import cpw.mods.modlauncher.api.NamedPath;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class DestroyHealthPlugin implements ILaunchPluginService {
    private static final String ENTITY = "net/minecraft/world/entity/Entity";
    private static final String LIVING_ENTITY = "net/minecraft/world/entity/LivingEntity";
    private static final String PLAYER = "net/minecraft/world/entity/player/Player";
    private static final String SEAL_HELPER = "net/METS_Blade/CoreMod/Helper/SealHelper";
    private final Map<String, Boolean> inheritanceCache = new ConcurrentHashMap<>();
    private final Map<String, String> superNameCache = new ConcurrentHashMap<>();
    @Override
    public String name() {
        return "destroy_health_plugin";
    }
    @Override
    public void initializeLaunch(ILaunchPluginService.ITransformerLoader transformerLoader, NamedPath[] specialPaths) {
    }
    @Override
    public EnumSet<ILaunchPluginService.Phase> handlesClass(Type classType, boolean isEmpty) {
        return isEmpty ? EnumSet.noneOf(ILaunchPluginService.Phase.class) : EnumSet.of(ILaunchPluginService.Phase.BEFORE);
    }
    @Override
    public boolean processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type classType) {
        superNameCache.put(classNode.name, classNode.superName);
        if (!isLivingEntitySubclass(classNode.name)) {
            return false;
        }
        boolean modified = false;
        for (MethodNode method : classNode.methods) {
            if ("m_21223_".equals(method.name) && "()F".equals(method.desc)) {
                InsnList inject = new InsnList();
                LabelNode skipLabel = new LabelNode();
                String descriptor = Type.getMethodDescriptor(
                        Type.BOOLEAN_TYPE,
                        Type.getObjectType("net/minecraft/world/entity/LivingEntity")
                );
                inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                inject.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        SEAL_HELPER,
                        "isSealed",
                        descriptor,
                        false
                ));
                inject.add(new JumpInsnNode(Opcodes.IFEQ, skipLabel));
                inject.add(new LdcInsnNode(0.0F));
                inject.add(new InsnNode(Opcodes.FRETURN));
                inject.add(skipLabel);
                method.instructions.insert(inject);
                modified = true;
            }
        }
        return modified;
    }
    private boolean isLivingEntitySubclass(String className) {
        Boolean cached = inheritanceCache.get(className);
        if (cached != null) {
            return cached;
        }
        if (ENTITY.equals(className) || LIVING_ENTITY.equals(className)) {
            inheritanceCache.put(className, false);
            return false;
        }
        String current = className;
        int maxDepth = 100;
        while (current != null && maxDepth-- > 0) {
            switch (current) {
                case PLAYER, ENTITY -> {
                    inheritanceCache.put(className, false);
                    return false;
                }
                case LIVING_ENTITY -> {
                    inheritanceCache.put(className, true);
                    return true;
                }
            }
            String parent = superNameCache.get(current);
            if (parent == null) {
                inheritanceCache.put(className, false);
                return false;
            }
            current = parent;
        }
        inheritanceCache.put(className, false);
        return false;
    }
}
