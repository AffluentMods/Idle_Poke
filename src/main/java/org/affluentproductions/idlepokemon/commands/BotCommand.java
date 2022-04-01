package org.affluentproductions.idlepokemon.commands;

import org.affluentproductions.idlepokemon.superclass.CommandEvent;

public abstract class BotCommand {

    public abstract void execute(CommandEvent e);

    protected boolean runnableAsBanned = false;

    protected String name = "null";

    protected String help = "No help available";

    public boolean ownerCommand = false;

    protected double cooldown = 0.25;

    protected String[] aliases = new String[0];

    protected boolean hidden = false;

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public double getCooldown() {
        return cooldown;
    }

    public String[] getAliases() {
        return aliases;
    }
}