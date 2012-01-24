package uk.soton.service.mediation.edoal;

import uk.soton.service.mediation.FunctionalDependency;

public interface FDInverter {

	FunctionalDependency invert(FunctionalDependency fd);
}
