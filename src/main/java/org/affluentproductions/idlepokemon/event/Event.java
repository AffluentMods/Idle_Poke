package org.affluentproductions.idlepokemon.event;

public class Event {

    private final String userId;

    public Event(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}