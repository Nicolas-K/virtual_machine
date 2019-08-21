/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualmachine;
import java.util.ArrayList;

/**
 *
 * @author 16104325
 */
public class Memory {
   	private static Memory instance;
	private ArrayList<Integer> stack = null;
	private int stackSize;
	
	public static Memory getInstance() {
		if( instance == null ) {
			instance = new Memory();
		}
		return instance;
	}
	
	private Memory() {
		stack = new ArrayList<Integer>();
		stackSize = -1;
	}
	
	public void pushValue(int value) {
		stack.add(value);
		increaseStackSize();
	}
	
	public int popValue() {
		int value;
		value = stack.get(stackSize);
		decreaseStackSize();
		return value;
	}
	
	public void insertValue(int value, int n) {
		stack.set(n, value);
	}
	
	public int getValue(int n) {
		int value;
		value = stack.get(n);
		return value;
	}
        
        public void increaseStackSize(){
            this.stackSize++;
        }
        
        public void decreaseStackSize(){
            this.stackSize--;
        }
        
        public int getStackSize(){
            return this.stackSize;
        }
}