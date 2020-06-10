package com.exercise.employment.countryemployment.constants;

public interface SqlQueryConstant {
    String SELECT_COUNTRY_STATE_ID = "SELECT country_id,state_id FROM Country_State_Mapping_Data WHERE state_id IN(select id from state_id_tmp)";
    String SELECT_COUNTRY_STATE_MASTER_DATA = "Select c.country_id,c.country_name,s.state_id,s.state_name from country_master c " +
            "INNER JOIN  state_master s ON c.country_id=s.country_id";
    String INSERT_COUNTRY_STATE_DATA = "insert into Country_State_Mapping_Data(STATE_ID,COUNTRY_ID,STATE_AREA,STATE_NUM_EMPLOYED,STATE_POPULATION,CREATED_BY,CREATED_TS,EMPLOYMENT_RATE) " +
            "values(:stateId,:countryId,:stateArea,:stateNumEmployed,:statePopulation,:created_by,:created_ts,:employmentRate)";
    String UPDATE_COUNTRY_STATE_DATA = "update  Country_State_Mapping_Data set STATE_AREA=:stateArea,STATE_NUM_EMPLOYED=:stateNumEmployed,STATE_POPULATION=:statePopulation," +
            "MODIFIED_BY=:modified_by,MODIFIED_TS=:modified_ts,EMPLOYMENT_RATE=:employmentRate where STATE_ID=:stateId and COUNTRY_ID=:countryId";
    String SELECT_COUNTRY_STATE_DATA = "SELECT c.country_id,c.country_name,s.state_id,s.state_name,cs.state_area,cs.state_num_employed,\n" +
            "    cs.state_population,cs.created_by,cs.modified_by,cs.created_ts,cs.modified_ts,cs.employment_rate\n" +
            "    FROM country_master c INNER JOIN state_master s ON c.country_id = s.country_id\n" +
            "    INNER JOIN country_state_mapping_data cs ON s.state_id = cs.state_id";
    String INSERT_STATE_IDS="Insert into state_id_tmp values(:id)";
    String CREATE_TEMP_TABLE="CREATE Global TEMPORARY TABLE state_id_tmp (id INT NOT NULL)";
    String DELETE_TEMP_TABLE= "Delete from state_id_tmp";
}