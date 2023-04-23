package ssf.miniproject.booklisttracker.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Book implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Book.class);

    private static final long serialVersionUID = 1L;


    private String id;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    private List<String> authors;
    private String authors2;


    public String getAuthors2() {
        return authors2;
    }

    public void setAuthors2(String authors2) {
        this.authors2 = authors2;
    }


    private String publishedDate;
    private String urlLink;
    private String imageUrl;
    private String previewLink;

    public Book(){
    }

    public Book(String id, String title, String description,  List<String>authors, String publishedDate, String urlLink, String imageUrl, String previewLink){
        this.id = id;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.urlLink = urlLink;
        this.imageUrl = imageUrl;
        this.previewLink = previewLink;
    }

    public Book(String id, String title, String description, String authors2, String publishedDate, String urlLink, String imageUrl, String previewLink){
        this.id = id;
        this.title = title;
        this.description = description;
        this.authors2 = authors2;
        this.publishedDate = publishedDate;
        this.urlLink = urlLink;
        this.imageUrl = imageUrl;
        this.previewLink = previewLink;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }
    
    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }


    public static List<Book> createJson(String json) throws IOException{
        List<Book> bookList = new LinkedList<>();

        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject obj = reader.readObject();
            JsonArray itemsArr = obj.getJsonArray("items");
            for(int i = 0; i < itemsArr.size(); i++){
                JsonObject booksObject = itemsArr.getJsonObject(i);
                Book book = createSingleBookJson(booksObject);
                boolean answer = Stream.of(book).allMatch(Objects::nonNull);
    
                if(answer == true){
                    bookList.add(book);
                }

            }
        }

         return bookList;
    }

    // public static Book createSingleBook(String json) {

    // }


    public static Book createSingleBookJson(JsonObject booksObject){
        Book book = new Book();

        //logger.info("asdfasdfa " + volumeObject.getJsonString("description"));
        book.setId(booksObject.getString("id"));
        logger.info("book id >>> " + book.getId());

        JsonObject volumeObject = booksObject.getJsonObject("volumeInfo");
        logger.info("type >>> " + volumeObject );

        book.setTitle(volumeObject.getString("title"));
        logger.info("book's title >>> " + book.getTitle());
        
        book.description = (volumeObject.getJsonString("description") == null) ? null : volumeObject.getString("description");
        logger.info("book's description >>> " + book.getDescription());
        
        book.publishedDate = (volumeObject.getJsonString("publishedDate") == null) ? null : volumeObject.getString("publishedDate");
        logger.info("book's published date >>> " + book.getPublishedDate());
        
        JsonObject imageLinks = (volumeObject.getJsonObject("imageLinks") == null) ? null :  volumeObject.getJsonObject("imageLinks");
        book.imageUrl = (imageLinks == null || imageLinks.getJsonString("thumbnail") == null) ? null : (imageLinks.getString("thumbnail"));
        logger.info("book's imageurl >>> " + book.getImageUrl());
        
        book.urlLink = (volumeObject.getJsonString("infoLink") == null) ? null : volumeObject.getString("infoLink");
        logger.info("book's url link >>> " + book.getUrlLink());
        
        book.previewLink = (volumeObject.getJsonString("previewLink") == null) ? null : volumeObject.getString("previewLink");
        logger.info("book's preview link >>>> " + book.getPreviewLink());
        
        if (volumeObject.getJsonArray("authors") != null){
        JsonArray authorsArray = volumeObject.getJsonArray("authors");
        List<String> authorsArrayList = new LinkedList<>();
                if (authorsArray.size() != 0) {
                    for (int j = 0; j < authorsArray.size(); j++) {
                        authorsArrayList.add(authorsArray.getString(j));
                    }
                }
                
        book.setAuthors(authorsArrayList);
        logger.info("book's authors >>>> " + book.getAuthors());

            } else {
                book.setAuthors(null);    
        }
        return book;
    }  


    public static Book createSQL(SqlRowSet rs){
        Book book = new Book();
        book.setId(rs.getString("bookId"));
        book.setTitle(rs.getString("title"));
        book.setDescription(rs.getString("description"));
        
        book.setPublishedDate(rs.getString("publishedDate"));
        book.setUrlLink(rs.getString("urlLink"));
        book.setPreviewLink(rs.getString("previewLink"));

        //book.setAuthors(rs.getString("authors")));

        return book;



    }
} 

