package com.exercise.employment.countryemployment.repository;

import com.exercise.employment.countryemployment.beans.CountryStateMapping;
import com.exercise.employment.countryemployment.beans.ExcelSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ExcelRepository implements IExcelRepository {
    Logger logger = LoggerFactory.getLogger(ExcelRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CountryStateMapping> loadCountryStateCache() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("Select c.country_id,c.country_name,s.state_id,s.state_name from country_master c INNER JOIN  state_master s ON c.country_id=s.country_id");
        return jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<>(CountryStateMapping.class));
    }

    @Override
    public Map<Integer, Integer> getExistingCountryStateId(Set<Integer> states) {
        Map<Integer, Integer> map = new HashMap<>();
        String query = new String("SELECT country_id,state_id FROM Country_State_Mapping_Data WHERE state_id IN(:data)");
        String queryParam = states.stream().map(String::valueOf).collect(Collectors.joining(","));
        query = query.replace(":data", queryParam);
        return jdbcTemplate.query(query, new ResultSetExtractor<Map>() {
            @Override
            public Map extractData(ResultSet rs) throws SQLException, DataAccessException {
                HashMap<Integer, Integer> stateCountryId = new HashMap<Integer, Integer>();
                while (rs.next()) {
                    stateCountryId.put(rs.getInt("state_id"), rs.getInt("country_id"));
                }
                return stateCountryId;
            }
        });
    }

    @Override
    public void insertBatchExecute(List<ExcelSheet> excelRecord) throws Exception {
        try {
            if (null != excelRecord) {
                batchInsert(excelRecord);
                logger.debug("updateBatchExecute for record count,{}", excelRecord.size());

            }
        } catch (Exception ex) {
            logger.error("Error in insertBatchExecute {}", ex.getMessage());
            throw ex;

        }
    }

    @Override
    public void updateBatchExecute(List<ExcelSheet> excelRecord) {
        try {
            if (null != excelRecord) {
                batchUpdate(excelRecord);
                logger.debug("updateBatchExecute for record count,{}", excelRecord.size());
            }
        } catch (Exception ex) {
            logger.error("Error in updateBatchExecute {}", ex.getMessage());
            throw ex;

        }
    }

    private int[] batchInsert(List<ExcelSheet> record) {
        return this.jdbcTemplate.batchUpdate(
                "insert into Country_State_Mapping_Data(STATE_ID,COUNTRY_ID,STATE_AREA,STATE_NUM_EMPLOYED," +
                        "STATE_POPULATION,CREATED_BY,CREATED_TS,EMPLOYMENT_RATE) values(?,?,?,?,?,?,?,?) ",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, record.get(i).getStateId());
                        ps.setInt(2, record.get(i).getCountryId());
                        ps.setInt(3, record.get(i).getStateArea());
                        ps.setLong(4, record.get(i).getStateNumOfEmployed());
                        ps.setLong(5, record.get(i).getStatePopulation());
                        ps.setString(6, record.get(i).getCreated_by());
                        ps.setDate(7, new Date(record.get(i).getCreated_ts().getTime()));
                        ps.setDouble(8, record.get(i).getEmploymentRate());
                    }

                    public int getBatchSize() {
                        return record.size();
                    }
                });
    }

    private int[] batchUpdate(List<ExcelSheet> record) {
        return this.jdbcTemplate.batchUpdate(
                "update  Country_State_Mapping_Data set STATE_AREA=?,STATE_NUM_EMPLOYED=?,STATE_POPULATION=?,MODIFIED_BY=?" +
                        ",MODIFIED_TS=?,EMPLOYMENT_RATE=? where STATE_ID=? and COUNTRY_ID=? ",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, record.get(i).getStateArea());
                        ps.setLong(2, record.get(i).getStateNumOfEmployed());
                        ps.setLong(3, record.get(i).getStatePopulation());
                        ps.setString(4, record.get(i).getModified_by());
                        ps.setDate(5, new Date(record.get(i).getModified_ts().getTime()));
                        ps.setDouble(6, record.get(i).getEmploymentRate());
                        ps.setInt(7, record.get(i).getStateId());
                        ps.setInt(8, record.get(i).getCountryId());
                    }

                    public int getBatchSize() {
                        return record.size();
                    }
                });
    }
}
