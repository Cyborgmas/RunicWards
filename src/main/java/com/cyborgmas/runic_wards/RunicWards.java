package com.cyborgmas.runic_wards;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RunicWards.MOD_ID)
public class RunicWards
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "runic_wards";

    public RunicWards() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.registerAll(modbus);
        modbus.addListener(RunicWards::addPlayerEntityAttributes);
    }

    public static void addPlayerEntityAttributes(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            GlobalEntityTypeAttributes.put(EntityType.PLAYER, PlayerEntity.func_234570_el_().createMutableAttribute(Registration.RUNIC_WARD.get(), 0).create());
        });
    }
}
