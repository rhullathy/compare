package com.vw.compare.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

import com.vw.compare.common.CompareServiceException;
import com.vw.compare.domain.Comparison;
import com.vw.compare.domain.Makes;
import com.vw.compare.domain.MockResponse;
import com.vw.compare.domain.ModelTrims;
import com.vw.compare.domain.Trims;
import com.vw.compare.domain.YearmakeModels;
import com.vw.compare.domain.Years;
import com.vw.compare.service.CompareService;

/**
 * Spring Rest controller that handles requests for special offers.
 * 
 * @author prakash manchenahalli
 */
@Controller
public class CompareController {

    private static final String WITH_ADVANTAGE_TEXT = "WithAdvantageText";

    /**
     * Logger.
     */
    private static final Logger log = Logger.getLogger(CompareController.class);

    private static final String YEAR = "year";
    private static final String MODEL_ID = "modelid";
    private static final String TRIM_ID = "trimid";
    private static final String MAKE = "make";
    private static final String MODEL_NAME = "modelname";
    private static final String TRIM_NAME = "trimname";
    
    
    

    /**
     * Service injected by Spring that provides search operations for
     */
    @Autowired
    private CompareService compareService;

    /**
     * At least one trimId is required /compare/suggested_trims/{trimIds [1..10]
     * separated by /}
     * http://localhost:8080/compare/suggested_trims/1234.xml
     * @param trimIds
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ={"/suggested_trims*/**"}, method = { RequestMethod.GET, RequestMethod.POST })
    public final ModelTrims getTrimDetailsWithSuggestedTrims(final ServletRequest request)
            throws CompareServiceException {
        log.debug("BEGIN : getTrimDetailsWithSuggestedTrims");
        String[] trims =  null;
        try{
            String trimids = getURIParams(request, "suggested_trims");
            if(trimids != null)
                trims = trimids.split("\\|");
            
            if(trims == null){
                throw new CompareServiceException(100); //Minimum one Trim Id is required
            }
            if(trims.length > 10){
                throw new CompareServiceException(101); //Input exceeds the supported limit(10)
            }
            
            return compareService.getTrimDetailsWithSuggestedTrims(trims);
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }
 



    /**
     * make is optional, if provided provide available years for given make
     * /compare/years/{make}
     * http://localhost:8080/compare/years.xml
     * http://localhost:8080/compare/years/honda.xml
     * 
     * @param make
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ={"/years*/**"}, method = { RequestMethod.GET, RequestMethod.POST })    
    Years getYears(final ServletRequest request) throws CompareServiceException{
        log.debug("BEGIN : getYears");
        try{
            
            String make = getURIParams(request, "years");
            return compareService.getYears(make);
            
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getYears", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }

    /**
     * List makes available for given years, at least one year is required
     * 
     * /compare/makes/{years [1..10] separated by /}
     * http://localhost:8080/compare/makes/2012|2013.xml
     * 
     * output is similar to :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen
     * /MakesByYear.asp?AS3_ClientID=Volkswagen&YearID=2013
     * 
     * 
     * @param years
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ={"/makes*/**"}, method = { RequestMethod.GET, RequestMethod.POST })    
    Makes getMake(final ServletRequest request) throws CompareServiceException{
        log.debug("BEGIN : getMake");
        String[] yearArray =  null;
        try{
            String years = getURIParams(request, "makes");
            if(years != null)
                yearArray = years.split("\\|");
    
            if(yearArray == null){
                throw new CompareServiceException(200); //Minimum one Year is required
            }
            if(yearArray.length > 10){
                throw new CompareServiceException(101); //Input exceeds the supported limit(10)
            }
            for(String year:yearArray)
            {
            	if(!StringUtils.isNumeric(year))
            	{
            		throw new CompareServiceException(300);
            	}
            }
            return compareService.getMake(yearArray);
            
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getMake", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }

    /**
     * Provide Model name and model id for given year and make, both make and
     * year are required
     * 
     * /compare/model/{year}/{make}
     * http://localhost:8080/compare/model/2012/Volkswagen.xml
     * 
     * output is similar to :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen
     * /YearMakeModels.asp?AS3_ClientID=Volkswagen&Year=2013&Make=Cadillac
     * 
     * 
     * @param year
     * @param make
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ="/model/{year}/{make}" , method = { RequestMethod.GET, RequestMethod.POST })    
    YearmakeModels getModel(@PathVariable final String year, @PathVariable final String  make) throws CompareServiceException{
        log.debug("BEGIN : getModel");
        try{
            
            return compareService.getModel(Integer.parseInt(year), make);
        }
        catch(NumberFormatException ne){
            throw new CompareServiceException(300); //Year value must be integer       
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getModel", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
        
    }
 
    
    
    /**
     * Return All available Trim name, Trim ID and image URL for given model id,
     * model id is required
     * 
     * /compare/trims/{modelid}
     * http://localhost:8080/compare/trims/7396.xml
     * 
     * output is similar to :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen
     * /ModelTrims.asp?AS3_ClientID=Volkswagen&ModelID=7730
     * 
     * @param modelId
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ={"/trims/{modelid}"}, method = { RequestMethod.GET, RequestMethod.POST })    
    ModelTrims getTrims(@PathVariable final String modelid) throws CompareServiceException{
        log.debug("BEGIN : getTrims");
        try{
            return compareService.getTrims(modelid);
            
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }
 
    
    
    /**
     * filterTypeValueMap is map of <filterType, filterTypeValues> FilterType
     * can be - year, make, modelid or trimid filtervalue - based on filterType,
     * corresponding list of values will be present
     * 
     * eg: filterTypeValue{{{year},[2012, 2011]},{{make},[VOLKSWAGEN, HONDA]} }
     * 
     * Complete URL: /compare/trimdetails/year/{years[0..10] separated by
     * /}/make/{makes[0..10] separated by /}/modelid/{modelid[0..10] separated
     * by /}/trimid/{trimid[0..10] separated by /}
     * 
     * Possible usages: /compare/trimdetails --> list all available years,
     * makes, models & trims /compare/trimdetails/year/{years[0..10] separated
     * by /} --> Results are filters by years
     * /compare/trimdetails/make/{makes[0..10] separated by /} --> Results are
     * filters by makes /compare/trimdetails/modelid/{modelid[0..10] separated
     * by /} --> Results are filters by modelIds
     * /compare/trimdetails/trimid/{trimid[0..10] separated by /} --> Results
     * are filters by trimIds
     * 
     * Example: /compare/trimdetails/year/2012/2011
     * /compare/trimdetails/make/VOLKSWAGEN/HONDA
     * /compare/trimdetails/modelid/12324/98456
     * /compare/trimdetails/trimid/33876/23432
     * 
     * Advance Filter option
     * 
     * 1) /compare/trimdetails/year/2012/make/VOLKSWAGEN/modelid/12324/trimid/
     * 33876 --> restrict the results to 2012 year AND VOLKSWAGEN brand AND
     * modleId 12324 AND trimId 33876
     * 
     * /compare/trimdetails/year/2012/trimid/33876 --> restrict the results to
     * 2012 year AND trimId 33876 output is similar to :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen/Trims.asp
     * 
     * @param filterTypeValueMap
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ={"/trimdetails*/**"}, method = { RequestMethod.GET, RequestMethod.POST })    
    Trims getTrimDetails(final ServletRequest request) throws CompareServiceException{
        log.debug("BEGIN : getTrimDetails");
        String[] filterArray =  null;
        try{
            String params = getURIParams(request, "trimdetails");        
            if(params != null)
                filterArray = params.split("/");
            
            //validate & populate map here
            Map<String, ArrayList<String>> filterMap = null;
            if(filterArray != null){
                filterMap = new HashMap<String, ArrayList<String>> ();
                ArrayList<String> alist = new ArrayList<String>();   
                String paramName=null;
                for(String value:filterArray){
                    if(YEAR.equalsIgnoreCase(value) ||
                            MODEL_ID.equalsIgnoreCase(value) ||
                            MODEL_NAME.equalsIgnoreCase(value) ||
                            TRIM_NAME.equalsIgnoreCase(value) ||                            
                            TRIM_ID.equalsIgnoreCase(value)  ||
                            MAKE.equalsIgnoreCase(value))
                    {
                        alist = new ArrayList<String>();
                        filterMap.put(value, alist);
                        paramName=value;
                        continue;
                    }else{
                        String[] filterValues = value.split("\\|");
                        
                        if(filterValues.length > 10){
                            throw new CompareServiceException(101); //Input exceeds the supported limit(10)
                        }
                        
                        for(String filterValue:filterValues)
                        {
                        	if(YEAR.equalsIgnoreCase(paramName))
                        	{
                        		if(!StringUtils.isNumeric(filterValue))
                        			throw new CompareServiceException(300);
                        	}
                            alist.add(filterValue);
                        }
                    }
                    
                }
            }
            
            return compareService.getTrimDetails(filterMap);
            
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetails", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }
  
    /**
     * 1 or 4 trimIds can be passed, where first trim id should corresponds to
     * VW trimId , vwtrimid is required
     * 
     * /compare/vehicles/{vw_trimid}/{other trimids[0..3] separated by /}
     * 
     * Response :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen/CompReport.asp?AS3_LanguageCode=7&AS3_ClientID=Volkswagen&Vehicles=34485|31070
     * 
     * @param trimids
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ={"/vehicles*/**"}, method = { RequestMethod.GET, RequestMethod.POST })    
    Comparison getComparisonReport(final ServletRequest request) throws CompareServiceException{
        log.debug("BEGIN : getComparisonReport");
        try{
            String[] trims =  null;
            String trimids = getURIParams(request, "vehicles");
            boolean advantageText = false;
            if(trimids.indexOf("/") > 0 && WITH_ADVANTAGE_TEXT.equalsIgnoreCase(trimids.substring(trimids.indexOf("/")+1))){
                advantageText = true;
                trimids = trimids.substring(0, trimids.indexOf("/"));
            }
                
            if(trimids != null)
                trims = trimids.split("\\|");
            
            if(trims == null){
                throw new CompareServiceException(100); //Minimum one Trim Id is required
            }
            if(trims.length > 4){
                throw new CompareServiceException(102); //Input exceeds the supported limit(4)
            }
            return compareService.getComparisonReport(trims, advantageText);
            
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getModel", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }
   
    /**
     * make is optional, if provided provide available years for given make
     * /compare/data_refresh
     * http://localhost:8080/compare/years.xml
     * http://localhost:8080/compare/years/honda.xml
     *  
     * @param make
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ={"/data_refresh*/**"}, method = { RequestMethod.GET, RequestMethod.POST })    
    MockResponse refreshData(final ServletRequest request) throws CompareServiceException{
        log.debug("BEGIN : refreshData");
        try{
            
            String type = getURIParams(request, "data_refresh");   
            return compareService.refreshData(type);
            
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in refreshData", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }

    /**
     * make is optional, if provided provide available years for given make
     * /compare/data_refresh
     * http://localhost:8080/compare/years.xml
     * http://localhost:8080/compare/years/honda.xml
     * 
     * @param make
     * @return
     * @throws CompareServiceException
     */
    @RequestMapping(value ={"/cache_refresh"}, method = { RequestMethod.GET, RequestMethod.POST })    
    MockResponse refreshCache(final ServletRequest request) throws CompareServiceException{
        log.debug("BEGIN : refreshCache");
        try{
            
            return compareService.refreshCache();
            
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in refreshCache", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }


    /**
     * 
     * @param request
     * @param string
     * @return
     */
    private String getURIParams(ServletRequest request, String service) {
        String params = ((String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).replaceFirst(service, "");
        params = params.replaceAll(".xml", "");
        params = params.replaceAll(".json", "");
        if(params.indexOf("/") != -1){
            params = params.substring(1).trim().toLowerCase();
            if(params.length() == 0)
            params = null;
        }else{
            params =  null;            
        }
        return params;
    }

}