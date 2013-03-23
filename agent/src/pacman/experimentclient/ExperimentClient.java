package pacman.experimentclient;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.script.ScriptException;

import com.google.gson.Gson;
import pacman.Executor;
import pacman.entries.pacman.MyPacMan;
import pacman.entries.pacman.Parameters;

public class ExperimentClient
{
	private static final String SERVER_URL = "http://www.stewartml.co.uk/pac-man/";
	private static final String EXPERIMENT_URL = SERVER_URL + "active_experiment";
	private static final String SAVE_URL = SERVER_URL + "experiments";
	
	public static void main(String[] args)
	{
		ExperimentClient client = new ExperimentClient();
		
		if (args.length == 2)
		{
            System.out.printf("Uploading script: %s\n", args[0]);
			client.uploadScript(args[0], Integer.parseInt(args[1]));
		}
        else if (args.length == 1)
        {
            System.out.printf("Running script: %s\n", args[0]);
            client.run(args[0]);
        }
		else
		{
            System.out.printf("Starting experiment client.\n");
			client.run();
		}
	}
	
	
	public void run()
	{
		ExperimentRunner runner = new ExperimentRunner();
		
		while (true)
		{
			Experiment experiment = getExperiment();
			
			if (experiment == null)
				break;
			
			try
			{
				int score = runner.run(experiment.script);
				saveResult(experiment._id, score);
				System.out.printf("Finished running.  Score: %d.\n", score);
			}
			catch (ScriptException ex)
			{
				ex.printStackTrace();
			}
		}
	}


    public void run(String path)
    {
        try
        {
            ExperimentRunner runner = new ExperimentRunner();
            Executor executor = new Executor();

            String script = readFile(path);
            Parameters parameters = runner.loadParameters(script);

            executor.runGame(new MyPacMan(parameters), parameters.opponent, true, 40);
        }
        catch (ScriptException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
	
	
	public void uploadScript(String path, int count)
	{
		try
		{
			String script = readFile(path);

            //check script for errors
            ExperimentRunner runner = new ExperimentRunner();
            runner.loadParameters(script);
			
//			NewExperiment experiment = new NewExperiment();
//            experiment.name = path.substring(path.indexOf("/") + 1);
//			experiment.scores = new int[0];
//			experiment.script = script;
//			experiment.count = count;
//
//			HttpClient client = new HttpClient();
//			Gson gson = new Gson();
//			String json = gson.toJson(experiment);
//
//			client.post(SAVE_URL, json);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
        catch (ScriptException ex)
        {
            System.out.println(ex.getMessage());
        }
	}


    private String readFile(String path) throws IOException
    {
        Scanner scanner = new Scanner(new FileReader(path));
        scanner.useDelimiter("\\A");

        String file = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return file;
    }
	
	
	private Experiment getExperiment()
	{
		try
		{
			HttpClient client = new HttpClient();
			String json = client.get(EXPERIMENT_URL);
			
			Gson gson = new Gson();
			Experiment experiment = gson.fromJson(json, Experiment.class);
			return experiment;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	
	private void saveResult(String experiment, int score)
	{
		try
		{
			HttpClient client = new HttpClient();
			String response = client.post(String.format(SAVE_URL + "/%s", experiment), String.format("%d", score));
			
			if (!response.equals("OK"))
				System.out.println("Error saving score: " + response);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public class Experiment
	{
		public String _id;
		public String script;
	}
	
	public class NewExperiment
	{
        public String name;
		public String script;
		public int[] scores;
		public int count;
	}
}
