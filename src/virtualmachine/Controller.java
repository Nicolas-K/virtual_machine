/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualmachine;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author 16104325
 */
public class Controller {
    private static Controller instance;
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
        
        }
	
	public void openCodeFile(String codePath) {
            int indexFile;
            String newLine, receiveCommand[], newCommand, receiveValues[], newValue;
            BufferedReader reader = null;
		
            try {	    
            	reader = new BufferedReader(new FileReader(codePath));
		indexFile = 0;
			
		/*
		 *	Leitura linha por linha do arquivo do código
		 *	para inserir na lista dos comandos
		 *	(Obter o comando e valor presente na linha)
		 */
		while ((newLine = reader.readLine()) != null) {			
                    receiveCommand = newLine.split(" ");   //Dividir em 2 strings (Separação no espaço)
                    newCommand = receiveCommand[0];				
                    commands.get(indexFile).setCommandName(newCommand);
				
                    if(receiveCommand[1].contains(",")) {  //Conter virgular = dois valores junto com comando							
			receiveValues = receiveCommand[1].split(",");
			newValue = receiveValues[0];			
			commands.get(indexFile).setFirstValue(Integer.parseInt(newValue));
					
			newValue = receiveValues[1];
			commands.get(indexFile).setSecondValue(Integer.parseInt(newValue));			
		    } else {
			newValue = receiveCommand[1];
			commands.get(indexFile).setFirstValue(Integer.parseInt(newValue));
		    }
		    indexFile++;
		}  
            }
		
            catch(FileNotFoundException fileException) {						
		System.out.println("Arquivo do código não encontrado\n");
		fileException.printStackTrace();
            }
            catch (IOException ioException) {
		ioException.printStackTrace();
            }
            finally {
		try {
                    if (reader != null) {
                        reader.close();
                    }
                } 
		catch (IOException ioException) {
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
		case "START":	virtualMemory = Memory.getInstance();
                                PC++;
				break;
				
		case "HLT": 	PC++;
                        	break;
			
		/*
		* 	Carregar Constante, Carregar Valor, Atribuição e Nulo
		*/
		case "LDC":	op.operationLDC(commands.get(PC).getFirstValue());
                                PC++;
				break;
				
		case "LDV":     op.operationLDV(commands.get(PC).getFirstValue());
                                PC++;
				break;
				
		case "STR":     op.operationSTR(commands.get(PC).getFirstValue());
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
		case "JMP":     PC = op.operationJMP(commands.get(PC).getFirstValue());
				break;
				
		case "JMPF":    PC = op.operationJMPF(commands.get(PC).getFirstValue(), PC);
                                break;
				
		/*
		 *  Entrada e Saida
		*/			
		case "RD":      //op.operationRD();
                                PC++;
                                break;
				
		case "PRN":     //op.operationOR();
                                PC++;
                                break;
				
		/*
		 * Alocação e Desalocação de Váriavel
		*/
		case "ALLOC": 	op.operationALLOC(commands.get(PC).getFirstValue(), commands.get(PC).getSecondValue());
                                PC++;
				break;
				
		case "DALLOC":	op.operationDALLOC(commands.get(PC).getFirstValue(), commands.get(PC).getSecondValue());
                                PC++;
				break;
				
		/*
		 * Chamada de Rotina
		 */
		case "CALL":	PC = op.operationCALL(commands.get(PC).getFirstValue(), PC);
      				break;
				
		case "RETURN":  PC = op.operationRETURN();
				break;
								
		default:        System.out.println("Operação não válida\n");
				break;
			}
		}
	}
}