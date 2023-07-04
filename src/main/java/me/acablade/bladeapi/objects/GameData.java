package me.acablade.bladeapi.objects;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Acablade/oz
 */

@Data
public class GameData {

    private final Set<UUID> playerList = new HashSet<>();
    private final Set<UUID> spectatorList = new HashSet<>();

    public Stream<Player> playerStream(){
        return this.playerList.stream().map(Bukkit::getPlayer);
    }

    public Stream<Player> spectatorStream(){
        return this.spectatorList.stream().map(Bukkit::getPlayer);
    }

    public Stream<Player> allPlayers(){
        return Stream.concat(playerStream(), this.spectatorStream());
    }


}
