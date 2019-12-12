package virtualmachine;

import java.util.ArrayList;

public class Command {

    private int commandLine;
    private String commandName;
    private ArrayList<String> parameters;

    public Command() {
        commandName = null;
        commandLine = -1;
        parameters = new ArrayList<>();
    }

    public void setCommandLine(int commandLine) {
        this.commandLine = commandLine;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setParameters(String parameter) {
        this.parameters.add(parameter);
    }

    public int getCommandLine() {
        return this.commandLine;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public ArrayList<String> getParameters() {
        return this.parameters;
    }
}
