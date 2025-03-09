package com.finchy.pipeorgans.item;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class GenericCopperHornItem extends Item {

    protected final SoundEvent bassTune;
    protected final SoundEvent melodyTune;
    protected final SoundEvent harmonyTune;
    protected final String tuneTranslation;

    public GenericCopperHornItem(Properties properties, RegistryObject<SoundEvent> bassTune, RegistryObject<SoundEvent> melodyTune, RegistryObject<SoundEvent> harmonyTune, String tuneTranslation) {
        super(properties);

        this.bassTune = bassTune.get();
        this.melodyTune = melodyTune.get();
        this.harmonyTune = harmonyTune.get();
        this.tuneTranslation = tuneTranslation;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {

            player.startUsingItem(hand);

            if (player.isCrouching()) {
                level.playSound(null, player, bassTune, SoundSource.PLAYERS, 1.f, 1.f);
            } else if (player.getYHeadRot() < -45) {
                level.playSound(null, player, harmonyTune, SoundSource.PLAYERS, 1.f, 1.f);
            } else {
                level.playSound(null, player, melodyTune, SoundSource.PLAYERS, 1.f, 1.f);
            }

            player.getCooldowns().addCooldown(this, 80);

            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 20;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        MutableComponent component = Component.translatable("item." + PipeOrgans.MOD_ID + "." + tuneTranslation);
        tooltipComponents.add(component.withStyle(ChatFormatting.GRAY));
    }
}
