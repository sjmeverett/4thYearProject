package pacman.experimentclient;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpClient
{
	public String getResponse(String url, String method, String data) throws IOException
	{
		URL u = new URL(url);
		HttpURLConnection connection = (HttpURLConnection)u.openConnection();
		
		connection.setRequestMethod(method);
		connection.setDoInput(true);
		
		if (data != null)
		{
			connection.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(data);
			writer.flush();
			writer.close();
		}
		
		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
			throw new IOException("Failed request: HTTP " + connection.getResponseCode());
		
		Scanner scanner = new Scanner(connection.getInputStream());
		scanner.useDelimiter("\\A");
		String response = scanner.hasNext() ? scanner.next() : "";
		
		scanner.close();
		return response;
	}
	
	
	public String post(String url, String data) throws IOException
	{
		return getResponse(url, "POST", data);
	}
	
	
	public String get(String url) throws IOException
	{
		return getResponse(url, "GET", null);
	}
}
