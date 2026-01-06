package com.finchy.pipeorgans.data;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import com.finchy.pipeorgans.data.PipeOrgansAdvancementGenerator;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Advancements extends ForgeAdvancementProvider
{
    public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, existingFileHelper, List.of(new PipeOrgansAdvancementGenerator()));
    }
}
