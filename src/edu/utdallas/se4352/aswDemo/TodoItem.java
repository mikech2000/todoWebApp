package edu.utdallas.se4352.aswDemo;

import java.util.Date;



public class TodoItem
{
	private Long num;
	private Date date;
	private String title;
	private String text;
	private Boolean completed;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public Long getNum()
    {
	    return num;
    }

	public void setNum(Long num)
    {
	    this.num = num;
    }
}
