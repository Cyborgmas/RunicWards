package com.cyborgmas.runic_wards;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, RunicWards.MOD_ID);

    public static final RegistryObject<Attribute> RUNIC_WARD = ATTRIBUTES.register("generic.runic_ward", () -> new RangedAttribute("attribute.name.generic.runic_ward", 0, 0, Util.RUNIC_WARD_MAX).setShouldWatch(true));

    public static void registerAll(IEventBus bus) {
        ATTRIBUTES.register(bus);
    }
}
