package view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.omg.CORBA.ARG_OUT;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;
import properties.PropertiesLoader;

public class MazeWindow extends BasicWindow implements View {

	private MazeDisplay mazeDisplay;
	private MazeViewAdapter mva;
	private int floor = 0;
	
	@Override
	protected void initWidgets() {
		
	    Menu m = new Menu(shell, SWT.BAR);
	    // create a file menu and add an exit item
	    final MenuItem file = new MenuItem(m, SWT.CASCADE);
	    file.setText("&File");
	    final Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
	    file.setMenu(filemenu);
	    final MenuItem openItem = new MenuItem(filemenu, SWT.PUSH);
	    openItem.setText("&Open\tCTRL+O");
	    openItem.setAccelerator(SWT.CTRL + 'O');
	    final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
	    exitItem.setText("E&xit");

	    openItem.addSelectionListener(new OpenFile());
	    exitItem.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	          MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
	              | SWT.YES | SWT.NO);
	          messageBox.setMessage("Do you really want to exit?");
	          messageBox.setText("Exiting Application");
	          int response = messageBox.open();
	          if (response == SWT.YES) {
	        	  setChanged();
	        	  notifyObservers("exit");
	          }
	        }
	      });
	    shell.setMenuBar(m);
	    
		GridLayout gridLayout = new GridLayout(2, false);
		shell.setLayout(gridLayout);				
		
		Composite btnGroup = new Composite(shell, SWT.BORDER);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		btnGroup.setLayout(rowLayout);
		
		Button btnGenerateMaze = new Button(btnGroup, SWT.PUSH);
		btnGenerateMaze.setText("Generate maze");	
		
		btnGenerateMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showGenerateMazeOptions();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnSolveMaze = new Button(btnGroup, SWT.PUSH);
		btnSolveMaze.setText("Solve maze");
		btnSolveMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setChanged();
				notifyObservers("solve_maze " + mva.name + " DFS " +mva.currPos.x + " " + mva.currPos.y+ " " + mva.currPos.z);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mazeDisplay = new MazeDisplay(shell, SWT.NONE);	
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.verticalSpan = 2;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		mazeDisplay.setLayoutData(gridData);
		
		final KeyAdapter keyListener = new KeyAdapter()
        {
			@Override public void keyPressed(final KeyEvent e)
            {
				final Object source = e.getSource();
                    Position nextPos = null;           
                switch (e.keyCode) {
                
				case SWT.ARROW_UP:
					nextPos = mva.forwardPos();
					break;
				case SWT.ARROW_DOWN:
					nextPos = mva.backwardsPos();
					break;
				case SWT.ARROW_LEFT:
					nextPos = mva.leftPos();
					break;
				case SWT.ARROW_RIGHT:
					nextPos = mva.rightPos();
					break;
				case SWT.PAGE_UP:
					nextPos = mva.upPos();
					break;
				case SWT.PAGE_DOWN:
					nextPos = mva.downPos();
					break;
				default:
					break;
				}
                if (nextPos != null) {
                	mva.setCurrPos(nextPos);
                	displayMaze();
                }
            }
        };
		
		
		shell.addKeyListener(keyListener);
		mazeDisplay.addKeyListener(keyListener);
		mazeDisplay.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				mazeDisplay.forceFocus();
				
			}
			
		});
		btnGenerateMaze.addKeyListener(keyListener);
		btnSolveMaze.addKeyListener(keyListener);
		
	}
	
	class OpenFile implements SelectionListener {
	      public void widgetSelected(SelectionEvent event) {
	        FileDialog fd = new FileDialog(shell, SWT.OPEN);
	        fd.setText("Open");
	        fd.setFilterPath("C:/");
	        String[] filterExt = { "*.xml"};
	        fd.setFilterExtensions(filterExt);
	        String selected = fd.open();
	        File source = new File(selected);
	        File dest = new File(PropertiesLoader.RESOURCESDIR + PropertiesLoader.PROPERTIES_FILE_NAME);

	        try {
				Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }

	      public void widgetDefaultSelected(SelectionEvent event) {
	      }
	    }


	protected void showGenerateMazeOptions() {
		Shell shell = new Shell();
		shell.setText("Generate Maze");
		shell.setSize(300, 200);
		
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		
		Label lblX = new Label(shell, SWT.NONE);
		lblX.setText("X: ");
		Text txtX = new Text(shell, SWT.BORDER);
		
		Label lblY = new Label(shell, SWT.NONE);
		lblY.setText("Y: ");
		Text txtY = new Text(shell, SWT.BORDER);
		
		Label lblZ = new Label(shell, SWT.NONE);
		lblZ.setText("Z: ");
		Text txtZ = new Text(shell, SWT.BORDER);
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setText("Name: ");
		Text txtName = new Text(shell, SWT.BORDER);
		
		Button btnGenerate = new Button(shell, SWT.PUSH);
		btnGenerate.setText("Generate");
		btnGenerate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setChanged();
				notifyObservers("generate_maze " + txtName.getText() + " " +txtX.getText() + " " + txtY.getText()+ " " + txtZ.getText());
				shell.close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		shell.open();		
	}

	@Override
	public void notifyMazeIsReady(String name) {
		display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageBox msg = new MessageBox(shell);
				msg.setMessage("Maze " + name + " is ready");
				msg.open();	
				
				setChanged();
				notifyObservers("display_maze " + name);
			}
		});			
	}

	public void displayMaze() {
			
		mazeDisplay.setMazeData(mva.getMazeData());
	}
	
	@Override
	public void displayMaze(String name, Maze3d maze) {
		mva = new MazeViewAdapter(name, maze, maze.getStartPosition());
		
		mazeDisplay.setMazeData(mva.getMazeData());
	}
	
	private class MazeViewAdapter  {
		protected String name;
		Position currPos;
		Maze3d maze;
		private MazeViewAdapter (String name, Maze3d maze, Position currPos) {
			this.currPos = currPos;
			this.maze = maze;
			this.name = name;
			initMazeData();
		}
		
		private void initMazeData() {
			mazeData = maze.getCrossSectionByZ(currPos.z);
			if (currPos.equals(maze.getGoalPosition()))
				mazeData[currPos.x][currPos.y] = 3;
			else {
				mazeData[currPos.x][currPos.y] = 2;
			}
		}
		
		public Position getCurrPos() {
			return currPos;
		}
		
		public Position rightPos(){
			Position p = new Position(currPos.x, currPos.y+1, currPos.z);
			if (p.y+1 <= maze.getY() && maze.isFree(p))
				return p;
			return null;
		}
		public Position leftPos(){
			Position p = new Position(currPos.x, currPos.y-1, currPos.z);
			if (p.y >= 0 && maze.isFree(p))
				return p;
			return null;
		}
		public Position forwardPos(){
			Position p = new Position(currPos.x-1, currPos.y, currPos.z);
			if (p.x >= 0 && maze.isFree(p))
				return p;
			return null;
		}
		public Position backwardsPos(){
			Position p = new Position(currPos.x+1, currPos.y, currPos.z);
			if ( p.x+1 <= maze.getX() &&maze.isFree(p))
				return p;
			return null;
		}
		public Position upPos(){
			Position p = new Position(currPos.x, currPos.y, currPos.z+1);
			if ( p.z+1 <= maze.getZ() &&maze.isFree(p))
				return p;
			return null;
		}
		public Position downPos(){
			Position p = new Position(currPos.x, currPos.y, currPos.z-1);
			if (p.z >= 0 && maze.isFree(p))
				return p;
			return null;
		}

		public void setCurrPos(Position currPos) {
			this.currPos = currPos;
			if (currPos.equals(maze.getGoalPosition())) {
				MessageBox msg = new MessageBox(shell);
				msg.setMessage("Maze is solved");
				msg.open();	
			}
			initMazeData();
		}

		int[][] mazeData = null;
		
		int[][] getMazeData() {
			return mazeData;
		}
	}

	@Override
	public void displayMessage(String msg) {
		MessageBox msgBox = new MessageBox(shell);
		msgBox.setMessage(msg);
		msgBox.open();	
		
	}

	@Override
	public void start() {
		run();		
	}

	@Override
	public void stop() {
		shell.close();
		System.exit(0);
	}

	public void notifySolutionReady(String name) {
		display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageBox msg = new MessageBox(shell);
				msg.setMessage("Solution for Maze " + name + " is ready");
				msg.open();	
				
				setChanged();
				notifyObservers("display_solution " + name);
			}
		});	
		
	}

	@Override
	public void displaySolution(Solution<Position> solution) {
		
		List<State<Position>> states = solution.getStates();
		final int time = 500;

	    Runnable timer = new Runnable() {
	      int currIndex = 0;
	      
	      public void run() {
	    	  	if (currIndex != states.size() - 1) {
					State<Position> nextPos = states.get(currIndex + 1);
					mva.setCurrPos(nextPos.getValue());
                	displayMaze();
					currIndex++;
					display.timerExec(time, this);
				}
	      }
	    };
	    display.timerExec(time, timer);
	    
		
				
		
	}

}
