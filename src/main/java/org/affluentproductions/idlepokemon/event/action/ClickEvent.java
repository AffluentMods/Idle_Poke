package org.affluentproductions.idlepokemon.event.action;

import org.affluentproductions.idlepokemon.click.Click;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.event.Event;

import javax.annotation.Nullable;

public class ClickEvent extends Event {
    private final Player player;
    private final Click click;

    public ClickEvent(Player player, @Nullable Click click) {
        super(player.getUserId());
        this.player = player;
        this.click = click;
    }

    public Player getPlayer() {
        return player;
    }

    @Nullable
    public Click getClick() throws NullPointerException {
        return click;
    }
}