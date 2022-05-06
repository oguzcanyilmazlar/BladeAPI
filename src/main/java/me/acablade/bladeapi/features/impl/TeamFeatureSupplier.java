package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.objects.Team;

import java.util.Set;

/**
 * supplies team feature
 */

public interface TeamFeatureSupplier {

    /**
     * Method to supply teams
     * @return Teams supplied
     */
    Set<Team> supplyTeams();


}
