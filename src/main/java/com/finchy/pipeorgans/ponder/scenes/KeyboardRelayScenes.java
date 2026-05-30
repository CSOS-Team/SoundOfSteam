package com.finchy.pipeorgans.ponder.scenes;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlock;
import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.PonderUtil;
import com.finchy.pipeorgans.util.Keybinding;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class KeyboardRelayScenes {
    public static void keyboardRelaySetup(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("keyboard_relay_setup", "Setting up and using the Keyboard Relay");
        scene.configureBasePlate(0, 0, 5);

        // Begin building animation by showing base layer
        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        BlockPos keyboardRelay = util.grid().at(2, 1, 1);

        BlockPos diapason1 = util.grid().at(4, 2, 4); //F#
        BlockPos diapason2 = util.grid().at(3, 2, 4); //F
        BlockPos diapason3 = util.grid().at(2, 2, 4); //E
        BlockPos diapason4 = util.grid().at(1, 2, 4); //D#
        BlockPos diapason5 = util.grid().at(0, 2, 4); //D
        
        BlockPos windchest5 = util.grid().at(0, 1, 4); //D
        
        BlockPos link1 = util.grid().at(4, 1, 3); //F#
        BlockPos link2 = util.grid().at(3, 1, 3); //F
        BlockPos link3 = util.grid().at(2, 1, 3); //E
        BlockPos link4 = util.grid().at(1, 1, 3); //D#
        BlockPos link5 = util.grid().at(0, 1, 3); //D

        Vec3 relayTop = util.vector().topOf(keyboardRelay);
        Vec3 linksVec = util.vector().blockSurface(util.grid().at(4, 1, 3), Direction.SOUTH)
                .add(0, 0, -3 / 16f);



        PonderUtil.revealBlock(scene, util, keyboardRelay, PonderTimings.BUILD_STEP); // reveal Keyboard Relay

        for (int x=4; x>=0; x--) { // reveal windchests, links and pipes in columns
            PonderUtil.revealBlocks(scene, util, util.select().fromTo(x, 1, 3, x, 4, 4), PonderTimings.BUILD_STEP);
        }
        scene.idle(PonderTimings.BUILD_STEP);

        scene.idle(6);

        //What is the keyboard relay?
        scene.addKeyframe();
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The Keyboard Relay is used to play your organ in real time using a MIDI keyboard")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardRelay, Direction.WEST));


        scene.idle(PonderTimings.READING_WINDOW);

        //Oh! Thanks magical voice that explains stuff... but how do I set it up?
        String midiConfigKeybind = Keybinding.MIDI_CONFIG_KEY.getKey().getDisplayName().getString();
        scene.addKeyframe();
        scene.overlay().showText(PonderTimings.READING_TIME-20)
                .text("You can set it up by pressing the '%s' key", midiConfigKeybind)
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardRelay, Direction.WEST));

        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);

        //show the semicolon icon
        PonderUtil.showCustomPonderIcon(scene, relayTop, Pointing.DOWN, 16, 16, 37, 0, PonderTimings.READING_TIME-20-PonderTimings.CONTEXT_INFO_BUFFER);


        scene.idle(PonderTimings.READING_WINDOW-20);

        //TEXT 3
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Then selecting your MIDI keyboard and clicking the save icon")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardRelay, Direction.WEST));

        //Save icon here?!?!?!

        scene.idle(PonderTimings.READING_WINDOW);


        //Filtering frequencies
        scene.addKeyframe();
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("If your MIDI keyboard has multiple output channels, you can filter them by Sneaking and Right-Clicking...")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardRelay, Direction.WEST));

        scene.idle(PonderTimings.READING_WINDOW/2);

        //Show shift+click icon
        scene.overlay().showControls(relayTop, Pointing.DOWN, PonderTimings.READING_TIME-(PonderTimings.READING_WINDOW/2))
                .whileSneaking().rightClick();

        scene.idle(PonderTimings.READING_WINDOW/2);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("...and setting the channel to a frequency using items")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardRelay, Direction.WEST));

        scene.idle(PonderTimings.READING_WINDOW/2);

        PonderUtil.showMidiGuiSlot(scene, keyboardRelay.getCenter().add(0, 0.5, 0), Pointing.DOWN, new ItemStack(AllItems.ZINC_INGOT), 1,
                PonderTimings.READING_WINDOW/2
        );
        for (int i = 0; i <= 4; i++) {
            Vec3 redstoneLinkVec = util.vector().topOf(i, 1, 3).subtract(0, 0.35, -0.3);
            scene.overlay().showFilterSlotInput(redstoneLinkVec, PonderTimings.READING_WINDOW/2);
        }
        //This next line displays a zinc ingot overtop the links. Don't think it's needed, but... keeping it just because
        //scene.overlay().showControls(linksVec.add(0, 0.15, 0), Pointing.DOWN, PonderTimings.READING_WINDOW/2).withItem(AllItems.ZINC_INGOT.asStack());
        scene.idle(PonderTimings.READING_WINDOW/2);
        scene.idle(PonderTimings.READING_BUFFER);

        //Using it

        scene.addKeyframe();
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Activate it by standing within 5 blocks and Right-Clicking it")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardRelay, Direction.WEST));

        scene.idle(PonderTimings.READING_WINDOW/2);
        //right click :)
        scene.overlay().showControls(relayTop, Pointing.DOWN, PonderTimings.READING_TIME-(PonderTimings.READING_WINDOW/2))
                .rightClick();

        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.ACTIVE), false);

        scene.idle(PonderTimings.READING_WINDOW/2);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Now, all the notes you play on your MIDI keyboard...")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardRelay, Direction.WEST));

        scene.idle(PonderTimings.READING_WINDOW/2);
        //Cycle through links
        //Number one
        scene.world().toggleRedstonePower(util.select().fromTo(link1, diapason1));
        scene.effects().indicateRedstone(link1);
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(10);
        scene.world().toggleRedstonePower(util.select().fromTo(link1, diapason1));
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(5);
        //Number two
        scene.world().toggleRedstonePower(util.select().fromTo(link2, diapason2));
        scene.effects().indicateRedstone(link2);
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(10);
        scene.world().toggleRedstonePower(util.select().fromTo(link2, diapason2));
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(5);
        //Number three
        scene.world().toggleRedstonePower(util.select().fromTo(link3, diapason3));
        scene.effects().indicateRedstone(link3);
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(10);
        scene.world().toggleRedstonePower(util.select().fromTo(link3, diapason3));
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(5);
        //Number four
        scene.world().toggleRedstonePower(util.select().fromTo(link4, diapason4));
        scene.effects().indicateRedstone(link4);
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(10);
        scene.world().toggleRedstonePower(util.select().fromTo(link4, diapason4));
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(5);
        //Number five
        scene.world().toggleRedstonePower(util.select().fromTo(link5, diapason5));
        scene.effects().indicateRedstone(link5);
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(10);
        scene.world().toggleRedstonePower(util.select().position(link5)); //broke into two lines so the windchest doesn't visibly toggle redstone
        scene.world().toggleRedstonePower(util.select().position(diapason5));
        scene.world().modifyBlock(windchest5, s -> s.setValue(BlockStateProperties.POWERED, true), false); // ensure the windchest stays powered
        scene.world().modifyBlock(keyboardRelay,s -> s.cycle(KeyboardRelayBlock.TRANSMITTING), false);
        scene.idle(5);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("...will trigger the corresponding redstone links!")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardRelay, Direction.WEST));

        scene.idle(PonderTimings.READING_TIME);
        scene.idle(PonderTimings.BUILD_FINISH);
        //the end!!!!!!
        scene.markAsFinished();
    }
}
