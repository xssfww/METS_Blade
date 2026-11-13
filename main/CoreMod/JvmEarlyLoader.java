package net.METS_Blade.CoreMod;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.*;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.METS_Blade.CoreMod.Transformer.Destroy.*;
import net.METS_Blade.METS_SlashBlade;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.module.Configuration;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
@SuppressWarnings({"unchecked", "rawtypes"})
public class JvmEarlyLoader implements ITransformationService {
    static {
        try {
            Field field= Launcher.class.getDeclaredField("launchPlugins");
            field.setAccessible(true);
            Object obj=field.get(Launcher.INSTANCE);
            field= LaunchPluginHandler.class.getDeclaredField("plugins");
            field.setAccessible(true);
            ((Map<String, ILaunchPluginService>)field.get(obj)).put("destroy_health_plugin",new DestroyHealthPlugin());
        } catch (Throwable e) {
            METS_SlashBlade.LOGGER.error("[JvmEarlyLoader] LaunchPluginService加载失败 {}", e.getMessage());
        }
    }
    @Override
    public @NotNull String name() { return "jvm_loader"; }
    @Override
    public void initialize(IEnvironment env) {
        METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] initialized()");
    }
    @Override
    public void onLoad(IEnvironment env, Set<String> others) {
        METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] onLoad() SPI清理");
        ModLoadable();
    }
    @Override
    public @NotNull List<ITransformer> transformers() {
        return Collections.emptyList();
    }
    public static void ModLoadable() {
        METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] 尝试清理SPI");
        try {
            Class<?> discoverer = Class.forName("net.minecraftforge.fml.loading.ModDirTransformerDiscoverer");
            VarHandle foundHandle = MethodHandles.privateLookupIn(discoverer, MethodHandles.lookup())
                    .findStaticVarHandle(discoverer, "found", List.class);
            List<?> found = (List<?>) foundHandle.get();
            final Method[] pathsMethod = {null};
            found.removeIf(namedPath -> {
                try {
                    if (pathsMethod[0] == null) {
                        pathsMethod[0] = namedPath.getClass().getMethod("paths");
                    }
                    Path[] paths = (Path[]) pathsMethod[0].invoke(namedPath);
                    boolean match = paths[0].toString().contains("MoreElectricTools_Blade")
                            || paths[0].toString().contains("moreelectrictools_blade");
                    if (match) {
                        METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] 从found中移除: {}", paths[0]);
                    }
                    return match;
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            METS_SlashBlade.LOGGER.error("[JvmEarlyLoader] Step1失败: {}", e.getMessage());
        }
        try {
            Class<?> launcherClass = Class.forName("cpw.mods.modlauncher.Launcher");
            Class<?> moduleLayerHandlerClass = Class.forName("cpw.mods.modlauncher.ModuleLayerHandler");
            Class<?> layerInfoClass = Class.forName("cpw.mods.modlauncher.ModuleLayerHandler$LayerInfo");
            VarHandle instanceHandle = MethodHandles.privateLookupIn(launcherClass, MethodHandles.lookup())
                    .findStaticVarHandle(launcherClass, "INSTANCE", launcherClass);
            Object launcher = instanceHandle.get();
            if (launcher == null) {
                METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] Launcher.INSTANCE == null，跳过 Step 2");
                return;
            }
            VarHandle mlhHandle = MethodHandles.privateLookupIn(launcherClass, MethodHandles.lookup())
                    .findVarHandle(launcherClass, "moduleLayerHandler", moduleLayerHandlerClass);
            Object moduleLayerHandler = mlhHandle.get(launcher);
            if (moduleLayerHandler == null) {
                METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] moduleLayerHandler == null，跳过 Step 2");
                return;
            }
            VarHandle layersHandle = MethodHandles.privateLookupIn(moduleLayerHandlerClass, MethodHandles.lookup())
                    .findVarHandle(moduleLayerHandlerClass, "completedLayers", EnumMap.class);
            EnumMap<?, ?> completedLayers = (EnumMap<?, ?>) layersHandle.get(moduleLayerHandler);
            if (completedLayers == null || completedLayers.isEmpty()) {
                METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] completedLayers 为空，跳过 Step 2");
                return;
            }
            VarHandle layerHandle = MethodHandles.privateLookupIn(layerInfoClass, MethodHandles.lookup())
                    .findVarHandle(layerInfoClass, "layer", ModuleLayer.class);
            String moduleName = JvmEarlyLoader.class.getModule().getName();
            METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] 模块名: {}", moduleName);
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);
            Field nameToModuleField = Configuration.class.getDeclaredField("nameToModule");
            long nameToModuleOffset = unsafe.objectFieldOffset(nameToModuleField);
            Field modulesField = Configuration.class.getDeclaredField("modules");
            long modulesOffset = unsafe.objectFieldOffset(modulesField);
            for (Object layerInfo : completedLayers.values()) {
                ModuleLayer layer = (ModuleLayer) layerHandle.get(layerInfo);
                if (layer == null) continue;
                Configuration config = layer.configuration();
                if (config == null) continue;
                Map<String, Object> nameToModule = (Map<String, Object>) unsafe.getObject(config, nameToModuleOffset);
                Set<Object> modules = (Set<Object>) unsafe.getObject(config, modulesOffset);
                if (nameToModule != null && nameToModule.containsKey(moduleName)) {
                    Map<String, Object> newNameToModule = new HashMap<>(nameToModule);
                    Set<Object> newModules = new HashSet<>(modules);
                    Object removed = newNameToModule.remove(moduleName);
                    if (removed != null) {
                        newModules.remove(removed);
                        METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] 已从 Configuration 移除模块: {}", moduleName);
                    }
                    unsafe.putObject(config, nameToModuleOffset, Collections.unmodifiableMap(newNameToModule));
                    unsafe.putObject(config, modulesOffset, Collections.unmodifiableSet(newModules));
                }
            }
        } catch (Exception e) {
            METS_SlashBlade.LOGGER.error("[JvmEarlyLoader] Step 2 失败: {}", e.getMessage());
        }
        METS_SlashBlade.LOGGER.info("[JvmEarlyLoader] makeModLoadable() — 清理完成");
    }
}
