package ssf.miniproject.booklisttracker.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ssf.miniproject.booklisttracker.model.User;

import static ssf.miniproject.booklisttracker.repository.Queries.*;

import java.util.Optional;

@Repository
public class UserRepo {


    @Autowired
    private JdbcTemplate template;


    public  boolean insertUserIntoRepo(String id, String username, String password, String email){

        System.out.println("inserting into mysql");
        int inserted = template.update(SQL_INSERT_USER, id, username, password, email);

        return inserted > 0;
    }

    public boolean validateUser(String username, String password) {

        final SqlRowSet rs = template.queryForRowSet(SQL_VALIDATE_USER, 
                    username, password);
        
        if(rs.next()){
            return rs.getBoolean("valid_cred");
        }

        return false;
    }


    public User findUserByUsernameAndPassword(String username, String password){
        
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_BY_USERNAME_PASSWORD, username, password);

        User user = new User();
              
      
        if(rs.next()){
            user.setId(rs.getString(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            user.setEmail(rs.getString(4));
        }
    
        return user;
     

        // if (!rs.next())
		// 	return Optional.empty();
        // return Optional.of(User.create(rs));
        
    }


    public Optional<User> findUserbyId(String id){
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_ID, id);

        if (!rs.next())
			return Optional.empty();
    
        return Optional.of(User.create(rs));
        
    }
 
}
