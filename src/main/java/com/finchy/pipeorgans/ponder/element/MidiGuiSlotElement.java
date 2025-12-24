package com.finchy.pipeorgans.ponder.element;

import com.finchy.pipeorgans.PipeOrgans;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.element.AnimatedOverlayElementBase;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class MidiGuiSlotElement extends AnimatedOverlayElementBase {
    private final Vec3 sceneSpace;
    private final Pointing direction;
    private final ItemStack item;

    private static final MidiGuiSlotSprite SPRITE = new MidiGuiSlotSprite();

    public MidiGuiSlotElement(Vec3 sceneSpace, Pointing direction, ItemStack item) {
        this.sceneSpace = sceneSpace;
        this.direction = direction;
        this.item = item;
    }

    @Override
    public void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade) {
        int width = MidiGuiSlotSprite.width+2;
        int height = MidiGuiSlotSprite.height+2;

        float xFade = direction == Pointing.RIGHT ? -1 : direction == Pointing.LEFT ? 1 : 0;
        float yFade = direction == Pointing.DOWN ? -1 : direction == Pointing.UP ? 1 : 0;
        xFade *= 10 * (1 - fade);
        yFade *= 10 * (1 - fade);

        if (fade < 1 / 16f)
            return;
        Vec2 sceneToScreen = scene.getTransform()
                .sceneToScreen(sceneSpace, partialTicks);

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(sceneToScreen.x + xFade, sceneToScreen.y + yFade, 400);

        PonderUI.renderSpeechBox(graphics, 0, 0, width, height, false, direction, true);
        poseStack.translate(0, 0, 100);

        poseStack.pushPose();
        SPRITE.render(graphics, 1, 1);
        poseStack.popPose();

        if (!item.isEmpty()) {
            GuiGameElement.of(item)
                    .<GuiGameElement.GuiRenderBuilder>at(21, 2)
                    .render(graphics);
            RenderSystem.disableDepthTest();
        }

        poseStack.popPose();
    }

    private static class MidiGuiSlotSprite implements ScreenElement {
        private static final ResourceLocation SPRITE_ATLAS = PipeOrgans.asResource("textures/ponder/ponder_sprites.png");
        private static final int SPRITE_ATLAS_SIZE = 64;
        private static final int width = 37;
        private static final int height = 18;

        @Override
        public void render(GuiGraphics graphics, int x, int y) {
            graphics.blit(SPRITE_ATLAS, x,  y, 0, 0, 0, width, height, SPRITE_ATLAS_SIZE, SPRITE_ATLAS_SIZE);
        }
    }
}