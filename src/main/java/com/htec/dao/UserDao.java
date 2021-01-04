package com.htec.dao;

import com.htec.model.User;
import com.htec.util.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to provide access to user data stored inside database.
 */
public class UserDao implements IUserDao {

    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    private Configuration configuration = Configuration.instance();
    Logger lgr = Logger.getLogger(UserDao.class.getName());

    /**
     * Provides view of all user inside database.
     * @return list of users that are present inside database
     */
    @Override
    public List<User> findAll() {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        List<User> users = new ArrayList<>();

        String query = "SELECT * FROM USERS;";

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            users = jtm.query(query,
                    new BeanPropertyRowMapper(User.class));
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
        }

        return users;
    }

    /**
     * Provides user data insertion inside database.
     * @param user ready to be stored inside database
     * @return is user stored successfully to database
     */
    @Override
    public boolean save(User user) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "INSERT INTO USERS(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, SALT) VALUES (?, ?, ?, ?, ?)";

        boolean ret = true;

        String salt = generateRandomSalt(10);
        String saltedPassword = getSecurePassword(user.getPassword(), salt.getBytes(StandardCharsets.UTF_8));

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            jtm.update(sql, new Object[]{user.getFirstName(), user.getLastName(), user.getUsername(),
                    saltedPassword, salt});

        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);

            ret = false;
        }

        return ret;
    }

    /**
     * Search for user based on comment id.
     * @param id (primary key) of user to be found
     * @return user in case there is valid result
     */
    @Override
    public User find(Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "SELECT * FROM USERS WHERE ID_USER=?";

        User user = User.builder().build();

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            user = (User) jtm.queryForObject(sql, new Object[]{id},
                    new BeanPropertyRowMapper(User.class));
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
        }

        return user;
    }

    /**
     * Provides user data for user inside database based on username. Used for authentication.
     * @param username username for searched user
     * @return user data based on username
     */
    @Override
    public User findRoleByUsername(String username) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        String sql = "SELECT * FROM USERS WHERE USERNAME=?";

        User user = User.builder().build();

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            user = (User) jtm.queryForObject(sql, new Object[]{username},
                    new BeanPropertyRowMapper(User.class));
        } catch (DataAccessException dae) {
            lgr.log(Level.SEVERE, dae.getMessage(), dae);
        }

        return user;
    }

    /**
     * Provides user data update inside database.
     * @param user new user data to be updated
     * @param id of user to be updated
     * @return is user updated successfully to database
     */
    @Override
    public boolean update(User user, Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        boolean ret = true;

        String sql = "";

        User dbUser = find(id);
        User updatedUser;
        String updatedSalt = generateRandomSalt(10);
        boolean isPasswordChanged = false;

        if(getSecurePassword(user.getPassword(),dbUser.getSalt().getBytes(StandardCharsets.UTF_8)).equals(dbUser.getPassword())){
            updatedUser=user;
            sql = "UPDATE USERS SET FIRST_NAME=?, LAST_NAME=?, USERNAME=? WHERE ID_USER=?";
            isPasswordChanged = false;
        }
        else{
            updatedUser = User.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .password(getSecurePassword(user.getPassword(),updatedSalt.getBytes(StandardCharsets.UTF_8)))
                    .salt(updatedSalt).build();
            sql = "UPDATE USERS SET FIRST_NAME=?, LAST_NAME=?, USERNAME=?, PASSWORD=?, SALT=? WHERE ID_USER=?";
            isPasswordChanged = true;
        }

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            int rowsNo;
            if(isPasswordChanged) {
                rowsNo = jtm.update(sql, new Object[]{updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getUsername(), updatedUser.getPassword(),
                        updatedUser.getSalt(), id});
            }
            else{
                rowsNo = jtm.update(sql, new Object[]{updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getUsername(), id});
            }
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
     * Provides user data deletion inside database.
     * @param id of user to be deleted
     * @return is user deleted successfully to database
     */
    @Override
    public boolean delete(Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        boolean ret = true;

        String sql = "DELETE FROM USERS WHERE ID_USER=?";
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

    /**
     * Provides role update for user based on user id.
     * @param role role to be given to user
     * @param id id of user which will be updated
     * @return is role update successful
     */
    @Override
    public boolean updateRole(String role, Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        boolean ret = true;

        String sql = "UPDATE USERS SET ROLE=? WHERE ID_USER=?";

        try {
            JdbcTemplate jtm = new JdbcTemplate(ds);
            int rowsNo = jtm.update(sql, new Object[]{role, id});
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
     * Register user after initial sign up.
     * @param id id of user to be registered
     * @return is user registration successful
     */
    @Override
    public boolean register(Long id) {

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(configuration.DB_URL);

        boolean ret = true;

        String sql = "UPDATE USERS SET REGISTERED=1 WHERE ID_USER=?";

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

    /**
     * Generates salted password based on password and salt.
     * @param password password for salt application
     * @param salt random salt
     * @return
     */
    public static String getSecurePassword(String password, byte[] salt) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * Generator for random <code>String</code> salt of configured length.
     * @param length defined length of random salt
     * @return randomly generated <code>String</code> salt
     */
    public static String generateRandomSalt(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int c = RANDOM.nextInt(62);
            if (c <= 9) {
                sb.append(String.valueOf(c));
            } else if (c < 36) {
                sb.append((char) ('a' + c - 10));
            } else {
                sb.append((char) ('A' + c - 36));
            }
        }
        return sb.toString();
    }
}
