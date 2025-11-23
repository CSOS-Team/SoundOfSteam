package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayMenu;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayScreen;
import com.finchy.pipeorgans.content.midi.rollpuncher.RollPuncherMenu;
import com.finchy.pipeorgans.content.midi.rollpuncher.RollPuncherScreen;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarMenu;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarScreen;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class AllMenuTypes {

    public static final MenuEntry<TrackerBarMenu> TRACKER_BAR_MENU =
            register("tracker_bar", TrackerBarMenu::new, () -> TrackerBarScreen::new);

    public static final MenuEntry<KeyboardRelayMenu> KEYBOARD_RELAY_MENU =
            register("keyboard_relay", KeyboardRelayMenu::new, () -> KeyboardRelayScreen::new);

    public static final MenuEntry<RollPuncherMenu> ROLL_PUNCHER_MENU =
            register("roll_puncher", RollPuncherMenu::new, () -> RollPuncherScreen::new);

    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
            String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return PipeOrgans.registrate().menu(name, factory, screenFactory).register();
    }

    public static void register() {
    }

}
