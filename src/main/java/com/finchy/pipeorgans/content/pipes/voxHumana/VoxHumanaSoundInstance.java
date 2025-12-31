package com.finchy.pipeorgans.content.pipes.voxHumana;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;


import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class VoxHumanaSoundInstance extends GenericSoundInstance {
    private boolean trem;
    private float tremPhase;

    public VoxHumanaSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition, boolean trem) {
        super(size, worldPosition, getSound(size, trem));
        this.trem = trem;

    }

    public void setTrem(boolean trem) {
        this.trem = trem;
    }

    private static SoundEvent getSound(EPipeSizes.PipeSize size, boolean trem) {
        if (trem) {
            return switch (size) {
                case TINY -> VOX_HUMANA_SUPERHIGH_TREM.get();
                case SMALL -> VOX_HUMANA_HIGH_TREM.get();
                case MEDIUM -> VOX_HUMANA_MEDIUM_TREM.get();
                case LARGE -> VOX_HUMANA_LOW_TREM.get();
                case HUGE -> VOX_HUMANA_DEEP_TREM.get();
            };
        } else {
            return switch (size) {
                case TINY -> VOX_HUMANA_SUPERHIGH.get();
                case SMALL -> VOX_HUMANA_HIGH.get();
                case MEDIUM -> VOX_HUMANA_MEDIUM.get();
                case LARGE -> VOX_HUMANA_LOW.get();
                case HUGE -> VOX_HUMANA_DEEP.get();
            };
        }
    }


    //Code if we decide to synthesize trem


   // @Override
  //  public void tick() {
    //    super.tick();

    //    if (!trem)
    //        return;

    //    tremPhase += 2f; // speed

     //   float tremAmount = 0.03f; // depth (≈ ±3%)
     //   float mod = Mth.sin(tremPhase) * tremAmount;
       // this.pitch += mod;
   // }

}

