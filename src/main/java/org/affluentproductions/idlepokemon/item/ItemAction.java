package org.affluentproductions.idlepokemon.item;

import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.entity.Player;

public abstract class ItemAction {

    public abstract void run(Player player, TextChannel tc);
}