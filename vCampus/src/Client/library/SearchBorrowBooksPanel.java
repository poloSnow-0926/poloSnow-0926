package Client.library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import Client.main.App;
import Client.messageQueue.ResponseQueue;
import net.Request;
import net.Response;
import Client.main.App;

// 查找和借阅图书的面板
class SearchBorrowBooksPanel extends JPanel {
    private Student student;
    private JTextField searchField;
    private JTextArea resultArea;
    private JList<Book> bookList; // 新增：用于显示结果的列表

    public SearchBorrowBooksPanel(Student student) {
        this.student = student;

        setLayout(new BorderLayout());
        
        // 设置统一的字体（微软雅黑）
        Font font = new Font("微软雅黑", Font.PLAIN, 14);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TextArea.font", font);

        // 顶部的输入栏和搜索按钮
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel("题名");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");

        // 搜索按钮事件监听
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });

        topPanel.add(label);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // 中间的列表区域显示搜索结果
        bookList = new JList<>();
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选模式
        JScrollPane scrollPane = new JScrollPane(bookList);

        // 底部的借阅按钮
        JButton borrowButton = new JButton("借阅");
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowSelectedBook();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(borrowButton, BorderLayout.SOUTH);
    }

    // 搜索图书
    private void searchBooks() {
        String title = searchField.getText().trim();
        
        // 构造Request，发送给服务器进行搜索
        Request request = new Request("library.searchBooks", new Object[]{title});
        App.requestQueue.add(request);

        // 等待服务器响应
        Response response = ResponseQueue.getResponse(request);

        if (response != null) {
            List<Book> books = (List<Book>) response.getData(); // 假设服务端返回一个图书列表
            bookList.setListData(books.toArray(new Book[0])); // 显示在列表中
        } else {
            JOptionPane.showMessageDialog(this, "搜索失败，请重试！");
        }
    }

    // 借阅选中的图书
    private void borrowSelectedBook() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "请先选择一本书！");
            return;
        }

        // 构造Request，发送借阅请求给服务器
        Request request = new Request("library.borrowBook", new Object[]{student.getStudentId(), selectedBook.getId()});
        App.requestQueue.add(request);

        // 等待服务器响应
        Response response = ResponseQueue.getResponse(request);

        if (response != null && (boolean) response.getData()) {
            JOptionPane.showMessageDialog(this, "借阅成功！");
        } else {
            JOptionPane.showMessageDialog(this, "借阅失败！");
        }
    }
}