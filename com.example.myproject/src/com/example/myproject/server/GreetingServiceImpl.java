package com.example.myproject.server;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.example.myproject.client.GreetingService;
import com.example.myproject.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 10 characters long");
		}

		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "The stuttered words are:  " + stutter(input);
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
	/**
	 * Checks a string for repeated words
	 * 
	 * @param input the string to check
	 * @return the stuttered words
	 */
	private String stutter(String input){
		String[] lines = input.split("\n");
		String lineString = "";
		String[] formattedLineString;
		Map<String, Integer> wordMap;
		String wordString = "";
		String stutterResults = "";
		int lineNumber;
		
		//traverses the lines to check for stutters
		for(int i = 0; i<lines.length; i +=1){
			lineNumber = i + 1;
			lineString = lines[i];
			//format the lines so we can do something with it
            formattedLineString = lineString.replaceAll("[\\n]", " ").replaceAll("[!.,?!:;/()]","").split(" ");
            //create the new map for storing the words
            wordMap = new LinkedHashMap<String,Integer>();
            for(int j =0; j< formattedLineString.length; j+=1){
                String tempString = formattedLineString[j];
                //check if map has the word, if it doesn't, add it, or increase the count
                if(wordMap.get(tempString) == null){
                	wordMap.put(tempString, 1);
                }else{
                	wordMap.put(tempString, wordMap.get(tempString) +1);
                }
            }
            Set<String> words = wordMap.keySet();
            for(String word:words){
            	//print if the word is repeated
            	if(wordMap.get(word) > 1){
            		wordString = word;
            		for(int k = 0; k<wordMap.get(word)-1; k +=1){
            			wordString += " " + word;
            		}
            		stutterResults += "Repeated word on line " + lineNumber + " : " + wordString + "<br>";
            	}
            }
		}
		if(stutterResults == ""){
			stutterResults = "There are no stuttered words!";
		}
		return stutterResults;
	}
}
