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
        if(instance == null ) {
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
        int indexFile;
        String nextInstruction, instruction[], newCommand, parameters[], newValue;
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
                instruction = nextInstruction.split(" ");
                newCommand = instruction[0];
                commands.get(indexFile).increaseCommandID();
                commands.get(indexFile).setCommandName(newCommand);
				
                if(instruction[1].contains(" ")) {							
                    parameters = instruction[1].split(" ");
                    newValue = parameters[0];			
                    commands.get(indexFile).setFirstParameter(Integer.parseInt(newValue));
					
                    newValue = parameters[1];
                    commands.get(indexFile).setSecondParameter(Integer.parseInt(newValue));			
		} else {
                    newValue = instruction[1];
                    commands.get(indexFile).setFirstParameter(Integer.parseInt(newValue));
		}
		indexFile++;
            }  
        }	
        catch(FileNotFoundException fileException) {	
            System.out.println("Error! Arquivo não encontrado\n");
            fileException.printStackTrace();
        }
        catch (IOException ioException) {
            System.out.println("Error! Leitura/Escrita do arquivo\n");
            ioException.printStackTrace();
        }
        finally {
            try {
                if(reader != null){
                    reader.close();
                }
            }
            catch(IOException ioException){
                System.out.println("Error! Não foi possível fechar o arquivo\n");
                ioException.printStackTrace();
            }
        }
    }
	
    public void executeCode() {
        int PC = 0;
		
        while(PC < commands.size()) {
            switch(commands.get(PC).getCommandName()) {
                /*
                *  Iniciar/Finalizar Execução
                */
                case "START":   virtualMemory = Memory.getInstance();
                                PC++;
                                break;
				
                case "HLT":     PC++;
                                break;
			
                /*
                 * 	Carregar Constante, Carregar Valor, Atribuição e Nulo
                 */
                case "LDC":     op.operationLDC(commands.get(PC).getFirstParameter());
                                PC++;
                                break;

                case "LDV":     op.operationLDV(commands.get(PC).getFirstParameter());
                                PC++;
                                break;

                case "STR":     op.operationSTR(commands.get(PC).getFirstParameter());
                                PC++;
                                break;

                case "NULL":    PC++;
                                break;
				
                /*
		 *	Operações Aritméticas
		 */
		case "ADD":     op.operationADD();
                                PC++;
                                break;
				
                case "SUB":     op.operationSUB();
                                PC++;
                                break;
				
		case "MULT":    op.operationMULT();
                                PC++;
				break;
					
		case "DIVI":    op.operationDIVI();
                                PC++;
				break;
					
		case "INV":     op.operationINV();
                                PC++;
				break;
					 
		/*
                 *	Operações Lógicas
		 */
		case "AND":     op.operationAND();
                                PC++;
				break;
					 
                case "OR": 	op.operationOR();
                                PC++;
				break;
					
		case "NEG":     op.operationNEG();
                                PC++;
				break;
					
		/*
                 *	Operações de Comparação
		 */
		case "CME":     op.operationCME();
                                PC++;
				break;
					
		case "CMA":     op.operationCMA();
                                PC++;
				break;
					
		case "CEQ":     op.operationCEQ();
                                PC++;
				break;
					
		case "CDIF":    op.operationCDIF();
                                PC++;
				break;
					
		case "CMEQ":    op.operationCMEQ();
                                PC++;
				break;
					
		case "CMAQ":    op.operationCMAQ();
                                PC++;
				break;
				
		/*
		 * Desvios
		 */
		case "JMP":     PC = op.operationJMP(commands.get(PC).getFirstParameter());
				break;
				
		case "JMPF":    PC = op.operationJMPF(commands.get(PC).getFirstParameter(), PC);
                                break;
				
		/*
		 *  Entrada e Saida
		 */			
		case "RD":      int readValue = Integer.parseInt(test.nextLine());
                                op.operationRD(readValue);
                                PC++;
                                break;
				
		case "PRN":     int printValue = op.operationPRN();
                                PC++;
                                break;
				
		/*
		 * Alocação e Desalocação de Váriavel
		 */
		case "ALLOC": 	op.operationALLOC(commands.get(PC).getFirstParameter(), commands.get(PC).getSecondParameter());
                                PC++;
				break;
				
		case "DALLOC":	op.operationDALLOC(commands.get(PC).getFirstParameter(), commands.get(PC).getSecondParameter());
                                PC++;
				break;
				
		/*
		 * Chamada de Rotina
		 */
		case "CALL":	PC = op.operationCALL(commands.get(PC).getFirstParameter(), PC);
      				break;
				
		case "RETURN":  PC = op.operationRETURN();
				break;
								
                default:        System.out.println("Operação não válida\n");
				break;
            }
	}
    }
}