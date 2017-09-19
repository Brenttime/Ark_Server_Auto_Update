import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class main 
{
	//What EXE you are using
	public static String gameExecutable = "ShooterGameServer.exe";

	public static void main(String[] args) throws InterruptedException, IOException 
	{
		JFrame frame = new JFrame();
		killProcess(gameExecutable);
		JOptionPane.showMessageDialog(frame, "Starting to update....");
		System.out.println("Starting to update....");
		try 
		{
			String line;
			ProcessBuilder builder = new ProcessBuilder("C:\\\\Steam\\\\steamcmd.exe");
			builder.redirectErrorStream(true);
			Process p = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			TimeUnit.SECONDS.sleep(5);
			writer.write("login anonymous\n");
			writer.flush();
			TimeUnit.SECONDS.sleep(5);
			writer.write("force_install_dir \"C:\\arkserver\\\n");
			writer.flush();
			TimeUnit.SECONDS.sleep(5);
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
			
			p.destroy();
			reader.close();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Now start ark
		startGame();
		JOptionPane.showMessageDialog(frame, "Update Complete....");
		System.out.println("Update Complete....");
		System.out.println("Ark is Now Running...");
	}
	
	private static void startGame() throws IOException
	{
		Process p = Runtime.getRuntime().exec("C:\\arkserver\\ShooterGame\\Binaries\\Win64\\run.bat");
	}
	
	private static void killProcess(String executable)
	{
		String currentLine;
		try {
		    Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
		    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

		    while((currentLine = input.readLine()) != null)
		    {
			    if(currentLine.contains(gameExecutable))
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
