package ssf.miniproject.booklisttracker.controller;


import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import ssf.miniproject.booklisttracker.model.Book;
import ssf.miniproject.booklisttracker.model.User;
import ssf.miniproject.booklisttracker.model.UserResponse;
import ssf.miniproject.booklisttracker.repository.BooksRepo;
import ssf.miniproject.booklisttracker.service.BookService;
import ssf.miniproject.booklisttracker.service.UserService;

@Controller
@RequestMapping("/api")
public class BookRESTController {
    private static final Logger logger = LoggerFactory.getLogger(BookRESTController.class);

    @Autowired
    BookService service;

    @Autowired
    private UserService userSvc;

    @Autowired
    BooksRepo repo;



    @GetMapping(path="/search", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<List<Book>> searchResults(
                                        @RequestParam String keyword) {

        List<Book> bookList = service.getSearchBooks(keyword);

        // exceeded api request limit
        if (bookList == null) {
            return ResponseEntity.status(504).build();
        }

        System.out.println(bookList);
        
        return ResponseEntity.ok(bookList);
    }

    @GetMapping(path="/book/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<Book> getSingleBook(@PathVariable String id) {
       
       Book book = service.getSingleBook(id);

        // exceeded api request limit
        if (book == null) {
            return ResponseEntity.status(504).build();
        }
        
        return ResponseEntity.ok(book);
    }

    @GetMapping(path="/user/{userId}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<User> getUserBooks(@PathVariable("userId") String id){


        Optional<User> userOpts = userSvc.findUserById(id); 
    
        User u = new User();
        if(!userOpts.isEmpty() ){
            u = userOpts.get();
            u.setBooks(service.getUserBooks(u.getId()));
        }

        return ResponseEntity.ok(u);

    
    }

    @DeleteMapping(path = "/delete",  produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<String> deleteBookFromRepo(@RequestParam String userId, @RequestParam String bookId) {

        System.out.println(bookId);
        System.out.println(userId);
        Optional<String> opt = service.deleteBookById(bookId, userId);



        if (opt.isEmpty()) {
            UserResponse response = new UserResponse();
            response.setStatus(404);
            response.setMessage("Unable to retrieve book detail from book detail");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(response.toJson().toString());

        } else {
            return new ResponseEntity<>(bookId, HttpStatus.NO_CONTENT);

        }
    }

    @PostMapping(path="/save/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE )
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<Book> saveUserBook(@PathVariable("userId") String id, @RequestPart String form) {     
        
        Book book = new Book();
        JsonReader reader = Json.createReader(new StringReader(form));
        JsonObject data = reader.readObject();
        

        System.out.println(data.getJsonArray("authors").toString());
        
        
        book.setId(data.getString("id"));
        book.setTitle(data.getString("title"));
        book.setDescription(data.getString("description"));
        if(data.getJsonArray("authors")!=null){
            JsonArray authorsArr = data.getJsonArray("authors");
            List<String> authorsList = new LinkedList<>();
                if(authorsArr.size() != 0){
                    for (int j = 0; j < authorsArr.size(); j++) {
                        authorsList.add(authorsArr.getString(j));
                    }
                }
    
            book.setAuthors2(data.getJsonArray("authors").toString());
        }

        book.setPublishedDate(data.getString("publishedDate"));
        book.setUrlLink(data.getString("urlLink"));
        book.setPreviewLink(data.getString("previewLink"));


      

        service.saveBook(book, id);

        return ResponseEntity.ok(book);
    }
}