package ssf.miniproject.booklisttracker.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    private String username;
    private String id;
    private String password;
    private String email;
    private List<Book> books = new LinkedList<>();
    
    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public User(){}

    public User(String id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // private String generateId(int numChars) {
    //     Random r = new Random();
    //     StringBuilder sb = new StringBuilder();
    //     while (sb.length() < numChars) {
    //         sb.append(Integer.toHexString(r.nextInt()));
    //     }

    //     return sb.toString().substring(0,numChars);
    // }

    public void saveBook(String bookIdToSave, List<Book> list){
        Book book = new Book();
        for(Book b : list){
            if(String.valueOf(b.getId()).equals(bookIdToSave)){
                book = b;
                for(Book bOne : books){
                    if(bOne.getId() == book.getId()){
                        return;
                    }
                }

                books.add(book);
            }

        }
    }

    public static User createForm(MultiValueMap<String, String> form) {
		User user = new User();
        user.setId(user.getId());
        user.setUsername(form.getFirst("username"));
        user.setPassword(form.getFirst("password"));
        user.setEmail(form.getFirst("email"));
        return user;
		
	}

    public static User create(SqlRowSet rs) {
		User user = new User();
		user.setId(rs.getString("id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		return user;
	}


    public JsonObject toJson() {
		return Json.createObjectBuilder()
			.add("username", username)
            .add("password", password)
            .add("email", email)
			.build();
	}
    
        public void delBook(String bookIdToDel){
            for(Book book: books){
                if(String.valueOf(book.getId()).equals(bookIdToDel)){
                    books.remove(book);
                }
            }
        }
    
    }