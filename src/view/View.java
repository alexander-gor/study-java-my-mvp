package view;

import java.io.File;
import java.util.HashMap;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public interface View {
	void notifyMazeIsReady(String name);
	void displayMessage(String msg);	
	void start();
	void stop();
	void notifySolutionReady(String name);
	void displaySolution(Solution<Position> solution);
	void displayMaze(String name, Maze3d maze);
}
