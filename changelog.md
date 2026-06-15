
------------------------------------------------------
Create: Sound of Steam 0.9.1
------------------------------------------------------

### Changes:

- Made the keyboard relay rotatable with a wrench

### Bugfixes:

- Crash when starting dedicated server (#103)
- Changed timings in keyboard relay ponder
- Fixed keyboard relay not being mirrored and rotated correctly in schematics
- Rohrflute no longer triggers subbass advancement

------------------------------------------------------
Create: Sound of Steam 0.9.0
------------------------------------------------------

### Features:

- Pipes:
  - Open Wood 16'
  - Chamade 8'
  - Tierce 1 3/5'
  - Bassoon 16'
  - Krummhorn 8'
- Config Options
    - Pipe Attenuation range
    - Pipe fade out duration
    - Display brackets around the octave goggle tooltips
    - CAP config
- Note Links
- Ponders
- A few easter eggs ;)

### Changes:

- Steam whistles and Pipe Base now work on Windchests
- Added on/off visuals and sounds to the Keyboard Relay
- Custom placement sounds for all pipes
- Decreased the volume of the Gedeckt, Hohlflute and Piccolo
- Added a minimum RPM to the Tracker Bar
- The Tracker bar has particles when it's playing
- Added crafting instructions to the boots

### Bugfixes:

- A couple samples not looping well
- Chinese traditional and simplified file naming error (#83)
- Keyboard relay notes would get stuck when holding keys and breaking the block
- Tuned more pipes
- Pipes no longer vanish when a contraption is disassembled
- Hoppers/Funnels now update the inventory of the Tracker Bar
- Game no longer crashes when placing blocks with Effortless Building (#62)

### Known Issues:

- The Keyboard relay is unreliable on servers


------------------------------------------------------
Create: Sound of Steam 0.8.2
------------------------------------------------------

### Fixes:

- Fixed missing Roll Authoring Table recipe
- Fixed broken Tracker Bar recipe

------------------------------------------------------
Create: Sound of Steam 0.8.1
------------------------------------------------------

### Changes:

- Rebalanced some pipe samples
- Added config options for MIDI file uploads
- The tooltip when looking at a pipe while wearing Engineer's Goggles now shows the octave as well as the pitch
- New translations
- Readded the antenna to the side of the Keyboard Relay
- Copper and Brass Boots are now crafted with sheets instead of ingots
- Updated to Create 6.0.8

### Fixes

- Swapping pipes now grants the advancement for placing the new pipe
- Correctly named the Roll Authoring Table block
- Fixed a bug where the keyboard relay would replace itself after being broken
- The Tracker Bar and Windchests can now be mined with tools

------------------------------------------------------
Create: Sound of Steam 0.8.0
------------------------------------------------------

### Features

- The Tracker Bar
   - a new way to play MIDI files!
- The Roll Authoring Table
  - Lets you punch paper rolls with music on them
- Pipes: Viola, Vox Celeste, Rohrflote, Hohlflute, Prestant, English Horn, & Haunted Whistle

### Changes

- Reed pipes (Trompette, Subbass, Vox Humana, English Horn) are now crafted via sequenced assembly, using new reed and tuning wire items
- The Gedeckt is now visually stopped
- A more intuitive system for key redstone frequencies in the Tracker Bar and Keyboard Relay
- Removed the Stop Master
- Subbass and Posaune now have the full 5-octave range

### Bugfixes

- Pipe extensions no longer flood the logs with errors on startup

### Known Issues

- Pipes appear bugged when updating to 0.8. This is purely a visual change, however it can cause issues when trying to change the length of such pipes. TO FIX THIS, SWAP IT WITH ANOTHER PIPE BY RIGHT-CLICKING, THEN SWAP BACK.

------------------------------------------------------
Create: Sound of Steam 0.7.3
------------------------------------------------------

### Fixes

- Crash upon placing pipes/loading a world with pipes placed caused by rendering error

------------------------------------------------------
Create: Sound of Steam 0.7.2
------------------------------------------------------

### Features

- Translations (Czech, Dutch, French, German, Polish, Russian, Spanish)

### Changes

- Updated to Create 6.0.6
- New particle texture for windchest and windchest master

### Fixes

- Desync issue when linking a stop master to a keyboard relay

------------------------------------------------------
Create: Sound of Steam 0.7.1
------------------------------------------------------

### Fixes

- Added item model for Keyboard Relay
- Added particles for Keyboard Relay
- Added Stop Master and Keyboard Relay to the creative inventory

------------------------------------------------------
Create: Sound of Steam 0.7.0
------------------------------------------------------

### Features

- Compatibility with MIDI!
- Stop Master
- Keyboard Relay


Currently, MIDI can only be used for live playback. Playing .midi files is planned in future updates. To use MIDI compat:

- Select a valid MIDI device in the config menu (press `;` by default)
- Right-click a Keyboard Relay block with a Stop Master in your hand, then place the Stop Master
- Set the tuning frequency on top of the Stop Master (or don't)
- For any note you wish to activate, place a receiving redstone link with the tuning frequency first and the specific note frequency last.
- Right-click the Keyboard Relay to start playing!

------------------------------------------------------
Create: Sound of Steam 0.6.1
------------------------------------------------------

### Changes

- Windchests and Windchest Controllers are now placed similarly to fans, and their direction can be reversed by holding shift while placing
- Changed Windchest Controller crafting recipe

### Fixes

- Vox Humana extensions being placed with incorrect orientation
- Missing lang translation for the copper boot

------------------------------------------------------
Create: Sound of Steam 0.6
------------------------------------------------------

### Features

- Pipes:
  - Posaune
  - Vox Humana
  - Brass, copper, and dark oak boots, which are used to craft reed pipes

### Changes

- You can now right-click on any pipe with a different pipe in your hand to swap the two.
- Windchests and windchest controllers can now be rotated and picked up with a wrench.

### Bugfixes

- Windchests and windchest controllers rotating and mirroring incorrectly in schematics

### Known issues

- Create's steam whistles cannot be used on windchests
- Create's steam whistles cannot be swapped for other pipes

------------------------------------------------------
Create: Sound of Steam 0.5.1-c6.0.0
------------------------------------------------------

### Changes

- Updated to Create 6.0.0


(Note: all previous versions are for Create 0.5)

------------------------------------------------------
Create: Sound of Steam 0.5.1
------------------------------------------------------

(Note: this and all previous versions are for Create 0.5)

### Bugfixes

- Fixed a bug relating to windchest controller powering

------------------------------------------------------
Create: Sound of Steam 0.5.0
------------------------------------------------------

### Features
- Windchest blocks
  - Windchests allow you to easily manage stops on a large scale in your organs!

### Bugfixes
- Wrenching Subbass or Piccolo extensions now sets the extension to the appropriate length.

------------------------------------------------------
Create: Sound of Steam 0.4.0
------------------------------------------------------

### Fixes
- Fixed crashes when too many mods are installed

### Changes
- Changed to SemVer(ish) version numbering

------------------------------------------------------
Create: Sound of Steam 0.3.0
------------------------------------------------------
### Changes
- Switched to Forge version 47.3.0

------------------------------------------------------
Sound of Steam 0.2.0
------------------------------------------------------

### Features
- Added Gedackt
- Added Diapason
- Added Gamba
- Added Piccolo
- Added Subbass
- Added Trompette
- Added Nasard
- Added pipe base block
