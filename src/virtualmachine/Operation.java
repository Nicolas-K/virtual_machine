package virtualmachine;

import java.util.ArrayList;

public class Operation {

    private int value1, value2, result;
    private Memory virtualStack = Memory.getInstance();

    private void getOperands() {
        value1 = virtualStack.popValue();
        value2 = virtualStack.popValue();
    }

    private void storeResult() {
        virtualStack.pushValue(result);
    }

    private synchronized int getLabel(ArrayList<Command> commandList, String label) {
        Integer position = null;
        String instruction, instructionLabel;

        for (int index = 0; index < commandList.size(); index++) {
            instruction = commandList.get(index).getCommandName();

            if (instruction.equals("NULL")) {
                instructionLabel = commandList.get(index).getParameters().get(0);

                if (instructionLabel.equals(label)) {
                    position = index;
                    break;
                }
            }
        }

        return position;
    }

    /*
     *  Memoria
     */
    public void LDC(int k) {
        virtualStack.pushValue(k);
    }

    public void LDV(int n) {
        virtualStack.pushValue(virtualStack.getValue(n));
    }

    public void STR(int n) {
        int a = virtualStack.popValue();
        virtualStack.insertValue(n, a);
    }

    public void ALLOC(int m, int n) {
        int k, value;

        for (k = 0; k <= n - 1; k++) {
            virtualStack.pushValue(0);
            value = virtualStack.getValue(m + k);
            virtualStack.insertValue((virtualStack.getStackSize() - 1), value);
        }
    }

    public void DALLOC(int m, int n) {
        int k, value;

        for (k = n - 1; k >= 0; k--) {
            value = virtualStack.popValue();
            virtualStack.insertValue(m + k, value);
        }
    }

    /*
     *  Aritmeticas
     */
    public void ADD() {
        getOperands();
        result = value2 + value1;
        storeResult();
    }

    public void SUB() {
        getOperands();
        result = value2 - value1;
        storeResult();
    }

    public void MULT() {
        getOperands();
        result = value2 * value1;
        storeResult();
    }

    public void DIVI() {
        getOperands();
        result = value2 / value1;
        storeResult();
    }

    public void INV() {
        result = virtualStack.popValue() * (-1);
        storeResult();
    }

    /*
     *  Logicas
     */
    public void AND() {
        getOperands();

        if (value2 == 1 && value1 == 1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    public void OR() {
        getOperands();

        if (value2 == 1 || value1 == 1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    public void NEG() {
        result = 1 - virtualStack.popValue();
        storeResult();
    }

    /*
     *  Comparação
     */
    public void CME() {
        getOperands();

        if (value2 < value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    public void CMA() {
        getOperands();

        if (value2 > value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    public void CEQ() {
        getOperands();

        if (value2 == value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    public void CDIF() {
        getOperands();

        if (value2 != value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    public void CMEQ() {
        getOperands();

        if (value2 <= value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    public void CMAQ() {
        getOperands();

        if (value2 >= value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    /*
     *  Controle da VM
     */
    public void START() {
        virtualStack.resetMemory();
    }

    public void HLT() {
        virtualStack = null;
    }

    public int RETURN() {
        return virtualStack.popValue();
    }

    public int RETURNF(int m, int n) {
        int pc;

        result = virtualStack.popValue();

        if (m != -1 && n != -1) {
            DALLOC(m, n);
        }

        pc = RETURN();
        storeResult();
        return pc;
    }

    /*
     *  Entrada e Saida
     */
    public void RD(Integer input) {
        virtualStack.pushValue(input);
    }

    public int PRN() {
        return virtualStack.popValue();
    }

    /*
     *  Saltos 
     */
    public int JMP(ArrayList<Command> list, String jmpLabel) {
        return getLabel(list, jmpLabel);
    }

    public int JMPF(ArrayList<Command> list, String jmpfLabel, int currentPC) {
        int value = virtualStack.popValue();

        if (value == 0) {
            return getLabel(list, jmpfLabel);
        } else {
            return currentPC;
        }
    }

    public int CALL(ArrayList<Command> list, String callLabel, int currentPC) {
        virtualStack.pushValue(currentPC);
        return getLabel(list, callLabel);
    }
}
