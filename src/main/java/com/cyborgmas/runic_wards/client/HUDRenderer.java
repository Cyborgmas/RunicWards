package com.cyborgmas.runic_wards.client;

import com.cyborgmas.runic_wards.RunicWards;
import com.cyborgmas.runic_wards.Util;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.client.gui.ForgeIngameGui.left_height;

@Mod.EventBusSubscriber(modid = RunicWards.MOD_ID, value = Dist.CLIENT)
public class HUDRenderer {
    private static final ResourceLocation ICONS = Util.getId("textures/gui/armor_wards_icons.png");

    @SubscribeEvent(receiveCanceled = true)
    public static void renderArmorAndWards(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ARMOR)
            return;
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;

        int armor = player.getTotalArmorValue();
        int wards = Util.getWardsValue(player);
        if (armor == 0 && wards == 0)
            return;
        int width = event.getWindow().getScaledWidth();
        int height = event.getWindow().getScaledHeight();

        mc.textureManager.bindTexture(ICONS);
        RenderSystem.enableBlend();

        if (armor != 0)
            render(event.getMatrixStack(), armor, width, height, false, false);
        if (wards != 0)
            render(event.getMatrixStack(), wards, width, height, true, armor != 0);

        RenderSystem.disableBlend();
        mc.textureManager.bindTexture(AbstractGui.GUI_ICONS_LOCATION);

        left_height += 10;
        event.setCanceled(true);
    }

    private static void render(MatrixStack mStack, int value, int width, int height, boolean ward, boolean onRight) {
        int left = width / 2 - 91;
        left += onRight ? (8 * 5) : 0;
        int top = height - left_height;

        int texY = ward ? 9 : 0;

        for (int i = 1; i < 20; i += 4)
        {
            int texX = 0;

            if (i < value)
            {
                if ((value - i + 1) >= 4)
                    texX = 4;
                else if ((value - i + 1) % 3 == 0)
                    texX = 3;
                else
                    texX = 2;
            }
            else if (i == value)
            {
                texX = 1;
            }

            AbstractGui.blit(mStack, left, top, 0, texX * 9, texY, 9, 9, 256, 256);
            left += 8;
        }
    }
}
