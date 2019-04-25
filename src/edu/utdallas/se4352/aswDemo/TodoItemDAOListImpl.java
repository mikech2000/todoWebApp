package edu.utdallas.se4352.aswDemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class TodoItemDAOListImpl implements TodoItemDAO
{
	private static final Logger logger = Logger.getLogger(TodoItemDAOListImpl.class);
	private List<TodoItem> items = new ArrayList<TodoItem>();

	public TodoItemDAOListImpl()
	{
		items = buildTodos();
	}
	
	@Override
    public TodoItem addItem(TodoItem item) throws Exception
    {
		if(item.getNum() != null) {
			throw new Exception("Only New Items can be added");
		}
		item.setNum(nextKey());
		items.add(item);
		return item;
    }

	@Override
    public List<TodoItem> getTodos(boolean hideCompleted)
    {
	    List<TodoItem> result = new ArrayList<>();
	    for(TodoItem item : items) {
	    	if(hideCompleted && item.isCompleted()) continue;
	    	result.add(item);
	    }
	    return result;
    }

	@Override
    public TodoItem getItem(Long id)
    {
	    for(TodoItem item : items) {
	    	if(item.getNum() == id) return item;
	    }
	    return null;
    }

	@Override
    public int saveItem(TodoItem item) throws Exception
    {
		if(item.getNum() == null) {
			throw new Exception("Only Added Items can be saved");
		}
		items.add(item);
	    return 1;
    }
	
	private long nextKey()
	{
		long nextKey = 0;
		for(TodoItem item : items) {
			nextKey = (item.getNum() >= nextKey) ? item.getNum() + 1 : nextKey;
		}
		return nextKey;
	}

	private List<TodoItem> buildTodos()
	{
		logger.debug("BuildTodo Run");
		List<TodoItem> todos = new ArrayList<>();
		for (long idx = 0; idx < 10; idx++) {
			TodoItem newitem = new TodoItem();
			newitem.setNum(idx);
			newitem.setDate(new Date());
			newitem.setTitle("Item " + idx);
			newitem.setText("This is the text describing the todo");
			newitem.setCompleted(false);
			todos.add(newitem);
		}
		return todos;
	}

}
