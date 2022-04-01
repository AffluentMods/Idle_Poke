package org.affluentproductions.idlepokemon.item;

import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.entity.PokemonUser;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.util.ShinyUtil;

public class ThreeShinyPokemon extends Item {

    public ThreeShinyPokemon() {
        super("3 Shiny Pokemon", 200, "Gives you 3 random shiny pokémon", true, true, 100000,
                new Bonus(0, BonusType.NULL), new ItemAction() {
                    @Override
                    public void run(Player player, TextChannel tc) {
                        PokemonUser pu = player.getPokemonUser();
                        Pokemon p1 = Pokemon.getRandomOwnedPokemon(pu);
                        Pokemon p2 = Pokemon.getRandomOwnedPokemon(pu);
                        Pokemon p3 = Pokemon.getRandomOwnedPokemon(pu);
                        ShinyUtil.addShiny(player, p1);
                        ShinyUtil.addShiny(player, p2);
                        ShinyUtil.addShiny(player, p3);
                        tc.sendMessage(MessageUtil.info("3 Shiny Pokémon",
                                "You received the following shiny pokémons:\n\n- " + p1.getDisplayName() + "\n- " +
                                p2.getDisplayName() + "\n- " + p3.getDisplayName() + "\n\nCongratulations!")).queue();
                    }
                });
    }
}