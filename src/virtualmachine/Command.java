/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualmachine;

/**
 *
 * @author 16104325
 */
public class Command {
    private String commandName;
	private int commandID, firstParameter, secondParameter;
	private boolean breakpoint;
	
	private Command() {
		commandName = null;
                commandID = 0;
		firstParameter = 0;
		secondParameter = 0;
		breakpoint = false;
	}
        
        public void increaseCommandID(){
            this.commandID++;
        }
	
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
	public void setFirstParameter(int firstParameter) {
		this.firstParameter = firstParameter;
	}
	
	public void setSecondParameter(int secondValue) {
		this.secondParameter = secondParameter;
	}
	
	public void setBreakPoint(boolean breakpoint) {
		this.breakpoint = breakpoint;
	}
        
        public int getCommandID(){
            return this.commandID;
        }
	
	public String getCommandName() {
		return this.commandName;
	}
	
	public int getFirstParameter() {
		return this.firstParameter;
	}
	
	public int getSecondParameter() {
		return this.secondParameter;
	}
	
	public boolean getBreakPoint() {
		return this.breakpoint;
	}
}