package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterMenu;
import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterScreen;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarMenu;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarScreen;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class AllMenuTypes {

    public static final MenuEntry<StopMasterMenu> STOP_MASTER_MENU =
            register("stop_master_menu", StopMasterMenu::new, () -> StopMasterScreen::new);

    public static final MenuEntry<TrackerBarMenu> TRACKER_BAR_MENU =
            register("tracker_bar", TrackerBarMenu::new, () -> TrackerBarScreen::new);

    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
            String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return PipeOrgans.registrate().menu(name, factory, screenFactory).register();
    }

    public static void register() {
    }

}
