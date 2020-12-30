package com.cyborgmas.runic_wards;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;

@Mod(RunicWards.MOD_ID)
public class RunicWards
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "runic_wards";

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, RunicWards.MOD_ID);

    public static final RegistryObject<Attribute> RUNIC_WARD = ATTRIBUTES.register("generic.runic_ward", () ->
            new RangedAttribute("attribute.name.generic.runic_ward", 0, Util.RUNIC_WARD_MIN, Util.RUNIC_WARD_MAX).setShouldWatch(true));

    public RunicWards() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        ATTRIBUTES.register(modbus);
        modbus.addListener(RunicWards::addPlayerEntityAttributes);
    }

    @SuppressWarnings("unchecked")
    public static void addPlayerEntityAttributes(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                AttributeModifierMap oldmap;

                Field forgeMapField = GlobalEntityTypeAttributes.class.getDeclaredField("FORGE_ATTRIBUTES");
                forgeMapField.setAccessible(true);
                oldmap = ((Map<EntityType<? extends LivingEntity>, AttributeModifierMap>) forgeMapField.get(null)).get(EntityType.PLAYER);

                if (oldmap == null) {
                    Field vanillaMapField = ObfuscationReflectionHelper.findField(GlobalEntityTypeAttributes.class, "field_233833_b_");
                    vanillaMapField.setAccessible(true);
                    oldmap = ((Map<EntityType<? extends LivingEntity>, AttributeModifierMap>) vanillaMapField.get(null)).get(EntityType.PLAYER);
                }

                Field internalMapField = ObfuscationReflectionHelper.findField(AttributeModifierMap.class, "field_233802_a_");
                internalMapField.setAccessible(true);
                Map<Attribute, ModifiableAttributeInstance> internalMap = (Map<Attribute, ModifiableAttributeInstance>) internalMapField.get(oldmap);

                AttributeModifierMap.MutableAttribute builder = AttributeModifierMap.createMutableAttribute();

                for (Map.Entry<Attribute, ModifiableAttributeInstance> e : internalMap.entrySet())
                    builder.createMutableAttribute(e.getKey(), e.getValue().getBaseValue());

                GlobalEntityTypeAttributes.put(EntityType.PLAYER, builder.createMutableAttribute(RUNIC_WARD.get(), 0).create());
            } catch (Exception e) {
                LOGGER.fatal("Could not properly add the runic ward attribute to the player");
                throw new RuntimeException("Failed to load runic wards: ", e);
            }
        });
    }
}
