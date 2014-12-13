package lab02_han;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DataBaseHandler 
{
	private static Connection connection;
	private static Statement statement;
	
	public DataBaseHandler() throws ClassNotFoundException, SQLException
	{
		connection=getConnection();
		statement = connection.createStatement();
	}
	
	//建立数据库连接
	private static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Driver loader");
		
		Connection connection = DriverManager.getConnection
				("jdbc:mysql://localhost/java", "root", "user");
		System.out.println("Database connected");
		
		return connection;
	}
	
	//执行查询语句
	private static ResultSet exeQuery(String query) throws SQLException, ClassNotFoundException
	{
		if (connection == null)
		{
			connection = getConnection();
			statement = connection.createStatement();
		}
		
		
		return statement.executeQuery(query);
	}
	
	private static void exeUpdateQuery(String query) throws SQLException, ClassNotFoundException
	{
		if (connection == null)
		{
			connection = getConnection();
			statement = connection.createStatement();
		}
		//statement.executeQuery(query);
		statement.executeUpdate(query);
	}
	
	//验证用户名
	public static boolean isAuthorized(String name, String password) throws ClassNotFoundException
	{
		String query = "select * from userinfo where name='"+name+"' and password='"+password+"' ;";
		try
		{
			ResultSet rs = exeQuery(query);
			return rs.next();
		}catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	//用户注册
	//注册成功返回1，用户名已存在返回0，数据库操作失败返回-1
	public static int regUser(String name , String password) throws ClassNotFoundException
	{
		
		String query1 = "select count(*) from userinfo where name='"+name+"';";
		String query2 = "insert into userinfo values('"+name+"','"+password+"');";
		//String query2 = "insert into userinfo values('tom','123');";
		
		ResultSet rs;
		try
		{
			rs = exeQuery(query1);
			rs.next();
			if (rs.getInt(1)>0)//用户名已存在
			{
				return 0;
			}
			else //注册成功
			{
				exeUpdateQuery(query2);
				return 1;
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		
		
	}
	
	//获取userinfo总用户数
	public static int getUsersNum() throws ClassNotFoundException
	{
		String query = "select count(*) from userinfo";
		try
		{
			ResultSet rs = exeQuery(query);
			rs.next();
			return rs.getInt(1);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	//获取userinfo中的所有用户名
	public static List getUsersName() throws ClassNotFoundException
	{
		String q = "select name from userinfo";
		List<String> names = new ArrayList<String>();
		try
		{
			ResultSet rs = exeQuery(q);
			while(rs.next())
			{
				names.add(rs.getString(1));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return names;
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{
			DataBaseHandler db = new DataBaseHandler();
			
			/*
			String q = "select * from Employees";

			ResultSet resultSet = DataBaseHandler.exeQuery(q);
			
			
			while (resultSet.next())
			{
				System.out.println(resultSet.getString(1)+"\t" +
					resultSet.getString(2)+"\t"+resultSet.getString(3));
			}
			*/
			//System.out.println(DataBaseHandler.regUser("han", "123"));
			//System.out.println(DataBaseHandler.regUser("Bob", "123"));
			//System.out.println(DataBaseHandler.getUsersNum());
			List<String> names = db.getUsersName();
			for (String str:names)
			{
				System.out.println(str);
			}
			
				

	}
}