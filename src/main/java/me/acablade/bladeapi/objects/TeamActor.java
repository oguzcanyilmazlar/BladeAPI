package me.acablade.bladeapi.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamActor implements IActor{

    private final List<Player> players;
    private final boolean[] spectators;

    public static TeamActor of(Player... players){
        return new TeamActor(players);
    }


    private TeamActor(Player... players){
        this.players = new NoDuplicateArrayList<>(Arrays.asList(players));
        spectators = new boolean[players.length];
    }


    @Override
    public void teleport(Location location) {
        players.forEach(p -> p.teleport(location));
    }

    @Override
    public void sendMessage(String string) {
        players.forEach(p -> p.sendMessage(string));
    }

    @Override
    public boolean isSpectator(Player player) {
        return spectators[players.indexOf(player)];
    }

    @Override
    public void setSpectator(Player player, boolean spectator) {

    }
}
