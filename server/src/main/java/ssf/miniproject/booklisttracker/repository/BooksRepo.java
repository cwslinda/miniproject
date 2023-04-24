package ssf.miniproject.booklisttracker.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ssf.miniproject.booklisttracker.model.Book;
import static ssf.miniproject.booklisttracker.repository.Queries.*;

@Repository
public class BooksRepo {
    private static final Logger logger = LoggerFactory.getLogger(BooksRepo.class);
  
    @Autowired
    private JdbcTemplate template;

    public Optional<List<Book>> getUserBooksFromRepo(String userId){


        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_BOOKS_BY_USERID, userId);

        final List<Book> bookList = new LinkedList<>();
        while(rs.next()){
            bookList.add(Book.createSQL(rs));
        }

        return Optional.of(bookList);

    }

    public boolean insertBookNotExist(Book book, String userId){
         int inserted = template.update(SQL_INSERT_BOOK, book.getId(), book.getTitle(), book.getDescription(), book.getAuthors(), book.getPublishedDate(), book.getPreviewLink(), book.getUrlLink(), book.getImageUrl(), userId);

        return inserted > 0;

    }


    public boolean checkIfBookExists(Book book){
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_BOOK_BY_BOOKID, book.getId());


        return rs.next();
    }

    public Optional<String> deleteBookFromUserId(String bookId, String userId){
        System.out.println("deletion progress: " + "%s".formatted(bookId) + "being deleted from %s".formatted(userId));

        int result = template.update(SQL_DELETE_BOOK_BY_ID_AND_BOOK_ID, bookId, userId);

        if(result == 1 ){
            String msg = "Book is deleted";
            return Optional.of(msg);


        }

        String msg = "Book is unable to be deleted";
        return Optional.of(msg);
    }

    
}

