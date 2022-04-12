package me.acablade.bladeapi.objects;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Acablade/oz
 */

@Data
public class GamePlayer {

    private static Map<UUID, GamePlayer> gamePlayerHashMap = new HashMap<>();

    public static GamePlayer getGamePlayer(UUID uuid){
        return gamePlayerHashMap.putIfAbsent(uuid, new GamePlayer(uuid));
    }


    private final UUID uuid;
    private Team team;

}
