package virtualmachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    private static Controller instance;

    ArrayList<Command> commands = new ArrayList<>();
    Memory virtualMemory = null;
    Operation op = new Operation();

    private ArrayList<Command> listAux;
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

                    newCommand.setIntegerParameters(newCommand.getParameters());
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

    public void setDebug(ArrayList<Integer> breakPoints) {
        int counterBreak = 0, counterCommand;

        while (counterBreak < breakPoints.size()) {
            counterCommand = breakPoints.get(counterBreak);

            if (commands.get(counterCommand).getCommandLine() == breakPoints.get(counterBreak)) {
                commands.get(counterCommand).setBreakPoint(true);
            }
        }
    }

    public void clearDebug() {
        int count = 0;

        while (count < commands.size()) {
            commands.get(count).setBreakPoint(false);
            count++;
        }
    }

    public int executeCode(int PC, int input) {
        String label;
        listAux = commands;

        switch (commands.get(PC).getCommandName()) {
            case "START":
                virtualMemory = Memory.getInstance();
                PC++;
                break;

            case "LDC":
                op.operationLDC(commands.get(PC).getIntegerParameters().get(0));
                PC++;
                break;

            case "LDV":
                op.operationLDV(commands.get(PC).getIntegerParameters().get(0));
                PC++;
                break;

            case "STR":
                op.operationSTR(commands.get(PC).getIntegerParameters().get(0));
                PC++;
                break;

            case "NULL":
                PC++;
                break;

            case "ADD":
                op.operationADD();
                PC++;
                break;

            case "SUB":
                op.operationSUB();
                PC++;
                break;

            case "MULT":
                op.operationMULT();
                PC++;
                break;

            case "DIVI":
                op.operationDIVI();
                PC++;
                break;

            case "INV":
                op.operationINV();
                PC++;
                break;

            case "AND":
                op.operationAND();
                PC++;
                break;

            case "OR":
                op.operationOR();
                PC++;
                break;

            case "NEG":
                op.operationNEG();
                PC++;
                break;

            /*
             *	Operações de Comparação
             */
            case "CME":
                op.operationCME();
                PC++;
                break;

            case "CMA":
                op.operationCMA();
                PC++;
                break;

            case "CEQ":
                op.operationCEQ();
                PC++;
                break;

            case "CDIF":
                op.operationCDIF();
                PC++;
                break;

            case "CMEQ":
                op.operationCMEQ();
                PC++;
                break;

            case "CMAQ":
                op.operationCMAQ();
                PC++;
                break;

            case "JMP":
                PC = op.operationJMP(listAux, commands.get(PC).getParameters().get(0));
                break;

            case "JMPF":
                PC = op.operationJMPF(listAux, PC, commands.get(PC).getParameters().get(0));
                break;

            case "RD":
                op.operationRD(input);
                PC++;
                break;

            case "PRN":
                setPrintValue(op.operationPRN());
                PC++;
                break;

            case "ALLOC":
                op.operationALLOC(commands.get(PC).getIntegerParameters().get(0),
                        commands.get(PC).getIntegerParameters().get(1));
                PC++;
                break;

            case "DALLOC":
                op.operationDALLOC(commands.get(PC).getIntegerParameters().get(0),
                        commands.get(PC).getIntegerParameters().get(1));
                PC++;
                break;

            case "CALL":
                label = commands.get(PC).getParameters().get(0);
                PC = op.operationCALL(listAux, PC, label);
                break;

            case "RETURN":
                PC = op.operationRETURN();
                break;

            case "RETURNF":
                if (!commands.get(PC).getIntegerParameters().isEmpty()) {

                    PC = op.operationRETURNF(commands.get(PC).getIntegerParameters().get(0),
                            commands.get(PC).getIntegerParameters().get(1));

                } else {
                    PC = op.operationRETURNF(0, 0);
                }
                break;
        }

        return PC;
    }

    public int getCurrentStackSize() {
        return virtualMemory.getStackSize();
    }

    public ArrayList<Integer> getCurrentStack() {
        return virtualMemory.getCurrentMemoryStack();
    }

    public void setPrintValue(int value) {
        this.toPrint = value;
    }

    public int getPrintValue() {
        return this.toPrint;
    }

    public void endExecution() {
        virtualMemory = null;
        op = null;
        instance = null;
    }
}
