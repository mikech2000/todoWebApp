package edu.utdallas.se4352.aswDemo;

import java.util.List;

public interface TodoItemDAO
{
	List<TodoItem> getTodos(boolean showCompleted) throws Exception;
	TodoItem addItem(TodoItem item) throws Exception;
	TodoItem getItem(Long id) throws Exception;
	int saveItem(TodoItem item) throws Exception;
}
