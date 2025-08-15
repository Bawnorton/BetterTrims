# Plan

Each trim material or pattern can provide an effect
full sets of trim materials or trim patterns can provide effects
effects are defined by data driven resource files

```json5
{
  "applies_to": { // MANDATORY - must match all 
    "material": "#namespace:material", // OPTIONAL - a tag that contains the material item ids or literal 
    "pattern": "#namespace:pattern", // OPTIONAL - a tag that contains the pattern ids or literal
    // one of material or pattern must be present
    "min_count": 1 // MANDATORY - at least 1, 4 for full set
  },
  "ability": {
    "type": "bettertrims:attribute" // Set list of types that determine what the ability should do
    // depends on type
  }
}
```

## Displaying
Since the combination isn't necessarly obvious to the end user the trimmed equipment that the ability applies to
should render on the side of the tooltip with its requirement. 

ie.<br>
bettertrims.ability.[namespace].[ability_name] (Ability Name)<br>
—————<br>
Requires [min_count] (1) Silence Trim (Gold)<br>
+1 Mining Speed

Displays will need to be rendered differently based on the type

## Possible types:
1. attribute based - give player an attribute
2. effect based - give player an effect
3. unique - perform unique behaviour
4. functional? provide a reference to an mcfunction like enchantments?