package com.cyborgmas.runic_wards;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.cyborgmas.runic_wards.RunicWards.MOD_ID;

public class Util {
    public static final Integer RUNIC_WARD_MAX = 30;
    public static final Integer RUNIC_WARD_MIN = -30;

    public static int getWardsValue(LivingEntity entity) {
        if (entity instanceof PlayerEntity) // For now only handle player entities
            return MathHelper.floor(entity.getAttributeValue(RunicWards.RUNIC_WARD.get()));
        return 0;
    }

    public static ResourceLocation getId(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static final Map<String, UUID> NAME_UUID_MAP = new HashMap<>();

    public static UUID getUUID(ItemStack stack) {
        return NAME_UUID_MAP.computeIfAbsent(stack.getItem().getRegistryName().toString(), s -> UUID.nameUUIDFromBytes(s.getBytes()));
    }
}
