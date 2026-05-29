package com.finchy.pipeorgans.ponder.element;

import com.finchy.pipeorgans.PipeOrgans;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.element.AnimatedOverlayElementBase;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;;

/**
 * Uses "textures/ponder/ponder_sprites.png" for the sprites. Top left corner is 0,0
 * @width = the width of your sprite on the x axis
 * @height = the height of your sprite on the y axis
 * @offsetX = the offset of the starting pixel of your sprite going left to right
 * @offsetY = the offset of the starting pixel of your sprite going top to bottom
 */
public class CustomPonderIconElement extends AnimatedOverlayElementBase {
    private final Vec3 sceneSpace;
    private final Pointing direction;

    private final CustomPonderIconSprite sprite;

    public CustomPonderIconElement(Vec3 sceneSpace, Pointing direction, int width, int height, int offsetX, int offsetY) {
        this.sceneSpace = sceneSpace;
        this.direction = direction;

        this.sprite = new CustomPonderIconSprite(width, height, offsetX, offsetY);
    }

    @Override
    public void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade) {

        int width = sprite.width + 2;
        int height = sprite.height + 2;

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

        sprite.render(graphics, 1, 1);

        poseStack.popPose();
    }

    private static class CustomPonderIconSprite implements ScreenElement {

        private static final ResourceLocation SPRITE_ATLAS =
                PipeOrgans.asResource("textures/ponder/ponder_sprites.png");

        private static final int SPRITE_ATLAS_SIZE = 64;

        private final int width;
        private final int height;

        private final int offsetX;
        private final int offsetY;

        public CustomPonderIconSprite(int width, int height, int offsetX, int offsetY) {
            this.width = width;
            this.height = height;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        @Override
        public void render(GuiGraphics graphics, int x, int y) {
            graphics.blit(
                    SPRITE_ATLAS,
                    x,
                    y,
                    offsetX,
                    offsetY,
                    width,
                    height,
                    SPRITE_ATLAS_SIZE,
                    SPRITE_ATLAS_SIZE
            );
        }
    }
}