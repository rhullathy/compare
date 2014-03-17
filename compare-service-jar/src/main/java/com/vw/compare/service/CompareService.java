package com.vw.compare.service;

import java.util.ArrayList;
import java.util.Map;

import com.vw.compare.common.CompareServiceException;
import com.vw.compare.domain.Comparison;
import com.vw.compare.domain.Makes;
import com.vw.compare.domain.MockResponse;
import com.vw.compare.domain.ModelTrims;
import com.vw.compare.domain.Trims;
import com.vw.compare.domain.YearmakeModels;
import com.vw.compare.domain.Years;



/**
 * Spring service interface that defines operations for Specialoffers 
 * @author prakash manchenahalli
 * 
 */
public interface CompareService {

    /**
     * At least one trimId is required  
     *  /compare/suggested_trims/{trimIds [1..10] separated by /}
     *         
     * @param trimIds
     * @return
     * @throws CompareServiceException
     */
    ModelTrims getTrimDetailsWithSuggestedTrims(String[] trimIds) throws CompareServiceException;

    /**
     * make is optional, if provided provide available years for given make
     *  /compare/years/[make/{make}]
     * 
     * @param make
     * @return
     * @throws CompareServiceException
     */
    Years getYears(String make) throws CompareServiceException;
    
    /**
     * List makes available for given years, at least one year is required
     * 
     * /compare/makes/{years [1..10] separated by /}
     * 
     * output is similar to :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen/MakesByYear.asp?AS3_ClientID=Volkswagen&YearID=2013
     * 
     * 
     * @param yearArray
     * @return
     * @throws CompareServiceException
     */
    Makes getMake(String[] yearArray) throws CompareServiceException;
    
    
    /**
     * Provide Model name and model id for given year and make, both make and year are required 
     * 
     * /compare/model/{year}/{make}
     * 
     * output is similar to :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen/YearMakeModels.asp?AS3_ClientID=Volkswagen&Year=2013&Make=Cadillac
     * 
     * 
     * @param year
     * @param make
     * @return
     * @throws CompareServiceException
     */
    YearmakeModels getModel(Integer year, String make) throws CompareServiceException;
    
    /**
     * Return All available Trim name, Trim ID and image URL for given model id, model id is required
     * 
     * /compare/trims/{modelid}
     *  
     *  output is similar to :
     *  http://beta.comp.polk.com/2002autosuite/clients/Volkswagen/ModelTrims.asp?AS3_ClientID=Volkswagen&ModelID=7730
     *  
     * @param modelId
     * @return
     * @throws CompareServiceException
     */
    ModelTrims getTrims(String modelId) throws CompareServiceException;
    
    /**
     * filterTypeValueMap is map of <filterType, filterTypeValues> 
     * FilterType can be - year, make, modelid or trimid
     * filtervalue - based on filterType, corresponding list of values will be present
     * 
     * eg:
     * filterTypeValue{{{year},[2012, 2011]},{{make},[VOLKSWAGEN, HONDA]} }
     * 
     * Complete URL: 
     * /compare/trimdetails/year/{years[0..10] separated by /}/make/{makes[0..10] separated by /}/modelid/{modelid[0..10] separated by /}/trimid/{trimid[0..10] separated by /}
     * 
     * Possible usages:
     * /compare/trimdetails   --> list all available years, makes, models & trims
     * /compare/trimdetails/year/{years[0..10] separated by /} --> Results are filters by years
     * /compare/trimdetails/make/{makes[0..10] separated by /} --> Results are filters by makes
     * /compare/trimdetails/modelid/{modelid[0..10] separated by /} --> Results are filters by modelIds
     * /compare/trimdetails/trimid/{trimid[0..10] separated by /} --> Results are filters by trimIds
     *  
     * Example:
     * /compare/trimdetails/year/2012/2011
     * /compare/trimdetails/make/VOLKSWAGEN/HONDA
     * /compare/trimdetails/modelid/12324/98456
     * /compare/trimdetails/trimid/33876/23432
     * 
     * Advance Filter option
     * 
     * 1)
     * /compare/trimdetails/year/2012/make/VOLKSWAGEN/modelid/12324/trimid/33876  --> restrict the results to 
     *                                                                                2012 year AND
     *                                                                                VOLKSWAGEN brand AND
     *                                                                                modleId 12324  AND
     *                                                                                trimId  33876   
     * 
     * /compare/trimdetails/year/2012/trimid/33876  -->                            restrict the results to 
     *                                                                                2012 year AND
     *                                                                                trimId  33876   
     * output is similar to :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen/Trims.asp
     * 
     * @param filterTypeValueMap
     * @return
     * @throws CompareServiceException
     */
    Trims getTrimDetails(Map<String, ArrayList<String>>filterTypeValueMap) throws CompareServiceException;
    
    /**
     * 1 or 4  trimIds can be passed, where first trim id should corresponds to VW trimId , vwtrimid is required
     * 
     * /compare/vehicles/{vw_trimid}/{other trimids[0..3] separated by /}
     * 
     * Response :
     * http://beta.comp.polk.com/2002autosuite/clients/Volkswagen/CompReport.asp?AS3_LanguageCode=7&AS3_ClientID=Volkswagen&Vehicles=34485|31070
     * 
     * @param trimids
     * @param advantageText TODO
     * @return
     * @throws CompareServiceException
     */
    Comparison getComparisonReport(String[] trimids, boolean advantageText) throws CompareServiceException;

    MockResponse refreshData(String type)throws CompareServiceException;
    
    MockResponse refreshCache()throws CompareServiceException;
    

	

}
