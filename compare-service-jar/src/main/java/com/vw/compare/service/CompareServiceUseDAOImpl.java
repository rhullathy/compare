package com.vw.compare.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.vw.compare.common.CompareServiceException;
import com.vw.compare.dao.CompareDAO;
import com.vw.compare.domain.Comparison;
import com.vw.compare.domain.Make;
import com.vw.compare.domain.Makes;
import com.vw.compare.domain.MockResponse;
import com.vw.compare.domain.ModelTrims;
import com.vw.compare.domain.Trim;
import com.vw.compare.domain.Trims;
import com.vw.compare.domain.Year;
import com.vw.compare.domain.YearmakeModels;
import com.vw.compare.domain.Years;

/**
 * spring service implementation for special offers operations
 * 
 * @author prakash manchenahalli
 * 
 */
@Service("CompareService")
public class CompareServiceUseDAOImpl implements CompareService {

    /**
     * logger.
     */
    private static final Logger log = Logger.getLogger(CompareServiceUseDAOImpl.class);

    private static final String ALLREPORTS = "allreports";
    
    private CompareDAO compareDao;
    
    /**
     * 
     */
    public ModelTrims getTrimDetailsWithSuggestedTrims(String[] trimIds) throws CompareServiceException {

        try {
            ArrayList<Trim> trims = compareDao.getTrimDetailsByTrimIds(trimIds);
            compareDao.populateSuggestedTrims(trims);
            ModelTrims modelTrims = new ModelTrims();
            modelTrims.setTrim(trims);
            return modelTrims;
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }

    /**
     * 
     */
    public Years getYears(String make) throws CompareServiceException {

        try {
            ArrayList<Year> yearList = compareDao.getYearsByMake(make);
            Years years = new Years();
            years.setYear(yearList);
            return years;
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }

    /**
     * 
     */
    public Makes getMake(String[] years) throws CompareServiceException {

        try {
            ArrayList<Make> makeList = compareDao.getMakesByYears(years);
            Makes makes = new Makes();
            makes.setMake(makeList);
            return makes;
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }

    /**
     * 
     */
    public YearmakeModels getModel(Integer year, String make) throws CompareServiceException {

        try {

            YearmakeModels yearmakeModels = compareDao.getModelsByYearMake(year, make);
            if(yearmakeModels == null)
                yearmakeModels = new YearmakeModels();
            
            return yearmakeModels;

        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }

    /**
     * 
     */
    public ModelTrims getTrims(String modelId) throws CompareServiceException {

        try {
            ArrayList<Trim> trims = compareDao.getTrimDetailsByModelId(modelId);
            ModelTrims modelTrims = new ModelTrims();
            modelTrims.setTrim(trims);
            return modelTrims;
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }

    }

    /**
     * 
     */
    public Trims getTrimDetails(Map<String, ArrayList<String>> filterTypeValueMap) throws CompareServiceException {

        try {
            boolean modelOnly = false;
            Trims trims = compareDao.getTrimDetailsByFilters(filterTypeValueMap, modelOnly);
            if(trims == null)
                trims = new Trims();
            return trims;

        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }
    

    /**
     * 
     */
    public Comparison getComparisonReport(String[] trimids, boolean advantageText) throws CompareServiceException {
        try {
        	List<String> lstTrims = new ArrayList<String>();
            String[] trims = null;
            if(trimids.length == 1){
            	String[] suggestedTrimids = compareDao.getSuggestedTrimIds(trimids[0]) ;
            	lstTrims.add(trimids[0]);
                if(suggestedTrimids != null && suggestedTrimids.length > 0)
                {
                	log.debug(suggestedTrimids.length);
                	List<String> suggestedTrimList = Arrays.asList(suggestedTrimids); 
                	if(suggestedTrimids.length >3)
                	{
                		lstTrims.addAll(suggestedTrimList.subList(0, 3));      
                	}
                	else
                	{
                		lstTrims.addAll(suggestedTrimList);
                	}
                	trims = new String[lstTrims.size()];
                }
            	trims = lstTrims.toArray(new String[0]);
            }
            else
            {
            	  trims = trimids;
            }
            Comparison comparison = compareDao.generateComparisionReport(trims, advantageText);
            if(comparison == null)
                comparison = new Comparison();
            return comparison;

        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    }
    

    /**
     * 
     */
    public MockResponse refreshData(String type) throws CompareServiceException {
        
        try{
            //clean up data 
            compareDao.cleanUpDynamicData();
            
            //populate vehicle data
            compareDao.populateVehicleData();
            
            
            
          //populate suggested trim data
            compareDao.populateSuggestedTrimData();
            
            if(ALLREPORTS.equalsIgnoreCase(type)){
              //populate vehicle data
                compareDao.cacheAllReports();
            }
            
            return refreshCache();
            
        }catch(CompareServiceException ce){
            throw ce;
        }catch(Throwable t){
            log.error("Exception in getTrimDetailsWithSuggestedTrims", t);
            throw new CompareServiceException(999); //Encountered Application Error
        }
    } 

    public MockResponse refreshCache() throws CompareServiceException {
        compareDao.resetCache();
        return new MockResponse("Done");
    }

    public CompareDAO getCompareDao() {
        return compareDao;
    }

    public void setCompareDao(CompareDAO compareDao) {
        this.compareDao = compareDao;
    }
    

}
