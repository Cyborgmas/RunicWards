package com.cyborgmas.runic_wards.handlers;

import com.cyborgmas.runic_wards.RunicWards;
import com.cyborgmas.runic_wards.Util;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RunicWards.MOD_ID)
public class DamageHandler {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        int wards = Util.getWardsValue(entity);
        if (wards == 0)
            return;
        if (WardedItemsManager.isDamageSourceProtected(event.getSource())) {
            // The logic here is copied from CombatRules#getDamageAfterAbsorb
            float val = event.getAmount();
            float f1 = MathHelper.clamp(wards - val / 2, wards * 0.2F, 20.0F);
            event.setAmount(val * (1.0F - f1 / 25.0F));
        }
    }
}
