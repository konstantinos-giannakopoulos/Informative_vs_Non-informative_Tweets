package infotweetsalgorithms.distributions;

import cc.mallet.util.Randoms;

public class Gamma {

    Randoms r;

    public Gamma() {
	this.r = new Randoms();
    } // Gamma()

    public double pdf(double x, double alpha, double beta) {
	return ((Math.pow(x,alpha-1)*Math.exp((-x)/beta)) / (Math.pow(beta,alpha)*Functions.gamma((int)alpha)));
    } // pdf()

    /**
     * Returns a drawn from a Gamma distribution.
     */
    public double draw(double alpha, double beta) {
	  return r.nextGamma(alpha,beta);
    } // draw()

} // Gibbs