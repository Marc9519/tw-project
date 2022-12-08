package org.tw.intface.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The UrlConnector
 */
public class UrlConnector {

	/*
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(UrlConnector.class);

	/**
	 * @param requestUrl The URL
	 * @return The inputstream
	 * @throws IOException If an I/O Error occurs
	 */
	public InputStream getInputStreamByUrl(String requestUrl) throws IOException {
		URL netUrl = new URL(requestUrl);
		URLConnection connection = netUrl.openConnection();
		return connection.getInputStream();
	}

	/**
	 * Gets the InputStream of the URL connection
	 * @param worldPrefix the prefix
	 * @return the InputStream
	 */
	public List<InputStream> getMapDataRequestInputStreams(String worldPrefix) {
		List<String> requestUrls = generateRequestUrls(worldPrefix);
		return getUrlRequestInputStreams(requestUrls);
	}

	private List<String> generateRequestUrls(String worldPrefix) {
		List<String> result = new ArrayList<>();
		//0_0,0_20,...,0_980
		//980_0,980_20,...,980_980
		for (int i = 0; i < 50; i++) {
			StringBuilder currentUrl = new StringBuilder();
			currentUrl.append("https://" + worldPrefix + ".die-staemme.de/map.php?");
			for (int j = 0; j < 50; j++) {
				int currentXOffset = 20 * i;
				int currentYOffset = 20 * j;
				currentUrl.append(currentXOffset + "_" + currentYOffset + "=1&");
			}
			result.add(currentUrl.substring(0, currentUrl.length()-1));
		}

		return result;
	}

	private List<InputStream> getUrlRequestInputStreams(List<String> requestUrls) {
		List<InputStream> ins = new ArrayList<>();
		try {
			for (String requestUrl : requestUrls) {
				InputStream in = getInputStreamByUrl(requestUrl);
				ins.add(in);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return ins;
	}

}