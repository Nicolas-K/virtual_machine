package virtualmachine;

public class VirtualMachine {

    public static void main(String[] args) {
        Controller control = Controller.getInstance();
        control.start();
    }
}
