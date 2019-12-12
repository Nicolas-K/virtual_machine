package virtualmachine;

import java.util.ArrayList;
import java.util.List;

public class Memory {

    private static Memory instance;
    private List<Integer> stack;
    private int stackSize;

    public static Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }
        return instance;
    }

    private Memory() {
        this.stack = new ArrayList<>();
        this.stackSize = 0;
    }

    public synchronized void pushValue(int value) {
        this.stack.add(value);
        this.stackSize++;
    }

    public synchronized int popValue() {
        Integer result = null;
        if (this.stackSize > 0 && this.stackSize <= this.stack.size()) {
            result = this.stack.remove(--this.stackSize);
        } else {
            //TODO -- Throw Error
        }
        return result;
    }

    public synchronized void insertValue(int n, int value) {
        try {
            this.stack.set(n, value);
        } catch (IndexOutOfBoundsException error) {
            pushValue(value);
        }
    }

    public synchronized int getValue(int n) {
        return this.stack.get(n);
    }

    public synchronized int getStackSize() {
        return this.stackSize;
    }

    public synchronized void setStackSize(int value) {
        this.stackSize = value;
    }

    public synchronized void resetMemory() {
        stackSize = 0;
        stack = new ArrayList<>();
    }
}
