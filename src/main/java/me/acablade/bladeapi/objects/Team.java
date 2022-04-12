package me.acablade.bladeapi.objects;

import lombok.Data;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Acablade/oz
 */
@Data
public class Team {

    private final String name;
    private final ChatColor color;
    private final int teamSize;
    private final Set<UUID> playerList = new HashSet<>();

}
