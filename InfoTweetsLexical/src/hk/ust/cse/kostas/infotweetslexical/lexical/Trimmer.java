/*
 * Created on February 25, 2015 by kostas-κγ 
 *
 * This is part of the InfoTweetsLexical project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package infotweetslexical.lexical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class Trimmer {
    //public Pattern numericP; // numeric pattern
    
    public Trimmer() {
	//this.numericP = Pattern.compile( "^[0-9.]+$" );
    }
	
    public String trim(String word){	
	while ( word.endsWith(".") 
		|| word.endsWith(",") 
		|| word.endsWith("/")
		|| word.endsWith("?") 
		|| word.endsWith("\"")
		|| word.endsWith("!")
		|| word.endsWith("'")
		|| word.endsWith("~")
		|| word.endsWith("`")
		|| word.endsWith("*")
		|| word.endsWith(";")    
		|| word.endsWith(")")
		|| word.endsWith("]")
		|| word.endsWith("-")
		|| word.endsWith("!") 
		|| word.endsWith(">") 
		|| word.endsWith("?") 
		|| word.endsWith(":")  
		|| word.endsWith("]")
		|| word.endsWith("[")    
		) {
	    
	    word =  word.substring(0, word.length() - 1);
	}
	
	while ( word.startsWith(" ")
		|| word.startsWith("\t")
		//	|| word.startsWith("#")
		|| word.startsWith("'")
		|| word.startsWith("`")
		|| word.startsWith("\"")
		|| word.startsWith("(")
		|| word.startsWith("[")
		|| word.startsWith("]]")
		|| word.startsWith("-")
		|| word.startsWith("\"")
		|| word.startsWith(">")
		|| word.startsWith("<")
		|| word.startsWith("*")
		|| word.startsWith(".")
		|| word.startsWith(",")
		|| word.startsWith(")")
		|| word.startsWith(":")
		){
	    word = word.substring(1, word.length());
	}
    
	word = word.toLowerCase();

	//if(word.length() <= 1   // single letter
	//   || word.length() > 40   // too long
	//   ) 
	//    return null;

	return word;
    } // trim()
} // Trimmer
