package infotweetsalgorithms.distance;

import infotweetsalgorithms.input.datapoint.Datapoint;

public interface Distance {
    public double estimate(Datapoint point1, Datapoint point2);
}