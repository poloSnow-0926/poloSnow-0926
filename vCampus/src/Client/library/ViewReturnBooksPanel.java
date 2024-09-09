package Client.library;
import javax.swing.*;

import dao.LibraryDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

//查看和归还图书的面板
class ViewReturnBooksPanel extends JPanel {
 private Student student;
 private LibraryDAO libraryDAO;
 private JTextArea resultArea;

 public ViewReturnBooksPanel(Student student, LibraryDAO libraryDAO) {
     this.student = student;
     this.libraryDAO = libraryDAO;

     setLayout(new BorderLayout());

     // 中间的文本区域显示借阅的书籍
     resultArea = new JTextArea(10, 40);
     resultArea.setEditable(false);
     JScrollPane scrollPane = new JScrollPane(resultArea);

     // 顶部的按钮来刷新借阅的书籍
     JButton refreshButton = new JButton("更新已借图书");
     refreshButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             viewBorrowedBooks();
         }
     });

     // 底部的归还按钮
     JButton returnButton = new JButton("归还图书");
     returnButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             returnBook();
         }
     });

     add(refreshButton, BorderLayout.NORTH);
     add(scrollPane, BorderLayout.CENTER);
     add(returnButton, BorderLayout.SOUTH);

     // 初始化显示借阅的书籍
     viewBorrowedBooks();
 }

 private void viewBorrowedBooks() {
     List<Book> borrowedBooks = libraryDAO.getBorrowedBooks(student.studentId);
     resultArea.setText("");
     for (Book book : borrowedBooks) {
         resultArea.append(book.toString() + "\n");
     }
 }

 private void returnBook() {
     try {
         String bookIdString = JOptionPane.showInputDialog("Enter the Book ID to return:");
         int bookId = Integer.parseInt(bookIdString);
         boolean success = libraryDAO.returnBook(student.studentId, bookId);
         if (success) {
             JOptionPane.showMessageDialog(this, "Book returned successfully!");
             viewBorrowedBooks(); // 更新借阅书籍列表
         } else {
             JOptionPane.showMessageDialog(this, "Failed to return book.");
         }
     } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this, "Invalid Book ID.");
     }
 }
}
