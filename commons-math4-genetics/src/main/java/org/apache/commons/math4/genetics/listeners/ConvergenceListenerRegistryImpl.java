package org.apache.commons.math4.genetics.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.genetics.stats.PopulationStatisticalSummary;

/**
 * This class is the default implementation of ConvergenceListenerRegistry. It
 * will be responsible for registering the interested listeners and notifying
 * all when required.
 *
 */
public class ConvergenceListenerRegistryImpl implements ConvergenceListenerRegistry {

	private List<ConvergenceListener> listeners = new ArrayList<>();

	/**
	 * Registers the interested ConvergenceListener passed as an argument.
	 * 
	 * @param the convergence listener.
	 */
	public void addConvergenceListener(ConvergenceListener convergenceListener) {
		this.listeners.add(convergenceListener);
	}

	/**
	 * Notifies all registered ConvergenceListeners about the population statistics.
	 * 
	 * @param population statistics
	 */
	public void notifyAll(PopulationStatisticalSummary populationStatisticalSummary) {
		for (ConvergenceListener convergenceListener : listeners) {
			convergenceListener.notify(populationStatisticalSummary);
		}
	}

}