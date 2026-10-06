package com.finchy.pipeorgans.content.noteLink;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class NoteLinkRenderer {
    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        HitResult target = mc.hitResult;
        if (!(target instanceof BlockHitResult result)) return;

        ClientLevel world = mc.level;
        BlockPos pos = result.getBlockPos();
        
        NoteLinkBehaviour behaviour = BlockEntityBehaviour.get(world, pos, NoteLinkBehaviour.TYPE);
        if (behaviour == null)
            return;

        //todo: fix label rendering instead of clipboard tooltip
        Component label = Component.translatable("block.pipeorgans.note_link.key_slot.label");

        AABB aabb = new AABB(Vec3.ZERO, Vec3.ZERO).inflate(.25f);
        boolean hit = behaviour.testHit(target.getLocation());
        ValueBoxTransform transform = behaviour.keySlot;
        
        ValueBox box = new ValueBox(label, aabb, pos).passive(!hit);
        boolean empty = behaviour.keyFrequency.getStack().isEmpty();

        if (!empty)
            box.wideOutline();

        Outliner.getInstance().showOutline(transform, box.transform(transform))
                .highlightFace(result.getDirection());
        
        if (!hit)
            return;

        List<MutableComponent> tip = new ArrayList<>();
        tip.add(label.copy());
        tip.add(CreateLang.translateDirect(empty ? "logistics.filter.click_to_set" : "logistics.filter.click_to_replace"));
        CreateClient.VALUE_SETTINGS_HANDLER.showHoverTip(tip);
    }

    public static void renderOnBlockEntity(SmartBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be == null || be.isRemoved())
            return;

        Entity camEntity = Minecraft.getInstance().cameraEntity;
        float maxDistance = AllConfigs.client().filterItemRenderDistance.getF();

        if (!be.isVirtual() && camEntity != null && camEntity.position()
                .distanceToSqr(VecHelper.getCenterOf(be.getBlockPos())) > (maxDistance * maxDistance))
            return;

        NoteLinkBehaviour behaviour = be.getBehaviour(NoteLinkBehaviour.TYPE);
        if (behaviour == null)
            return;

        ValueBoxTransform transform = behaviour.keySlot;
        ItemStack stack = behaviour.keyFrequency.getStack();

        ms.pushPose();
        transform.transform(be.getLevel(), be.getBlockPos(), be.getBlockState(), ms);
        ValueBoxRenderer.renderItemIntoValueBox(stack, ms, buffer, light, overlay);
        ms.popPose();
    }
}
