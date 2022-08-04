package infotweetsalgorithms.distance;

public class DistanceFactory {
    public static Distance chooseDistance(String choice) {
	if(choice.equals("euclidean"))
	    return new EuclideanDistance();
	else if(choice.equals("chebyshev"))
	    return new ChebyshevDistance();
	else if(choice.equals("manhattan"))
	    return new ManhattanDistance();
	else 
	    return null;
    }
    //thow new IllegalArgumentException("No appropriate distance metric");
} // DistanceFactory();