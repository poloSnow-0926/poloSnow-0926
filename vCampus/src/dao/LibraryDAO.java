package dao;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Client.library.*;

public class LibraryDAO {
	private Connection connect() {
        String url = "jdbc:sqlite:library.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return conn;
    }

    public List<Book> searchBooks(String title) {
        String sql = "SELECT * FROM Books WHERE title LIKE ?";
        List<Book> books = new ArrayList<>();

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + title + "%");
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                books.add(new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getInt("available") == 1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    public boolean borrowBook(int studentId, int bookId) {
        String sqlUpdate = "UPDATE Books SET available = 0 WHERE book_id = ?";
        String sqlInsert = "INSERT INTO BorrowedBooks(student_id, book_id, borrow_date) VALUES(?, ?, datetime('now'))";

        try (Connection conn = this.connect();
             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
             PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
            
            pstmtUpdate.setInt(1, bookId);
            pstmtInsert.setInt(1, studentId);
            pstmtInsert.setInt(2, bookId);

            pstmtUpdate.executeUpdate();
            pstmtInsert.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Book> getBorrowedBooks(int studentId) {
        String sql = "SELECT b.book_id, b.title, b.author, b.available " +
                     "FROM BorrowedBooks bb JOIN Books b ON bb.book_id = b.book_id " +
                     "WHERE bb.student_id = ? AND bb.return_date IS NULL";

        List<Book> books = new ArrayList<>();

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                books.add(new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getInt("available") == 1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    public boolean returnBook(int studentId, int bookId) {
        String sqlUpdateBook = "UPDATE Books SET available = 1 WHERE book_id = ?";
        String sqlUpdateBorrowed = "UPDATE BorrowedBooks SET return_date = datetime('now') WHERE student_id = ? AND book_id = ? AND return_date IS NULL";

        try (Connection conn = this.connect();
             PreparedStatement pstmtUpdateBook = conn.prepareStatement(sqlUpdateBook);
             PreparedStatement pstmtUpdateBorrowed = conn.prepareStatement(sqlUpdateBorrowed)) {
            
            pstmtUpdateBook.setInt(1, bookId);
            pstmtUpdateBorrowed.setInt(1, studentId);
            pstmtUpdateBorrowed.setInt(2, bookId);

            pstmtUpdateBook.executeUpdate();
            pstmtUpdateBorrowed.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
