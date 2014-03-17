package com.vw.compare.common;

public class CompareServiceConfigBundle {

    private String isProxyRequired;
    private String proxyHost;
    private String proxyUsername;
    private String proxyPort;
    private String proxyPassword;
    private String compareServiceURL;
    private String generateReportFromDB;
    private String importErrorThreshold;
    
    
    public String getIsProxyRequired() {
        return isProxyRequired;
    }
    public void setIsProxyRequired(String isProxyRequired) {
        this.isProxyRequired = isProxyRequired;
    }
    public String getProxyHost() {
        return proxyHost;
    }
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }
    public String getProxyUsername() {
        return proxyUsername;
    }
    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }
    public String getProxyPort() {
        return proxyPort;
    }
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }
    public String getProxyPassword() {
        return proxyPassword;
    }
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }
    public String getCompareServiceURL() {
        return compareServiceURL;
    }
    public void setCompareServiceURL(String compareServiceURL) {
        this.compareServiceURL = compareServiceURL;
    }
    public String getGenerateReportFromDB() {
        return generateReportFromDB;
    }
    public void setGenerateReportFromDB(String generateReportFromDB) {
        this.generateReportFromDB = generateReportFromDB;
    }
    public String getImportErrorThreshold() {
        return importErrorThreshold;
    }
    public void setImportErrorThreshold(String importErrorThreshold) {
        this.importErrorThreshold = importErrorThreshold;
    }
    
        
}
