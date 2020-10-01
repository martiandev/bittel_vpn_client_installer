package Installer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Setup 
{
	
	Command installOpenVPN;
	Command copyVPNClientToHome;
	Command copyVPNStarter;
	Command createVPNSysLink;
	Command copyVPNService;
	Command reloadDaemon;
	Command enable;
	Command startService;
	Command reboot;
	
	
	Command changeDBScriptAccess;
	Command addCrontab;
	
	
	public Setup()
	{
		
	}
	public void start()
	{
		try
		{
			System.out.println("==================");
			System.out.println("Starting setup");
			System.out.println("==================");

			System.out.println("------------------");
			System.out.println("Step 01");
			installOpenVPN();
			System.out.println("------------------");
			
			System.out.println("------------------");
			System.out.println("Step 02");
			createStarterScript() ;
			System.out.println("------------------");
			
			System.out.println("------------------");
			System.out.println("Step 03");
			copyVPNClientToHome();
			System.out.println("------------------");
			
			System.out.println("------------------");
			System.out.println("Step 04");
			copyVPNStarter();
			System.out.println("------------------");

			System.out.println("------------------");
			System.out.println("Step 05");
			copyVPNService();
			System.out.println("------------------");

			System.out.println("Step 06");
			System.out.println("------------------");
			createVPNSysLink();
			System.out.println("------------------");

			System.out.println("Step 07");
			System.out.println("------------------");
			reloadDaemon();
			System.out.println("------------------");

			System.out.println("Step 08");
			System.out.println("------------------");			
			enable();
			System.out.println("------------------");

			System.out.println("Step 09");
			System.out.println("------------------");
			startService();
			System.out.println("------------------");

			System.out.println("Step 10");
			System.out.println("------------------");
			createDBScript();
			System.out.println("------------------");

			System.out.println("Step 11");
			System.out.println("------------------");
			addCrontab();
			System.out.println("------------------");

			System.out.println("Done and rebooting");
			System.out.println("==================");
			reboot();

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void installOpenVPN() throws Exception
	{
		System.out.println("Install OPENVPN");
		installOpenVPN = new Command("sudo apt-get install -y openvpn");
		CommandExecutor.get().execute(installOpenVPN);	
	}
	
	public void createStarterScript() throws Exception
	{
		System.out.println("Creating Starter Script");
		FileReader fileReader = new FileReader(System.getProperty("user.dir")+System.getProperty("file.separator")+"vpnstart.sh"); 
		BufferedReader br = new BufferedReader(fileReader); 
		String content = null;
		String home = null;
	    InputStreamReader ir = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(ir); 
		System.out.println("Enter Home Directory:");
		home = reader.readLine();
		StringBuffer sb = new StringBuffer(); 
		String line;  
		while((line=br.readLine())!=null)  
		{  
			sb.append(line);  
			sb.append("\n"); 
		}  
		content =sb.toString();
		fileReader.close();
		content = content.replace("$HOME",home);
		
		System.out.println(content);
		
		FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"vpnstart.sh"));
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] bytes = content.getBytes();
		bos.write(bytes);
        bos.close();
        fos.close();
	}
	
	
	public void copyVPNClientToHome() throws Exception
	{
		System.out.println("Copy VPN Client to local machine");
		copyVPNClientToHome = new Command("cp client.ovpn ~/");
		CommandExecutor.get().execute(copyVPNClientToHome);
	}
	
	public void copyVPNStarter() throws Exception
	{
		System.out.println("Copying starter script");
		copyVPNStarter = new Command("cp vpnstart.sh /usr/sbin/");
		CommandExecutor.get().execute(copyVPNStarter);
	}
	

	public void copyVPNService() throws Exception
	{
		System.out.println("Copying service");
		copyVPNService = new Command("cp openvpn_starter.service /lib/systemd/system/");
		CommandExecutor.get().execute(copyVPNService);
	}
	public void createVPNSysLink() throws Exception
	{
		System.out.println("Creating systemlink");
		createVPNSysLink = new Command("sudo ln -s /lib/systemd/system/openvpn_starter.service openvpn_starter.service");
		CommandExecutor.get().execute(createVPNSysLink);
	}
	public void reloadDaemon() throws Exception
	{
		System.out.println("Reloading Daemon");
		reloadDaemon = new Command("sudo systemctl daemon-reload");
		CommandExecutor.get().execute(reloadDaemon);
	}
	public void enable() throws Exception
	{
		System.out.println("Enabling Service");
		enable = new Command("sudo systemctl enable openvpn_starter.service");
		CommandExecutor.get().execute(enable);
	}
	public void startService() throws Exception
	{
		System.out.println("Starting Service");
		startService = new Command("sudo systemctl start openvpn_starter.service");
		CommandExecutor.get().execute(startService);
	}
	
	
	
	public void createDBScript() throws Exception
	{
		System.out.println("Creating db script");
		FileReader fileReader = new FileReader(System.getProperty("user.dir")+System.getProperty("file.separator")+"dbback.sh"); 
		BufferedReader br = new BufferedReader(fileReader); 
		String content = null;
		
		String prefix 	= null;
		String home 	= null;
		String folder 	= null;
		String schema 	= null;
		
	    InputStreamReader ir = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(ir); 
		
		System.out.println("Enter File Prefix:");
		prefix = reader.readLine();
		
		System.out.println("Enter Home Directory:");
		home = reader.readLine();
		
		
		System.out.println("Enter Remote Folder:");
		folder = reader.readLine();
		
		System.out.println("Enter Schema to backup:");
		schema = reader.readLine();
		
		StringBuffer sb=new StringBuffer(); 
		String line;  
		while((line=br.readLine())!=null)  
		{  
			sb.append(line);      //appends line to string buffer  
			sb.append("\n");     //line feed   
		}  
		content =sb.toString();
		fileReader.close();
		content = content.replace("$HOME",home);
		content = content.replace("$PREFIX",prefix);
		content = content.replace("$FOLDER",folder);
		content = content.replace("$SCHEMA",schema);
		
		System.out.println(content);
		
		FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"dbback.sh"));
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] bytes = content.getBytes();
		bos.write(bytes);
        bos.close();
        fos.close();
        changeDBScriptAccess = new Command("sudo chmod 777 "+System.getProperty("user.dir")+System.getProperty("file.separator")+"dbback.sh");
        CommandExecutor.get().execute(changeDBScriptAccess);
	}
	

	public void addCrontab() throws Exception
	{
		System.out.println("Adding cron job");
		System.out.println("Enter Home Directory:");
	    InputStreamReader ir = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(ir); 
		String home = reader.readLine();
		File f = new File("/var/spool/cron/crontabs/root");
		StringBuffer sb=new StringBuffer(); 
		String line;
		FileReader fileReader = new FileReader(f.getAbsolutePath()); 
		BufferedReader br = new BufferedReader(fileReader);
		while((line=br.readLine())!=null)  
		{  
			sb.append(line);
			sb.append("\n");
		}  
		String content =sb.toString();
		fileReader.close();
		content = content+"\n"+"* * * * * cd "+home+"/installer/ && ./dbback.sh";
		FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] bytes = content.getBytes();
		bos.write(bytes);
        bos.close();
        fos.close();
		
	}
	
	public void reboot() throws Exception
	{
		System.out.println("Rebooting");
		reboot = new Command("reboot");
		CommandExecutor.get().execute(reboot);	
	}
	
}
