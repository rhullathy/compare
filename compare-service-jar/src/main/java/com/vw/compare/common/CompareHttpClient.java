package com.vw.compare.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.security.SignatureException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
/**
 * 
 * @author MIBPQM0
 *
 * 
 */
public class CompareHttpClient {

	private static final Logger log = Logger.getLogger(CompareHttpClient.class);

	private HttpClient httpClient = null;
	private HttpClientConfig config = null;
	private static String DEFAULT_ENCODING = "UTF8";

	/**
	 * 
	 * @param config
	 */
	public CompareHttpClient(HttpClientConfig config) {
		this.config = config;
		httpClient = configureHttpClient();
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public String invokeService(Map<String, String> parameters)
			throws Exception {
		return invoke(String.class, parameters);
	}

	/**
	 * Remove all leading whitespace, trailing whitespace, repeated whitespace
	 * and replace any interior whitespace with a single space
	 */
	private static String clean(String s) {
		return s.replaceAll("\\s", " ").replaceAll(" {2,}", " ").trim();
	}

	private static String quoteAppName(String s) {
		return clean(s).replace("\\", "\\\\").replace("/", "\\/");
	}

	private static String quoteAppVersion(String s) {
		return clean(s).replace("\\", "\\\\").replace("(", "\\(");
	}

	private static String quoteAttributeName(String s) {
		return clean(s).replace("\\", "\\\\").replace("=", "\\=");
	}

	private static String quoteAttributeValue(String s) {
		return clean(s).replace("\\", "\\\\").replace(";", "\\;")
				.replace(")", "\\)");
	}

	/**
	 * Configure HttpClient with set of defaults as well as configuration from
	 * MarketplaceWebServiceOrdersConfig instance
	 * 
	 */
	private HttpClient configureHttpClient() {

		/* Set http client parameters */
		HttpClientParams httpClientParams = new HttpClientParams();
		if (config.getUserAgent() == null) {
			config.setUserAgent(
					quoteAttributeValue("Java/"
							+ System.getProperty("java.version") + "/"
							+ System.getProperty("java.class.version") + "/"
							+ System.getProperty("java.vendor")),

					quoteAttributeName("Platform"),
					quoteAttributeValue("" + System.getProperty("os.name")
							+ "/" + System.getProperty("os.arch") + "/"
							+ System.getProperty("os.version"))
					);

		}
		httpClientParams.setParameter(HttpMethodParams.USER_AGENT,
				config.getUserAgent());
		httpClientParams.setParameter(HttpClientParams.RETRY_HANDLER,
				new HttpMethodRetryHandler() {

					public boolean retryMethod(HttpMethod method,
							IOException exception, int executionCount) {
						if (executionCount > 3) {
							log.debug("Maximum Number of Retry attempts reached, will not retry");
							return false;
						}
						log.debug("Retrying request. Attempt " + executionCount);
						if (exception instanceof NoHttpResponseException) {
							log.debug("Retrying on NoHttpResponseException");
							return true;
						}
						if (exception instanceof InterruptedIOException) {
							log.debug(
									"Will not retry on InterruptedIOException",
									exception);
							return false;
						}
						if (exception instanceof UnknownHostException) {
							log.debug("Will not retry on UnknownHostException",
									exception);
							return false;
						}
						if (!method.isRequestSent()) {
							log.debug("Retrying on failed sent request");
							return true;
						}
						return false;
					}
				});

		/* Set host configuration */
		HostConfiguration hostConfiguration = new HostConfiguration();

		/* Set connection manager parameters */
		HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
		connectionManagerParams.setConnectionTimeout(50000);
		connectionManagerParams.setSoTimeout(50000);
		connectionManagerParams.setStaleCheckingEnabled(true);
		connectionManagerParams.setTcpNoDelay(true);
		connectionManagerParams.setMaxTotalConnections(config
				.getMaxConnections());
		connectionManagerParams.setMaxConnectionsPerHost(hostConfiguration,
				config.getMaxConnections());

		/* Set connection manager */
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.setParams(connectionManagerParams);

		/* Set http client */
		httpClient = new HttpClient(httpClientParams, connectionManager);

		/* Set proxy if configured */
		if (config.isSetProxyHost() && config.isSetProxyPort()) {
			log.debug("Configuring Proxy. Proxy Host: " + config.getProxyHost()
					+ "Proxy Port: " + config.getProxyPort());
			hostConfiguration.setProxy(config.getProxyHost(),
					config.getProxyPort());
			if (config.isSetProxyUsername() && config.isSetProxyPassword()) {
				httpClient.getState()
						.setProxyCredentials(
								new AuthScope(config.getProxyHost(),
										config.getProxyPort()),
								new UsernamePasswordCredentials(config
										.getProxyUsername(), config
										.getProxyPassword()));

			}
		}

		httpClient.setHostConfiguration(hostConfiguration);

		return httpClient;
	}
	
	/**
	 * Add authentication related and version parameter and set request body
	 * with all of the parameters
	 */
	private void addRequiredParametersToRequest(PostMethod method,
			Map<String, String> parameters) throws SignatureException {

		if(parameters != null){
			for (Entry<String, String> entry : parameters.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				// filter empty parameters
				if (key == null || key.equals("") || value == null
						|| value.equals("")) {
					continue;
				}
				method.addParameter(key, value);
			}
		}
	}

	/**
	 * Invokes request using parameters from parameters map. Returns response of
	 * the T type passed to this method
	 */
	@SuppressWarnings("unchecked")
	private <T> T invoke(Class<T> clazz, Map<String, String> parameters)
			throws Exception {

		T response = null;
		String responseBodyString = null;
		PostMethod method = new PostMethod(config.getServiceURL());
		int status = -1;

		log.debug("Invoking request. with parameters: " + parameters);

		try {

			/* Set content type and encoding */
			log.debug("Setting content-type to application/x-www-form-urlencoded; charset="
					+ DEFAULT_ENCODING.toLowerCase());
			method.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded; charset="
							+ DEFAULT_ENCODING.toLowerCase());
			/* Set X-Amazon-User-Agent to header */
			method.addRequestHeader("X-Amazon-User-Agent",
					config.getUserAgent());

			/* Add required request parameters and set request body */
			log.debug("Adding required parameters...");
			addRequiredParametersToRequest(method, parameters);
			log.debug("Done adding additional required parameteres. Parameters now: "
					+ parameters);

			boolean shouldRetry = true;
			int retries = 4;
			do {
				log.debug("Sending Request to host:  " + config.getServiceURL());

				try {

					/* Submit request */
					status = httpClient.executeMethod(method);

					/* Consume response stream */
					responseBodyString = getResponsBodyAsString(method
							.getResponseBodyAsStream());

					/*
					 * Successful response. Attempting to unmarshal into the
					 * <Action>Response type
					 */
					if (status == HttpStatus.SC_OK
							&& responseBodyString != null) {
						shouldRetry = false;
						log.debug("Received Response. Status: " + status + ". ");
								//+ "Response Body: " + responseBodyString);

					} else { /*
							 * Unsucessful response. Attempting to unmarshall
							 * into ErrorResponse type
							 */

						log.debug("Received Response. Status: " + status + ". ");
								//+ "Response Body: " + responseBodyString);

						if ((status == HttpStatus.SC_INTERNAL_SERVER_ERROR || status == HttpStatus.SC_SERVICE_UNAVAILABLE)
								&& pauseIfRetryNeeded(++retries)) {
							shouldRetry = true;
						} else {
							shouldRetry = false;
						}
					}

				} catch (IOException ioe) {
					log.debug("Caught IOException exception", ioe);
					throw ioe;
				} catch (Exception e) {
					log.debug("Caught Exception", e);
					throw e;
//				} catch (Throwable t) {
//					log.debug("Caught Exception", t);
//					throw t;
				} finally {
					method.releaseConnection();
				}
			} while (shouldRetry);

		} catch (Exception t) {
			log.debug("Caught Exception", t);
			throw new Exception(t.getMessage());
		}
		response = (T) responseBodyString;
		return response;
	}

	/**
	 * Read stream into string
	 * 
	 * @param input
	 *            stream to read
	 */
	private String getResponsBodyAsString(InputStream input) throws IOException {
		String responsBodyString = null;
		try {
			//Reader reader = new InputStreamReader(input, DEFAULT_ENCODING);
			Reader reader = new InputStreamReader(input);
			
			StringBuilder b = new StringBuilder();
			char[] c = new char[1024];
			int len;
			while (0 < (len = reader.read(c))) {
				b.append(c, 0, len);
			}
			
			responsBodyString = b.toString();

			
		} finally {
			input.close();
		}
		return responsBodyString;
	}

	/**
	 * Exponential sleep on failed request. Sleeps and returns true if retry
	 * needed
	 * 
	 * @param retries
	 *            current retry
	 * @throws java.lang.InterruptedException
	 */
	private boolean pauseIfRetryNeeded(int retries) throws InterruptedException {
		if (retries <= config.getMaxErrorRetry()) {
			long delay = (long) (Math.pow(4, retries) * 100L);
			log.debug("Retriable error detected, will retry in " + delay
					+ "ms, attempt number: " + retries);
			Thread.sleep(delay);
			return true;
		} else {
			return false;
		}
	}

}
