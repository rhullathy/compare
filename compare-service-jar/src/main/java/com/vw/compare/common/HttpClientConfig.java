package com.vw.compare.common;

public class HttpClientConfig {

	private String serviceURL = null;
	private String userAgent = null;
	private String proxyHost = null;
	private int proxyPort = -1;
	private String proxyUsername = null;
	private String proxyPassword = null;
	private int maxErrorRetry = 3;
	private int maxConnections = 100;

	/**
	 * Checks if SignatureVersion property is set
	 * 
	 * @return true if SignatureVersion property is set
	 */
	public boolean isSetSignatureVersion() {
		return true;
	}

	/**
	 * Checks if SignatureMethod property is set
	 * 
	 * @return true if SignatureMethod property is set
	 */
	public boolean isSetSignatureMethod() {
		return true;
	}

	/**
	 * Gets UserAgent property
	 * 
	 * @return User Agent String to use when sending request
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * Sets UserAgent property
	 * 
	 * @param userAgent
	 *            User Agent String to use when sending request
	 * 
	 */
	public void setUserAgent(String applicationName, String applicationVersion,
			String programmingLanguage, String... additionalNameValuePairs) {
		if (applicationName == null)
		{
			throw new IllegalArgumentException("applicationName cannot be NULL");
		}
			
		if (applicationVersion == null)
		{
			throw new IllegalArgumentException(
			"applicationVersion cannot be NULL");
		}
			
		if (programmingLanguage == null)
		{
			throw new IllegalArgumentException(
			"programmingLanguage cannot be NULL");
		}
			
		if (additionalNameValuePairs.length % 2 != 0)
		{
			throw new IllegalArgumentException(
			"there must be a matching value for every name you pass in");
		}
			
		StringBuilder b = new StringBuilder();
		b.append(applicationName);
		b.append("/");
		b.append(applicationVersion);
		b.append(" (Language=");
		b.append(programmingLanguage);
		int i = 0;
		while (i < additionalNameValuePairs.length) {
			String name = additionalNameValuePairs[i];
			String value = additionalNameValuePairs[i + 1];
			b.append("; ");
			b.append(name);
			b.append("=");
			b.append(value);
			i += 2;
		}
		b.append(")");
		this.userAgent = b.toString();
	}

	/**
	 * Sets UserAgent property and returns current VPOHttpClientConfig
	 * 
	 * @param userAgent
	 *            User Agent String to use when sending request
	 * 
	 * @return VPOHttpClientConfig
	 */
	public HttpClientConfig withUserAgent(String applicationName,
			String applicationVersion, String programmingLanguage,
			String... additionalNameValuePairs) {
		setUserAgent(applicationName, applicationVersion, programmingLanguage,
				additionalNameValuePairs);
		return this;
	}

	/**
	 * Checks if UserAgent property is set
	 * 
	 * @return true if UserAgent property is set
	 */
	public boolean isSetUserAgent() {
		return this.userAgent != null;
	}

	/**
	 * Gets ServiceURL property
	 * 
	 * @return Service Endpoint URL
	 */
	public String getServiceURL() {
		return serviceURL;
	}

	/**
	 * Sets ServiceURL property
	 * 
	 * @param serviceURL
	 *            Service Endpoint URL
	 * 
	 */
	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	/**
	 * Sets ServiceURL property and returns current VPOHttpClientConfig
	 * 
	 * @param serviceURL
	 *            Service Endpoint URL
	 * 
	 * @return VPOHttpClientConfig
	 */
	public HttpClientConfig withServiceURL(String serviceURL) {
		setServiceURL(serviceURL);
		return this;
	}

	/**
	 * Checks if ServiceURL property is set
	 * 
	 * @return true if ServiceURL property is set
	 */
	public boolean isSetServiceURL() {
		return this.serviceURL != null;
	}

	/**
	 * Gets ProxyHost property
	 * 
	 * @return Proxy Host for connection
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * Sets ProxyHost property
	 * 
	 * @param proxyHost
	 *            Proxy Host for connection
	 * 
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * Sets ProxyHost property and returns current VPOHttpClientConfig
	 * 
	 * @param proxyHost
	 *            Proxy Host for connection
	 * 
	 * @return VPOHttpClientConfig
	 */
	public HttpClientConfig withProxyHost(String proxyHost) {
		setProxyHost(proxyHost);
		return this;
	}

	/**
	 * Checks if ProxyHost property is set
	 * 
	 * @return true if ProxyHost property is set
	 */
	public boolean isSetProxyHost() {
		return this.proxyHost != null;
	}

	/**
	 * Gets ProxyPort property
	 * 
	 * @return Proxy Port for connection
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * Sets ProxyPort property
	 * 
	 * @param proxyPort
	 *            Proxy Port for connection
	 * 
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * Sets ProxyPort property and returns current VPOHttpClientConfig
	 * 
	 * @param proxyPort
	 *            Proxy Port for connection
	 * 
	 * @return VPOHttpClientConfig
	 */
	public HttpClientConfig withProxyPort(int proxyPort) {
		setProxyPort(proxyPort);
		return this;
	}

	/**
	 * Checks if ProxyPort property is set
	 * 
	 * @return true if ProxyPort property is set
	 */
	public boolean isSetProxyPort() {
		return this.proxyPort != -1;
	}

	/**
	 * Gets ProxyUsername property
	 * 
	 * @return Proxy Username
	 */
	public String getProxyUsername() {
		return proxyUsername;
	}

	/**
	 * Sets ProxyUsername property
	 * 
	 * @param proxyUsername
	 *            Proxy Username for connection
	 * 
	 */
	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	/**
	 * Sets ProxyUsername property and returns current VPOHttpClientConfig
	 * 
	 * @param proxyUsername
	 *            Proxy Username for connection
	 * 
	 * @return VPOHttpClientConfig
	 */
	public HttpClientConfig withProxyUsername(String proxyUsername) {
		setProxyUsername(proxyUsername);
		return this;
	}

	/**
	 * Checks if ProxyUsername property is set
	 * 
	 * @return true if ProxyUsername property is set
	 */
	public boolean isSetProxyUsername() {
		return this.proxyUsername != null;
	}

	/**
	 * Gets ProxyPassword property
	 * 
	 * @return Proxy Password
	 */
	public String getProxyPassword() {
		return proxyPassword;
	}

	/**
	 * Sets ProxyPassword property
	 * 
	 * @param proxyPassword
	 *            Proxy Password for connection
	 * 
	 */
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	/**
	 * Sets ProxyPassword property and returns current VPOHttpClientConfig
	 * 
	 * @param proxyPassword
	 *            Proxy Password for connection
	 * 
	 * @return VPOHttpClientConfig
	 */
	public HttpClientConfig withProxyPassword(String proxyPassword) {
		setProxyPassword(proxyPassword);
		return this;
	}

	/**
	 * Checks if ProxyPassword property is set
	 * 
	 * @return true if ProxyPassword property is set
	 */
	public boolean isSetProxyPassword() {
		return this.proxyPassword != null;
	}

	/**
	 * Gets MaxErrorRetry property
	 * 
	 * @return Max number of retries on 500th errors
	 */
	public int getMaxErrorRetry() {
		return maxErrorRetry;
	}

	/**
	 * Sets MaxErrorRetry property
	 * 
	 * @param maxErrorRetry
	 *            Max number of retries on 500th errors
	 * 
	 */
	public void setMaxErrorRetry(int maxErrorRetry) {
		this.maxErrorRetry = maxErrorRetry;
	}

	/**
	 * Sets MaxErrorRetry property and returns current VPOHttpClientConfig
	 * 
	 * @param maxErrorRetry
	 *            Max number of retries on 500th errors
	 * 
	 * @return VPOHttpClientConfig
	 */
	public HttpClientConfig withMaxErrorRetry(int maxErrorRetry) {
		setMaxErrorRetry(maxErrorRetry);
		return this;
	}

	/**
	 * Checks if MaxErrorRetry property is set
	 * 
	 * @return true if MaxErrorRetry property is set
	 */
	public boolean isSetMaxErrorRetry() {
		return this.maxErrorRetry > 0;
	}

	/**
	 * Gets MaxConnections property
	 * 
	 * @return Max number of http connections
	 */
	public int getMaxConnections() {
		return maxConnections;
	}

	/**
	 * Sets MaxConnections property
	 * 
	 * @param maxConnections
	 *            Max number of http connections
	 * 
	 */
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	/**
	 * Sets MaxConnections property and returns current VPOHttpClientConfig
	 * 
	 * @param maxConnections
	 *            Max number of http connections
	 * 
	 * @return VPOHttpClientConfig
	 */
	public HttpClientConfig withMaxConnections(int maxConnections) {
		setMaxConnections(maxConnections);
		return this;
	}

	/**
	 * Checks if MaxConnections property is set
	 * 
	 * @return true if MaxConnections property is set
	 */
	public boolean isSetMaxConnections() {
		return this.maxConnections > 0;
	}

}
