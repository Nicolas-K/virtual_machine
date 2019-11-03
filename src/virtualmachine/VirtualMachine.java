package virtualmachine;

import java.util.Scanner;

public class VirtualMachine {

    Controller control = Controller.getInstance();
    Scanner test = new Scanner(System.in);
    String path;

    public void main(String[] args) {
        path = test.nextLine();
        control.start(path);
    }
}
