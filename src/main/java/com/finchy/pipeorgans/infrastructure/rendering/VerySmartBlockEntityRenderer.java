package com.finchy.pipeorgans.infrastructure.rendering;

import com.finchy.pipeorgans.infrastructure.itemValueBox.ItemValueBoxRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class VerySmartBlockEntityRenderer<T extends SmartBlockEntity> extends SmartBlockEntityRenderer<T> {
    public VerySmartBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(T blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);
        ItemValueBoxRenderer.renderOnBlockEntity(blockEntity, partialTicks, ms, buffer, light, overlay);
    }
}
