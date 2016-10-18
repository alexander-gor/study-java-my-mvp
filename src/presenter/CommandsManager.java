package presenter;

import java.io.File;
import java.util.HashMap;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import model.Model;
import view.View;

public class CommandsManager {
	
	private Model model;
	private View view;
		
	public CommandsManager(Model model, View view) {
		this.model = model;
		this.view = view;		
	}
	
	public HashMap<String, Command> getCommandsMap() {
		HashMap<String, Command> commands = new HashMap<String, Command>();
		commands.put("generate_maze", new GenerateMazeCommand());
		commands.put("display_maze", new DisplayMazeCommand());
		commands.put("display_solution", new DisplaySolutionCommand());
		commands.put("maze_ready", new MazeReadyCommand());
		commands.put("load_properties", new LoadPropertiesCommand());
		commands.put("solve_maze", new SolveMazeCommand());
		commands.put("solution_ready", new SolutionReadyCommand());
		commands.put("exit", new ExitCommand());
		
		return commands;
	}
	
	class SolveMazeCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			String name = args[0];
			String algorithm = args[1];
			Maze3d maze = model.getMaze(name);
			if (args.length > 2) {
				int x = Integer.parseInt(args[2]);
				int y = Integer.parseInt(args[3]);
				int z = Integer.parseInt(args[4]);
				maze.setStartPosition(new Position(x, y, z));		
			}
			model.solveMaze(name, maze, algorithm);
		}		
	}
	
	class GenerateMazeCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			String name = args[0];
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int z = Integer.parseInt(args[3]);
			model.generateMaze(name, x, y, z);
		}		
	}
	
	class DisplayMazeCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			String name = args[0];
			Maze3d maze = model.getMaze(name);
			view.displayMaze(name, maze);
		}		
	}
	
	class DisplaySolutionCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			String name = args[0];
			Solution<Position> solution = model.getSolution(name);
			view.displaySolution(solution);
		}		
	}
	
	class ExitCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			view.stop();
		}		
	} 
	
	class SolutionReadyCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			String name = args[0];			
			view.notifySolutionReady(name);
		}		
	}
	
	class MazeReadyCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			String name = args[0];			
			view.notifyMazeIsReady(name);
		}		
	}
	
	class LoadPropertiesCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			model.loadProperties();
			
		}		
	}
	
	
}
