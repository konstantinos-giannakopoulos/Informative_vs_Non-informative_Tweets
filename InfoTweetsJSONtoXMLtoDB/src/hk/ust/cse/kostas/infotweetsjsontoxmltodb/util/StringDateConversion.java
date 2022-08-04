package infotweetsjsontoxmltodb.util;

import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 
 *
 * Gets as input string representation of date in format
 * "EEE MMM dd HH:mm:ss Z yyyy", 
 * and returns a timestamp: yyyy-MM-dd HH:mm
 */
public class StringDateConversion {

    private String inputDate;
    private DateFormat formatter;

    public StringDateConversion(String date) {
	this.inputDate = date;
	this.formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    } // StringDateConversion()

    /**
     * Returns date in the DateFormat yyyy-MM-dd HH:mm,
     * which is used as postgres timestamp.
     */
    public Date toDate() {
	Date result = null;
	try{
	    result = this.formatter.parse(this.inputDate);
	    return result; 
	} catch(java.text.ParseException pex){}
	return result;
    } // toDate()

    public Timestamp toSQLTimestamp() {
	Timestamp result; 
	result = new Timestamp(toDate().getTime());
	return result;
    }

} // StringDateConversion