package com.vw.compare.common;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.vw.compare.domain.Comparison;
import com.vw.compare.domain.Competitors;
import com.vw.compare.domain.InvalidTrim;
import com.vw.compare.domain.Makes;
import com.vw.compare.domain.ModelTrims;
import com.vw.compare.domain.Trims;
import com.vw.compare.domain.YearmakeModels;


public class CompareServiceHelper {

    private static final Logger log = Logger.getLogger(CompareServiceHelper.class);
    
    private  CompareServiceConfigBundle compareServiceConfigBundle;
    
    protected static final String GET_MAKES_URI = "/MakesByYear.asp?AS3_ClientID=Volkswagen&YearID=";

    protected static final String GET_MODEL_URI = "/YearMakeModels.asp?AS3_ClientID=Volkswagen&Year=";

    protected static final String GET_TRIMS_URI = "/ModelTrims.asp?AS3_ClientID=Volkswagen&ModelID=";
    
    protected static final String GET_COMPARE_REPORT_URI = "/CompReport.asp?AS3_LanguageCode=7&AS3_ClientID=Volkswagen&Vehicles=";

    protected static final String GET_ALL_TRIMS_URI = "/Trims.asp";
    
    protected static final String GET_SUGGESTED_TRIMS_URI = "/Competitors.asp?AS3_ClientID=Volkswagen";
    
    private Map<String, JAXBContext> contextMap = new HashMap<String, JAXBContext>();
        
        
    /**
     * 
     * @param serviceURL
     * @return
     * @throws Exception
     */
    public  String callExternalService(final String URI) throws CompareServiceException {
        try {
            HttpClientConfig config = new HttpClientConfig();
            String serviceURL = compareServiceConfigBundle.getCompareServiceURL()+URI;
            log.trace("URL>>"+ serviceURL);
            config.setServiceURL(serviceURL);
            if ("true".equalsIgnoreCase(compareServiceConfigBundle.getIsProxyRequired())) {
                config.setProxyHost(compareServiceConfigBundle.getProxyHost());
                config.setProxyPort(Integer.parseInt(compareServiceConfigBundle.getProxyPort()));
            }
            CompareHttpClient httpClient = new CompareHttpClient(config);
            log.debug("Invoking service");
            String xml = httpClient.invokeService(null);
            log.trace(xml);
            return xml;
        } catch (Exception e) {
                log.error(e,e);
            throw new CompareServiceException(999);
        }
    }
    
    /**
     * convert the xml into domain object.
     * 
     * @param xml
     * @param modelCode
     * @return
     * @throws CompareServiceException
     */
    public  Object unmarshalXML(String xml, Class... objectClass) throws CompareServiceException {
        log.debug("inside unMarshalXML  input xml >>" + xml);
        JAXBContext jContext = null;
        Unmarshaller unmarshaller = null;
        Object obj = null;
        try {

            if (xml == null) {
                return null;
            }
            byte[] byteArray = xml.getBytes();
            if(!contextMap.keySet().contains(objectClass))
                contextMap.put(StringUtils.arrayToCommaDelimitedString(objectClass), JAXBContext.newInstance(objectClass));
            
            jContext =  contextMap.get(StringUtils.arrayToCommaDelimitedString(objectClass));
            
            if (jContext != null) {
                // create a unMarshaller
                unmarshaller = jContext.createUnmarshaller();
                if (unmarshaller != null) {
                    Reader reader = new InputStreamReader(new ByteArrayInputStream(byteArray), "UTF-8");
                    obj = unmarshaller.unmarshal(reader);

                } else {
                    log.error("  unmarshaller NOT OK !!");
                }
            } else {
                log.error(" jContext NOT OK !!");
            }
            log.debug("Unmarshaling is success");

            
        } catch (Exception e) {
            log.error("Exception during unMarshalXML " + e, e);
            throw new CompareServiceException(999);
        }
        return obj;

    }

    public  CompareServiceConfigBundle getCompareServiceConfigBundle() {
        return compareServiceConfigBundle;
    }

    public  void setCompareServiceConfigBundle(CompareServiceConfigBundle compareServiceConfigBundle) {
        this.compareServiceConfigBundle = compareServiceConfigBundle;
    }
    
    public Makes getMake(String year)   {
        String[] array = new String[1];
        array[0] = year;
        return getMake(array);
    }
    
    public Makes getMake(String[] years)   {
        
        Makes combinedMakes = new Makes();
        for(String year: years){
            try{
                String xml = callExternalService(GET_MAKES_URI+year);
                log.trace("XML>>"+xml);
                Makes makes = (Makes)unmarshalXML(xml, Makes.class);
                combinedMakes.getMake().addAll(makes.getMake());
            }
            catch(Exception e){
                //don't do anything for now
            }
        }
        return combinedMakes;        
    }

    public YearmakeModels getModel(Integer year, String make)   {
        
        YearmakeModels yearmakeModels = new YearmakeModels();
        try{
            String uri = GET_MODEL_URI+year+"&Make="+make;
            String xml = callExternalService(uri);
            log.trace("XML>>"+xml);
            yearmakeModels = (YearmakeModels)unmarshalXML(xml, YearmakeModels.class);           
        }
        catch(Exception e){
            //don't do anything for now
        }
        return yearmakeModels;
        
        //return new MockResponse("getModel >>"+year+"<>"+make);
    }

    public ModelTrims getTrims(String modelId)   {
        
        ModelTrims modelTrims = new ModelTrims();
        try{
            String xml = callExternalService(GET_TRIMS_URI+modelId);
            log.trace("XML>>"+xml);
            modelTrims = (ModelTrims)unmarshalXML(xml, ModelTrims.class);           
        }
        catch(Exception e){
            //don't do anything for now
        }
        return modelTrims;        
    }
    


    public Trims getTrimDetails(Map<String, ArrayList<String>> filterTypeValueMap) throws CompareServiceException {
        Trims trims = new Trims();
        try{
            
            String xml = callExternalService(GET_ALL_TRIMS_URI);
            log.trace("XML>>"+xml);
            trims = (Trims)unmarshalXML(xml, Trims.class);           
        }
        catch(Exception e){
            //don't do anything for now
        }
        return trims;
        
    }

   
    public Comparison getComparisonReport(Object[] trimids) throws CompareServiceException {
        Comparison comparison = null;
        try{
            String trims = "";
            for(Object trim:trimids){
                trims += trim+"%7C";
            }
            String xml = callExternalService(GET_COMPARE_REPORT_URI+trims.substring(0,trims.length()-3));
            log.trace("XML>>"+xml);
            Object obj=unmarshalXML(xml, Comparison.class,InvalidTrim.class);    
            if(obj instanceof Comparison)
            	return (Comparison) obj;
            else
            	return new Comparison();
        }
        catch(Exception e){
            log.trace("getComparisonReport", e);
            //don't do anything for now
            throw new CompareServiceException(100);
        }
        
    }

    public boolean generateReportFromDB() {
        
        return "true".equalsIgnoreCase(compareServiceConfigBundle.getGenerateReportFromDB())?true:false;
        
    }   
    
    public int getImportErrorThreshold() {
        
        int value = 100;
        try{
            if(compareServiceConfigBundle.getImportErrorThreshold() != null)
                value = Integer.parseInt(compareServiceConfigBundle.getImportErrorThreshold());
        }
        catch(Exception e){
            
        }
        return value;
        
    }
    
   
    public Competitors getSuggestedTrims() throws CompareServiceException   {
           
   	 Competitors competitors = new Competitors();
           try{
               String xml = callExternalService(GET_SUGGESTED_TRIMS_URI);
               log.trace("XML>>"+xml);
               competitors = (Competitors)unmarshalXML(xml, Competitors.class);           
           }
           catch(Exception e){
        	   log.error(e);
           }
           return competitors;        
       }
    
}
