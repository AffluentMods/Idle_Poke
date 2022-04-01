package org.affluentproductions.idlepokemon.superclass;

import org.affluentproductions.idlepokemon.entity.Player;

import java.util.List;

public abstract class FormattedString {

    private final String string;
    private final List<String> placeholders;

    public FormattedString(String string, List<String> placeholders) {
        this.string = string;
        this.placeholders = placeholders;
    }

    public abstract String format(Player player, String placeholder, String string);

    public String getString(Player player) {
        String string = this.string;
        for (String placeholder : placeholders) {
            if (string.contains(placeholder)) string = format(player, placeholder, string);
        }
        return string;
    }
}
