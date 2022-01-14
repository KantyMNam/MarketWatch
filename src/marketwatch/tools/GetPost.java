package marketwatch.tools;

import java.io.*;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetPost {
	/** Get a string response form URL. This function may throw an exception.
	 * @param url URL to get response
	 * @exception IOException
	 * @return Response or null */
	private String getUrlString_private(String url) throws IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String output = rd.readLine();
			return output;
		}
		finally {
			is.close();
		}
	}
	
	/** Get a string response form URL.
	 * @param url URL to get response
	 * @return Response or null */
	public String getUrlString(String url) {
		//This method is for handling exception
		try {
			return getUrlString_private(url);
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	/** Get a map response form URL. This function may throw an exception.
	 * @param url URL to get response
	 * @exception IOException
	 * @exception ParseException
	 * @return Response or null */
	private JSONObject getUrlMap_private(String url) throws IOException, ParseException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			JSONParser parser = new JSONParser();
			JSONObject output = (JSONObject)parser.parse(rd.readLine());
			return output;
		}
		finally {
			is.close();
		}
	}

	/** Get a map response form URL.
	 * @param url URL to get response
	 * @return Response or null */
	public JSONObject getUrlMap(String url) {
		//This method is for handling exception
		try {
			return getUrlMap_private(url);
		}
		catch(Exception ex) {
			return null;
		}
	}
}