/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualmachine;

/**
 *
 * @author 16104325
 */
public class Operation {
    private int value1, value2, result;
	private Memory memoryStack = Memory.getInstance();
	
	/*
	 * 	Carregar Constante, Carregar Valor, Atribuição
	 */
	public void operationLDC(int k) {
            memoryStack.pushValue(k);
	}
	
	public void operationLDV(int n) {
            value1 = memoryStack.getValue(n);
            memoryStack.pushValue(value1);
	}
	
	public void operationSTR(int n) {
            value1 = memoryStack.popValue();
            memoryStack.decreaseStackSize();
            memoryStack.insertValue(value1, n);
	}
	
	/*
	 * 	Operações Aritmeticas
	 */
	public void operationADD() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            result = value1 + value2;
            memoryStack.pushValue(result);
	}
	
	public void operationSUB() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            result = value1 - value2;
            memoryStack.pushValue(result);
	}
	
	public void operationMULT() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            result = value1 * value2;
            memoryStack.pushValue(result);            
	}
	
	public void operationDIVI(){
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            result = value1 / value2;
            memoryStack.pushValue(result);           
	}
	
	public void operationINV() {
            value1 = memoryStack.popValue();
            result = value1 * (-1);
            memoryStack.pushValue(result);
	}
	
	/*
	 * 	Operações Lógicas
	 */
	public void operationAND() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            
            if (value2 == 1 && value1 == 1)
                result = 1;
            else
                result = 0;
            
            memoryStack.pushValue(result);
	}
	
	public void operationOR() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            
            if (value2 == 1 || value1 == 1)
                result = 1;
            else
                result = 0;
            
            memoryStack.pushValue(result);
	}
	
	public void operationNEG() {
            value1 = memoryStack.popValue();
            
            result = 1 - value1;
            
            memoryStack.pushValue(result);            
	}
	
	/*
	 * Operações de Comparação
	 */
	public void operationCME() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            
            if (value2 < value1)
                result = 1;
            else
                result = 0;
            
            memoryStack.pushValue(result);
	}
	
	public void operationCMA() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            
            if (value2 > value1)
                result = 1;
            else
                result = 0;
            
            memoryStack.pushValue(result);
	}
	
	
	public void operationCEQ() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            
            if (value2 == value1)
                result = 1;
            else
                result = 0;
            
            memoryStack.pushValue(result);
	}
	
	public void operationCDIF() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            
            if (value2 != value1)
                result = 1;
            else
                result = 0;
            
            memoryStack.pushValue(result);
	}
	
	public void operationCMEQ() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            
            if (value2 <= value1)
                result = 1;
            else
                result = 0;
            
            memoryStack.pushValue(result);
	}
	
	public void operationCMAQ() {
            value1 = memoryStack.popValue();
            value2 = memoryStack.popValue();
            
            if (value2 >= value1)
                result = 1;
            else
                result = 0;
            
            memoryStack.pushValue(result);
	}
	
	/*
	 * Operações de Desvio
	 */
	 public int operationJMP(int jumpValue) {
            return(jumpValue);
	 }

	public int operationJMPF(int jumpValue, int pc) {
            value1 = memoryStack.popValue();
            
            if (value1 == 0)
                return(jumpValue);
            else
                return(pc++);
	}

	/*
	 *	Operações de Entrada e Saida
	 */			
	public void operationRD(int readValue) {
            memoryStack.pushValue(readValue);
	}

	public int operationPRN() {
            value1 = memoryStack.popValue();
            return (value1);
	}

	/*
	 *	Operações de Alocação e Desalocação
	 */
	public void operationALLOC(int m, int n) {
            int k;
            for(k = 0; k <= n-1; k++){
                value1 = memoryStack.getValue(m+k);
                memoryStack.pushValue(value1);
            }
	}

	public void operationDALLOC(int m, int n) {
            int k;
            for(k = n-1; k >= 0; k++){
                value1 = memoryStack.popValue();
                memoryStack.insertValue(value1, m+k);
            } 
	}

	/*
	*	Operações de Chamada de Rotina
	*/
	public int operationCALL(int newPC, int PC) {
            memoryStack.pushValue(PC++);
            return(newPC);
	}

	public int operationRETURN() {
            result = memoryStack.popValue();
            return(result);
	}
}
