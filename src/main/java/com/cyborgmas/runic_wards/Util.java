package com.cyborgmas.runic_wards;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import static com.cyborgmas.runic_wards.RunicWards.MOD_ID;

public class Util {
    public static final Integer RUNIC_WARD_MAX = 64;


    public static int getWardsValue(LivingEntity entity) {
        if (entity instanceof PlayerEntity) // For now only handle player entities
            return MathHelper.floor(entity.getAttributeValue(Registration.RUNIC_WARD.get()));
        return 0;
    }

    public static ResourceLocation getId(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
