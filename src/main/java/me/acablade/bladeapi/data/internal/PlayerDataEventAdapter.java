package me.acablade.bladeapi.data.internal;

import me.acablade.bladeapi.IGame;
import me.acablade.bladeapi.data.IPlayerData;
import me.acablade.bladeapi.events.player.PlayerJoinGameEvent;
import me.acablade.bladeapi.events.player.PlayerLeaveGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class PlayerDataEventAdapter implements IPlayerData {

    private final IGame game;
    private final IPlayerData delegate;

    public PlayerDataEventAdapter(IGame game, IPlayerData delegate) {
        this.game = game;
        this.delegate = delegate;
    }

    @Override
    public boolean addPlayer(Player player) {
        boolean added = delegate.addPlayer(player);
        if (added) {
            Bukkit.getPluginManager().callEvent(new PlayerJoinGameEvent(player, game));
        }
        return added;
    }

    @Override
    public boolean removePlayer(Player player) {
        boolean removed = delegate.removePlayer(player);
        if (removed) {
            Bukkit.getPluginManager().callEvent(new PlayerLeaveGameEvent(player, game));
        }
        return removed;
    }


    @Override
    public Collection<UUID> getAllPlayers() {
        return delegate.getAllPlayers();
    }

}
