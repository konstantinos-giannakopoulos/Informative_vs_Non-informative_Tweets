package hk.ust.cse.kostas.infotweets.features;

import java.math.BigDecimal;

public class Features {

    public BigDecimal id;
    public boolean labelInformative;

    public int iWords;
    public int iHashtags;
    public int iRecipients;
    public boolean bUrl;
    int iRetweets; // not used
    double dScore; // not used
    double dWeight; // not used

    public Features() {

    }

    @Override public String toString() {
	return "\n[Tweet ID] " + this.id
	    + "\n[Label Informative] " + this.labelInformative
	    + "\n[Number of words] " + this.iWords
	    + "\n[Number of hashtags] " + this.iHashtags
	    + "\n[Number of recipients] " + this.iRecipients
	    + "\n[Has URL] " + this.bUrl
	    ;
    } // toString()

} // Features
