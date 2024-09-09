package Client.library;
import javax.swing.*;

import Client.main.App;
import Client.messageQueue.ResponseQueue;
import net.Request;
import net.Response;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Student student;

    public MainFrame(Student student) {
        this.student = student;
        
        // 设置窗口标题和尺寸
        setTitle("图书馆系统");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 居中显示

        // 创建按钮
        JButton searchBorrowButton = new JButton("查找图书");
        JButton viewReturnButton = new JButton("我的借阅");

        // 设置按钮的事件监听
        searchBorrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchBorrowBooksPanel searchBorrowPanel = new SearchBorrowBooksPanel(student, libraryDAO);
                showPanel(searchBorrowPanel);
            }
        });

        viewReturnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewReturnBooksPanel viewReturnPanel = new ViewReturnBooksPanel(student, libraryDAO);
                showPanel(viewReturnPanel);
            }
        });

        // 布局设置
        setLayout(new GridLayout(2, 1, 10, 10));
        add(searchBorrowButton);
        add(viewReturnButton);
    }

    // 显示不同的面板
    private void showPanel(JPanel panel) {
        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // 居中显示
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Student student = new Student(1, "李华");
        MainFrame mainFrame = new MainFrame(student);
        mainFrame.setVisible(true);
    }
}