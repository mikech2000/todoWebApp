package edu.utdallas.se4352.aswDemo;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/todoApp")
public class TodoApplicationRestService
{
	private static final Logger logger = Logger.getLogger(TodoApplicationRestService.class);
	private TodoItemDAO todoItemDAO;

	public TodoApplicationRestService()
	{
		logger.debug("SimpleRestService Created");

		todoItemDAO = new TodoItemDaoDbImpl();
	}

	@GET
	@Path("/getAllItems")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllItems(@QueryParam("hideCompleted") String hideCompletedStr) throws Exception
	{
		logger.debug("getAllItems: hide completed " + hideCompletedStr);

		boolean hideCompleted = Boolean.parseBoolean(hideCompletedStr);
		String response = null;
		Gson gson = new Gson();
		List<TodoItem> items = todoItemDAO.getTodos(hideCompleted);
		response = gson.toJson(items);

		return response;
	}

	@POST
	@Path("/addItem")
	@Produces(MediaType.TEXT_PLAIN)
	public void addItem(String newItemData) throws Exception
	{
		logger.debug("Start addItem");
		logger.debug("data: '" + newItemData + "'");

		Gson gson = new Gson();
		TodoItem newItem = gson.fromJson(newItemData, TodoItem.class);
		newItem.setDate(new Date());
		newItem.setCompleted(false);

		todoItemDAO.addItem(newItem);
	}

	@PUT
	@Path("/completedChanged")
	@Produces(MediaType.TEXT_PLAIN)
	public void completedChanged(String itemNumStr) throws Exception
	{
		logger.debug("Start completedChanged");
		logger.debug("data: '" + itemNumStr + "'");

		long itemNum = Long.parseLong(itemNumStr);
		TodoItem item = todoItemDAO.getItem(itemNum);
		if (item == null) {
			throw new Exception("TodoItem with key not found " + itemNum);
		}

		boolean completed = item.isCompleted();
		item.setCompleted(!completed);

		todoItemDAO.saveItem(item);
	}

	@GET
	@Path("/getServerInfo")
	@Produces(MediaType.TEXT_PLAIN)
	public String getServerInfo() throws Exception
	{
		logger.debug("getServerInfo");

		InetAddress ip = InetAddress.getLocalHost();
		String hostname = ip.getHostName();

		String serverInfo = "Server IP address : " + ip + " Server Hostname : " + hostname;
		logger.debug("ServerInfo " + serverInfo);
		return serverInfo;
	}

}
