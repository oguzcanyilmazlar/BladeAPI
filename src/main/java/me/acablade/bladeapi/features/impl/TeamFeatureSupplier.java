package me.acablade.bladeapi.features.impl;

import me.acablade.bladeapi.objects.Team;

import java.util.Set;

/**
 * @author Acablade/oz
 */

public interface TeamFeatureSupplier<T> {

    Set<Team> supplyTeams();


}
