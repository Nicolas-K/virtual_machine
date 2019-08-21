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
	private int firstValue, secondValue;
	private boolean breakpoint;
	
	private Command() {
		commandName = null;
		firstValue = 0;
		secondValue = 0;
		breakpoint = false;
	}
	
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
	public void setFirstValue(int firstValue) {
		this.firstValue = firstValue;
	}
	
	public void setSecondValue(int secondValue) {
		this.secondValue = secondValue;
	}
	
	public void setBreakPoint(boolean breakpoint) {
		this.breakpoint = breakpoint;
	}
	
	public String getCommandName() {
		return this.commandName;
	}
	
	public int getFirstValue() {
		return this.firstValue;
	}
	
	public int getSecondValue() {
		return this.secondValue;
	}
	
	public boolean getBreakPoint() {
		return this.breakpoint;
	}
}