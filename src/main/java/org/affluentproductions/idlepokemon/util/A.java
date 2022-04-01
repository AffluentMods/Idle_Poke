package org.affluentproductions.idlepokemon.util;

import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A {

    public static List<Upgrade> getMainUpgrades(int pid, String pokemonName, double p1, double p2, double p3,
                                                double p4) {
        Upgrade u1 = new Upgrade(1, 10, new UpgradeData(1, 0), pid, p1, "Increases " + pokemonName + "'s DPS by 100%");
        Upgrade u2 = new Upgrade(2, 25, new UpgradeData(1, 0), pid, p2, "Increases " + pokemonName + "'s DPS by 100%");
        Upgrade u3 = new Upgrade(3, 50, new UpgradeData(1, 0), pid, p3, "Increases " + pokemonName + "'s DPS by 100%");
        Upgrade u4 =
                new Upgrade(4, 100, new UpgradeData(1.5, 0), pid, p4, "Increases " + pokemonName + "'s DPS by 150%");
        return new ArrayList<>(Arrays.asList(u1, u2, u3, u4));
    }

}