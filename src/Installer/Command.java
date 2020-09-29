package Installer;

import java.io.File;

public class Command 
{
	String command;
	File directory;
	
	
	
	
	public File getDirectory() {
		return directory;
	}
	public void setDirectory(File directory) {
		this.directory = directory;
	}
	public Command(String command)
	{
		super();
		this.command = command;
	}
	public String getCommand() 
	{
		return command;
	}
	public void setCommand(String command) 
	{
		this.command = command;
	}
}
