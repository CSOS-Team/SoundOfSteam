package com.finchy.pipeorgans.midi;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

//MIDI pitches -> Minecraft items
public class PitchMapping {

    private static final Map<Integer, Item> pitchMap = new HashMap<>();

    // set 6-114 (F#-1 to F#8)
    static {
        pitchMap.put(6, Items.RED_CONCRETE); // F#(-1)
        pitchMap.put(7, Items.ORANGE_CONCRETE);
        pitchMap.put(8, Items.YELLOW_CONCRETE);
        pitchMap.put(9, Items.LIME_CONCRETE);
        pitchMap.put(10, Items.GREEN_CONCRETE);
        pitchMap.put(11, Items.CYAN_CONCRETE);
        pitchMap.put(12, Items.LIGHT_BLUE_CONCRETE);
        pitchMap.put(13, Items.BLUE_CONCRETE);
        pitchMap.put(14, Items.PURPLE_CONCRETE);
        pitchMap.put(15, Items.MAGENTA_CONCRETE);
        pitchMap.put(16, Items.PINK_CONCRETE);
        pitchMap.put(17, Items.BROWN_CONCRETE); // F0

        pitchMap.put(18, Items.RED_CONCRETE_POWDER); // F#0
        pitchMap.put(19, Items.ORANGE_CONCRETE_POWDER);
        pitchMap.put(20, Items.YELLOW_CONCRETE_POWDER);
        pitchMap.put(21, Items.LIME_CONCRETE_POWDER);
        pitchMap.put(22, Items.GREEN_CONCRETE_POWDER);
        pitchMap.put(23, Items.CYAN_CONCRETE_POWDER);
        pitchMap.put(24, Items.LIGHT_BLUE_CONCRETE_POWDER);
        pitchMap.put(25, Items.BLUE_CONCRETE_POWDER);
        pitchMap.put(26, Items.PURPLE_CONCRETE_POWDER);
        pitchMap.put(27, Items.MAGENTA_CONCRETE_POWDER);
        pitchMap.put(28, Items.PINK_CONCRETE_POWDER);
        pitchMap.put(29, Items.BROWN_CONCRETE_POWDER); // F1

        pitchMap.put(30, Items.RED_WOOL); // F#1
        pitchMap.put(31, Items.ORANGE_WOOL);
        pitchMap.put(32, Items.YELLOW_WOOL);
        pitchMap.put(33, Items.LIME_WOOL);
        pitchMap.put(34, Items.GREEN_WOOL);
        pitchMap.put(35, Items.CYAN_WOOL);
        pitchMap.put(36, Items.LIGHT_BLUE_WOOL);
        pitchMap.put(37, Items.BLUE_WOOL);
        pitchMap.put(38, Items.PURPLE_WOOL);
        pitchMap.put(39, Items.MAGENTA_WOOL);
        pitchMap.put(40, Items.PINK_WOOL);
        pitchMap.put(41, Items.BROWN_WOOL); // F2

        pitchMap.put(42, Items.RED_STAINED_GLASS); // F#2
        pitchMap.put(43, Items.ORANGE_STAINED_GLASS);
        pitchMap.put(44, Items.YELLOW_STAINED_GLASS);
        pitchMap.put(45, Items.LIME_STAINED_GLASS);
        pitchMap.put(46, Items.GREEN_STAINED_GLASS);
        pitchMap.put(47, Items.CYAN_STAINED_GLASS);
        pitchMap.put(48, Items.LIGHT_BLUE_STAINED_GLASS);
        pitchMap.put(49, Items.BLUE_STAINED_GLASS);
        pitchMap.put(50, Items.PURPLE_STAINED_GLASS);
        pitchMap.put(51, Items.MAGENTA_STAINED_GLASS);
        pitchMap.put(52, Items.PINK_STAINED_GLASS);
        pitchMap.put(53, Items.BROWN_STAINED_GLASS); // F3

        pitchMap.put(54, Items.RED_TERRACOTTA); // F#3
        pitchMap.put(55, Items.ORANGE_TERRACOTTA);
        pitchMap.put(56, Items.YELLOW_TERRACOTTA);
        pitchMap.put(57, Items.LIME_TERRACOTTA);
        pitchMap.put(58, Items.GREEN_TERRACOTTA);
        pitchMap.put(59, Items.CYAN_TERRACOTTA);
        pitchMap.put(60, Items.LIGHT_BLUE_TERRACOTTA);
        pitchMap.put(61, Items.BLUE_TERRACOTTA);
        pitchMap.put(62, Items.PURPLE_TERRACOTTA);
        pitchMap.put(63, Items.MAGENTA_TERRACOTTA);
        pitchMap.put(64, Items.PINK_TERRACOTTA);
        pitchMap.put(65, Items.BROWN_TERRACOTTA); // F4

        pitchMap.put(66, Items.RED_GLAZED_TERRACOTTA); // F#4
        pitchMap.put(67, Items.ORANGE_GLAZED_TERRACOTTA);
        pitchMap.put(68, Items.YELLOW_GLAZED_TERRACOTTA);
        pitchMap.put(69, Items.LIME_GLAZED_TERRACOTTA);
        pitchMap.put(70, Items.GREEN_GLAZED_TERRACOTTA);
        pitchMap.put(71, Items.CYAN_GLAZED_TERRACOTTA);
        pitchMap.put(72, Items.LIGHT_BLUE_GLAZED_TERRACOTTA);
        pitchMap.put(73, Items.BLUE_GLAZED_TERRACOTTA);
        pitchMap.put(74, Items.PURPLE_GLAZED_TERRACOTTA);
        pitchMap.put(75, Items.MAGENTA_GLAZED_TERRACOTTA);
        pitchMap.put(76, Items.PINK_GLAZED_TERRACOTTA);
        pitchMap.put(77, Items.BROWN_GLAZED_TERRACOTTA); // F5

        pitchMap.put(78, Items.RED_CARPET); // F#5
        pitchMap.put(79, Items.ORANGE_CARPET);
        pitchMap.put(80, Items.YELLOW_CARPET);
        pitchMap.put(81, Items.LIME_CARPET);
        pitchMap.put(82, Items.GREEN_CARPET);
        pitchMap.put(83, Items.CYAN_CARPET);
        pitchMap.put(84, Items.LIGHT_BLUE_CARPET);
        pitchMap.put(85, Items.BLUE_CARPET);
        pitchMap.put(86, Items.PURPLE_CARPET);
        pitchMap.put(87, Items.MAGENTA_CARPET);
        pitchMap.put(88, Items.PINK_CARPET);
        pitchMap.put(89, Items.BROWN_CARPET); // F6

        pitchMap.put(90, Items.RED_BED); // F#6
        pitchMap.put(91, Items.ORANGE_BED);
        pitchMap.put(92, Items.YELLOW_BED);
        pitchMap.put(93, Items.LIME_BED);
        pitchMap.put(94, Items.GREEN_BED);
        pitchMap.put(95, Items.CYAN_BED);
        pitchMap.put(96, Items.LIGHT_BLUE_BED);
        pitchMap.put(97, Items.BLUE_BED);
        pitchMap.put(98, Items.PURPLE_BED);
        pitchMap.put(99, Items.MAGENTA_BED);
        pitchMap.put(100, Items.PINK_BED);
        pitchMap.put(101, Items.BROWN_BED); // F7

        pitchMap.put(102, Items.RED_BANNER); // F#7
        pitchMap.put(103, Items.ORANGE_BANNER);
        pitchMap.put(104, Items.YELLOW_BANNER);
        pitchMap.put(105, Items.LIME_BANNER);
        pitchMap.put(106, Items.GREEN_BANNER);
        pitchMap.put(107, Items.CYAN_BANNER);
        pitchMap.put(108, Items.LIGHT_BLUE_BANNER);
        pitchMap.put(109, Items.BLUE_BANNER);
        pitchMap.put(110, Items.PURPLE_BANNER);
        pitchMap.put(111, Items.MAGENTA_BANNER);
        pitchMap.put(112, Items.PINK_BANNER);
        pitchMap.put(113, Items.BROWN_BANNER); // F8

        pitchMap.put(114, Items.BLACK_BANNER); // F#8
    }

    public static ItemStack getStack(int pitch) {
        if (!pitchMap.containsKey(pitch)) {
            PipeOrgans.LOGGER.error("Pitch {} out of range for pitch map", pitch);
            return new ItemStack(Items.LIGHT_BLUE_TERRACOTTA);
        }
        return new ItemStack(pitchMap.get(pitch));
    }
}
