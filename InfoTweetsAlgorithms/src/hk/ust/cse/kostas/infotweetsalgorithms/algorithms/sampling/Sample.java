/*
 * Created on May 1, 2015 by kostas-κγ
 *
 * This is part of the InfoTweetAlgorithms project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package infotweetsalgorithms.algorithms.sampling;

import java.util.Arrays;

import infotweetsalgorithms.input.document.Document;

/**
 * Sample: The Implementation of a sample object. 
 *
 * @author kostas-κγ 
 */
public class Sample {
    public double[] vector;

    public Sample(int size) {
	this.vector = new double[size];
    }

    /**
     *
     */
    public void initiallizeVector() {
	Arrays.fill(this.vector,1.0);
    } // initiallizeVector()
    
    /*
     * Return the index of variable in 
     * the vector of variables.
     *
    public int chooseVariableForResampling() {
	return 1;
    } // chooseVariableForResampling()
*/
    public double variableValueAtIndex(int index) {
	return this.vector[index];
    }

    public void updateVariableAtIndex(double variable, int index) {
	this.vector[index] = variable;
    }

    public void addVariableAtIndex(double variable, int index) {   
	this.vector[index] += variable;
    }

    @Override public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("[ ");
	for(double value : this.vector) {
	    sb.append(value);
	    sb.append(" ");
	}	
	sb.append("]\n");
	return sb.toString();
    } // toString()

} // Sample