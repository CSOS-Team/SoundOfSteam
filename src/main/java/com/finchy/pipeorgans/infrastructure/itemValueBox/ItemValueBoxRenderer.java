package com.finchy.pipeorgans.infrastructure.itemValueBox;

import com.finchy.pipeorgans.PipeOrgans;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class ItemValueBoxRenderer {
    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        HitResult target = mc.hitResult;
        if (!(target instanceof BlockHitResult result)) return;

        ClientLevel world = mc.level;
        BlockPos pos = result.getBlockPos();
        ItemValueBoxBehaviour behaviour = BlockEntityBehaviour.get(world, pos, ItemValueBoxBehaviour.TYPE);
        if (behaviour == null) return;
        int hitIndex = behaviour.testHit(target.getLocation());

        for (int i = 0; i < behaviour.boxGroups.size(); i++) {
            var boxGroup = behaviour.boxGroups.get(i);
            for (var transform : boxGroup.transforms()) {
                AABB aabb = new AABB(Vec3.ZERO, Vec3.ZERO).inflate(.25f);
                boolean hit = hitIndex == i;

                ValueBox box = new ValueBox(boxGroup.label(), aabb, pos).passive(!hit);
                boolean empty = boxGroup.retriever().getItemStack().isEmpty();
                if (!empty)
                    box.wideOutline();

                Outliner.getInstance().showOutline(transform, box.transform(transform)).highlightFace(result.getDirection());

                if (!hit) continue;

                var tooltip = new ArrayList<>(boxGroup.tooltip());
                tooltip.add(0, boxGroup.label().copy());
                tooltip.add(CreateLang.translateDirect(empty ? "logistics.filter.click_to_set" : "logistics.filter.click_to_replace"));
                CreateClient.VALUE_SETTINGS_HANDLER.showHoverTip(tooltip);
            }
        }

    }

    public static void renderOnBlockEntity(SmartBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be == null || be.isRemoved()) return;

        Entity camEntity = Minecraft.getInstance().cameraEntity;
        float maxDistance = AllConfigs.client().filterItemRenderDistance.getF();

        if (!be.isVirtual() && camEntity != null && camEntity.position().distanceToSqr(VecHelper.getCenterOf(be.getBlockPos())) > (maxDistance * maxDistance)) return;
        ItemValueBoxBehaviour behaviour = BlockEntityBehaviour.get(be, ItemValueBoxBehaviour.TYPE);
        if (behaviour == null) return;

        for (var boxGroup : behaviour.boxGroups) {
            ItemStack stack = boxGroup.retriever().getItemStack();
            if (stack.isEmpty()) continue;
            for (var transform : boxGroup.transforms()) {
                ms.pushPose();
                transform.transform(be.getLevel(), be.getBlockPos(), be.getBlockState(), ms);
                ValueBoxRenderer.renderItemIntoValueBox(stack, ms, buffer, light, overlay);
                ms.popPose();
            }
        }
    }
}
