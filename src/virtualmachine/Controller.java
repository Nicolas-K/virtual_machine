package virtualmachine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    private static Controller instance;
    Scanner input = new Scanner(System.in);

    int PC = 0;
    String controlError;

    ArrayList<Command> commands = new ArrayList<>();
    Memory virtualMemory = null;
    Operation op = new Operation();

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void start(String path) {
        try {
            openCodeFile(path);

            try {
                executeCode();

            } catch (Exception e) {
                if (e.getMessage() != null) {
                    controlError = e.getMessage();
                }

            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                controlError = e.getMessage();
            }
        }
    }

    public void openCodeFile(String codePath) throws Exception {
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

    public boolean executeCode() throws Exception {
        ArrayList<Command> list = commands;
        ArrayList<Integer> parameters = new ArrayList<>();
        String label;

        while (!commands.get(PC).getCommandName().equals("HLT")) {
            if (commands.get(PC).getBreakPoint() != true) {
                switch (commands.get(PC).getCommandName()) {

                    case "START":
                        virtualMemory = Memory.getInstance();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "HLT":
                        virtualMemory = null;
                        displayVirtualMachineState();
                        break;

                    /*
                     * 	Carregar Constante, Carregar Valor, Atribuição e Nulo
                     */
                    case "LDC":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationLDC(parameters.get(0));
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "LDV":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationLDV(parameters.get(0));
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "STR":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationSTR(parameters.get(0));
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "NULL":
                        PC++;
                        displayVirtualMachineState();
                        break;

                    /*
                     *	Operações Aritméticas
                     */
                    case "ADD":
                        op.operationADD();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "SUB":
                        op.operationSUB();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "MULT":
                        op.operationMULT();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "DIVI":
                        op.operationDIVI();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "INV":
                        op.operationINV();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    /*
                     *	Operações Lógicas
                     */
                    case "AND":
                        op.operationAND();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "OR":
                        op.operationOR();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "NEG":
                        op.operationNEG();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    /*
                     *	Operações de Comparação
                     */
                    case "CME":
                        op.operationCME();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "CMA":
                        op.operationCMA();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "CEQ":
                        op.operationCEQ();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "CDIF":
                        op.operationCDIF();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "CMEQ":
                        op.operationCMEQ();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "CMAQ":
                        op.operationCMAQ();
                        PC++;
                        displayVirtualMachineState();
                        break;

                    /*
                     * Desvios
                     */
                    case "JMP":
                        label = commands.get(PC).getParameters().get(0);
                        PC = op.operationJMP(list, label);
                        displayVirtualMachineState();
                        break;

                    case "JMPF":
                        label = commands.get(PC).getParameters().get(0);
                        PC = op.operationJMPF(list, PC, label);
                        displayVirtualMachineState();
                        break;

                    /*
                     *  Entrada e Saida
                     */
                    case "RD":
                        int readValue = Integer.parseInt(input.nextLine());
                        op.operationRD(readValue);
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "PRN":
                        int printValue = op.operationPRN();
                        System.out.print(printValue);
                        PC++;
                        displayVirtualMachineState();
                        break;

                    /*
                     * Alocação e Desalocação de Váriavel
                     */
                    case "ALLOC":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationALLOC(parameters.get(0), parameters.get(1));
                        PC++;
                        displayVirtualMachineState();
                        break;

                    case "DALLOC":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationDALLOC(parameters.get(0), parameters.get(1));
                        PC++;
                        displayVirtualMachineState();
                        break;

                    /*
                     * Chamada de Rotina
                     */
                    case "CALL":
                        label = commands.get(PC).getParameters().get(0);
                        PC = op.operationCALL(list, PC, label);
                        displayVirtualMachineState();
                        break;

                    case "RETURN":
                        PC = op.operationRETURN();
                        displayVirtualMachineState();
                        break;

                    case "RETURNF":
                        if (!commands.get(PC).getIntegerParameters().isEmpty()) {
                            parameters = commands.get(PC).getIntegerParameters();
                            PC = op.operationRETURNF(parameters.get(0), parameters.get(1));

                        } else {
                            PC = op.operationRETURNF(0, 0);
                        }

                        displayVirtualMachineState();
                        break;

                    default:
                        throw new Exception("Instruction Not Valid");
                }
            } else {
                return false;
            }
        }

        return true;
    }

    public void displayVirtualMachineState() {
        int currentPC, currentStackSize;
        Command currentCommand;
        ArrayList<Integer> currentStack;

        currentPC = PC;
        currentCommand = commands.get(currentPC);
        currentStack = virtualMemory.getCurrentMemoryStack();
        currentStackSize = virtualMemory.getStackSize();
    }
}
