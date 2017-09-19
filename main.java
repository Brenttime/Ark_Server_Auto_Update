import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*Lets auto update an ark_server*/
public class main 
{
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		/*Easy JFrame in order to make it legible to a non-technical (Just Alert Boxes)*/
		JFrame frame = new JFrame();
		String line;
		
		//Specific to ark (args maybe?)
		String gameExecutable = "ShooterGameServer.exe";
		//ark file path (args call)
		String filePath = "C:\\arkserver\\ShooterGame\\Binaries\\Win64\\";
		//Main dir for ark (args call)
		String directory = "C:\\arkserver\\";
		//Steam dir for steamcmd.exe (args call)
		String steamDir = "C:\\steam\\steamcmd.exe";
		
		//Look to see if the arkserver is already running
		killProcess(gameExecutable);
		
		JOptionPane.showMessageDialog(frame, "Starting to update....");
		System.out.println("Starting to update....");
		
		try 
		{
			ProcessBuilder builder = new ProcessBuilder(steamDir);
			builder.redirectErrorStream(true);
			Process p = builder.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			
			TimeUnit.SECONDS.sleep(5); //Let steamcmd do its things
			writer.write("login anonymous\n");
			writer.flush();
			
			TimeUnit.SECONDS.sleep(5); //Let steamcmd do its things
			writer.write("force_install_dir \" " + directory + " \" \n "); //Directory reads as force.... "C:\\....\\.exe"; therfore \" is called for
			writer.flush();
			
			TimeUnit.SECONDS.sleep(5); //Let steamcmd do its things
			writer.write("app_update 376030\n");
			writer.flush();

			while (!(line = reader.readLine()).contains("Success!")) {
			    while (line != null && ! line.trim().equals("--EOF--")) 
			    {
			        System.out.println ("Stdout: " + line);
			        line = reader.readLine();
					if(line.contains("Success!"))
					{
						line = null;
					}
			    }
			    if (line == null)
			    {
			        break;
			    }
			    
			}
		
			//Close up
			p.destroy();
			reader.close();
			writer.close();
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Now start ark
		startGame(filePath);
		JOptionPane.showMessageDialog(frame, "Update Complete....");
		System.out.println("Update Complete....");
		System.out.println("Ark is Now Running...");
	}
	
	/*start game once everything is handled */
	private static void startGame(String filePath) throws IOException
	{
		//Don't Destroy process! This is supposed to start with no kill
		Process p = Runtime.getRuntime().exec(filePath + "run.bat");
	}
	
	/*look for and kill the game process */
	private static void killProcess(String executable)
	{
		String currentLine;
		try {
		    Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
		    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

		    while((currentLine = input.readLine()) != null)
		    {
			    if(currentLine.contains(executable))
			    {
			    	System.out.println("found the exe: killing now");
			    	Runtime.getRuntime().exec("taskkill /F /IM " + executable);
			    }
		    }
		    
		    input.close();
		    
		} 
		catch (Exception err) 
		{
		    err.printStackTrace();
		}
	}
}
