package infotweetsalgorithms.distributions; 

public class Functions {
    public static int factorial(int n) {
	int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    } // factorial()

    /**
     * The Gamma Function.
     * Gamma(n) = (n-1)!
     */
    public static int gamma(int n) {
	return factorial(n-1);
    }
} // Functions