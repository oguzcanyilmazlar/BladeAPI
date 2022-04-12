package me.acablade.bladeapi.objects;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Acablade/oz
 */

@Data
public class GameData {

    private UUID winner;

    private final Set<UUID> playerList = new HashSet<>();
    private final Set<UUID> spectatorList = new HashSet<>();


}
