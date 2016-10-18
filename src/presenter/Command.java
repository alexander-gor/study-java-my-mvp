package presenter;
/**
 * interfaces that defines a command
 * @author Administrator
 *
 */
public interface Command {
	/**
	 * method that defines what should happen when a command is ran
	 * @param args arguments to the command
	 */
	void doCommand(String[] args);
}
