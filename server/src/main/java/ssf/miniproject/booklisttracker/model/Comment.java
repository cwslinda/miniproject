package ssf.miniproject.booklisttracker.model;

import java.util.Date;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public class Comment {

    private String commentId;
    private Date commentDate;
    private String userId;
    private String bookId;
    private String username;
    private String commentTitle;
    private String comment;
    private String bookTitle;


    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getCommentId() {
        return commentId;
    }
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
    public Date getCommentDate() {
        return commentDate;
    }
    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
    public void now() { this.setCommentDate(new Date()); }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getCommentTitle() {
        return commentTitle;
    }
    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    	public Document toDocument() {
		Document document = new Document();
		document.put("commentId", commentId);
		document.put("commentDate", commentDate);
		document.put("userId", userId);
        document.put("bookId", bookId);
		document.put("username", username);
		document.put("title", commentTitle);
		document.put("comment", comment);
        document.put("bookTitle", bookTitle);
		return document;
	}


    public static Comment create(Document doc) {
		Comment comment = new Comment();
		comment.setCommentId(doc.getString("commentId"));
		comment.setCommentDate(doc.getDate("commentDate"));
		comment.setUserId(doc.getString("userId"));
        comment.setBookId(doc.getString("bookId"));
		comment.setUsername(doc.getString("username"));
		comment.setCommentTitle(doc.getString("title"));
		comment.setComment(doc.getString("comment"));
        comment.setBookTitle(doc.getString("bookTitle"));
	
		return comment;
	}

    // public JsonObject toJSON(){
	// 	return Json.createObjectBuilder()
	// 			.add("commentId", getCommentId())
	// 			.add("commentDate", getCommentDate())
	// 			.add("userId", getUserId())
	// 			.add("bookId", getBookId())
    //             .add("username", getUsername())
    //             .add("commentTitle", getCommentTitle())
    //             .add("comment", getComment())
	// 			.build();

	// }
}
