package virtualmachine;

import java.util.ArrayList;

public class Operation {

    private int value1, value2, result;
    private final Memory memoryStack = Memory.getInstance();

    /*
     * 	Carregar Constante, Carregar Valor, Atribuição
     */
    public void operationLDC(int k) throws Exception {
        memoryStack.pushValue(k);
    }

    public void operationLDV(int n) throws Exception {
        value1 = memoryStack.getValue(n);
        memoryStack.pushValue(value1);
    }

    public void operationSTR(int n) throws Exception {
        value1 = memoryStack.popValue();
        memoryStack.insertValue(value1, n);
    }

    /*
     * 	Operações Aritmeticas
     */
    public void operationADD() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();
        result = value2 + value1;
        memoryStack.pushValue(result);
    }

    public void operationSUB() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();
        result = value2 - value1;
        memoryStack.pushValue(result);
    }

    public void operationMULT() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();
        result = value2 * value1;
        memoryStack.pushValue(result);
    }

    public void operationDIVI() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();
        result = value2 / value1;
        memoryStack.pushValue(result);
    }

    public void operationINV() throws Exception {
        value1 = memoryStack.popValue();
        result = value1 * (-1);
        memoryStack.pushValue(result);
    }

    /*
     * 	Operações Lógicas
     */
    public void operationAND() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();

        if (value2 == 1 && value1 == 1) {
            result = 1;
        } else {
            result = 0;
        }

        memoryStack.pushValue(result);
    }

    public void operationOR() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();

        if (value2 == 1 || value1 == 1) {
            result = 1;
        } else {
            result = 0;
        }

        memoryStack.pushValue(result);
    }

    public void operationNEG() throws Exception {
        value1 = memoryStack.popValue();

        result = 1 - value1;

        memoryStack.pushValue(result);
    }

    /*
     * Operações de Comparação
     */
    public void operationCME() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();

        if (value2 < value1) {
            result = 1;
        } else {
            result = 0;
        }

        memoryStack.pushValue(result);
    }

    public void operationCMA() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();

        if (value2 > value1) {
            result = 1;
        } else {
            result = 0;
        }

        memoryStack.pushValue(result);
    }

    public void operationCEQ() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();

        if (value2 == value1) {
            result = 1;
        } else {
            result = 0;
        }

        memoryStack.pushValue(result);
    }

    public void operationCDIF() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();

        if (value2 != value1) {
            result = 1;
        } else {
            result = 0;
        }

        memoryStack.pushValue(result);
    }

    public void operationCMEQ() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();

        if (value2 <= value1) {
            result = 1;
        } else {
            result = 0;
        }

        memoryStack.pushValue(result);
    }

    public void operationCMAQ() throws Exception {
        value1 = memoryStack.popValue();
        value2 = memoryStack.popValue();

        if (value2 >= value1) {
            result = 1;
        } else {
            result = 0;
        }

        memoryStack.pushValue(result);
    }

    /*
     * Operações de Desvio
     */
    public int operationJMP(ArrayList<Command> list, String label) throws Exception {
        int counter = 0, jumpValue = -1;
        /*
         *  Recuperar posição do label na lista de comandos
         */
        while (counter < list.size()) {
            if (list.get(counter).getParameters().contains(label)) {
                if (list.get(counter).getCommandName().equals("NULL")) {
                    jumpValue = list.get(counter).getCommandLine();
                }
            }
            counter++;
        }

        return (jumpValue);
    }

    public int operationJMPF(ArrayList<Command> list, int PC, String label) throws Exception {
        int counter = 0, jumpValue = -1;
        /*
         *  Recuperar posição do label na lista de comandos
         */
        while (counter < list.size()) {
            if (list.get(counter).getParameters().contains(label)) {
                if (list.get(counter).getCommandName().equals("NULL")) {
                    jumpValue = list.get(counter).getCommandLine();
                }
            }
            counter++;
        }

        value1 = memoryStack.popValue();

        if (value1 == 0) {
            return (jumpValue);
        } else {
            return (PC++);
        }
    }

    /*
     *	Operações de Entrada e Saida
     */
    public void operationRD(int readValue) throws Exception {
        memoryStack.pushValue(readValue);
    }

    public int operationPRN() throws Exception {
        value1 = memoryStack.popValue();
        return (value1);
    }

    /*
     *	Operações de Alocação e Desalocação
     */
    public void operationALLOC(int m, int n) {
        int k;
        for (k = 0; k <= n - 1; k++) {
            memoryStack.pushValue(0);
            value1 = memoryStack.getValue(m + k);
            memoryStack.insertValue(value1, memoryStack.getStackSize());
        }
    }

    public void operationDALLOC(int m, int n) throws Exception {
        int k;
        for (k = n - 1; k >= 0; k--) {
            value1 = memoryStack.popValue();
            memoryStack.insertValue(value1, m + k);
        }
    }

    /*
     *	Operações de Chamada de Rotina
     */
    public int operationCALL(ArrayList<Command> list, int PC, String label) throws Exception {
        int counter = 0, newPC = -1;
        /*
         *  Recuperar posição do label na lista de comandos
         */
        while (counter < list.size()) {
            if (list.get(counter).getParameters().contains(label)) {
                if (list.get(counter).getCommandName().equals("NULL")) {
                    newPC = list.get(counter).getCommandLine();
                }
            }
            counter++;
        }
        memoryStack.pushValue(PC++);
        return (newPC);
    }

    public int operationRETURN() throws Exception {
        result = memoryStack.popValue();
        return (result);
    }

    public int operationRETURNF(int m, int n) throws Exception {
        int newPC;

        result = memoryStack.popValue();
        operationDALLOC(m, n);

        newPC = operationRETURN();
        operationLDC(result);

        return (newPC);
    }
}
