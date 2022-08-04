package infotweetsalgorithms.distance;

import infotweetsalgorithms.input.datapoint.Datapoint;

public class ChebyshevDistance implements Distance {
    @Override public double estimate(Datapoint p1, Datapoint p2) {
	double dist = 0.0;
	double result = 0.0;
	for(int i = 0 ; i < p1.elements.length; i++) {
	    dist = Math.abs(p1.elements[i] - p2.elements[i]);
	    result = Math.max(dist,result);
	}
	return result;
    } // estimate()

} // ChebyshevDistance