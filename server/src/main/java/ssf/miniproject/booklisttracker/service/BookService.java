package ssf.miniproject.booklisttracker.service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
// import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ssf.miniproject.booklisttracker.model.Book;
import ssf.miniproject.booklisttracker.repository.BooksRepo;

@Service
public class BookService{

    @Value("searchParam")
    String searchParam;

    @Value("${API_KEY}")
    private String apiKey;


    @Autowired
    private BooksRepo bookRepo;
    

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private static final String indexUrl = "https://www.googleapis.com/books/v1/volumes?";
    private static final String singleBook = "https://www.googleapis.com/books/v1/volumes/";

    //private static final String singleUrl = "https://www.googleapis.com/books/v1/volumes/";
    
  

    //get books are that in the fiction genre
    public List<Book> getFictionBooks(){
        String bookUrl = UriComponentsBuilder.fromUriString(indexUrl)
                        .query("q=subject:fiction&orderBy=newest")
                        .queryParam("key", apiKey)
                        .toUriString();

        logger.info("complete books uri for the index page " + bookUrl);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        List<Book> bookList = new LinkedList<>();
        try {
            resp = template.getForEntity(bookUrl, String.class);
            bookList = Book.createJson(resp.getBody());
            
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return bookList;
    }

    //used for the search function - books that user search
    public List<Book> getSearchBooks(String keywords){
        this.searchParam = keywords;
        
        String bookUrl = UriComponentsBuilder.fromUriString(indexUrl)
                        .query("q=" + keywords)
                        .query("key=" + apiKey)
                        .query("maxResults=" + 20)
                        .toUriString();

        logger.info("complete books uri for the search page " + bookUrl);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        List<Book> bookList = new LinkedList<>();
        try {
            resp = template.getForEntity(bookUrl, String.class);
            bookList = Book.createJson(resp.getBody());
            
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return bookList;
    }

    public Book getSingleBook(String bookId){

        String bookUrl = UriComponentsBuilder.fromUriString(singleBook)
                .path(bookId)
                .query("key=" + apiKey)
                .toUriString();
        
                System.out.println(bookUrl);
        logger.info("complete single book url " + bookUrl);

        RestTemplate template = new RestTemplate();
        RequestEntity<Void> req = RequestEntity.get(bookUrl).accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        String payload = resp.getBody();     
        
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject result = reader.readObject();
        // JsonArray data = result.getJsonObject("data").getJsonArray("results");
        System.out.println(result.get("id"));
        

        Book b = Book.createSingleBookJson(result);

        return b;
    }


    public List<Book> getUserBooks(String userId){

        Optional<List<Book>> bookListOps = bookRepo.getUserBooksFromRepo(userId);

        return bookListOps.get();
    }

    public void saveBook(Book book, String userId){
            if(!bookRepo.checkIfBookExists(book)){
                bookRepo.insertBookNotExist(book, userId);

            System.out.println("book is inserted into database");
            }
        }


        public Optional<String> deleteBookById(String bookId, String userId) {
            return bookRepo.deleteBookFromUserId(bookId, userId);
        } 

    }
    

