package com.miki.pokecolor;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("pokecolor")
public class PokeColor {

    private static final Logger LOGGER = LogManager.getLogger();

    public PokeColor() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        // do something when the server starts
        LOGGER.warn("PokeColor, developed by Miki. Feel free to DM me on discord, at Miki#6432");
        event.getDispatcher().register(PokeColorCommand.register());
    }

}
