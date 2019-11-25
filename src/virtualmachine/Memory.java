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

    public synchronized int popValue() {
        int value;
        value = stack.get(stackSize);
        decreaseStackSize();   
        return value;
    }

    public synchronized void insertValue(int value, int n) {
        stack.set(n, value);
    }

    public synchronized int getValue(int n) {
        int value;
        value = stack.get(n);
        return value;
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
        return this.stack;
    }
}
