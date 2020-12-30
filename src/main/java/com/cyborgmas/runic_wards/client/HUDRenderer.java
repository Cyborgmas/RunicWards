package com.cyborgmas.runic_wards.client;

import com.cyborgmas.runic_wards.RunicWards;
import com.cyborgmas.runic_wards.Util;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
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

        int armor = mc.player.getTotalArmorValue();
        int wards = Util.getWardsValue(mc.player);

        boolean hasArmor = armor != 0;
        boolean hasWards = wards != 0;

        if (!hasArmor && !hasWards)
            return;
        int width = event.getWindow().getScaledWidth();
        int height = event.getWindow().getScaledHeight();

        mc.textureManager.bindTexture(ICONS);
        RenderSystem.enableBlend();

        if (hasArmor)
            render(event.getMatrixStack(), armor, width, height, false, false);
        if (hasWards)
            render(event.getMatrixStack(), wards, width, height, true, hasArmor);

        RenderSystem.disableBlend();
        mc.textureManager.bindTexture(AbstractGui.GUI_ICONS_LOCATION);

        left_height += 10;
        event.setCanceled(true);
    }

    private static void render(MatrixStack mStack, int value, int width, int height, boolean ward, boolean onRight) {
        int left = width / 2 - 91;
        left += onRight ? (8 * 5) : 0;
        int top = height - left_height;

        boolean neg = value < 0;
        int extra = value - 20;

        value = Math.min(Math.abs(value), 20);

        int initTexY = ward ? neg ? 18 : 9 : 0;

        for (int j = 0; j < 5; j++) {
            int i = 1 + j * (neg ? 2 : 4);
            int texX = 0;
            int texY = initTexY;

            if (!neg) {
                if (extra > j) // handle 20 -> 30
                    texX = (extra - 5) > j ? 6 : 5;
                else {
                    if (i < value) { //handle 0 -> 20
                        if ((value - i + 1) >= 4)
                            texX = 4;
                        else if ((value - i + 1) % 3 == 0)
                            texX = 3;
                        else
                            texX = 2;
                    }
                    else if (i == value) {
                        texX = 1;
                    }
                }
            } else { //handle -10 -> 0
                if (i < value)
                    texX = 2;
                else if (i == value)
                    texX = 1;
            }

            AbstractGui.blit(mStack, left, top, 0, texX * 9, texY, 9, 9, 256, 256);
            left += 8;
        }
    }
}
