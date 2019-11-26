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
        }
    }

    public int executeCode(int PC, int input) {
        String label;
        listAux = commands;

        if (commands.get(PC).getBreakPoint() != true) {
            switch (commands.get(PC).getCommandName()) {

                case "START":
                    virtualMemory = Memory.getInstance();
                    PC++;
                    return PC;

                case "LDC":
                    op.operationLDC(commands.get(PC).getIntegerParameters().get(0));
                    PC++;
                    return PC;

                case "LDV":
                    op.operationLDV(commands.get(PC).getIntegerParameters().get(0));
                    PC++;
                    return PC;

                case "STR":
                    op.operationSTR(commands.get(PC).getIntegerParameters().get(0));
                    PC++;
                    return PC;

                case "NULL":
                    PC++;
                    return PC;

                case "ADD":
                    op.operationADD();
                    PC++;
                    return PC;

                case "SUB":
                    op.operationSUB();
                    PC++;
                    return PC;

                case "MULT":
                    op.operationMULT();
                    PC++;
                    return PC;

                case "DIVI":
                    op.operationDIVI();
                    PC++;
                    return PC;

                case "INV":
                    op.operationINV();
                    PC++;
                    return PC;

                case "AND":
                    op.operationAND();
                    PC++;
                    return PC;

                case "OR":
                    op.operationOR();
                    PC++;
                    return PC;

                case "NEG":
                    op.operationNEG();
                    PC++;
                    return PC;

                /*
                     *	Operações de Comparação
                 */
                case "CME":
                    op.operationCME();
                    PC++;
                    return PC;

                case "CMA":
                    op.operationCMA();
                    PC++;
                    return PC;

                case "CEQ":
                    op.operationCEQ();
                    PC++;
                    return PC;

                case "CDIF":
                    op.operationCDIF();
                    PC++;
                    return PC;

                case "CMEQ":
                    op.operationCMEQ();
                    PC++;
                    return PC;

                case "CMAQ":
                    op.operationCMAQ();
                    PC++;
                    return PC;

                case "JMP":
                    PC = op.operationJMP(listAux, commands.get(PC).getParameters().get(0));
                    return PC;

                case "JMPF":
                    PC = op.operationJMPF(listAux, PC, commands.get(PC).getParameters().get(0));
                    return PC;

                case "RD":
                    op.operationRD(input);
                    PC++;
                    return PC;

                case "PRN":
                    setPrintValue(op.operationPRN());
                    PC++;
                    return PC;

                case "ALLOC":
                    op.operationALLOC(commands.get(PC).getIntegerParameters().get(0),
                            commands.get(PC).getIntegerParameters().get(1));
                    PC++;
                    return PC;

                case "DALLOC":
                    op.operationDALLOC(commands.get(PC).getIntegerParameters().get(0),
                            commands.get(PC).getIntegerParameters().get(1));
                    PC++;
                    return PC;

                case "CALL":
                    label = commands.get(PC).getParameters().get(0);
                    PC = op.operationCALL(listAux, PC, label);
                    return PC;

                case "RETURN":
                    PC = op.operationRETURN();
                    return PC;

                case "RETURNF":
                    if (!commands.get(PC).getIntegerParameters().isEmpty()) {

                        PC = op.operationRETURNF(commands.get(PC).getIntegerParameters().get(0),
                                commands.get(PC).getIntegerParameters().get(1));

                    } else {
                        PC = op.operationRETURNF(0, 0);
                    }

                    return PC;
            }
        }

        return -1;
    }
    
    public int getCurrentStackSize() {
        return virtualMemory.getStackSize();
    }
    
    public ArrayList<Integer> getCurrentStack(){
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
