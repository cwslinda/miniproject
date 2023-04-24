package ssf.miniproject.booklisttracker.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import ssf.miniproject.booklisttracker.model.Comment;
import ssf.miniproject.booklisttracker.model.User;
import ssf.miniproject.booklisttracker.service.CommentService;
import ssf.miniproject.booklisttracker.service.EmailService;
import ssf.miniproject.booklisttracker.service.UserService;




@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired 
    private UserService svc;

    @Autowired
    private CommentService commentSvc;

    @Autowired
	private EmailService senderService;

    @PostMapping(path = "/register", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, 
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<String>registerUser(@RequestPart("username") String username, @RequestPart("password") String password, @RequestPart("email") String email){
         
        User u = new User();


       try {
        if (svc.validateUser(username, password) == false){
            String userId = UUID.randomUUID().toString().substring(0,8);
            svc.createNewUser(userId, username, password, email);

            String content = "Hello! \n\n" +
                             "Welcome to BookPortal, you have now signed up an account with us! \n\n " +
                             "This is a system generated email. Please do alert us if you have not make this request. ";

            senderService.sendEmail(email, "You have signed up with Book Portal", content);

            return ResponseEntity.ok(Json.createObjectBuilder()
            .add("userId", userId)
            .add("username", username)
            .add("email", email)
            .build().toString());}
       } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                .body(Json.createObjectBuilder().add("error", e.getMessage()).toString());
       }

       User user = svc.findUserByUsernameAndPassword(username, password);  

       return ResponseEntity.ok(Json.createObjectBuilder()
                            .add("userId", user.getId())
                            .add("username", username)
                            .add("email", email)
                            .build().toString());
    }


    @GetMapping(path="/user-comments/{userId}")
    @ResponseBody
    public ResponseEntity<List<Comment>> getCommentsFromId(@PathVariable String userId) {

        List<Comment> commentsList = commentSvc.getCommentsFromId(userId);
      
      

        return ResponseEntity.ok(commentsList);
    }

   


}


    

