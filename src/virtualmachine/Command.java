package virtualmachine;

import java.util.ArrayList;

public class Command {

    private int commandID;
    private String commandName;
    private ArrayList<String>  parameters;
    private ArrayList<Integer> integerParameters;
    private boolean breakpoint;

    private Command() {
        commandName = null;
        commandID = 0;
        parameters = new ArrayList<String>();
        integerParameters = new ArrayList<Integer>();
        breakpoint = false;
    }

    public void increaseCommandID() {
        this.commandID++;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
    
    public void setParameters(String parameter){
        this.parameters.add(parameter);
    }

    public void setIntegerParameters(ArrayList<String> stringParameters){
        int parameterIndex = 0;
        
        if(!stringParameters.isEmpty()){
            while(parameterIndex < stringParameters.size()){
                this.integerParameters.add(Integer.parseInt(stringParameters.get(parameterIndex)));
            }
        } 
    }

    public void setBreakPoint(boolean breakpoint) {
        this.breakpoint = breakpoint;
    }

    public int getCommandID() {
        return this.commandID;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public ArrayList<String> getParameters(){
        return this.parameters;
    }
    
    public ArrayList<Integer> getIntegerParameters(){
        return this.integerParameters;
    }

    public boolean getBreakPoint() {
        return this.breakpoint;
    }
}
