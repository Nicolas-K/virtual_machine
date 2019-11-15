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
        openCodeFile(path);

        try {
            executeCode();

        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.out.println(e.getMessage());
            }
            System.out.println("Error detected");
        }
    }

    public void openCodeFile(String codePath) {
        int indexFile, parameterCounter, instructionSize;
        String nextInstruction;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(codePath));
            indexFile = 0;

            System.out.println("Receiving commands");

            /*
             *	Leitura linha por linha do arquivo do código
             *	para inserir na lista dos comandos
             *	(Obter o comando e valor presente na linha)
             */
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

        } catch (FileNotFoundException fileException) {
            System.out.println("Error! File Not Found");
        } catch (IOException ioException) {
            System.out.println("Error! Leitura/Escrita do arquivo");
        }

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioException) {
            System.out.println("Error! File Not Closed");
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

    public void executeCode() throws Exception {
        int PC = 0;
        ArrayList<Command> list = commands;
        ArrayList<Integer> parameters = new ArrayList<>();
        String label;

        while (!commands.get(PC).getCommandName().equals("HLT")) {
            if (commands.get(PC).getBreakPoint() != true) {
                switch (commands.get(PC).getCommandName()) {

                    /*
                     *  Iniciar/Finalizar Execução
                     */
                    case "START":
                        virtualMemory = Memory.getInstance();
                        PC++;
                        break;

                    case "HLT":
                        PC++;
                        break;

                    /*
                     * 	Carregar Constante, Carregar Valor, Atribuição e Nulo
                     */
                    case "LDC":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationLDC(parameters.get(0));
                        PC++;
                        break;

                    case "LDV":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationLDV(parameters.get(0));
                        PC++;
                        break;

                    case "STR":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationSTR(parameters.get(0));
                        PC++;
                        break;

                    case "NULL":
                        PC++;
                        break;

                    /*
                     *	Operações Aritméticas
                     */
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

                    /*
                     *	Operações Lógicas
                     */
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

                    /*
                     * Desvios
                     */
                    case "JMP":
                        label = commands.get(PC).getParameters().get(0);
                        PC = op.operationJMP(list, label);
                        break;

                    case "JMPF":
                        label = commands.get(PC).getParameters().get(0);
                        PC = op.operationJMPF(list, PC, label);
                        break;

                    /*
                     *  Entrada e Saida
                     */
                    case "RD":
                        int readValue = Integer.parseInt(input.nextLine());
                        op.operationRD(readValue);
                        PC++;
                        break;

                    case "PRN":
                        int printValue = op.operationPRN();
                        System.out.print(printValue);
                        PC++;
                        break;

                    /*
                     * Alocação e Desalocação de Váriavel
                     */
                    case "ALLOC":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationALLOC(parameters.get(0), parameters.get(1));
                        PC++;
                        break;

                    case "DALLOC":
                        parameters = commands.get(PC).getIntegerParameters();
                        op.operationDALLOC(parameters.get(0), parameters.get(1));
                        PC++;
                        break;

                    /*
                     * Chamada de Rotina
                     */
                    case "CALL":
                        label = commands.get(PC).getParameters().get(0);
                        PC = op.operationCALL(list, PC, label);
                        break;

                    case "RETURN":
                        PC = op.operationRETURN();
                        break;

                    case "RETURNF":
                        if (!commands.get(PC).getIntegerParameters().isEmpty()) {
                            parameters = commands.get(PC).getIntegerParameters();
                            op.operationDALLOC(parameters.get(0), parameters.get(1));
                        }
                        PC = op.operationRETURN();
                        break;

                    default:
                        throw new Exception("Instruction Not Valid");
                }
            }
        }
    }
}
