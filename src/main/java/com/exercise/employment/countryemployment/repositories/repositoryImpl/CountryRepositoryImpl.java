package com.exercise.employment.countryemployment.repositories.repositoryImpl;

import com.exercise.employment.countryemployment.beans.CountryData;
import com.exercise.employment.countryemployment.beans.CountryStateIDMaster;
import com.exercise.employment.countryemployment.constants.SqlQueryConstant;
import com.exercise.employment.countryemployment.repositories.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
        doBatchInsertLocalTable(states);
        return namedParameterJdbcTemplate.query(SqlQueryConstant.SELECT_COUNTRY_STATE_ID, new ResultSetExtractor<Map>() {
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
        return namedParameterJdbcTemplate.query(SqlQueryConstant.SELECT_COUNTRY_STATE_DATA, new BeanPropertyRowMapper<>(CountryData.class));
    }

    @Override
    public void exceCountryStateBackUpProc() {
        namedParameterJdbcTemplate.getJdbcTemplate().call(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection connection)
                    throws SQLException {
                CallableStatement callableStatement = connection.prepareCall("{call backup_country_state_data_proc()}");
                return callableStatement;

            }
        }, new ArrayList<>());
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

    private int[] doBatchInsertLocalTable(Set<Integer> stateIds) {
        final Map<String, Integer>[] ids = new Map[stateIds.size()];
        Iterator<Integer> data = stateIds.iterator();
        int i = 0;
        while (data.hasNext()) {
            ids[i] = Collections.singletonMap("id", data.next());
            i++;
        }
        int[] insertCount = namedParameterJdbcTemplate.batchUpdate(SqlQueryConstant.INSERT_STATE_IDS, ids);
        return insertCount;

    }
}
