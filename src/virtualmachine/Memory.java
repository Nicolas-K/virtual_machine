package virtualmachine;

import java.util.ArrayList;

public class Memory {

    private static Memory instance;
    private ArrayList<Integer> stack = null;
    private int stackSize;

    public static Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }
        return instance;
    }

    public Memory() {
        stack = new ArrayList<>();
        stackSize = -1;
    }

    public synchronized void pushValue(int value) {
        stack.add(value);
        increaseStackSize();
    }

    public synchronized int popValue() throws Exception {
        int value;
        
        if(stackSize > -1 && stackSize < stack.size()) {
            value = stack.get(stackSize);
            stack.remove(stackSize);
            decreaseStackSize();   
            return value;
        } else {
            throw new Exception();
        }
    }

    public synchronized void insertValue(int value, int n) {
        try {
            stack.set(n, value);
        } catch (IndexOutOfBoundsException e) {
            pushValue(value);
        }
    }

    public synchronized int getValue(int n) {
        return stack.get(n);
    }

    public synchronized void increaseStackSize() {
        this.stackSize++;
    }

    public synchronized void decreaseStackSize() {
        this.stackSize--;
    }

    public synchronized int getStackSize() {
        return this.stackSize;
    }
    
    public synchronized ArrayList<Integer> getCurrentMemoryStack() {
        ArrayList<Integer> auxStack = new ArrayList<>();
        
        for(int i = 0; i <= this.stackSize; i++) {
            auxStack.add(i, this.stack.get(i));
        }
        
        return auxStack;
    }
}
