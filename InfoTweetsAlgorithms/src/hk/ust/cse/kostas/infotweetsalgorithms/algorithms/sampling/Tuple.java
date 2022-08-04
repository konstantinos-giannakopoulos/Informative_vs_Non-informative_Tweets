package infotweetsalgorithms.algorithms.sampling;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Tuple {

    public double[] variables;

    public Tuple(int length) {
	this.variables = new double[length];
    } // Tuple()

    public void initiallizeVariableValues() {
	//Arrays.fill(this.variables, 0.0);
	this.variables[0] = 0.0; // a 
	this.variables[1] = 0.0; // c
	this.variables[2] = 1.0; // f
	this.variables[3] = 1.0; // d
	this.variables[4] = 0.0; // e
	this.variables[5] = 0.0; // b
	this.variables[6] = 0.0; // g
	this.variables[7] = 0.0; // h
    } // initiallizeVariableValues()

    public double getVariableValueAtIndex(int index) {
	return this.variables[index];
    } // getVariableValueAtIndex()

    public void updateVariableAtIndex(double variable, int index) {
	this.variables[index] = variable;
    } // updateVariableAtIndex()

    public List<Integer> getVariablePositions() {
	List<Integer> positions = new ArrayList<Integer>();
	for(int pos = 0; pos < this.variables.length; pos++)
	    if(this.variables[pos] == 1.0)
		positions.add(pos);
	return positions;
    } // getVariablePositions()

    

    @Override public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("[ ");
	for(double value : this.variables)
	    sb.append(value + " ");
	sb.append("]");
	return sb.toString();
    } // toString()

} // Tuple