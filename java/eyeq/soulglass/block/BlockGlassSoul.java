package eyeq.soulglass.block;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockGlassSoul extends BlockGlass {
    public static final PropertyInteger LIGHT = PropertyInteger.create("light", 0, 15);

    public static final AxisAlignedBB GLASSSOUL_AABB = new AxisAlignedBB(0.0625, 0.0625, 0.0625, 0.9375, 0.9375, 0.9375);

    public BlockGlassSoul() {
        super(Material.GLASS, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LIGHT, 0));
        this.setSoundType(SoundType.GLASS);
        this.setTickRandomly(true);
    }

    @Override
    public int quantityDropped(Random rand) {
        return 1;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return GLASSSOUL_AABB;
    }

    @Override
    public int tickRate(World world) {
        return 20;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);
        int light = Math.max(0, state.getValue(LIGHT) - 1);
        world.setBlockState(pos, state.withProperty(LIGHT, light));
        if(light > 0) {
            world.scheduleUpdate(new BlockPos(pos), this, this.tickRate(world));
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if(!(entity instanceof EntityLivingBase)) {
            return;
        }
        if(((EntityLivingBase) entity).hurtTime > 0) {
            return;
        }
        entity.attackEntityFrom(DamageSource.CACTUS, 1.0F);

        int light = Math.min(state.getValue(LIGHT) + 3, 15);
        world.setBlockState(pos, state.withProperty(LIGHT, light));
        if(light > 0) {
            world.scheduleUpdate(new BlockPos(pos), this, this.tickRate(world));
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(LIGHT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LIGHT, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LIGHT);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LIGHT);
    }
}
