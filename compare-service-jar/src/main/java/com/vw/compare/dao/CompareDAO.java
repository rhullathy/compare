package com.vw.compare.dao;

import java.util.ArrayList;
import java.util.Map;

import com.vw.compare.common.CompareServiceException;
import com.vw.compare.domain.Comparison;
import com.vw.compare.domain.Make;
import com.vw.compare.domain.Trim;
import com.vw.compare.domain.Trims;
import com.vw.compare.domain.Vehicle;
import com.vw.compare.domain.Year;
import com.vw.compare.domain.YearmakeModels;

public interface CompareDAO {

	ArrayList<Trim> getTrimDetailsByTrimIds(String[] trimIds)
			throws CompareServiceException;

	ArrayList<Trim> getTrimDetailsByModelId(String modelId)
			throws CompareServiceException;

	Trims getTrimDetailsByFilters(
			Map<String, ArrayList<String>> filterTypeValueMap, boolean modelOnly)
			throws CompareServiceException;

	void populateSuggestedTrims(ArrayList<Trim> trims)
			throws CompareServiceException;

	ArrayList<Year> getYearsByMake(String make) throws CompareServiceException;

	ArrayList<Make> getMakesByYears(String[] years)
			throws CompareServiceException;

	YearmakeModels getModelsByYearMake(Integer year, String make)
			throws CompareServiceException;

	Vehicle getVehicleDetailsByTrimId(String string)
			throws CompareServiceException;

	Comparison generateComparisionReport(String[] trimids, boolean advantageText)
			throws CompareServiceException;

	void cleanUpDynamicData() throws CompareServiceException;

	void populateVehicleData() throws CompareServiceException;

	void resetCache() throws CompareServiceException;

	void cacheAllReports() throws CompareServiceException;

	public String[] getSuggestedTrimIds(String trimID);
	
	void populateSuggestedTrimData() throws CompareServiceException;

}
