package view;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import algorithms.mazeGenerators.Maze3d;

public class MazeDisplay extends Canvas {
	private static final String RESOURCESDIR = "Resources\\";
	private int[][] mazeData;
	Color color;

	public void setMazeData(int[][] mazeData) {
		this.mazeData = mazeData;
		this.redraw();
	}

	public MazeDisplay(Shell parent, int style) {
		super(parent, style);
		this.setSize(parent.getSize().x, parent.getSize().y);
		color = new Color(null, 0, 0, 0);
		Display display = parent.getDisplay();
		Image image = new Image(display, RESOURCESDIR + "Theseus.png");

		this.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				if (mazeData == null)
					return;

				e.gc.setForeground(color);
				e.gc.setBackground(color);

				int width = getSize().x;
				int height = getSize().y;

				int w = width / mazeData[0].length;
				int h = height / mazeData.length;

				for (int i = 0; i < mazeData.length; i++)
					for (int j = 0; j < mazeData[i].length; j++) {
						int x = j * w;
						int y = i * h;
						if (mazeData[i][j] == Maze3d.WALL)
							e.gc.fillRectangle(x, y, w, h);
						else if (mazeData[i][j] == 2)
							e.gc.drawImage(image, 0, 0, 140, 239, x, y, w, h);
						else if (mazeData[i][j] == 3)
							e.gc.drawImage(new Image(display, RESOURCESDIR + "TheseusEnd.png"), 0, 0, 140, 168, x, y, w,
									h);
					}

			}
		});

		this.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				color.dispose();
			}
		});

	}
}
