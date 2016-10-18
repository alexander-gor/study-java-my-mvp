package view;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public class MyView extends Observable implements View, Observer {
	
	private BufferedReader in;
	private PrintWriter out;
	private CLI cli;	

	public MyView(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
				
		cli = new CLI(in, out);
		cli.addObserver(this);
	}	

	@Override
	public void notifyMazeIsReady(String name) {
		out.println("maze " + name + " is ready");
		out.flush();
	}

	@Override
	public void displayMaze(String name, Maze3d maze) {
		out.println(maze);
		out.flush();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		cli.start();
	}

	@Override
	public void displayMessage(String msg) {
		out.println(msg);
		out.flush();		
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == cli) {
			setChanged();
			notifyObservers(arg);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifySolutionReady(String name) {
		out.println("solution for maze " + name + " is ready");
		out.flush();
		
	}

	@Override
	public void displaySolution(Solution<Position> solution) {
		out.println(solution);
		out.flush();		
		
	}

}
