package com.htec.dao;

import com.htec.model.Comment;
import com.htec.util.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to provide access to comment data stored inside database.
 */
public class CommentDao implements ICommentDao {

    private Configuration configuration = Configuration.instance();
    Logger lgr = Logger.getLogger(CommentDao.class.getName());

    /**
     * Provides view of all comments inside database.
     * @return list of comments that are present inside database
     */
    @Override
    public List<Comment> findAll() {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        List<Comment> comments = new ArrayList<>();

        String query = "SELECT * FROM COMMENTS;";

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            comments = jtm.query(query,
                    new BeanPropertyRowMapper(Comment.class));
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
        }

        return comments;
    }
    /**
     * Provides comment data insertion inside database.
     * @param comment ready to be stored inside database
     * @return is comment stored successfully to database
     */
    @Override
    public boolean save(Comment comment) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "INSERT INTO COMMENTS(COMMENT, FK_CITY_ID, CREATED_DATE) VALUES (?, ?, ?)";

        boolean ret = true;

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            jtm.update(sql, new Object[]{comment.getComment(), comment.getFkCityId(), LocalDate.now()});

        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);

            ret = false;
        }

        return ret;
    }

    /**
     * Search for comment based on comment id.
     * @param id (primary key) of comment to be found
     * @return comment in case there is valid result
     */
    @Override
    public Comment find(Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "SELECT * FROM COMMENTS WHERE ID_COMMENT=?";

        Comment comment = Comment.builder().build();

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            comment = (Comment) jtm.queryForObject(sql, new Object[]{id},
                    new BeanPropertyRowMapper(Comment.class));
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
        }

        return comment;
    }

    /**
     * Provides comment data update inside database.
     * @param comment new comment data to be updated
     * @param id of comment to be updated
     * @return is comment updated successfully to database
     */
    @Override
    public boolean update(Comment comment, Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        boolean ret = true;

        String sql = "UPDATE COMMENTS SET COMMENT=?, FK_CITY_ID=?, MODIFIED_DATE=? WHERE ID_COMMENT=?";

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            int rowsNo = jtm.update(sql, new Object[]{comment.getComment(), comment.getFkCityId(), LocalDate.now(), id});
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
     * Provides comment data deletion inside database.
     * @param id of comment to be deleted
     * @return is comment deleted successfully to database
     */
    @Override
    public boolean delete(Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        boolean ret = true;

        String sql = "DELETE FROM COMMENTS WHERE ID_COMMENT=?";
        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            int rowsNo = jtm.update(sql, new Object[]{id});
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
