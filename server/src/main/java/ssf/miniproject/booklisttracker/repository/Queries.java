package ssf.miniproject.booklisttracker.repository;

public class Queries {

   //user 
   public static final String SQL_INSERT_USER = "insert into users(id, username, password, email) values(?, ?, sha(?), ?)";
   public static final String SQL_VALIDATE_USER = "select count(*) > 0 as valid_cred from users where username = ? and password = sha(?)";
   public static final String SQL_GET_USER_FROM_EMAIL = "select * from user where email like ?";
   public static final String SQL_FIND_BY_USERNAME_PASSWORD = "select * from users where username = ? and password = sha(?)";
   public static final String SQL_FIND_USER_BY_ID = "select * from users where id = ?";

   

   //books
   public static final String SQL_SELECT_BOOKS_BY_USERID = "select * from books where user_id = ?";
   public static final String SQL_INSERT_BOOK = "insert into books(bookId, title, description, authors, publishedDate, urlLink, imageUrl, previewLink, user_id) values (?,?,?,?,?,?,?,?,?)";
   public static final String SQL_SELECT_BOOK_BY_BOOKID = "select * from books where bookId = ?";
   public static final String SQL_DELETE_BOOK_BY_ID_AND_BOOK_ID = "delete from books where bookId = ? and user_id = ?";
}
