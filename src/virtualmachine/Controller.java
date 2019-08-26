package virtualmachine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    private static Controller instance;
    Scanner test = new Scanner(System.in);

    ArrayList<Command> commands = new ArrayList<Command>();
    Memory virtualMemory = null;
    Operation op = new Operation();

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void start() {
        String path;
        path = test.nextLine();
        openCodeFile(path);
        executeCode();
    }

    public void openCodeFile(String codePath) {
        int indexFile, parameterCounter;
        String nextInstruction, instruction[], instructionParameters[];
        boolean toInt;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(codePath));
            indexFile = 0;

            /*
             *	Leitura linha por linha do arquivo do código
             *	para inserir na lista dos comandos
             *	(Obter o comando e valor presente na linha)
             */
            while ((nextInstruction = reader.readLine()) != null) {
                toInt = false;
                nextInstruction = nextInstruction.trim();
                instruction = nextInstruction.split(" ");

                if (instruction[1].contains("NULL")) {                            //Tratamento de LABELS (LX NULL)
                    commands.get(indexFile).increaseCommandID();
                    commands.get(indexFile).setCommandName(instruction[1]);
                    commands.get(indexFile).setParameters(instruction[0]);

                } else {
                    if (instruction[0].contains("JMPF")) {                        //Tratamento de Labels (JMPF LX)
                        commands.get(indexFile).increaseCommandID();
                        commands.get(indexFile).setCommandName(instruction[0]);
                        commands.get(indexFile).setParameters(instruction[1]);

                    } else {
                        commands.get(indexFile).increaseCommandID();
                        commands.get(indexFile).setCommandName(instruction[0]);

                        if (instruction[1].contains(" ")) {                       //Tratamento de mais de um parametro
                            parameterCounter = 0;
                            
                            instructionParameters = instruction[1].split(" ");

                            while (parameterCounter < instructionParameters.length) {
                                commands.get(indexFile).setParameters(instructionParameters[parameterCounter]);
                            }
                            toInt = true;
                        }
                    }

                    if (toInt) {
                        commands.get(indexFile).setIntegerParameters(commands.get(indexFile).getParameters());
                    }
                    indexFile++;
                }
            }
        } catch (FileNotFoundException fileException) {
            System.out.println("Error! Arquivo não encontrado\n");
            fileException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Error! Leitura/Escrita do arquivo\n");
            ioException.printStackTrace();
        }

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioException) {
            System.out.println("Error! Não foi possível fechar o arquivo\n");
            ioException.printStackTrace();
        }
    }

    public void executeCode() {
        int PC = 0;
        ArrayList<Integer> parameters = new ArrayList<Integer>();
        ArrayList<String> labels = new ArrayList<String>();

        while (PC < commands.size()) {
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
                    parameters = null;
                    break;

                case "LDV":
                    parameters = commands.get(PC).getIntegerParameters();
                    op.operationLDV(parameters.get(0));
                    PC++;
                    parameters = null;
                    break;

                case "STR":
                    parameters = commands.get(PC).getIntegerParameters();
                    op.operationSTR(parameters.get(0));
                    PC++;
                    parameters = null;
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
                    parameters = commands.get(PC).getIntegerParameters();
                    PC = op.operationJMP(parameters.get(0));
                    break;

                case "JMPF":
                    labels = commands.get(PC).getParameters();
                    /*
                     *  TODO procurar posição da lista de comandos que possui o label correspondente  
                     */
                    labels = null;
                    break;

                /*
                 *  Entrada e Saida
                 */
                case "RD":
                    int readValue = Integer.parseInt(test.nextLine());
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
                    parameters = null;
                    break;

                case "DALLOC":
                    parameters = commands.get(PC).getIntegerParameters();
                    op.operationDALLOC(parameters.get(0), parameters.get(1));
                    PC++;
                    parameters = null;
                    break;

                /*
                 * Chamada de Rotina
                 */
                case "CALL":
                    parameters = commands.get(PC).getIntegerParameters();
                    PC = op.operationCALL(parameters.get(0), PC);
                    parameters = null;
                    break;

                case "RETURN":
                    PC = op.operationRETURN();
                    break;

                default:
                    System.out.println("Operação não válida\n");
                    break;
            }
        }
    }
}
