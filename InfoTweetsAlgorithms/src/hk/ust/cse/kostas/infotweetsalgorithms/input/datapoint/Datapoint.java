package infotweetsalgorithms.input.datapoint;

import java.math.BigDecimal;

public class Datapoint {

    BigDecimal id;
    public double [] elements;
    /* handcrafted label. */
    public boolean labelInformative;
    /* Algorithm assignments. */
    public boolean assignInformative;

    public Datapoint(BigDecimal id, 
		     double[] elements, boolean labelInformative) {
	this.id = id;
	this.elements = elements;
	this.labelInformative = labelInformative;
    } // Datapoint()

    @Override public String toString() {
	StringBuilder str = new StringBuilder();
	str.append("\n");	
	str.append("\n[id] " + this.id);
	str.append("\n[label] " + this.labelInformative);
	str.append("\n");	
	for(int i = 0; i < this.elements.length; i++)
	    str.append(this.elements[i] + "\t");
	return str.toString();
    }

} // Datapoint