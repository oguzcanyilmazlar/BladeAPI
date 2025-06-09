package me.acablade.bladeapi.data;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public interface IPlayerData extends IGameData {

    boolean addPlayer(Player player);
    boolean removePlayer(Player player);

    Collection<UUID> getAllPlayers();

}