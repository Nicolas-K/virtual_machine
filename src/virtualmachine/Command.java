package virtualmachine;

import java.util.ArrayList;

public class Command {

    private int commandLine;
    private String commandName;
    private ArrayList<String> parameters;
    private ArrayList<Integer> integerParameters;
    private boolean breakpoint;

    public Command() {
        commandName = null;
        commandLine = -1;
        parameters = new ArrayList<>();
        integerParameters = new ArrayList<>();
        breakpoint = false;
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

    public void setIntegerParameters(ArrayList<String> stringParameters) {
        int parameterIndex = 0;

        if (!stringParameters.isEmpty()) {
            while (parameterIndex < stringParameters.size()) {
                this.integerParameters.add(Integer.parseInt(stringParameters.get(parameterIndex)));
            }
        }
    }

    public void setBreakPoint(boolean breakpoint) {
        this.breakpoint = breakpoint;
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

    public ArrayList<Integer> getIntegerParameters() {
        return this.integerParameters;
    }

    public boolean getBreakPoint() {
        return this.breakpoint;
    }
}
