package ssf.miniproject.booklisttracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.miniproject.booklisttracker.model.User;
import ssf.miniproject.booklisttracker.repository.UserRepo;

@Service
public class UserService {
    

    @Autowired 
    private UserRepo userRepo;


    public boolean createNewUser(String id, String username, String password, String email) {
        System.out.println("in service");
        // System.out.println(username);
        // System.out.println(id);
        // System.out.println(password);
        return userRepo.insertUserIntoRepo(id, username, password, email);
    }

    public boolean validateUser(String username, String password) throws Exception{
        boolean result = userRepo.validateUser(username, password);
        System.out.println("validating result >>> " + result);
       return result;
    }


    public User findUserByUsernameAndPassword(String username, String password){
        return userRepo.findUserByUsernameAndPassword(username, password);
    }
        
    public Optional<User>findUserById(String id){
        return userRepo.findUserbyId(id);
    }
}
