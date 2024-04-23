## Changelog

# 2.3.1
- Fix crash with iris 1.7+ (#47)

<details>
<summary>Older Versions</summary>

# 2.3.0
- Removed the ability to increase the durability of trim templates (No one was using this anyway)
- Made it so trims that cause entities to ignore you no longer also ignore attacks and will now attack back
- Fixed ender pearl dogde occasionally damaging the player
- Fixed pillager entity targeting not respecting the config
- Changed default copper swim speed buf to 0.005 velocity/s per trim

# 2.2.3
- Fix dragons breath trim applying to owner if the effect is provided by the armour
- Effect cloud now only applies a 1 second copy of the effect so the effect is only shared while in the cloud
- Increased default radius of the cloud to balance this out

# 2.2.2
- Fix crash with Beacon Overhaul when faking night vision with silver trim

# 2.2.1
- Invert material checking to improve leniency with other mod's items when using All The Trims
- Previously was "material contains item", now is "item contains material", so "block of iron" will now be considered iron

# 2.2.0
- Add Compat with Better Trim Tooltips (Press Shift to see the effects)
- Fix default Ender Pearl dodge chance

# 2.1.2
- Updated Readme to include the changes
- Added exact figures to the Readme, in-game tooltips will still remain deliberately vague
- Re-balanced Netherbrick Trim

# 2.1.1
- Fix crash with incorrect application of compat mixins when another mod is not present

# 2.1.0
### Changes
- All trim effects now only apply if the armour is in the correct slot
- All trim effects can now be toggled in the config
- Silver effect can now be toggled to enable/disable the effect in dimensions that have a fixed time
  - Configurable (default: true)
- Slime effect on the boots now completely negates fall damage and causes you to bounce
  - Configurable (default: true)
- Leather effect now does not allow you to step-down higher than vanilla
- Dragon's Breath effect now doesn't re-apply the cloud effect to it's owner
- Enderpearl will only be able to teleport you every 10 ticks to prevent constant teleporting on repeated damage
- Couple changes to the default config for balance purposes
- Added no_no.json
### Bug Fixes
- Fix Copper Swim Speed not applying correclty
- Fix Iron Mining Speed applying to all tools on all blocks
- Fix Slime knockback effect not applying correctly 
- Fix Glowstone effect from constantly re-applying increasing the level of effects greatly
- Fix crash with Fire Charge effect
- Fix Fire Charge not applying to attacked mobs
- Fix trim effects from Trims Expanded not applying correctly
- Fix crash with Immersive Portals

# 2.0.5
- Buff iron trim to 50% increase in mining speed per trim
- Fix #26

# 2.0.4
- Fix effects not applying to vanilla materials

# 2.0.3
- Fix more translation issues

# 2.0.2
- Fix spelling mistake in the translations

</details>