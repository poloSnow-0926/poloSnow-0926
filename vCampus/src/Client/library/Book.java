package Client.library;

public class Book {
	 int bookId;
	    String title;
	    String author;
	    boolean available;

	    public Book(int bookId, String title, String author, boolean available) {
	        this.bookId = bookId;
	        this.title = title;
	        this.author = author;
	        this.available = available;
	    }

	    @Override
	    public String toString() {
	        return "Book ID: " + bookId + ", Title: " + title + ", Author: " + author + ", Available: " + available;
	    }
}
