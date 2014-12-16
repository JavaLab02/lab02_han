package lab02_han;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBWordInfo {
	private Connection connection;
	private Statement searchStatement;
	
	private Statement insertStatement;
	private PreparedStatement UpdatePreStatement;
	private PreparedStatement InsertPreStatement;

	public DBWordInfo(){
		try {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java","root","user");	
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String Search(String word){
		try {
			ResultSet resultset;
			searchStatement = connection.createStatement();
			String sql = "select * from WordInfo where word = '"+word+"';";
			resultset = searchStatement.executeQuery(sql);
			String wordInfo = "";
			while(resultset.next()){
				wordInfo = resultset.getInt(2)+"&"+resultset.getInt(3)+"&"+resultset.getInt(4)+wordInfo;
				return wordInfo;
			}
			return "0&0&0";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0&0&0";
	}
	
	public boolean Update(String word,int zanBaidu,int zanYoudao,int zanBing){
		try {
			insertStatement = connection.createStatement();
			String sql1 = "select * from WordInfo where word = '"+word+"';";
			ResultSet resultset = insertStatement.executeQuery(sql1);
			if(resultset.next()){
				int zanbai = 0;
				int zanyou = 0;
				int zanbing = 0;
				zanbai = resultset.getInt(2);
				zanyou = resultset.getInt(3);
				zanbing = resultset.getInt(4);
				
				zanbai = zanbai + zanBaidu;
				zanyou = zanyou + zanYoudao;
				zanbing = zanbing + zanBing;
				String sql2 = "update WordInfo set zanBaidu = ?,zanYoudao = ?,zanBing = ? "+"where word = ?;";
				UpdatePreStatement = connection.prepareStatement(sql2);
				UpdatePreStatement.setString(4, word);
				UpdatePreStatement.setInt(1, zanbai);
				UpdatePreStatement.setInt(2, zanyou);
				UpdatePreStatement.setInt(3, zanbing);
				UpdatePreStatement.executeUpdate();
			}else{
				String insertSQL = "insert into WordInfo(word,zanBaidu,zanYoudao,zanBing)"+" values(?,?,?,?)";
				InsertPreStatement = connection.prepareStatement(insertSQL);
				InsertPreStatement.setString(1, word);
				InsertPreStatement.setInt(2,zanBaidu);
				InsertPreStatement.setInt(3,zanYoudao);
				InsertPreStatement.setInt(4,zanBing);
				InsertPreStatement.executeUpdate();
			}
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
