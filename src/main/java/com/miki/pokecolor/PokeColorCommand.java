package com.miki.pokecolor;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.UUID;

public class PokeColorCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("pokecolor")
                .requires(commandSource -> checkPermission(commandSource, "pokecolor.base"))

                .then(Commands.argument("player", EntityArgument.player())
                        .requires(commandSource -> checkPermission(commandSource, "pokecolor.admin"))
                        .then(Commands.argument("slot", IntegerArgumentType.integer(1, 6))
                                .then(Commands.argument("nickname", StringArgumentType.greedyString())
                                        .executes(context -> colorPoke(context.getSource(), EntityArgument.getPlayer(context, "player").getUniqueID(), context.getArgument("slot", Integer.class), context.getArgument("nickname", String.class))))))

                .then(Commands.argument("slot", IntegerArgumentType.integer(1, 6))
                        .requires(commandSource -> commandSource.getEntity() != null)
                        .then(Commands.argument("nickname", StringArgumentType.greedyString())
                                .executes(context -> colorPoke(context.getSource(), context.getSource().asPlayer().getUniqueID(), context.getArgument("slot", Integer.class), context.getArgument("nickname", String.class)))));
    }

//    public static CompletableFuture<Suggestions> buildSuggestion(SuggestionsBuilder builder) {
//        for (int i = 1; i < 7; i++) {
//            builder.suggest(i);
//        }
//        return builder.buildFuture();
//    }

    public static boolean checkPermission(CommandSource src, String node) {
        Entity entity = src.getEntity();
        return entity == null || PermissionAPI.hasPermission((ServerPlayerEntity) entity, node);
    }



    public static int colorPoke(CommandSource source, UUID player, int slot, String nickname) {
        Pokemon pokemon = StorageProxy.getParty(player).get(slot-1);
        if (pokemon == null) {
            source.sendErrorMessage(new StringTextComponent("There's no pokemon in slot " + slot + "!"));
            return 0;
        }

        String charOnlyRegex = PokeColor.getConfig().getRegex().replace("&", "&(?=").concat(")");
        StringTextComponent nickComponent = new StringTextComponent(nickname.replaceAll(charOnlyRegex, "??"));
        String rawNick = nickname.replaceAll(PokeColor.getConfig().getRegex(), "");

        int maxChars = PokeColor.getConfig().getMaxChars();

        if (rawNick.length() > maxChars) {
            source.sendErrorMessage(new StringTextComponent("The nickname you've tried to set is too long! The maximum length is " + maxChars +"!"));
            return 0;
        }
        pokemon.setNickname(nickComponent);
        source.sendFeedback(new StringTextComponent("??eSuccessfully set " + pokemon.getLocalizedName() + "'s nickname to ").appendSibling(nickComponent), false);
        return 1;
    }
}
