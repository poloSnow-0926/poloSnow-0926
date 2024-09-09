package server.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import net.RequestListener;
import server.messageQueue.RequestHandler;
import server.messageQueue.RequestQueue;
import util.ServerUtils;

public class App extends JFrame {

	private JPanel contentPane;

	private RequestListener requestListener; // 请求监听器
	public static RequestQueue requestQueue; // 服务器端全局请求消息队列
	public static RequestHandler requestHandler; // 请求处理器
	public static Connection connection; // SQLite数据库连接

	public static JTextPane paneLog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {

		setResizable(false);
		setTitle("服务器端 - VCampus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 718, 493);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);

		JLabel label = new JLabel("服务器日志");
		label.setBounds(302, 13, 75, 18);
		panel_1.add(label);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 38, 662, 385);
		panel_1.add(scrollPane);
		App.paneLog = new JTextPane();
		App.paneLog.setEditable(false);
		scrollPane.setViewportView(paneLog);
		App.paneLog.setFont(new Font("宋体", Font.PLAIN, 14));

		/**
		 * 新增部分
		 */
		// 初始化全局消息队列
		App.requestQueue = RequestQueue.getInstance();

		// 连接到SQLite数据库
		String url = "jdbc:sqlite:vcampus.db"; // 假设数据库文件名为vcampus.db
		try {
			connection = DriverManager.getConnection(url);
			App.paneLog.setText("数据库连接成功！");
		} catch (SQLException e) {
			App.paneLog.setText("严重错误！数据库连接失败！" + e.toString());
			e.printStackTrace();
		}

		// 测试查询数据库
		try {
			String sql = "SELECT COUNT(*) FROM students"; // 假设有一张表students
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				App.paneLog.setText(App.paneLog.getText() + "\n" + "学生表记录数: " + rs.getInt(1));
			}
		} catch (SQLException e) {
			App.paneLog.setText("严重错误！数据库查询失败！请检查表结构或配置！");
			e.printStackTrace();
		}

		// 启动服务器端侦听
		requestListener = new RequestListener(Integer.parseInt(ServerUtils.getMainPort()));
		requestListener.start();

		// 启动请求处理器
		App.requestHandler = new RequestHandler();
		App.requestHandler.start();
		App.paneLog.setText(
				paneLog.getText() + (paneLog.getText().equals("") ? "" : "\n") + "开始服务器端侦听...端口=" + ServerUtils.getMainPort());

	}

	public static void appendLog(String msg) {
		App.paneLog.setText(App.paneLog.getText() + "\n" + msg);
	}
}