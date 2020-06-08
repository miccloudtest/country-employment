package com.exercise.employment.countryemployment.repositories.repositoryImpl;

import com.exercise.employment.countryemployment.beans.CountryStateIDMaster;
import com.exercise.employment.countryemployment.beans.CountryData;
import com.exercise.employment.countryemployment.constants.SqlQueryConstant;
import com.exercise.employment.countryemployment.repositories.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CountryRepositoryImpl implements CountryRepository {
    Logger logger = LoggerFactory.getLogger(CountryRepositoryImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<CountryStateIDMaster> loadCountryStateCache() {
        return namedParameterJdbcTemplate.query(SqlQueryConstant.SELECT_COUNTRY_STATE_MASTER_DATA, new BeanPropertyRowMapper<>(CountryStateIDMaster.class));
    }

    @Override
    public Map<Integer, Integer> getExistingCountryStateId(Set<Integer> states) {
        Map<Integer, Integer> map = new HashMap<>();
        String query = SqlQueryConstant.SELECT_COUNTRY_STATE_ID;
        String queryParam = states.stream().map(String::valueOf).collect(Collectors.joining(","));
        query = query.replace(":data", queryParam);
        return namedParameterJdbcTemplate.query(query, new ResultSetExtractor<Map>() {
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
    public void insertBatchExecute(List<CountryData> excelRecord) throws Exception {
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
    public void updateBatchExecute(List<CountryData> excelRecord) {
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

    @Override
    public List<CountryData> getCountriesData() {
        return namedParameterJdbcTemplate.query(SqlQueryConstant.SELECT_COUNTRY_STATE_MASTER_DATA, new BeanPropertyRowMapper<>(CountryData.class));
    }

    private int[] batchInsert(List<CountryData> record) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(record.toArray());
        int[] insertCount = namedParameterJdbcTemplate.batchUpdate(SqlQueryConstant.INSERT_COUNTRY_STATE_DATA, batch);
        return insertCount;
    }

    private int[] batchUpdate(List<CountryData> record) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(record.toArray());
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(SqlQueryConstant.UPDATE_COUNTRY_STATE_DATA, batch);
        return updateCounts;
    }
}
