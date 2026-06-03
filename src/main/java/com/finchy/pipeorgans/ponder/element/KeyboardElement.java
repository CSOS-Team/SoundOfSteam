package com.finchy.pipeorgans.ponder.element;

import com.finchy.pipeorgans.PipeOrgans;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.gui.element.BoxElement;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.element.AnimatedOverlayElementBase;
import net.createmod.ponder.foundation.element.TextWindowElement;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class KeyboardElement extends AnimatedOverlayElementBase {
    
    public KeyboardElement(Vec3 vec) {
        this.vec = vec;
    }
    
    @Nullable
    Vec3 vec;
    
    public int key = 0; // 0-12; NONE + C-B
    private KeysSprite sprite = new KeysSprite();
    
    @Override
    public void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade) {
        
        if (fade < 1 / 16f)
            return;
        PonderScene.SceneTransform transform = scene.getTransform();
        Vec2 sceneToScreen = vec != null ? transform.sceneToScreen(vec, partialTicks)
                : new Vec2(screen.width / 2f, (screen.height - 200) / 2f - 8);

        boolean settled = transform.xRotation.settled() && transform.yRotation.settled();
        float pY = settled ? (int) sceneToScreen.y : sceneToScreen.y;

        float yDiff = (screen.height / 2f - sceneToScreen.y - 10) / 100f;
        float targetX = (screen.width * Mth.lerp(yDiff * yDiff, 6f / 8, 5f / 8));
        
        targetX = Math.min(targetX, sceneToScreen.x + 50);

        if (settled)
            targetX = (int) targetX;

        int boxWidth = 66;
        int boxHeight = 35;

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(targetX, pY, 400);

        new BoxElement()
                .withBackground(PonderUI.BACKGROUND_FLAT)
                .gradientBorder(TextWindowElement.COLOR_WINDOW_BORDER)
                .at(-10, 3, -101)
                .withBounds(boxWidth, boxHeight - 1)
                .render(graphics);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, fade);
        
        RenderSystem.setShaderTexture(0, KeysSprite.SPRITE_ATLAS);
        sprite.render(graphics, -9, 4);
        
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();    
        
        poseStack.popPose();
        
    }
    
    private class KeysSprite implements ScreenElement {
        private static final ResourceLocation SPRITE_ATLAS = PipeOrgans.asResource("textures/ponder/piano_sprites.png");
        private static final int SPRITE_ATLAS_WIDTH = 64;
        private static final int SPRITE_ATLAS_HEIGHT = 416;
        private static final int width = 64;
        private static final int height = 32;
        
        @Override
        public void render(GuiGraphics graphics, int x, int y) {
            graphics.blit(SPRITE_ATLAS, x,  y, 0, 0, 32*KeyboardElement.this.key, width, height, SPRITE_ATLAS_WIDTH, SPRITE_ATLAS_HEIGHT);
        }
    }
}
