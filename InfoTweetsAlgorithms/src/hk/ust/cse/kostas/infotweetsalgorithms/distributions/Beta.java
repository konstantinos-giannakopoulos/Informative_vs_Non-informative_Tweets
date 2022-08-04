package infotweetsalgorithms.distributions;

import cc.mallet.util.Randoms;

public class Beta {

    Randoms r;

    public Beta() {
	this.r = new Randoms();
    } // Gamma()

    public double pdf(double x, double alpha, double beta) {
	double g = (Functions.gamma((int)(alpha+beta)))/
	    (Functions.gamma((int)alpha)*Functions.gamma((int)beta));
	return g*Math.pow(x,alpha-1)*Math.pow(1-x,beta-1);
    } // pdf()

    /**
     * Returns a drawn from a Beta distribution.
     */
    public double draw(double alpha, double beta) {
	  return r.nextGamma(alpha,beta);
    } // draw()

} // Gibbs