package eyeq.soulglass;

import eyeq.soulglass.block.BlockGlassSoul;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.renderer.block.statemap.StateMapperNormal;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

import static eyeq.soulglass.SoulGlass.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class SoulGlass {
    public static final String MOD_ID = "eyeq_soulglass";

    @Mod.Instance(MOD_ID)
    public static SoulGlass instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static Block soulGlass;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addRecipes();
        if(event.getSide().isServer()) {
            return;
        }
        renderBlockModels();
        renderItemModels();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event) {
        soulGlass = new BlockGlassSoul().setHardness(0.3F).setUnlocalizedName("soulGlass");

        GameRegistry.register(soulGlass, resource.createResourceLocation("soul_glass"));
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        GameRegistry.register(new ItemBlock(soulGlass), soulGlass.getRegistryName());
    }

    public static void addRecipes() {
        GameRegistry.addSmelting(Blocks.SOUL_SAND, new ItemStack(soulGlass), 0.1F);
    }

    @SideOnly(Side.CLIENT)
    public static void renderBlockModels() {
        ModelLoader.setCustomStateMapper(soulGlass, new StateMapperNormal(soulGlass.getRegistryName()));
    }

	@SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(soulGlass);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-SoulGlass");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, soulGlass, "Soul Glass");
        language.register(LanguageResourceManager.JA_JP, soulGlass, "ソウルガラス");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        UModelCreator.createBlockstateJson(project, soulGlass);
    }
}
