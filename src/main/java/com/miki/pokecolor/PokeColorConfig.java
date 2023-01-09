package com.miki.pokecolor;

import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
public class PokeColorConfig {

    String regex = "(?i)&[0-9a-fl-or]";

    int maxChars = 16;


    public String getRegex() {
        return regex;
    }

    public int getMaxChars() {
        return maxChars;
    }
}
