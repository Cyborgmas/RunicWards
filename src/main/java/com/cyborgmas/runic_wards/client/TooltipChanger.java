package com.cyborgmas.runic_wards.client;

import com.cyborgmas.runic_wards.RunicWards;
import com.cyborgmas.runic_wards.handlers.WardedItemsManager;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RunicWards.MOD_ID, value = Dist.CLIENT)
public class TooltipChanger {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Integer val = WardedItemsManager.getRunicWardedItems().get(event.getItemStack().getItem());
        if(val == null)
            return;
        event.getToolTip().add(4, new StringTextComponent("+" + val + " Wards").setStyle(Style.EMPTY.setFormatting(TextFormatting.DARK_PURPLE)));
    }
}
