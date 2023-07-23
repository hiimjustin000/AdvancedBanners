package com.hiimjustin000.advancedbanners;

import net.minecraft.world.item.DyeColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ColorUtilities
{
    public static final Map<String, Integer> COLOR_MAP = new HashMap<>();

    static
    {
        COLOR_MAP.put("white", 16383998);
        COLOR_MAP.put("orange", 16351261);
        COLOR_MAP.put("magenta", 13061821);
        COLOR_MAP.put("light_blue", 3847130);
        COLOR_MAP.put("yellow", 16701501);
        COLOR_MAP.put("lime", 8439583);
        COLOR_MAP.put("pink", 15961002);
        COLOR_MAP.put("gray", 4673362);
        COLOR_MAP.put("light_gray", 10329495);
        COLOR_MAP.put("cyan", 1481884);
        COLOR_MAP.put("purple", 8991416);
        COLOR_MAP.put("blue", 3949738);
        COLOR_MAP.put("brown", 8606770);
        COLOR_MAP.put("green", 6192150);
        COLOR_MAP.put("red", 11546150);
        COLOR_MAP.put("black", 1908001);
    }

    public static DyeColor roundColor(int color)
    {
        Map<String, Float> distances = new HashMap<>();
        for (String colorName : COLOR_MAP.keySet())
        {
            int r = ((color >> 16) & 255) - ((COLOR_MAP.get(colorName) >> 16) & 255);
            int g = ((color >> 8) & 255) - ((COLOR_MAP.get(colorName) >> 8) & 255);
            int b = (color & 255) - (COLOR_MAP.get(colorName) & 255);
            distances.put(colorName, r * r * 0.3f + g * g * 0.59f + b * b * 0.11f);
        }

        Optional<Float> min = distances.values().stream().min(Float::compare);
        if (min.isEmpty())
            return DyeColor.WHITE;
        Optional<Map.Entry<String, Float>> entry = distances.entrySet().stream().filter(e -> e.getValue() == (float)min.get()).findFirst();
        if (entry.isEmpty())
            return DyeColor.WHITE;
        return DyeColor.byName(entry.get().getKey(), DyeColor.WHITE);
    }

    public static String toHex(int color)
    {
        String hex = Integer.toHexString(color).toUpperCase();
        return "000000".substring(0, 6 - hex.length()) + hex;
    }
}
