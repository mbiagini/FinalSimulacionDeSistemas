package service.impl;

import java.util.List;

import persistence.granular.Passenger;
import persistence.cellIndex.Particle;

public class MetricsServiceImpl {

	public <E extends Number> double getMean(List<E> values) {
		double sum = 0;
		for (E value : values)
			sum += value.doubleValue();
		return sum / values.size();
	}
	
	public <E extends Number> double getVariance(List<E> values) {
		double mean = getMean(values);
        double aux = 0;
        for (Number value : values)
        	aux += (value.doubleValue() - mean) * (value.doubleValue() - mean);
        return aux / (values.size() - 1);
    }

	public <E extends Number> double getStandardDeviation(List<E> values) {
		return Math.sqrt(getVariance(values));
	}

	public double getKyneticEnergy(List<Particle> particles) {
		double energy = 0.0;
		for (Particle p : particles) {
			Passenger gp = (Passenger) p;
			energy += (1.0/2)*gp.getM()*gp.getV()*gp.getV();
		}
		return energy;
	}
	
	public double getPotentialEnergy(List<Particle> particles) {
		double energy = 0.0;
		for (Particle p : particles) {
			Passenger gp = (Passenger) p;
			energy += gp.getM()*(9.80665)*gp.getY();
		}
		return energy;
	}

}