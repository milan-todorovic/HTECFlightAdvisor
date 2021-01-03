package com.htec.dao;

import com.htec.model.City;
import com.htec.model.Comment;
import com.htec.util.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to provide access to city data stored inside database.
 */
public class CityDao implements ICityDao {

    private Configuration configuration = Configuration.instance();
    Logger lgr = Logger.getLogger(CityDao.class.getName());
    public final static RowMapper<City> cityMapper = BeanPropertyRowMapper.newInstance(City.class);
    public final static RowMapper<Comment> commentMapper = BeanPropertyRowMapper.newInstance(Comment.class);

    /**
     * Provides view of all cities inside database.
     * @return list of cities that are present inside database
     */
    @Override
    public List<City> findAll() {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "SELECT * FROM " +
                "(SELECT CI.ID_CITY, CI.NAME, CI.COUNTRY, CI.DESCRIPTION, C.ID_COMMENT, C.COMMENT, C.FK_CITY_ID, C.CREATED_DATE, C.MODIFIED_DATE " +
                "FROM CITIES CI LEFT JOIN COMMENTS C " +
                "on CI.ID_CITY = C.FK_CITY_ID) ";

        List<City> cities = new ArrayList<>();

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            cities = jtm.query(sql,
                    new ResultSetExtractor<List<City>>() {
                        public List<City> extractData(ResultSet rs) throws SQLException, DataAccessException {
                            List<City> workingCities = new ArrayList<>();
                            Long cityId = null;
                            City currentCity = null;
                            int cityIdx = 0;
                            int commentIdx = 0;
                            while (rs.next()) {
                                // first row or when city changes
                                if (currentCity == null || !cityId.equals(rs.getLong("id_city"))) {
                                    cityId = rs.getLong("id_city");
                                    currentCity = cityMapper.mapRow(rs, cityIdx++);
                                    commentIdx = 0;
                                    workingCities.add(currentCity);
                                }

                                currentCity.getComments().add(commentMapper.mapRow(rs, commentIdx++));
                            }
                            return workingCities;
                        }
                    });
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
        }

        return cities;
    }

    /**
     * Provides city data insertion inside database.
     * @param city ready to be stored inside database
     * @return is city stored successfully to database
     */
    @Override
    public boolean save(City city) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "INSERT INTO CITIES(NAME, COUNTRY, DESCRIPTION) VALUES (?, ?, ?)";

        boolean ret = true;

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            jtm.update(sql, new Object[]{city.getName(), city.getCountry(), city.getDescription()});

        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);

            ret = false;
        }

        return ret;
    }

    /**
     * Search for city based on city id.
     * @param id (primary key) of city to be found
     * @return city in case there is valid result
     */
    @Override
    public City find(Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "SELECT * FROM CITIES WHERE ID_CITY=?";

        City city = City.builder().build();

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            city = (City) jtm.queryForObject(sql, new Object[]{id},
                    new BeanPropertyRowMapper(City.class));
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
        }

        return city;
    }

    /**
     * Search for city based on city name.
     * @param name of city to be found
     * @return city in case there is valid result
     */
    @Override
    public List<City> findByName(String name) {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "SELECT * FROM " +
                "(SELECT CI.ID_CITY, CI.NAME, CI.COUNTRY, CI.DESCRIPTION, C.ID_COMMENT, C.COMMENT, C.FK_CITY_ID, C.CREATED_DATE, C.MODIFIED_DATE " +
                "FROM CITIES CI LEFT JOIN COMMENTS C " +
                "on CI.ID_CITY = C.FK_CITY_ID " +
                "WHERE NAME=?)";

        List<City> cities = new ArrayList<>();

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            cities = jtm.query(sql, new Object[]{name},
            new ResultSetExtractor<List<City>>() {
                public List<City> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<City> workingCities = new ArrayList<>();
                    Long cityId = null;
                    City currentCity = null;
                    int cityIdx = 0;
                    int commentIdx = 0;
                    while (rs.next()) {
                        // first row or when city changes
                        if (currentCity == null || !cityId.equals(rs.getLong("id_city"))) {
                            cityId = rs.getLong("id_city");
                            currentCity = cityMapper.mapRow(rs, cityIdx++);
                            commentIdx = 0;
                            workingCities.add(currentCity);
                        }

                        currentCity.getComments().add(commentMapper.mapRow(rs, commentIdx++));
                    }
                    return workingCities;
                }
            });
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
        }

        return cities;
    }

    /**
     * Provides city data update inside database.
     * @param city new city data to be updated
     * @param id of city to be updated
     * @return is city updated successfully to database
     */
    @Override
    public boolean update(City city, Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        boolean ret = true;

        String sql = "UPDATE CITIES SET NAME=?, COUNTRY=?, DESCRIPTION=? WHERE ID_CITY=?";

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            int rowsNo = jtm.update(sql, new Object[]{city.getName(), city.getCountry(), city.getDescription(), id});
            if (rowsNo != 1) {
                ret = false;
            }
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
            ret = false;
        }

        return ret;
    }

    /**
     * Provides city and linked comments (parent-child) data deletion inside database.
     * @param id of city to be deleted
     * @return is city and corresponding comments deleted successfully to database
     */
    @Override
    public boolean delete(Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        boolean ret = true;

        String sqlCities = "DELETE FROM CITIES WHERE ID_CITY=?";
        String sqlComments = "DELETE FROM COMMENTS WHERE FK_CITY_ID=?";
        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            int rowsNo = jtm.update(sqlComments, new Object[]{id});
            if (rowsNo != 1) {
                ret = false;
            }
            rowsNo = jtm.update(sqlCities, new Object[]{id});
            if (rowsNo != 1) {
                ret = false;
            }
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
            ret = false;
        }

        return ret;
    }
}
