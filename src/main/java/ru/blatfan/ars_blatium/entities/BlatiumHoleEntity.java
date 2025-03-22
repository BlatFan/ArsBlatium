package ru.blatfan.ars_blatium.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.blatfan.ars_blatium.ArsBlatium;
import ru.blatfan.ars_blatium.init.ABRegistry;
import ru.blatfan.ars_blatium.network.BlatiumHolePacket;
import ru.blatfan.ars_blatium.network.PurpleLightPacket;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;

public class BlatiumHoleEntity extends Entity implements GeoEntity {
    private UUID shooter;
    private int ticks;
    
    public BlatiumHoleEntity(EntityType<?> p_19870_, Level p_19871_) {
        this(p_19871_, 10);
    }
    
    public BlatiumHoleEntity(Level level, int time){
        super(ABRegistry.HOLE.get(), level);
        this.ticks=time*20;
    }
    
    public int time(){
        return ticks/20;
    }
    
    public void setShooter(UUID shooter) {
        this.shooter = shooter;
    }
    
    @Override
    public void tick() {
        if(level().isClientSide) return;
        ticks--;
        List<LivingEntity> nearestEntities = level().getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(), Minecraft.getInstance().player,
            getBoundingBox().inflate(5, 5, 5));
        if(ticks % 10 == 0) ArsBlatium.PacketsHandler.sendToTracking(level(), getOnPos().offset(0, 1, 0), new PurpleLightPacket(getOnPos()));
        if(ticks<=0) {
            ArsBlatium.PacketsHandler.sendToTracking(level(), getOnPos().offset(0, 1, 0), new PurpleLightPacket(getOnPos()));
            ArsBlatium.PacketsHandler.sendToTracking(level(), getOnPos().offset(0, 1, 0), new BlatiumHolePacket(getOnPos()));
            for (LivingEntity entity : nearestEntities)
                if(entity != null && entity.getUUID()!=shooter && !(entity instanceof ArmorStand))
                    entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 10));
            remove(RemovalReason.DISCARDED);
            return;
        }
        for (LivingEntity entity : nearestEntities)
            if(entity != null && entity.getUUID()!=shooter && !(entity instanceof ArmorStand)) {
                Vec3 entityPos = position().add(0, 0.5, 0);
                Vec3 motion = entityPos.subtract(entity.position());
                entity.setDeltaMovement(motion.normalize().scale(0.75f));
                entity.hurt(level().damageSources().magic(), entity.getMaxHealth()/5);
            }
    }
    
    @Override
    protected void defineSynchedData() {}
    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {}
    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {}
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}
    private final AnimatableInstanceCache c = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return c;
    }
}
