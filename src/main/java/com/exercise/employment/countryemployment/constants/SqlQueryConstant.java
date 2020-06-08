package com.exercise.employment.countryemployment.constants;

public interface SqlQueryConstant {
    String SELECT_COUNTRY_STATE_ID = "SELECT country_id,state_id FROM Country_State_Mapping_Data WHERE state_id IN(:data)";
    String SELECT_COUNTRY_STATE_MASTER_DATA = "Select c.country_id,c.country_name,s.state_id,s.state_name from country_master c " +
            "INNER JOIN  state_master s ON c.country_id=s.country_id";
    String INSERT_COUNTRY_STATE_DATA = "insert into Country_State_Mapping_Data(STATE_ID,COUNTRY_ID,STATE_AREA,STATE_NUM_EMPLOYED,STATE_POPULATION,CREATED_BY,CREATED_TS,EMPLOYMENT_RATE) " +
            "values(:stateId,:countryId,:stateArea,:stateNumOfEmployed,:statePopulation,:created_by,:created_ts,:employmentRate)";
    String UPDATE_COUNTRY_STATE_DATA = "update  Country_State_Mapping_Data set STATE_AREA=:stateArea,STATE_NUM_EMPLOYED=:stateNumOfEmployed,STATE_POPULATION=:statePopulation," +
            "MODIFIED_BY=:modified_by,MODIFIED_TS=:modified_ts,EMPLOYMENT_RATE=:employmentRate where STATE_ID=:stateId and COUNTRY_ID=:countryId";
}
