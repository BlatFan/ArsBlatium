package ru.blatfan.ars_blatium.events;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.blatfan.ars_blatium.config.ArsArmorSetConfig;
import ru.blatfan.ars_blatium.ArsBlatium;
import ru.blatfan.ars_blatium.items.ArsArmorItem;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ArsBlatium.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorEvents {
    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        processArmorEvent(event.getEntity(), EquipmentSlot.FEET, () -> true, ArsArmorSetConfig::preventFallDamage, () -> event.setCanceled(true));
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingDamageEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide()) return;

        LivingEntity entity = event.getEntity();

        processArmorEvent(entity, EquipmentSlot.HEAD, () -> event.getSource().is(DamageTypeTags.IS_DROWNING), ArsArmorSetConfig::preventKinetic, () -> {
            entity.setAirSupply(entity.getMaxAirSupply());
            event.setCanceled(true);
        });
        processArmorEvent(entity, EquipmentSlot.HEAD, () -> event.getSource().is(DamageTypes.FLY_INTO_WALL), ArsArmorSetConfig::preventKinetic, () -> event.setCanceled(true));
        processArmorEvent(entity, EquipmentSlot.CHEST, () -> event.getSource().is(DamageTypeTags.IS_FIRE), ArsArmorSetConfig::preventFire, () -> event.setCanceled(true));
        processArmorEvent(entity, EquipmentSlot.CHEST, () -> event.getSource().is(DamageTypes.DRAGON_BREATH), ArsArmorSetConfig::preventDragonsBreath, () -> event.setCanceled(true));
        processArmorEvent(entity, EquipmentSlot.LEGS, () -> event.getSource().is(DamageTypes.WITHER), ArsArmorSetConfig::preventWither, () -> event.setCanceled(true));
    }

    @SubscribeEvent
    public static void onEffectApplied(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        processArmorEvent(entity, EquipmentSlot.LEGS, () -> instance.getEffect().equals(MobEffects.WITHER), ArsArmorSetConfig::preventWither, () -> event.setResult(MobEffectEvent.Applicable.Result.DENY));
        processArmorEvent(entity, EquipmentSlot.LEGS, () -> instance.getEffect().equals(MobEffects.LEVITATION), ArsArmorSetConfig::preventLevitation, () -> event.setResult(MobEffectEvent.Applicable.Result.DENY));
    }

    private static void processArmorEvent(LivingEntity entity, EquipmentSlot slot, Supplier<Boolean> predicate, Function<ArsArmorSetConfig, Supplier<Boolean>> configFn, Runnable cancel) {
        if (entity.getItemBySlot(slot).getItem() instanceof ArsArmorItem armorItem && configFn.apply(armorItem.getConfig()).get() && predicate.get()) {
            cancel.run();
        }
    }
}
