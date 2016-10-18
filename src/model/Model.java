package model;

import java.io.File;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public interface Model {
	void generateMaze(String name, int x, int y, int z);
	Maze3d getMaze(String name);
	void exit();
	void solveMaze(String name, Maze3d maze, String algorithm);
	void loadProperties();
	Solution<Position> getSolution(String name);
	
}
