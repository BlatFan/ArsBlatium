package ru.blatfan.ars_blatium.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.perk.*;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import ru.blatfan.ars_blatium.config.ArsArmorSetConfig;
import ru.blatfan.ars_blatium.items.client.ArsArmorRenderer;
import ru.blatfan.ars_blatium.items.client.GenericArmorModel;
import ru.blatfan.blatium.Blatium;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ArsArmorItem extends ArmorItem implements GeoItem {
    private final int tier;
    
    private final GenericModel<ArsArmorItem> model;
    private final ArsArmorSetConfig config;
    
    public ArsArmorItem(ArsArmorSetConfig config, Type slot, int tier) {
        super(
            Material.INSTANCE,
            slot,
            ItemsRegistry.defaultItemProperties()
                .stacksTo(1)
                .fireResistant()
                .rarity(Objects.equals(config.name(), "blatium") ? Blatium.RARITY_BLATIUM : Blatium.RARITY_NLIUM)
        );
        this.tier = tier;
        this.config = config;
        this.model = new GenericArmorModel<ArsArmorItem>(config.name()).withEmptyAnim();
    }
    
    @Override
    public void verifyTagAfterLoad(CompoundTag compoundTag) {
        super.verifyTagAfterLoad(compoundTag);
        compoundTag.getCompound("an_stack_perks").putInt("tier", tier);
    }
    
    public ArsArmorSetConfig getConfig() {
        return config;
    }
    
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    
    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return true;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, world, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.blatium.unbreakable").withStyle(style -> style.withColor(Blatium.COLOR_BLATIUM)));
        IPerkProvider<ItemStack> data = PerkRegistry.getPerkProvider(this);
        if (data != null) {
            if (data.getPerkHolder(stack) instanceof ArmorPerkHolder armorPerkHolder) {
                tooltipComponents.add(Component.translatable("ars_nouveau.tier", armorPerkHolder.getTier() + 1).withStyle(ChatFormatting.GOLD));
            }
            data.getPerkHolder(stack).appendPerkTooltip(tooltipComponents, stack);
        }
    }
    
    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
    
    AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
    
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private ArsArmorRenderer renderer;
            
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (renderer == null) {
                    renderer = new ArsArmorRenderer(getArmorModel());
                }
                renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return renderer;
            }
        });
    }
    
    private GenericModel<ArsArmorItem> getArmorModel() {
        return model;
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributes = new ImmutableMultimap.Builder();
        attributes.putAll(getDefaultAttributeModifiers(slot));
        if (slot != getEquipmentSlot()) {
            return attributes.build();
        }
        
        IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(stack);
        if (perkHolder == null) return attributes.build();
        for (PerkInstance perkInstance : perkHolder.getPerkInstances()) {
            IPerk perk = perkInstance.getPerk();
            attributes.putAll(perk.getModifiers(slot, stack, perkInstance.getSlot().value));
        }
        return attributes.build();
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != getEquipmentSlot()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            return builder.build();
        }
        return config.buildAttributeMap(this);
    }
    
    public static class Material implements ArmorMaterial {
        public static final Material INSTANCE = new Material();
        
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return 0;
        }
        
        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return 0;
        }
        
        @Override
        public int getEnchantmentValue() {
            return 30;
        }
        
        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }
        
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ItemsRegistry.MAGE_FIBER);
        }
        
        @Override
        public String getName() {
            return "arcanist";
        }
        
        @Override
        public float getToughness() {
            return 0;
        }
        
        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }
}