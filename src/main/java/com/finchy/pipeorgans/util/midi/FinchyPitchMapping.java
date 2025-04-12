package com.finchy.pipeorgans.util.midi;

import net.minecraft.world.item.Items;

public abstract class FinchyPitchMapping extends PitchMapping {

    public FinchyPitchMapping() {
        super("finchy");

    }

    /** This mapping is based on the pipes' octaves, rather than the repetition being centred around C.
     * <br>
     * i.e. F# is the first colour of an octave, and it loops around to F, rather than C to B.
     */
    @Override
    protected void setMappings() {
        // need to set 6-114 (F#-1 to F#8)

        set(6, Items.RED_CONCRETE); // F#(-1)
        set(7, Items.ORANGE_CONCRETE);
        set(8, Items.YELLOW_CONCRETE);
        set(9, Items.LIME_CONCRETE);
        set(10, Items.GREEN_CONCRETE);
        set(11, Items.CYAN_CONCRETE);
        set(12, Items.LIGHT_BLUE_CONCRETE);
        set(13, Items.BLUE_CONCRETE);
        set(14, Items.PURPLE_CONCRETE);
        set(15, Items.MAGENTA_CONCRETE);
        set(16, Items.PINK_CONCRETE);
        set(17, Items.BROWN_CONCRETE); // F0

        set(18, Items.RED_CONCRETE_POWDER); // F#0
        set(19, Items.ORANGE_CONCRETE_POWDER);
        set(20, Items.YELLOW_CONCRETE_POWDER);
        set(21, Items.LIME_CONCRETE_POWDER);
        set(22, Items.GREEN_CONCRETE_POWDER);
        set(23, Items.CYAN_CONCRETE_POWDER);
        set(24, Items.LIGHT_BLUE_CONCRETE_POWDER);
        set(25, Items.BLUE_CONCRETE_POWDER);
        set(26, Items.PURPLE_CONCRETE_POWDER);
        set(27, Items.MAGENTA_CONCRETE_POWDER);
        set(28, Items.PINK_CONCRETE_POWDER);
        set(29, Items.BROWN_CONCRETE_POWDER); // F1

        set(30, Items.RED_WOOL); // F#1
        set(31, Items.ORANGE_WOOL);
        set(32, Items.YELLOW_WOOL);
        set(33, Items.LIME_WOOL);
        set(34, Items.GREEN_WOOL);
        set(35, Items.CYAN_WOOL);
        set(36, Items.LIGHT_BLUE_WOOL);
        set(37, Items.BLUE_WOOL);
        set(38, Items.PURPLE_WOOL);
        set(39, Items.MAGENTA_WOOL);
        set(40, Items.PINK_WOOL);
        set(41, Items.BROWN_WOOL); // F2

        set(42, Items.RED_STAINED_GLASS); // F#2
        set(43, Items.ORANGE_STAINED_GLASS);
        set(44, Items.YELLOW_STAINED_GLASS);
        set(45, Items.LIME_STAINED_GLASS);
        set(46, Items.GREEN_STAINED_GLASS);
        set(47, Items.CYAN_STAINED_GLASS);
        set(48, Items.LIGHT_BLUE_STAINED_GLASS);
        set(49, Items.BLUE_STAINED_GLASS);
        set(50, Items.PURPLE_STAINED_GLASS);
        set(51, Items.MAGENTA_STAINED_GLASS);
        set(52, Items.PINK_STAINED_GLASS);
        set(53, Items.BROWN_STAINED_GLASS); // F3

        set(54, Items.RED_TERRACOTTA); // F#3
        set(55, Items.ORANGE_TERRACOTTA);
        set(56, Items.YELLOW_TERRACOTTA);
        set(57, Items.LIME_TERRACOTTA);
        set(58, Items.GREEN_TERRACOTTA);
        set(59, Items.CYAN_TERRACOTTA);
        set(60, Items.LIGHT_BLUE_TERRACOTTA);
        set(61, Items.BLUE_TERRACOTTA);
        set(62, Items.PURPLE_TERRACOTTA);
        set(63, Items.MAGENTA_TERRACOTTA);
        set(64, Items.PINK_TERRACOTTA);
        set(65, Items.BROWN_TERRACOTTA); // F4

        set(66, Items.RED_GLAZED_TERRACOTTA); // F#4
        set(67, Items.ORANGE_GLAZED_TERRACOTTA);
        set(68, Items.YELLOW_GLAZED_TERRACOTTA);
        set(69, Items.LIME_GLAZED_TERRACOTTA);
        set(70, Items.GREEN_GLAZED_TERRACOTTA);
        set(71, Items.CYAN_GLAZED_TERRACOTTA);
        set(72, Items.LIGHT_BLUE_GLAZED_TERRACOTTA);
        set(73, Items.BLUE_GLAZED_TERRACOTTA);
        set(74, Items.PURPLE_GLAZED_TERRACOTTA);
        set(75, Items.MAGENTA_GLAZED_TERRACOTTA);
        set(76, Items.PINK_GLAZED_TERRACOTTA);
        set(77, Items.BROWN_GLAZED_TERRACOTTA); // F5

        set(78, Items.RED_CARPET); // F#5
        set(79, Items.ORANGE_CARPET);
        set(80, Items.YELLOW_CARPET);
        set(81, Items.LIME_CARPET);
        set(82, Items.GREEN_CARPET);
        set(83, Items.CYAN_CARPET);
        set(84, Items.LIGHT_BLUE_CARPET);
        set(85, Items.BLUE_CARPET);
        set(86, Items.PURPLE_CARPET);
        set(87, Items.MAGENTA_CARPET);
        set(88, Items.PINK_CARPET);
        set(89, Items.BROWN_CARPET); // F6

        set(90, Items.RED_BED); // F#6
        set(91, Items.ORANGE_BED);
        set(92, Items.YELLOW_BED);
        set(93, Items.LIME_BED);
        set(94, Items.GREEN_BED);
        set(95, Items.CYAN_BED);
        set(96, Items.LIGHT_BLUE_BED);
        set(97, Items.BLUE_BED);
        set(98, Items.PURPLE_BED);
        set(99, Items.MAGENTA_BED);
        set(100, Items.PINK_BED);
        set(101, Items.BROWN_BED); // F7

        set(102, Items.RED_BANNER); // F#7
        set(103, Items.ORANGE_BANNER);
        set(104, Items.YELLOW_BANNER);
        set(105, Items.LIME_BANNER);
        set(106, Items.GREEN_BANNER);
        set(107, Items.CYAN_BANNER);
        set(108, Items.LIGHT_BLUE_BANNER);
        set(109, Items.BLUE_BANNER);
        set(110, Items.PURPLE_BANNER);
        set(111, Items.MAGENTA_BANNER);
        set(112, Items.PINK_BANNER);
        set(113, Items.BROWN_BANNER); // F8

        set(114, Items.BLACK_BANNER); // F#8
    }
}
