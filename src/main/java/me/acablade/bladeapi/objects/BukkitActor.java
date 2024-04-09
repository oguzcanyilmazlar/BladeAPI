package me.acablade.bladeapi.objects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

//wrapper

public class BukkitActor implements IActor, Comparable<Player>{

    public static BukkitActor of(UUID uuid){
        return new BukkitActor(Bukkit.getPlayer(uuid));
    }

    public static BukkitActor of(Player player){
        return new BukkitActor(player);
    }

    private boolean spectator;
    @Getter
    private final Player player;

    private BukkitActor(Player player){
        this.player = player;
    }


    @Override
    public void teleport(Location location) {
        player.teleport(location);
    }

    @Override
    public void sendMessage(String string) {
        player.sendMessage(string);
    }

    @Override
    public boolean isSpectator(Player player) {
        return spectator;
    }

    @Override
    public void setSpectator(Player player, boolean spectator) {
        this.spectator = spectator;
    }

    @Override
    public int compareTo(Player o) {
        if(o.equals(player)) return 3;
        return -1;
    }
}
