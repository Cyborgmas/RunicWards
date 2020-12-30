package com.cyborgmas.runic_wards.handlers;

import com.cyborgmas.runic_wards.RunicWards;
import com.cyborgmas.runic_wards.Util;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RunicWards.MOD_ID)
public class WardedHandler {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        int wards = Util.getWardsValue(entity);
        if (wards == 0)
            return;
        if (WardedItemsManager.isDamageSourceProtected(event.getSource())) {
            // The logic here is copied from CombatRules#getDamageAfterAbsorb
            float val = event.getAmount();
            float f1 = MathHelper.clamp(Math.abs(wards) - val / 2, Math.abs(wards) * 0.2F, 20.0F);
            f1 = wards < 0 ? -f1 : f1; //For negative ward numbers: the % increase is smaller as you take more damage.
            // Taking 5 * 1 dmg will hurt for ~7dmg but taking 1 * 5 dmg will hurt for 6dmg.
            event.setAmount(val * (1.0F - f1 / 25.0F));
        }
    }

    @SubscribeEvent
    public static void addModifier(ItemAttributeModifierEvent event) {
        Item item = event.getItemStack().getItem();
        Integer val = item instanceof ArmorItem && ((ArmorItem) item).getEquipmentSlot() == event.getSlotType() ?
                WardedItemsManager.getRunicWardedItems().get(item) : null;
        if (val != null)
            event.addModifier(RunicWards.RUNIC_WARD.get(), new AttributeModifier(Util.getUUID(event.getItemStack()), "Runic ward modifier", val, AttributeModifier.Operation.ADDITION));
    }
}
