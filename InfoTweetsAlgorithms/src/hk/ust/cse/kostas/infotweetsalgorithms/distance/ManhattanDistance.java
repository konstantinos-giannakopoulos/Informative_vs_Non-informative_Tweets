package infotweetsalgorithms.distance;

import infotweetsalgorithms.input.datapoint.Datapoint;

public class ManhattanDistance implements Distance {
    @Override public double estimate(Datapoint p1, Datapoint p2) {
	double sum = 0.0;
	for(int i = 0 ; i < p1.elements.length; i++) {
	    sum += Math.abs(p1.elements[i]-p2.elements[i]);
	}
	return sum;
    } // estimate()
} // ManhattanDistance