package ssf.miniproject.booklisttracker.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.miniproject.booklisttracker.model.Comment;
import ssf.miniproject.booklisttracker.model.User;
import ssf.miniproject.booklisttracker.repository.CommentRepo;
import ssf.miniproject.booklisttracker.repository.UserRepo;

@Service
public class CommentService {

    private Logger logger = Logger.getLogger(CommentService.class.getName());

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    public List<Comment> getComments(String bookId) {

		// Find the bookId
		List<Comment> results = commentRepo.getComments(bookId);

        List<Comment> commentsList = new LinkedList<>();

        for(Comment c: results){

            Comment comment = new Comment();
            comment.setBookId(c.getBookId());
            comment.setComment(c.getComment());
            comment.setCommentDate(c.getCommentDate());
            comment.setUserId(c.getUserId());
            comment.setCommentId(c.getCommentId());
            comment.setCommentTitle(c.getCommentTitle());
			comment.setBookTitle(c.getBookTitle());

            
            commentsList.add(c);
            


        }
		

		return commentsList;
	}

	public List<Comment> getCommentsFromId(String userId) {

		// Find the bookId
		List<Comment> results = commentRepo.getCommentsFromId(userId);

        List<Comment> commentsList = new LinkedList<>();

        for(Comment c: results){

            Comment comment = new Comment();
            comment.setBookId(c.getBookId());
            comment.setComment(c.getComment());
            comment.setCommentDate(c.getCommentDate());
            comment.setUserId(c.getUserId());
            comment.setCommentId(c.getCommentId());
            comment.setCommentTitle(c.getCommentTitle());
			comment.setBookTitle(c.getBookTitle());
            
            commentsList.add(c);
            
        }
		

		return commentsList;
	}
    
    public Optional<String> createComment(Comment comment) {
        System.out.println("creating in comment service");

		// Check if the user is valid
		Optional<User> opt = userRepo.findUserbyId(comment.getUserId());
		if (opt.isEmpty())
			return Optional.empty();

		// Fill the post with pertinent user details
		User user = opt.get();
		comment.setUsername(user.getUsername());
		comment.setUserId(user.getId());

		// Set the post date
		comment.now();

		// Set a unique post id
		String commentId = UUID.randomUUID().toString().substring(0, 8);
		comment.setCommentId(commentId);


		// Create post in Mongo
		ObjectId objectId = commentRepo.insertPost(comment);

		logger.log(Level.INFO
				, "postId: %s -> _id: %s".formatted(commentId, objectId.toString()));

        System.out.println(comment.getBookId());

		// Setup likes and dislike
		return Optional.of(commentId);
	}


	public Optional<Document> deleteComment(String commentId) {

		System.out.println("delete comment in");
		return commentRepo.deleteComment(commentId);
	} 
}
