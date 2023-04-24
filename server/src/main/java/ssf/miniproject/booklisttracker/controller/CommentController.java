package ssf.miniproject.booklisttracker.controller;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import ssf.miniproject.booklisttracker.model.Comment;
import ssf.miniproject.booklisttracker.model.UserResponse;
import ssf.miniproject.booklisttracker.service.CommentService;

@Controller
@RequestMapping(path = "/api")
public class CommentController {


    @Autowired
    private CommentService commentSvc;


    @PostMapping(path = "/comment/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    @CrossOrigin()
    public ResponseEntity<String> postComment(@RequestPart String userId, @RequestPart String bookId, @RequestPart String bookTitle, @RequestPart String commentTitle, @RequestPart String comment){   

        Comment c = new Comment();
        c.setUserId(userId);
        c.setBookId(bookId);
        c.setBookTitle(bookTitle);
        c.setCommentTitle(commentTitle);
        c.setComment(comment);
        
        Optional<String> opt = commentSvc.createComment(c);
        String commentId = opt.get();


		JsonObject response = Json.createObjectBuilder()
        .add("commentId", commentId)
        .build();

        return ResponseEntity.ok(response.toString());

    }

    @GetMapping(path="/{bookId}/comments")
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<List<Comment>> getComments(@PathVariable String bookId) {

        List<Comment> commentsList = commentSvc.getComments(bookId);
      
      

        return ResponseEntity.ok(commentsList);
    }

    @DeleteMapping(path = "/comment/delete",  produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<String> deleteBookFromRepo(@RequestParam String commentId) {

        System.out.println("deleting" + commentId);
        Optional<Document> opt = commentSvc.deleteComment(commentId);


        if (opt.isEmpty()) {
            UserResponse response = new UserResponse();
            response.setStatus(404);
            response.setMessage("Unable to retrieve book detail from book detail");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(response.toJson().toString());

        } else {
            return new ResponseEntity<>(commentId, HttpStatus.NO_CONTENT);

        }
    }

}
