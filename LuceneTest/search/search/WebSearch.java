package search;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.output.ByteArrayOutputStream;

public class WebSearch {
	private static final int RESULT_OK = 200;
	private static final int BUF_SIZE = 1024 * 8; // 8k
 
	public static void main(String[] args) {
		// API key
		String strKey = "AIzaSyBwvtxmR5STL0Ip_B2xYAqprDgdudXVH3M";
		// Unique ID
		String strID = "018363244458407387672:lr-zbi6wipu"; 

		String strQ = "lucene";

		String strRequest = "https://www.googleapis.com/customsearch/v1?key=AIzaSyBwvtxmR5STL0Ip_B2xYAqprDgdudXVH3M&cx=018363244458407387672:lr-zbi6wipu&q=lucene";
		//strRequest = strRequest.replace("%key%", strKey).replace("%id%", strID).replace("%q%", strQ);
		
		HttpURLConnection conn = null;
		String result = "";
		try {
			URL url = new URL(strRequest);
			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			int resultCode = conn.getResponseCode();
			System.out.println(resultCode);
			if (resultCode == RESULT_OK) {
				System.out.println(resultCode);
				InputStream is = conn.getInputStream();
				result = readAsString(is);
				is.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		
		System.out.println(result);
	}
 
	/**
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public static String readAsString(InputStream ins) throws IOException {
		ByteArrayOutputStream outs = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUF_SIZE];
		int len = -1;
		try {
			while ((len = ins.read(buffer)) != -1) {
				outs.write(buffer, 0, len);
			}
		} finally {
			outs.flush();
			outs.close();
		}
		return outs.toString();
	}
}
