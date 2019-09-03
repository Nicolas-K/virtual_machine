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

    private Memory() {
        stack = new ArrayList<>();
        stackSize = -1;
    }

    public void pushValue(int value) {
        stack.add(value);
        increaseStackSize();
        
        System.out.println("\nPush Value: ");
        System.out.print(value);
        System.out.println("\nStack Size: ");
        System.out.print(stackSize);
    }

    public int popValue() {
        int value;
        value = stack.get(stackSize);
        decreaseStackSize();
        
        System.out.println("\nPop Value: ");
        System.out.print(value);
        System.out.println("\nStack Size: ");
        System.out.print(stackSize);   
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

    public void increaseStackSize() {
        this.stackSize++;
    }

    public void decreaseStackSize() {
        this.stackSize--;
    }

    public int getStackSize() {
        return this.stackSize;
    }
}
