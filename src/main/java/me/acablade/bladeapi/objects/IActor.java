package me.acablade.bladeapi.objects;


import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IActor {

    void teleport(Location location);
    void sendMessage(String string);
    boolean isSpectator(Player player);

    void setSpectator(Player player, boolean spectator);

}
