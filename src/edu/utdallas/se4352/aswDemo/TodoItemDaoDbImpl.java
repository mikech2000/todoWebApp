package edu.utdallas.se4352.aswDemo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class TodoItemDaoDbImpl implements TodoItemDAO
{
	private static final Logger logger = Logger.getLogger(TodoItemDaoDbImpl.class);
	
	private DataSource dataSource; 
	
	synchronized private DataSource getDataSource() throws Exception
	{
		if(dataSource == null) {
			dataSource = buildDS();
		}
		return dataSource;
	}
	
	private static final String selectToDo = "select num,date,title,text,completed from todo ";
			
	@Override
	public List<TodoItem> getTodos(boolean hideCompleted) throws Exception
	{
		String selectSQL = (hideCompleted)? selectToDo + " where completed = false" : selectToDo;
		
		DataSource ds = getDataSource();
		Connection connection = ds.getConnection();

		List<TodoItem> result = new ArrayList<>();

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectSQL);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				TodoItem item = new TodoItem();
				item.setNum(rs.getLong("num"));
				item.setDate(rs.getTimestamp("date"));
				item.setTitle(rs.getString("title"));
				item.setText(rs.getString("text"));
				item.setCompleted(rs.getBoolean("completed"));
				
				result.add(item);
			}
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
    
		return result;
	}

	private static final String insertTODO = "insert into todo(date,title,text,completed) values (?,?,?,?);";
	@Override
	public TodoItem addItem(TodoItem item) throws Exception
	{
		if (item.getNum() != null) {
			throw new Exception("Trying to insert Item with NON-NULL NUM");
		}

		DataSource ds = getDataSource();
		Connection connection = ds.getConnection();

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertTODO, Statement.RETURN_GENERATED_KEYS);
			ps.setTimestamp(1, new java.sql.Timestamp(item.getDate().getTime()));
			ps.setString(2, item.getTitle());
			ps.setString(3, item.getText());
			ps.setBoolean(4, item.isCompleted());
			ps.executeUpdate();

			// Copy the assigned ID to the customer instance.
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			item.setNum((long) lastKey);
			return item;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public TodoItem getItem(Long id) throws Exception
	{
		if (id == null) {
			throw new Exception("Trying to retrieve Item with NULL ID");
		}

		DataSource ds = getDataSource();
		Connection connection = ds.getConnection();

		String selectSQL = selectToDo + " where num = ? ";

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectSQL);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				return null;
			}
			
			TodoItem item = new TodoItem();
			item.setNum(rs.getLong("num"));
			item.setDate(rs.getDate("date"));
			item.setTitle(rs.getString("title"));
			item.setText(rs.getString("text"));
			item.setCompleted(rs.getBoolean("completed"));
			return item;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	private static final String updateItem = "update todo set date = ?, title = ?, text = ?, completed = ? where num = ? ";
	
	@Override
	public int saveItem(TodoItem item) throws Exception
	{
		logger.debug("saveItem " + item.isCompleted());
		
		Long id = item.getNum();
		if (id == null) {
			throw new Exception("Trying to update ITEM with NULL ID");
		}

		DataSource ds = getDataSource();
		Connection connection = ds.getConnection();

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateItem);
			ps.setTimestamp(1, new java.sql.Timestamp(item.getDate().getTime()));
			ps.setString(2, item.getTitle());
			ps.setString(3, item.getText());
			ps.setBoolean(4, item.isCompleted());
			ps.setLong(5, id);

			int rows = ps.executeUpdate();
			return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	private DataSource buildDS() throws Exception
	{
		Context initialContext = new InitialContext();
		Context environmentContext = (Context) initialContext.lookup("java:comp/env");
		String dataResourceName = "jdbc/TodoDB";
		DataSource dataSource = (DataSource) environmentContext.lookup(dataResourceName);
		return dataSource;
	}

}
