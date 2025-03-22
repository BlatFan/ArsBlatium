package ru.blatfan.ars_blatium.spells;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import ru.blatfan.ars_blatium.ArsBlatium;
import ru.blatfan.ars_blatium.entities.BlatiumHoleEntity;

import java.util.Set;

public class SmallBlackHoleEffect extends AbstractEffect {
    public static SmallBlackHoleEffect INSTANCE = new SmallBlackHoleEffect();
    public SmallBlackHoleEffect() {
        super(BlatiumGlyphes.small_hole, "Small Black Hole");
    }
    
    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        spawn(world, shooter, rayTraceResult.getBlockPos());
    }
    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        spawn(world, shooter, rayTraceResult.getEntity().getOnPos());
    }
    
    private static void spawn(Level world, LivingEntity shooter, BlockPos pos){
        BlatiumHoleEntity entity = new BlatiumHoleEntity(world, 2);
        entity.setShooter(shooter.getUUID());
        entity.setPos(pos.getCenter().add(0, 1, 0));
        world.addFreshEntity(entity);
    }
    
    @Override
    public SpellTier getConfigTier() {
        return defaultTier();
    }
    
    @Override
    public SpellTier defaultTier() {
        return ArsBlatium.BLATIUM;
    }
    
    @Override
    protected int getDefaultManaCost() {
        return 200;
    }
    
    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.augmentSetOf();
    }
    
    public @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(BlatiumSchool.INSTANCE);
    }
}
