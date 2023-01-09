package com.miki.pokecolor;

import info.pixelmon.repack.org.spongepowered.CommentedConfigurationNode;
import info.pixelmon.repack.org.spongepowered.yaml.NodeStyle;
import info.pixelmon.repack.org.spongepowered.yaml.YamlConfigurationLoader;
import io.leangen.geantyref.TypeToken;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

@Mod("pokecolor")
public class PokeColor {

    private static final Logger LOGGER = LogManager.getLogger();
    File directory = new File("config/happytrade");
    final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .nodeStyle(NodeStyle.BLOCK)
            .path(FileSystems.getDefault().getPath("config", "happytrade", "trade.yml"))
            .build();
    static PokeColorConfig config;
    public CommentedConfigurationNode node;

    public PokeColor() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) throws IOException {
        LOGGER.warn("PokeColor, developed by Miki. Feel free to DM me on discord, at Miki#6432");
        if (!directory.exists()) {
            directory.mkdir();
        }
        loadConfig();

        event.getDispatcher().register(PokeColorCommand.register());
    }

    private void loadConfig() throws IOException {
        this.node = this.loader.load();
        TypeToken<PokeColorConfig> type = new TypeToken<PokeColorConfig>() {};
//        final TypeToken<TradeConfig> type = new com.google.gson.reflect.TypeToken<TradeConfig>();
        config = this.node.get(type, new PokeColorConfig());
        saveConfig();
    }

    protected void saveConfig() {
        try {
            TypeToken<PokeColorConfig> type = new TypeToken<PokeColorConfig>() {};
            this.node.set(type, config);
            this.loader.save(this.node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() throws IOException {
        loadConfig();
    }

    public static PokeColorConfig getConfig() {
        return config;
    }
}
