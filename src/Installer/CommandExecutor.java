package Installer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandExecutor 
{
	//===================================================== Variable =====================================================
	private static CommandExecutor executor;
	//====================================================================================================================
	//=================================================== Constructor ====================================================
	private CommandExecutor()
	{
		
	}
	//====================================================================================================================
	//===================================================== Static =======================================================
	public static CommandExecutor get()
	{
		if(executor == null)
		{
			executor = new CommandExecutor();
		}
		
		return executor;
	}
	//====================================================================================================================
	//===================================================== Method =======================================================
	public synchronized boolean execute(Command command) throws Exception
	{
		boolean result = false;
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", command.command);
		
		if(command.getDirectory()!=null)
		{
			builder.directory(command.getDirectory());
		}
	    builder.redirectErrorStream(true);
	    Process p = builder.start();    
	    int exitCode = p.waitFor();
	    
	    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String line;
	    while (true) 
	    {
	            line = r.readLine();
	            if (line == null) { break; }
	            System.out.println(line);
	    }
	    result =  exitCode == 0;
	    if(result)
	    {
	    	 BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    	 while (true) 
	    	 {
	    	            line = r.readLine();
	    	            if (line == null) { break; }
	    	            System.out.println(line);
	    	 }
	    }
	   return result;
	}
	//====================================================================================================================

	
}
