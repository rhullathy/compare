package com.vw.compare.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.vw.compare.common.CompareServiceException;
import com.vw.compare.common.CompareServiceHelper;
import com.vw.compare.domain.Comparison;
import com.vw.compare.domain.ComparisonData;
import com.vw.compare.domain.Competitors;
import com.vw.compare.domain.Heading;
import com.vw.compare.domain.Make;
import com.vw.compare.domain.Model;
import com.vw.compare.domain.PhotoGalleryInfo;
import com.vw.compare.domain.Row;
import com.vw.compare.domain.SuggestedTrims;
import com.vw.compare.domain.Trim;
import com.vw.compare.domain.Trims;
import com.vw.compare.domain.Vehicle;
import com.vw.compare.domain.VehicleData;
import com.vw.compare.domain.Year;
import com.vw.compare.domain.YearmakeModels;

@Repository
public class CompareDAOImpl implements CompareDAO {

    private static final String ALTER_TABLE_COMPARE_DATAPOINT_VALUE_AUTO_INCREMENT = "alter table compare_datapoint_value auto_increment=";

    private static final String SELECT_MAX_DATAPOINTID_FROM_COMPARE_DATAPOINT_TYPE = "select max( datapointid) from compare_datapoint_type";

    private static final String ALTER_TABLE_COMPARE_DATAPOINT_VALUE_AUTO_INCREMENT_1 = "alter table compare_datapoint_value auto_increment=1";

    private static final String ALTER_TABLE_COMPARE_VEHICLE_AUTO_INCREMENT_1 = "alter table compare_vehicle auto_increment=1";

    private static final Logger log = Logger.getLogger(CompareDAOImpl.class);

    private static Map<String, Trim> trimCache = new HashMap<String, Trim>();
    private static List<String> datapointAvailable = new ArrayList<String>();
    private static Map<String, String> comparisonReportCache = new HashMap<String, String>();

    private static final String YEAR = "year";
    private static final String MODEL_ID = "modelid";
    private static final String TRIM_ID = "trimid";
    private static final String MAKE = "make";
    private static final String MODEL_NAME = "modelname";
    private static final String TRIM_NAME = "trimname";

    private static final String GET_COMPARE_REPORT_URI = "/CompReport.asp?AS3_LanguageCode=7&AS3_ClientID=Volkswagen&Vehicles=";

    private static final String GET_TRIM_DETAILS_SQL = "select imageurl, make, model, year, trimid, trim, modelid, substitutetrimid from compare_vehicle ";

    private static final String GET_SUGGESTED_TRIMS_SQL = "select suggestedtrimid from compare_suggested_trim where trimid = '";

    private static final String GET_YEARS_BY_MAKE_SQL = "select distinct year from compare_vehicle ";

    private static final String DELETE_COMPARE_DATA_SQL = "delete from compare_datapoint_value";

    private static final String DELETE_COMPARE_SUGGESTED_TRIM_DATA_SQL = "delete from compare_suggested_trim";

    private static final String DELETE_COMPARE_VEHICLE_SQL = "delete from compare_vehicle";

    protected static final String INSERT_COMPARE_VEHICLE_SQL = "insert into compare_vehicle(year, make," +
    		                                            " model, modelid, trim, trimid,imageurl) values(?,?,?,?,?,?,?)";




    private static final String GET_MAKES_BY_YEAR_SQL = "select distinct make, year from compare_vehicle ";

    protected static final String INSERT_DATAPOINT_VALUE_SQL = "insert into compare_datapoint_value(datapointid, trimid,  value) values (?, ?, ?)";

    private static final String UPDATE_SUB_TRIMID_SQL = "update compare_vehicle set substitutetrimid = '";

    protected static final String INSERT_DATAPOINT_TYPE_SQL = "insert into compare_datapoint_type(datapointtype, datapointsubtype) values(?, ?)";

    private static final String SELECT_DATAPOINT_ID_SQL = "select datapointid from compare_datapoint_type where datapointtype='";

    private static final String SELECT_COMPARE_DATA_SQL = "select datapointtype, datapointsubtype, valuetype, value, trimIndex, trim, model, compare_vehicle.trimid from compare_datapoint_value" +
    		" INNER JOIN compare_datapoint_type ON compare_datapoint_value.datapointid = compare_datapoint_type.datapointid " +
    		" INNER JOIN compare_vehicle ON compare_datapoint_value.trimid = compare_vehicle.trimid ";

    protected static final String INSERT_CACHE_REPORT_SQL = "insert into compare_report_cache(reportid, reportdata) values(?, ?)";

    private static final String GREATERTHAN = "GREATERTHAN";

    private static final String STANDARD = "STANDARD";

    private static final String LESSTHAN = "LESSTHAN";

    private JdbcTemplate jdbcTemplate;
    protected CompareServiceHelper compareServiceHelper;

    protected static final String INSERT_COMPARE_SUGGESTED_TRIM_SQL = "INSERT INTO compare_suggested_trim (trimid, suggestedtrimid) VALUES (?, ?)";




    /**
     *
     */
    public ArrayList<Trim> getTrimDetailsByTrimIds(String[] trimIds) {
        //check vehicle cache, if cache is empty fall back to query
        //if no parameter send all the data from cache
        //else get one by one and add to return list, mark the once which doesn't have cache data
        //query for uncached trim ids
        //add resulting list to return list

        String query = GET_TRIM_DETAILS_SQL;
        if (trimIds != null) {
            StringBuffer where = new StringBuffer(" Where trimid in (");
            for (int i = 0; i < trimIds.length; i++) {
                if (i != 0)
                    where.append(", ");
                where.append("'").append(trimIds[i]).append("'");
            }
            where.append(" )");
            query += where.toString();
        }
        return getTrimDetails(query);
    }

    /**
     *
     */
    public ArrayList<Trim> getTrimDetailsByModelId(String modelId)  {

        //No need to get from cache this is simple query

        String query = GET_TRIM_DETAILS_SQL;
        if (modelId != null) {
            String where = " Where modelid = '" + modelId + "'";
            query += where;
        }
        ArrayList<Trim> trims = getTrimDetails(query);
        if(trims != null){
            for(Trim trim:trims){
                trim.setModelID(null);
            }
        }

        return trims;
    }

    /**
     * @param modelOnly
     *
     */
    public Trims getTrimDetailsByFilters(Map<String, ArrayList<String>> filterTypeValueMap, boolean modelOnly) throws CompareServiceException{

        //If filter set is empty we can use cache, but order buy will be tricky. think how it can be done!!!

        String query = GET_TRIM_DETAILS_SQL;
        if (filterTypeValueMap != null) {
            query = applyFilters(filterTypeValueMap, query);
        }
        query += " order by year, make, model, trim ";
        ArrayList<Trim> trimlist = getTrimDetails(query);
        return populateTrimDetails(trimlist, modelOnly);

    }

    public void populateSuggestedTrims(ArrayList<Trim> trims) {

        for (int i = 0; trims != null && i < trims.size(); i++) {
            Trim trim = trims.get(i);
            String[] suggestedTrimIds = getSuggestedTrimIds(trim.getTrimID());
            if(suggestedTrimIds != null && suggestedTrimIds.length > 0){
                SuggestedTrims suggetsedTrims = new SuggestedTrims();
                suggetsedTrims.setTrim(getTrimDetailsByTrimIds(suggestedTrimIds));
                trim.setSuggestedTrims(suggetsedTrims);
            }
        }
    }

    /**
     * select distinct year from compare_vehicle
     */
    public ArrayList<Year> getYearsByMake(String make) {

        //simple query, but see if we can cache map<modelyear, list<of make names>>

        String query = GET_YEARS_BY_MAKE_SQL;
        if (make != null) {
            query += " where make = '" + make + "'";
        }

        try {
            return (ArrayList<Year>) jdbcTemplate.query(query, new ResultSetExtractor<ArrayList<Year>>() {
                public ArrayList<Year> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                    ArrayList<Year> list = new ArrayList<Year>();
                    while (resultSet.next()) {
                        Year year = new Year();
                        year.setModelYear(resultSet.getString(1));
                        list.add(year);
                    }
                    return list;
                }
            });
        } catch (Exception e) {
            log.trace("No Trims found for query >>" + query);
            return null;

        }
    }

    /**
     * select distinct make, year from compare_vehicle
     */
    public ArrayList<Make> getMakesByYears(String[] years) {

        //No need to cache, simple query

        String query = GET_MAKES_BY_YEAR_SQL;
        if (years != null) {
            StringBuffer where = new StringBuffer(" Where year in (");
            for (int i = 0; i < years.length; i++) {
                if (i != 0)
                    where.append(", ");
                where.append(years[i]);
            }
            where.append(" )");
            query += where.toString();
        }

        try {
            return (ArrayList<Make>) jdbcTemplate.query(query, new ResultSetExtractor<ArrayList<Make>>() {
                public ArrayList<Make> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                    ArrayList<Make> list = new ArrayList<Make>();
                    while (resultSet.next()) {
                        Make make = new Make();
                        make.setMakeName(resultSet.getString(1));
                        make.setYear(resultSet.getString(2));
                        list.add(make);
                    }
                    return list;
                }
            });
        } catch (Exception e) {
            log.trace("No Trims found for query >>" + query);
            return null;

        }
    }

    /**
     *
     */
    public YearmakeModels getModelsByYearMake(Integer year, String make) throws CompareServiceException {

        //Reusing filter method

        YearmakeModels yearmakeModels = new YearmakeModels();
        Map<String, ArrayList<String>> filterTypeValueMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> valueList = new ArrayList<String>();
        valueList.add("" + year);
        filterTypeValueMap.put(YEAR, valueList);
        valueList = new ArrayList<String>();
        valueList.add(make);
        filterTypeValueMap.put(MAKE, valueList);
        boolean modelOnly = true;
        Trims trims = getTrimDetailsByFilters(filterTypeValueMap, modelOnly);
        yearmakeModels.setYear(trims.getYear());
        return yearmakeModels;

    }

    /**
     *
     */
    public Vehicle getVehicleDetailsByTrimId(String trimid) {

        //Use trim cache for this

        Vehicle veh = null;
        String[] trimArray = new String[1];
        trimArray[0] = trimid;
        ArrayList<Trim> trimList = getTrimDetailsByTrimIds(trimArray);
        if(trimList != null && trimList.size() > 0){

            veh = new Vehicle();
            trimList.get(0).getImageURL();
            veh.setImageURL(trimList.get(0).getImageURL());
            veh.setMakeName(trimList.get(0).getMakeName());
            veh.setModelName(trimList.get(0).getModelName());
            veh.setModelYear(trimList.get(0).getModelYear());
            veh.setTrimID(trimList.get(0).getTrimID());
            veh.setTrimName(trimList.get(0).getTrimName());

            if(trimList.get(0).getSubstituteTrimId() != null && trimList.get(0).getSubstituteTrimId().length() > 0){

                String[] subTrimArray = new String[1];
                subTrimArray[0] = trimList.get(0).getSubstituteTrimId();
                ArrayList<Trim> subTrimList = getTrimDetailsByTrimIds(subTrimArray);

                PhotoGalleryInfo photoGalleryInfo = new PhotoGalleryInfo();
                photoGalleryInfo.setSubstituteTrimID(trimList.get(0).getSubstituteTrimId());
                photoGalleryInfo.setSubstituteUsed("True");
                photoGalleryInfo.setSubstituteTrimName(subTrimList.get(0).getTrimName());
                veh.setPhotoGalleryInfo(photoGalleryInfo );
            }
        }

        return veh;
    }


    public Comparison generateComparisionReport(String[] trimids, boolean advantageText) throws CompareServiceException  {

        if(compareServiceHelper.generateReportFromDB()){

            return  generateComparisionReport1(trimids, advantageText);
        }

        Comparison comparison = null;
        try{
            String trims = "";
            for(Object trim:trimids){
                trims += trim+"%7C";
            }
            String xml = null;
            if(comparisonReportCache.keySet().contains(trims.substring(0,trims.length()-3))){
                xml = comparisonReportCache.get(trims.substring(0,trims.length()-3));
                log.debug("CACHED");

            }else{
                xml = compareServiceHelper.callExternalService(GET_COMPARE_REPORT_URI+trims.substring(0,trims.length()-3));
                comparisonReportCache.put(trims.substring(0,trims.length()-3), xml);
                insertCacheReport(trims.substring(0,trims.length()-3), xml);
            }
            log.debug("XML>>"+xml);
            comparison = (Comparison)compareServiceHelper.unmarshalXML(xml, Comparison.class);
        }
        catch(Exception e){
            //don't do anything for now
            log.info("Exception in generateComparisionReport >>"+ e.getMessage());
        }
        return comparison;

    }


    private void insertCacheReport(final String reportkey, final String xml) {
        try{
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement statement = connection.prepareStatement(INSERT_CACHE_REPORT_SQL);
                    statement.setString(1, reportkey);
                    statement.setString(2, xml);
                    return statement;
                }
            });
        }catch(Exception e){
           log.trace("insertCacheReport >>"+e.getMessage());
        }
    }



    /**
     *
     * @param advantageText TODO
     * @throws CompareServiceException
     *
     */
    public Comparison generateComparisionReport1(String[] trimids, boolean advantageText) throws CompareServiceException  {

        String baseTrimid = trimids[0];
        ArrayList<String> noCachedIds = new ArrayList<String>();

        //Update the cache to start with, first request may take some time, this will avoid
        if(datapointAvailable.size() == 0)
            resetCache();

        for(String trimid:trimids){
            if(!datapointAvailable.contains(trimid))
                noCachedIds.add(trimid);
        }

        if(noCachedIds.size() > 0)
            generateReportAndCache(noCachedIds, baseTrimid);

        return queryForComparisonReport(trimids, advantageText);

    }



    private void generateReportAndCache(ArrayList<String> noCachedIds, String baseTrimid) throws CompareServiceException {
        boolean isBaseTrimCached = true;

        try{
            if(noCachedIds.get(0).equalsIgnoreCase(baseTrimid)){
                isBaseTrimCached = false;
            }else{
                noCachedIds.add(0, baseTrimid);
            }
            Object[] trimids =  noCachedIds.toArray();
            Comparison report = compareServiceHelper.getComparisonReport(trimids);

            splitAndCache(report,noCachedIds, isBaseTrimCached);

        }catch(Exception e){
            log.trace("Exception in generateReportAndCache >>"+e);
            throw new CompareServiceException(999);
        }

    }

    private void splitAndCache(Comparison report,ArrayList<String> noCachedIds, boolean isBaseTrimCached) throws CompareServiceException{
        for(int i =0; i < noCachedIds.size(); i++){
            if(i == 0 && isBaseTrimCached)
                continue;
            else{

                try{
                    Vehicle vehicle = null;
                    if(i == 0)
                        vehicle = report.getPrimaryVehicle();
                    else{
                        vehicle = report.getCompetitorVehicle().get(i-1);
                    }

                    //update the substitute trim id if present
                    if(vehicle != null && vehicle.getPhotoGalleryInfo() != null &&
                            vehicle.getPhotoGalleryInfo().getSubstituteTrimID() != null &&
                            vehicle.getPhotoGalleryInfo().getSubstituteTrimID().length() > 0 ){
                        updateSubstituteTrimid(noCachedIds.get(i),vehicle.getPhotoGalleryInfo().getSubstituteTrimID());
                    }
                    if(report.getComparisonData() != null && report.getComparisonData().getHeading() != null){
                        for(Heading reportHeading: report.getComparisonData().getHeading()){
                            for(Row reportRow:reportHeading.getRow()){

                                //insert the record into data value table
                                insertDataPoint(noCachedIds.get(i), reportHeading.getHeadingName(),
                                        reportRow.getRowLabel(),reportRow.getVehicleData().get(i).getColData());

                            }
                        }
                    }
                    if(!datapointAvailable.contains(noCachedIds.get(i)))
                    datapointAvailable.add(noCachedIds.get(i));

                }catch(Exception e){
                    //e.printStackTrace();
                    log.trace("Exception in splitAndCache >>"+e, e);
                    throw new CompareServiceException(999);
                }
            }
        }
    }

    /**
     * update compare_vehicle set substitutetrimid = '
     *
     * @param string
     * @param substituteTrimID
     */
    private void updateSubstituteTrimid(String trimId, String substituteTrimID) {

            String query = UPDATE_SUB_TRIMID_SQL;
            query+= substituteTrimID+ "' where trimid='"+trimId+"'";
        try{
            jdbcTemplate.update(query);
        }catch(Exception e){
            log.trace("Exception in updateSubstituteTrimid >>"+query, e);
        }
    }


    private void insertDataPoint(String trimId, String headingName, String rowLabel, String colData) {
        try{
            Integer datapointid = getDataPointId(headingName,rowLabel );
            PreparedStatementCreator psc = getPreparedStatementCreator(datapointid, trimId, colData);
            jdbcTemplate.update(psc);
        }catch(Exception e){
            log.trace("Exception in insertDataPoint >>"+INSERT_DATAPOINT_VALUE_SQL, e);
        }

    }

    /**
     * insert into compare_datapoint_value(datapointid, trimid,  value) values (?, ?, ?)
     *
     * @param datapointid
     * @param trimId
     * @param colData
     * @return
     */
    private PreparedStatementCreator getPreparedStatementCreator(final Integer datapointid,final String trimId, final String colData)  {
        return new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                PreparedStatement statement = connection.prepareStatement(INSERT_DATAPOINT_VALUE_SQL);
                statement.setInt(1, datapointid);
                statement.setString(2, trimId);
                statement.setString(3, colData);
                return statement;
            }
        };
    }

    /**
     * select datapointid from compare_datapoint_type where datapointtype='heading' and datapointsubtype='rowlable'
     *
     *
     *
     * @param headingName
     * @param rowLabel
     * @return
     */
    private Integer getDataPointId(String headingName, String rowLabel)  {
        String selectQuery = SELECT_DATAPOINT_ID_SQL;
        selectQuery += headingName +"' AND datapointsubtype='"+rowLabel+"'";

        Integer datapointId = null;
        try{
            datapointId = jdbcTemplate.queryForInt(selectQuery);
        }catch(Exception e){
            try{
                //insert record and get id
                PreparedStatementCreator psc = getPreparedStatementCreator(headingName, rowLabel);
                jdbcTemplate.update(psc);

                datapointId = jdbcTemplate.queryForInt(selectQuery);

            }catch(Exception e1){
                log.trace("Exception in getDataPointId >>"+e1, e1);
            }
        }
        return datapointId;
    }

    /**
     * insert into compare_datapoint_type(datapointtype, datapointsubtype) values(?, ?)
     *
     * @param datapointid
     * @param trimId
     * @param colData
     * @return
     */
    private PreparedStatementCreator getPreparedStatementCreator(final String headingName, final String rowLabel)  {
        return new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                PreparedStatement statement = connection.prepareStatement(INSERT_DATAPOINT_TYPE_SQL);
                statement.setString(1, headingName);
                statement.setString(2, rowLabel);
                return statement;
            }
        };
    }

    /**
     *
     *
     * @param trimids
     * @return
     */
    private Comparison queryForComparisonReport(String[] trimids, boolean advantageText) {

        Comparison comparison = new Comparison();

        StringBuffer subQuery = new StringBuffer("INNER JOIN  ( Select trimid, trimIndex from ( ");
        StringBuffer trimIdList = new  StringBuffer("(");

        for(int i =0; i < trimids.length; i++){
            Vehicle vehicle = getVehicleDetailsByTrimId(trimids[i]);
            if(i == 0){
                comparison.setPrimaryVehicle(vehicle);

                subQuery.append(" Select ").append(trimids[i]).append(" AS trimid, ").append( i).append(" AS trimIndex ");
                trimIdList.append("'").append(trimids[i]).append("' ");

            }else{
                if(comparison.getCompetitorVehicle() == null)
                    comparison.setCompetitorVehicle(new ArrayList<Vehicle>());
                comparison.getCompetitorVehicle().add(vehicle);

                subQuery.append(" UNION ");
                subQuery.append(" Select ").append(trimids[i]).append(", ").append( i).append(" ");
                trimIdList.append(", '").append(trimids[i]).append("' ");
            }

        }
        subQuery.append(" ) dynamicTrimIDIndex ) trim_index ON compare_datapoint_value.trimid = trim_index.trimid  ");
        trimIdList.append(")");

        //construct query and get heading information
        populateComparisondata(comparison, trimids, subQuery.toString(), trimIdList.toString(), advantageText);

        return comparison;
    }

    /**
     *
     * select datapointtype, datapointsubtype, valuetype, value, trimIndex from compare_datapoint_value
     * INNER JOIN compare_datapoint_type ON compare_datapoint_value.datapointid = compare_datapoint_type.datapointid
     * INNER JOIN indexSortQuery trim_index USING (trimid)
     * WHERE trimid in (xxxx)
     *
     * @param comparison
     * @param trimids
     * @param indexSortQuery
     */

    private void populateComparisondata(Comparison comparison, final String[] trimids, String indexSortQuery, String trimIdList, final boolean advantageText)  {

        ComparisonData comparisionData = new ComparisonData();
        comparison.setComparisonData(comparisionData );
        List<Heading> headings = new ArrayList<Heading>();

        StringBuffer query = new StringBuffer(SELECT_COMPARE_DATA_SQL);
        query.append(indexSortQuery).append(" WHERE compare_datapoint_value.trimid in ").append(trimIdList)
        .append(" order by typeorder, subtypeorder, trimIndex");

        try {
            headings = (ArrayList<Heading>) jdbcTemplate.query(query.toString(), new ResultSetExtractor<ArrayList<Heading>>() {
                public ArrayList<Heading> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                    ArrayList<Heading> headingList = new ArrayList<Heading>();
                    ArrayList<Row> rowList = new ArrayList<Row>();
                    Heading heading = null;
                    Row row = null;
                    VehicleData vehicleData = null;
                    String lastHeadingName = null;
                    String currHeadingName = null;
                    String lastRowName = null;
                    String currRowName = null;
                    String parentColValue= null;
                    String parentTrimName= null;

                    while (resultSet.next()) {
                        currHeadingName = resultSet.getString(1);
                        currRowName = resultSet.getString(2);

                        if( lastHeadingName == null || !lastHeadingName.equalsIgnoreCase(currHeadingName) ){

                            heading = new Heading();
                            headingList.add(heading);
                            heading.setHeadingName(currHeadingName);
                            heading.setRow(new ArrayList<Row>());

                            if(advantageText && row != null && "False".equalsIgnoreCase(row.getVehicleData().get(0).getAdvantageFlag())){
                                row.getVehicleData().get(0).setAdvantageText(null);
                                for(VehicleData vdata :row.getVehicleData()){
                                    vdata.setAdvantageFlag(null);
                                }
                            }
                            fillEmptyData(row,trimids);
                            row = new Row();
                            heading.getRow().add(row);
                            row.setRowLabel(currRowName);
                            row.setVehicleData(new ArrayList<VehicleData>());

                        }else if(!currRowName.equalsIgnoreCase(lastRowName)){

                            if(advantageText && row != null && "False".equalsIgnoreCase(row.getVehicleData().get(0).getAdvantageFlag())){
                                row.getVehicleData().get(0).setAdvantageText(null);
                                for(VehicleData vdata :row.getVehicleData()){
                                    vdata.setAdvantageFlag(null);
                                }
                            }
                            fillEmptyData(row, trimids);
                            row = new Row();
                            heading.getRow().add(row);
                            row.setRowLabel(currRowName);
                            row.setVehicleData(new ArrayList<VehicleData>());
                        }

                        String dataType = resultSet.getString(3);
                        String colValue = resultSet.getString(4);
                        String trimIndex = resultSet.getString(5);
                        String trimName = resultSet.getString(6);
                        String modelName = resultSet.getString(7);
                        String trimId = resultSet.getString(8);

                        if(Integer.parseInt(trimIndex) == 0){
                            parentColValue = colValue;
                            parentTrimName = modelName+" "+trimName;
                        }

                        vehicleData = new VehicleData();
                        vehicleData.setColData(colValue);
                        vehicleData.setTrimId(trimId);
                        row.getVehicleData().add(vehicleData);

                        if(advantageText)
                            setAdvantageText(dataType, parentColValue, colValue, trimIndex, vehicleData, row , currRowName, parentTrimName, modelName+" "+trimName );

                        lastHeadingName = currHeadingName;
                        lastRowName = currRowName;
                    }
                    fillEmptyData(row, trimids);
                    return headingList;
                }


            });
        } catch (Exception e) {
            log.info("No Trims found for query >>" + query);
            //return null;

        }

        comparisionData.setHeading(headings );

    }

    protected void fillEmptyData(Row row,String[] trimids) {
		if(row!=null && row.getVehicleData()!=null  && trimids!=null && row.getVehicleData().size()!=trimids.length)
		{
			Map<String,VehicleData> map=new LinkedHashMap<String,VehicleData>();
			for(VehicleData data:row.getVehicleData())
			{
				map.put(data.getTrimId(), data);
			}
			row.getVehicleData().clear();
			for(String trimId:trimids)
			{
				VehicleData data=map.get(trimId);
				if(map.get(trimId)==null)
				{
					data=new VehicleData();
					data.setColData("Not Available");
				}
				row.getVehicleData().add(data);
			}
		}
	}

	private void setAdvantageText(String dataType, String parentColValue, String currentColValue,
            String trimIndex, VehicleData currentVehicleData, Row row, String currRowName, String parentTrimName, String trimName)  {
        try{
            //if trimidex is 0, return - we don't have anything to compare
            if(Integer.parseInt(trimIndex) == 0)
                return;

            //check if parent has advantage over child value based on datatype

            String advantageText = hasAdvantage(dataType, parentColValue, currentColValue, currRowName,  parentTrimName,  trimName);


            //If advantage Text is not null ,
            //      set advantage flag to currentVehicleData
            //      set advantage text to currentVehicleData
            //      set advantage falg to parent data
            //      append the advantage text to parent
            String parentAdvText = row.getVehicleData().get(0).getAdvantageText() == null?null:
                row.getVehicleData().get(0).getAdvantageText();

            if(parentAdvText==null)
            	parentAdvText="";

            if(advantageText != null){
                currentVehicleData.setAdvantageFlag("True");
                currentVehicleData.setAdvantageText(advantageText);
                row.getVehicleData().get(0).setAdvantageFlag("True");
                if(Integer.parseInt(trimIndex) != 1){
                    parentAdvText +="|"+ advantageText;
                }else{
                    parentAdvText += advantageText;
                }
            }
            else{
                currentVehicleData.setAdvantageFlag("False");
                if(row.getVehicleData().get(0).getAdvantageFlag()== null){
                    row.getVehicleData().get(0).setAdvantageFlag("False");
                }
            }
            row.getVehicleData().get(0).setAdvantageText(parentAdvText);
        }catch(Exception e){
            log.trace(e.getMessage());
        }

    }

    private String hasAdvantage(String dataType, String parentColValue, String currentColValue,String currRowName, String parentTrimName, String trimName) {
        String advantageText = null;
        if(GREATERTHAN.equalsIgnoreCase(dataType)){
            advantageText = checkGreaterThan(parentColValue,currentColValue , currRowName,  parentTrimName,  trimName);
        }else if(LESSTHAN.equalsIgnoreCase(dataType)){
            advantageText = checkLessThan(parentColValue,currentColValue , currRowName,  parentTrimName,  trimName);
        }else if(STANDARD.equalsIgnoreCase(dataType)){
            advantageText = checkStandard(parentColValue,currentColValue, currRowName,  parentTrimName,  trimName );
        }
        return advantageText;
    }


    private String checkGreaterThan(String parentColValue, String currentColValue,String currRowName, String parentTrimName, String trimName) {
        String text = null;
        try{
            StringBuffer sb = new StringBuffer("The ").append(currRowName).append(" is ").append(parentColValue).append(" on the ")
                            .append(parentTrimName).append(" while ").append(currentColValue).append(" on ").append(trimName);

            Float pValue = getFloatValue(parentColValue);
            Float cValue = getFloatValue(currentColValue);
            if(pValue != null && cValue != null && pValue > cValue)
                text = sb.toString();

        }catch(Exception e){
            log.trace(e.getMessage());
        }

        return text;
    }

    private Float getFloatValue(String colValue) {
        String value = new String(colValue);
        Float f = null;
        try{
            value = replace(value, ",", "");
            value = replace(value, "$", "");
            value = replace(value, "*", "");
            value = replace(value, "%", "");
            f = Float.parseFloat(value);
        }catch(Exception e){
            log.trace(e.getMessage()+" value "+colValue);
        }
        return f;
    }

    public  String replace(String src, String find, String replace){

        String s1 = new String(src);
        int index = -1;
        while((index = s1.indexOf(find, index)) != -1){
                String s = s1.substring(0,index)+replace+ s1.substring(index+find.length());
                index = index+replace.length();
                s1 = s;
        }
        return s1;

    }

    private String checkLessThan(String parentColValue, String currentColValue,String currRowName, String parentTrimName, String trimName) {
        String text = null;
        try{
            StringBuffer sb = new StringBuffer("The ").append(currRowName).append(" is ").append(parentColValue).append(" on the ")
                            .append(parentTrimName).append(" while ").append(currentColValue).append(" on ").append(trimName);

            Float pValue = getFloatValue(parentColValue);
            Float cValue = getFloatValue(currentColValue);
            if(pValue != null && cValue != null && pValue < cValue)
                text = sb.toString();

        }catch(Exception e){

        }

        return text;
    }


    private String checkStandard(String parentColValue, String currentColValue,String currRowName, String parentTrimName, String trimName) {

        String text = null;
        try{

            String parent = parentColValue != null && parentColValue.toLowerCase().indexOf("standard") >=0 ? "Standard":
                            parentColValue != null && parentColValue.toLowerCase().indexOf("optional") >=0 ? "Optional":"Not Available";
            String current = currentColValue != null && currentColValue.toLowerCase().indexOf("standard") >=0 ? "Standard":
                            currentColValue != null && currentColValue.toLowerCase().indexOf("optional") >=0 ? "Optional":"Not Available";

            if(("Standard".equalsIgnoreCase(parent) &&  !"Standard".equalsIgnoreCase(current)) ||
                    ("Optional".equalsIgnoreCase(parent) &&  !"Standard".equalsIgnoreCase(current) && !"Optional".equalsIgnoreCase(current))){

                StringBuffer sb = new StringBuffer("The ").append(currRowName).append(" is ").append(parent).append(" on the ")
                .append(parentTrimName).append(" while ").append(current).append(" on ").append(trimName);

                text = sb.toString();
            }

        }catch(Exception e){

        }
        return text;

    }

    public void resetCache()  throws CompareServiceException {

        //reset vehicle cache
        //reset compare cache
        trimCache.clear();
        datapointAvailable.clear();

        //populate vehicle cache
        ArrayList<Trim> trims = getTrimDetailsByTrimIds(null);
        for(Trim trim:trims){
            trimCache.put(trim.getModelYear()+"-"+trim.getTrimID(), trim);
        }

        populateCompareDataCache();

        populateCompareReportCache();

    }

    private void populateCompareReportCache()  throws CompareServiceException{

        String query = "select reportid, reportdata from compare_report_cache";

        try {
            jdbcTemplate.query(query, new ResultSetExtractor<Map<String, String> >() {
                public Map<String, String>  extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                     while (resultSet.next()) {
                         comparisonReportCache.put(resultSet.getString(1), resultSet.getString(2));
                    }
                    return comparisonReportCache;
                }
            });
        } catch (Exception e) {
            log.info("No Trims found for query >>" + query);
        }
    }

    private void populateCompareDataCache()  throws CompareServiceException{

        try{
            datapointAvailable.clear();
            List<String> trims = jdbcTemplate.queryForList("select distinct trimid from compare_datapoint_value", String.class);
            datapointAvailable.addAll(trims);
        }catch(Exception e){
            log.info(e.getMessage());
        }

    }

    /**
     * delete from compare_datapoint_value; delete from compare_vehicle;
     */
    public void cleanUpDynamicData()  throws CompareServiceException{
        try {
            jdbcTemplate.execute(DELETE_COMPARE_DATA_SQL);
        } catch (Exception e) {
            log.info("Exception while running query >>" + DELETE_COMPARE_DATA_SQL);
        }

        try {
            jdbcTemplate.execute(DELETE_COMPARE_VEHICLE_SQL);
        } catch (Exception e) {
            log.info("Exception while running query >>" + DELETE_COMPARE_VEHICLE_SQL);
        }

        try {
            jdbcTemplate.execute(DELETE_COMPARE_SUGGESTED_TRIM_DATA_SQL);
        } catch (Exception e) {
            log.info("Exception while running query >>" + DELETE_COMPARE_SUGGESTED_TRIM_DATA_SQL);
        }

        try {
            jdbcTemplate.execute(ALTER_TABLE_COMPARE_VEHICLE_AUTO_INCREMENT_1);
        } catch (Exception e) {
            log.info("Exception while running query >>alter table compare_vehicle auto_increment=1");
        }

        try {
            jdbcTemplate.execute(ALTER_TABLE_COMPARE_DATAPOINT_VALUE_AUTO_INCREMENT_1);
        } catch (Exception e) {
            log.info("Exception while running query >>alter table compare_datapoint_value auto_increment=1");
        }

        try{
            int maxDataTypeId = jdbcTemplate.queryForInt(SELECT_MAX_DATAPOINTID_FROM_COMPARE_DATAPOINT_TYPE);

            jdbcTemplate.execute(ALTER_TABLE_COMPARE_DATAPOINT_VALUE_AUTO_INCREMENT+(maxDataTypeId+2));
        } catch (Exception e) {
            log.info("Exception while running query >>alter table compare_datapoint_value auto_increment=");
        }

    }

    /**
     * insert into compare_vehicle(year, make, model, modelid, trim, trimid,
     * imageurl) values(?,?,?,?,?,?,?);
     */
    public void populateVehicleData()  throws CompareServiceException{

        Trims allTrims = new Trims();
        try {
            allTrims = compareServiceHelper.getTrimDetails(null);

            Integer modelYear = null;
            String modelId = null;
            String makeName = null;
            String modelName = null;
            List<Year> years = allTrims.getYear();

            for (Year year : years) {
                modelYear = Integer.parseInt(year.getModelYear());
                List<Make> makes = year.getMake();
                for (Make make : makes) {
                    makeName = make.getMakeName();
                    List<Model> models = make.getModel();
                    for (Model model : models) {
                        modelId = model.getModelID();
                        modelName = model.getModelName();
                        List<Trim> trims = model.getTrim();
                        for (Trim trim : trims) {
                            PreparedStatementCreator psc = getPreparedStatementCreator(modelYear, modelId, makeName, modelName, trim);
                            jdbcTemplate.update(psc);
                        }
                    }
                }
            }
        } catch (CompareServiceException e) {
            log.info("Exception during populateVehicleData"+ e.getMessage());
        }

    }



    public void cacheAllReports()  throws CompareServiceException{
        //get VW trim ids
        //loop through trim ids
        //call split and cache for every 5 trim ids with base VW id

        List<String> vwTrims = jdbcTemplate.queryForList("select distinct trimid from compare_vehicle where make in ('Volkswagen')", String.class);

        List<String> nonVwTrims = jdbcTemplate.queryForList("select distinct trimid from compare_vehicle where make not in ('Volkswagen')", String.class);

        String baseTrim = null;
        ArrayList<String> compareTrims = new ArrayList<String>();
        int vwIndex = 0;
        int nonVwIndex = 0;
        int exceptionCount = 0;
        int exceptionThreshold = compareServiceHelper.getImportErrorThreshold();


        for(int i = 0; i < vwTrims.size()/6 && exceptionCount < exceptionThreshold; i++){
            compareTrims.clear();
            for(int j=0; j < 6; j++)
                compareTrims.add( vwTrims.get(vwIndex++));

            try {
                generateReportAndCache(compareTrims, compareTrims.get(0));
                Thread.sleep(1000);
            } catch (Exception e) {
                exceptionCount++;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                }
                log.info("Exception during cacheAllReports errorcount>>"+exceptionCount +" - "+ e.getMessage());
            }
        }

        compareTrims.clear();
        while( vwIndex < vwTrims.size() && exceptionCount < exceptionThreshold){
            compareTrims.add( vwTrims.get(vwIndex++));
        }
        try {

            generateReportAndCache(compareTrims, compareTrims.get(0));
            Thread.sleep(1000);

        }  catch (Exception e) {
            exceptionCount++;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
            }
            log.info("Exception during cacheAllReports errorcount>>"+exceptionCount +" - "+ e.getMessage());
        }

        vwIndex = 0;

        for(int i = 0; i < nonVwTrims.size()/5 && exceptionCount < exceptionThreshold; i++){
            compareTrims.clear();
            for(int j=1; j < 6; j++)
                compareTrims.add( nonVwTrims.get(nonVwIndex++));

            try {
                if(vwIndex >= vwTrims.size())
                    vwIndex = 0;
                baseTrim = vwTrims.get(vwIndex++);
                generateReportAndCache(compareTrims, baseTrim);
                Thread.sleep(1000);
            }  catch (Exception e) {
                exceptionCount++;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                }
                log.info("Exception during cacheAllReports errorcount>>"+exceptionCount +" - "+ e.getMessage());
            }

        }

        compareTrims.clear();
        while(  nonVwIndex < nonVwTrims.size() && exceptionCount < exceptionThreshold){
            compareTrims.add(nonVwTrims.get(nonVwIndex++));
        }
        try {
            if(vwIndex >= vwTrims.size())
                vwIndex = 0;
            baseTrim = vwTrims.get(vwIndex++);
            generateReportAndCache(compareTrims, baseTrim);

        }  catch (Exception e) {

            log.info("Exception during cacheAllReports errorcount>>"+exceptionCount +" - "+ e.getMessage());
        }

    }


    /**
     * select imageurl, make, model, year, trimid, trim, modelid from
     * compare_vehicle
     *
     * @param query
     * @return
     */
    private ArrayList<Trim> getTrimDetails(String query) {
        try {
            return (ArrayList<Trim>) jdbcTemplate.query(query, new ResultSetExtractor<ArrayList<Trim>>() {
                public ArrayList<Trim> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                    ArrayList<Trim> list = new ArrayList<Trim>();
                    while (resultSet.next()) {
                        Trim trim = new Trim();
                        trim.setImageURL(resultSet.getString(1));
                        trim.setMakeName(resultSet.getString(2));
                        trim.setModelName(resultSet.getString(3));
                        trim.setModelYear(resultSet.getString(4));
                        trim.setTrimID(resultSet.getString(5));
                        trim.setTrimName(resultSet.getString(6));
                        trim.setModelID(resultSet.getString(7));
                        trim.setSubstituteTrimId(resultSet.getString(8));
                        list.add(trim);
                    }
                    return list;
                }
            });
        } catch (Exception e) {
            log.info("No Trims found for query >>" + query);
            return null;

        }
    }

    /**
     * select suggestedtrimid from compare_suggested_trim where trimid = '
     *
     * @param trimID
     * @return
     */
    public String[] getSuggestedTrimIds(String trimID)   {

        //Check suggested Trim cache if data is present
        //else run below query

        String query = GET_SUGGESTED_TRIMS_SQL + trimID + "'";

        try {
            return (String[]) jdbcTemplate.query(query, new ResultSetExtractor<String[]>() {
                public String[] extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                    ArrayList<String> list = new ArrayList<String>();
                    while (resultSet.next()) {
                        list.add(resultSet.getString(1));
                    }
                    if(list != null && list.size() > 0){
                        String[] returnList = new String[list.size()];
                        returnList = (String[]) list.toArray(returnList);
                        return returnList;
                    }
                    else
                        return null;
                }
            });
        } catch (Exception e) {
            log.info("No Trims found for query >>" + query);
            return null;

        }
    }





    private Trims populateTrimDetails(ArrayList<Trim> allTrims, boolean modelOnly) {
        Trims trims = new Trims();

        Trim pTrim = null;

        ArrayList<Year> yearList = new ArrayList<Year>();
        ArrayList<Make> makeList = null;
        ArrayList<Model> modelList = null;
        ArrayList<Trim> trimList = null;

        for (Trim cTrim : allTrims) {

            if (pTrim == null || !pTrim.getModelYear().equalsIgnoreCase(cTrim.getModelYear())) {

                makeList = new ArrayList<Make>();
                modelList = new ArrayList<Model>();
                trimList = new ArrayList<Trim>();

                Year year = new Year();
                yearList.add(year);
                year.setModelYear(cTrim.getModelYear());
                year.setMake(makeList);

                Make make = new Make();
                makeList.add(make);
                make.setMakeName(cTrim.getMakeName());
                make.setModel(modelList);

                Model model = new Model();
                modelList.add(model);
                model.setModelID(new String(cTrim.getModelID()));
                model.setModelName(cTrim.getModelName());
                if (modelOnly) {
                    model.setMakeName(cTrim.getMakeName());
                    model.setModelYear(cTrim.getModelYear());
                } else {
                    model.setTrim(trimList);
                }

            } else if (!pTrim.getMakeName().equalsIgnoreCase(cTrim.getMakeName())) {

                modelList = new ArrayList<Model>();
                trimList = new ArrayList<Trim>();

                Make make = new Make();
                makeList.add(make);
                make.setMakeName(cTrim.getMakeName());
                make.setModel(modelList);

                Model model = new Model();
                modelList.add(model);
                model.setModelID(new String(cTrim.getModelID()));
                model.setModelName(cTrim.getModelName());
                if (modelOnly) {
                    model.setMakeName(cTrim.getMakeName());
                    model.setModelYear(cTrim.getModelYear());
                } else {
                    model.setTrim(trimList);
                }
            } else if (!pTrim.getModelName().equalsIgnoreCase(cTrim.getModelName())) {

                trimList = new ArrayList<Trim>();

                Model model = new Model();
                modelList.add(model);
                model.setModelID(new String(cTrim.getModelID()));
                model.setModelName(cTrim.getModelName());
                if (modelOnly) {
                    model.setMakeName(cTrim.getMakeName());
                    model.setModelYear(cTrim.getModelYear());
                } else {
                    model.setTrim(trimList);
                }
            }

            cTrim.setModelID(null);
            trimList.add(cTrim);

            pTrim = cTrim;
        }
        trims.setYear(yearList);

        return trims;
    }

    /**
     *
     * @param filterTypeValueMap
     * @param query
     * @return
     */
    private String applyFilters(Map<String, ArrayList<String>> filterTypeValueMap, String query)  {
        StringBuffer where = new StringBuffer(" Where trimid is NOT NULL ");
        if (filterTypeValueMap != null) {
            Iterator<String> it = filterTypeValueMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                where.append(" AND ");
                if (YEAR.equalsIgnoreCase(key)) {
                    where.append(" year in (");
                    ArrayList<String> filterValue = filterTypeValueMap.get(key);
                    for (int i = 0; i < filterValue.size(); i++) {
                        if (i != 0)
                            where.append(",");
                        where.append(filterValue.get(i));
                    }
                    where.append(") ");
                } else if (MODEL_ID.equalsIgnoreCase(key)) {
                    where.append(" modelid in (");
                    ArrayList<String> filterValue = filterTypeValueMap.get(key);
                    for (int i = 0; i < filterValue.size(); i++) {
                        if (i != 0)
                            where.append(", ");
                        where.append("'").append(filterValue.get(i)).append("'");
                    }
                    where.append(") ");
                } else if (TRIM_ID.equalsIgnoreCase(key)) {
                    where.append(" trimid in (");
                    ArrayList<String> filterValue = filterTypeValueMap.get(key);
                    for (int i = 0; i < filterValue.size(); i++) {
                        if (i != 0)
                            where.append(", ");
                        where.append("'").append(filterValue.get(i)).append("'");
                    }
                    where.append(") ");
                } else if (MAKE.equalsIgnoreCase(key)) {
                    where.append(" make in (");
                    ArrayList<String> filterValue = filterTypeValueMap.get(key);
                    for (int i = 0; i < filterValue.size(); i++) {
                        if (i != 0)
                            where.append(", ");
                        where.append("'").append(filterValue.get(i)).append("'");
                    }
                    where.append(") ");
                } else if (MODEL_NAME.equalsIgnoreCase(key)) {
                    where.append(" model in (");
                    ArrayList<String> filterValue = filterTypeValueMap.get(key);
                    for (int i = 0; i < filterValue.size(); i++) {
                        if (i != 0)
                            where.append(", ");
                        where.append("'").append(filterValue.get(i)).append("'");
                    }
                    where.append(") ");
                } else if (TRIM_NAME.equalsIgnoreCase(key)) {
                    where.append(" trim in (");
                    ArrayList<String> filterValue = filterTypeValueMap.get(key);
                    for (int i = 0; i < filterValue.size(); i++) {
                        if (i != 0)
                            where.append(", ");
                        where.append("'").append(filterValue.get(i)).append("'");
                    }
                    where.append(") ");
                }
            }
        }
        query += where.toString();
        return query;
    }


    /**
     * insert into compare_vehicle(year, make, model, modelid, trim, trimid,
     * imageurl) values(?,?,?,?,?,?,?);
     */
    private PreparedStatementCreator getPreparedStatementCreator(final Integer modelYear, final String modelId, final String makeName,
            final String modelName,  final Trim trim) {

        return new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                PreparedStatement statement = connection.prepareStatement(INSERT_COMPARE_VEHICLE_SQL);
                statement.setInt(1, modelYear);
                statement.setString(2, makeName);
                statement.setString(3, modelName);
                statement.setString(4, modelId);
                statement.setString(5, trim.getTrimName());
                statement.setString(6, trim.getTrimID());
                statement.setString(7, trim.getImageURL());

                return statement;
            }
        };
    }


//  INSERT INTO compare_suggested_trim (trimid, suggestedtrimid) VALUES (?, ?);
    public void populateSuggestedTrimData()  throws CompareServiceException{

    	Competitors competitors = new Competitors();
    	try
    	{
    		competitors = compareServiceHelper.getSuggestedTrims();
    		List<Trim> baseVehicleTrims = competitors.getBaseVehicle();
    		String trimdId = null;
    		String suggestedTrimId = null;
			HashSet<String> insertedTrimKey;
    		for(Trim trim : baseVehicleTrims)
    		{
				insertedTrimKey = new HashSet<String>();
    			trimdId = trim.getTrimID();
    			List<Vehicle> competitorVehicles = trim.getCompetitorVehicle();
    			for(Vehicle vehicle : competitorVehicles)
    			{
    				 suggestedTrimId = vehicle.getTrimID();

					 if(!insertedTrimKey.contains(suggestedTrimId)) {
    				 	 PreparedStatementCreator psc = getPrepStmtCreatorForSugTrims(trimdId, suggestedTrimId);
                    	 jdbcTemplate.update(psc);
                    	 insertedTrimKey.add(suggestedTrimId);
					 }
    			}

    		}
    	}

    	catch (CompareServiceException e) {
            log.info("Exception during populateVehicleData"+ e.getMessage());
        }

    }

    private PreparedStatementCreator getPrepStmtCreatorForSugTrims(final String trimId, final String suggestedTrimId)	 {

        return new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                PreparedStatement statement = connection.prepareStatement(INSERT_COMPARE_SUGGESTED_TRIM_SQL);
                statement.setString(1, trimId);
                statement.setString(2, suggestedTrimId);


                return statement;
            }
        };
    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CompareServiceHelper getCompareServiceHelper() {
        return compareServiceHelper;
    }

    public void setCompareServiceHelper(CompareServiceHelper compareServiceHelper) {
        this.compareServiceHelper = compareServiceHelper;
    }







}
