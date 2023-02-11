package mobi.vandewalle.thermostat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;



public class HttpUtils {
	 
	  public static String getContents(String url) {
	        String contents ="";
	 
	  try {
	        URLConnection conn = new URL(url).openConnection();
	 
	        InputStream in = conn.getInputStream();
	        contents = convertStreamToString(in);
	   } catch (MalformedURLException e) {
		   Log.e("MALFORMED URL EXCEPTION", url);
	        System.out.println("111");
		   e.printStackTrace();
	   } catch (IOException e) {
	        Log.e(e.getMessage(), "");
	        System.out.println("222");
	        e.printStackTrace();
	   }
	 
	  return contents;
	}
	 
	private static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
	 
	      BufferedReader reader = new BufferedReader(new    
	                              InputStreamReader(is, "UTF-8"));
	        StringBuilder sb = new StringBuilder();
	         String line = null;
	         try {
	                while ((line = reader.readLine()) != null) {
	                        sb.append(line + "\n");
	                }
	           } catch (IOException e) {
	               System.out.println("333"); 
	        	   e.printStackTrace();
	           } finally {
	                try {
	                        is.close();
	                } catch (IOException e) {
	                	System.out.println("444"); 
	                        e.printStackTrace();
	                }
	            }
	        return sb.toString();
	  }
	}
