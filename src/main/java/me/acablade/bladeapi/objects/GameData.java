package me.acablade.bladeapi.objects;

import java.util.*;

public class GameData implements IGameData {

    private final Set<BukkitActor> playerList = new HashSet<>();
    private final Set<BukkitActor> spectatorList = new HashSet<>();

    public Set<BukkitActor> getPlayers(){
        return playerList;
    }

    public Set<BukkitActor> getSpectators(){
        return spectatorList;
    }


}
