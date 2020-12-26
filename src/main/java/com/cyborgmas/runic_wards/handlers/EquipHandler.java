package com.cyborgmas.runic_wards.handlers;

import com.cyborgmas.runic_wards.Registration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class EquipHandler {
    public static final Map<String, UUID> NAME_UUID_MAP = new HashMap<>();

    public static UUID getUUID(ItemStack stack) {
        return NAME_UUID_MAP.computeIfAbsent(stack.getItem().getRegistryName().toString(), s -> UUID.nameUUIDFromBytes(s.getBytes()));
    }

    @SubscribeEvent
    public static void onEquip(LivingEquipmentChangeEvent event) {
        if (event.getSlot().getSlotType() != EquipmentSlotType.Group.ARMOR)
            return;
        LivingEntity wearer = event.getEntityLiving();
        change(wearer, event.getFrom(), true);
        change(wearer, event.getTo()  , false);
    }

    public static void change(LivingEntity wearer, ItemStack armor, boolean removed) {
        if (WardedItemsManager.getRunicWardedItems().containsKey(armor.getItem())) {
            ModifiableAttributeInstance instance = wearer.getAttribute(Registration.RUNIC_WARD.get());
            if (instance != null) {
                int val = WardedItemsManager.getRunicWardedItems().get(armor.getItem());
                if (!removed) // Non persistent as this event is called when joining a world when equipped.
                    instance.applyNonPersistentModifier(new AttributeModifier(getUUID(armor), "Runic ward modifier", val, AttributeModifier.Operation.ADDITION));
                else
                    instance.removeModifier(getUUID(armor));
            }
        }
    }
}
