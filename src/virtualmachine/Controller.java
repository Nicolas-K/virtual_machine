package virtualmachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    private static Controller instance;
    ArrayList<Command> commands = new ArrayList<>();
    Memory virtualMemory = Memory.getInstance();
    Operation op = new Operation();
    private int toPrint;

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public ArrayList<Command> openCodeFile(String codePath) throws Exception {
        int indexFile, parameterCounter, instructionSize;
        String nextInstruction;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(codePath));
            indexFile = 0;

            while ((nextInstruction = reader.readLine()) != null) {
                Command newCommand = new Command();
                nextInstruction = nextInstruction.trim();
                String[] instruction = nextInstruction.split(" ");
                instructionSize = instruction.length;

                if (instructionSize == 1) {
                    newCommand.setCommandLine(indexFile);
                    newCommand.setCommandName(instruction[0]);

                } else if (instruction[1].equals("NULL")) {
                    newCommand.setCommandLine(indexFile);
                    newCommand.setCommandName(instruction[1]);
                    newCommand.setParameters(instruction[0]);

                } else if (instruction[0].equals("JMP") || instruction[0].equals("JMPF") || instruction[0].equals("CALL")) {
                    newCommand.setCommandLine(indexFile);
                    newCommand.setCommandName(instruction[0]);
                    newCommand.setParameters(instruction[1]);

                } else {
                    parameterCounter = 1;
                    newCommand.setCommandLine(indexFile);
                    newCommand.setCommandName(instruction[0]);

                    while (instructionSize > 1) {
                        newCommand.setParameters(instruction[parameterCounter]);
                        parameterCounter++;
                        instructionSize--;
                    }
                }

                commands.add(indexFile, newCommand);
                newCommand = null;
                indexFile++;
            }

            reader.close();
            return commands;

        } catch (IOException e) {
            throw new Exception("[ Virtual Machine ] | Error, couldn't open the file properly");
        }
    }

    public int executeCode(int PC, int input) {

        try {
            switch (commands.get(PC).getCommandName()) {
                case "LDC":
                    op.LDC(Integer.parseInt(commands.get(PC).getParameters().get(0)));
                    PC++;
                    break;

                case "LDV":
                    op.LDV(Integer.parseInt(commands.get(PC).getParameters().get(0)));
                    PC++;
                    break;

                case "STR":
                    op.STR(Integer.parseInt(commands.get(PC).getParameters().get(0)));
                    PC++;
                    break;

                case "ALLOC":
                    op.ALLOC(Integer.parseInt(commands.get(PC).getParameters().get(0)), Integer.parseInt(commands.get(PC).getParameters().get(1)));
                    PC++;
                    break;

                case "DALLOC":
                    op.DALLOC(Integer.parseInt(commands.get(PC).getParameters().get(0)), Integer.parseInt(commands.get(PC).getParameters().get(1)));
                    PC++;
                    break;

                case "ADD":
                    op.ADD();
                    PC++;
                    break;

                case "SUB":
                    op.SUB();
                    PC++;
                    break;

                case "MULT":
                    op.MULT();
                    PC++;
                    break;

                case "DIVI":
                    op.DIVI();
                    PC++;
                    break;

                case "INV":
                    op.INV();
                    ;
                    PC++;
                    break;

                case "AND":
                    op.AND();
                    PC++;
                    break;

                case "OR":
                    op.OR();
                    PC++;
                    break;

                case "NEG":
                    op.NEG();
                    PC++;
                    break;

                case "CME":
                    op.CME();
                    PC++;
                    break;

                case "CMA":
                    op.CMA();
                    PC++;
                    break;

                case "CEQ":
                    op.CEQ();
                    PC++;
                    break;

                case "CDIF":
                    op.CDIF();
                    PC++;
                    break;

                case "CMEQ":
                    op.CMEQ();
                    PC++;
                    break;

                case "CMAQ":
                    op.CMAQ();
                    PC++;
                    break;

                case "START":
                    op.START();
                    PC++;
                    break;

                case "HLT":
                    op.HLT();
                    break;

                case "RETURN":
                    PC = op.RETURN();
                    PC++;
                    break;

                case "RETURNF":
                    int parameter1,
                     parameter2;

                    if (commands.get(PC).getParameters().size() > 0) {
                        parameter1 = Integer.parseInt(commands.get(PC).getParameters().get(0));
                        parameter2 = Integer.parseInt(commands.get(PC).getParameters().get(1));

                    } else {
                        parameter1 = -1;
                        parameter2 = -1;
                    }

                    PC = op.RETURNF(parameter1, parameter2);
                    PC++;
                    break;

                case "RD":
                    op.RD(input);
                    PC++;
                    break;

                case "PRN":
                    setPrintValue(op.PRN());
                    PC++;
                    break;

                case "JMP":
                    PC = op.JMP(commands, commands.get(PC).getParameters().get(0));
                    PC++;
                    break;

                case "JMPF":
                    PC = op.JMPF(commands, commands.get(PC).getParameters().get(0), PC);
                    PC++;
                    break;

                case "CALL":
                    PC = op.CALL(commands, commands.get(PC).getParameters().get(0), PC);
                    PC++;
                    break;

                case "NULL":
                    PC++;
                    break;
            }

            return PC;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Error occurred on this line: %d\n", PC);
        }

        return -1;
    }

    public int getCurrentStackSize() {
        return virtualMemory.getStackSize();
    }

    public void setPrintValue(int value) {
        this.toPrint = value;
    }

    public int getPrintValue() {
        return this.toPrint;
    }

    public void endExecution() {
        op = null;
        instance = null;
    }
}
