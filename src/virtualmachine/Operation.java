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
    protected void LDC(int k) {
        virtualStack.pushValue(k);
    }

    protected void LDV(int n) {
        virtualStack.pushValue(virtualStack.getValue(n));
    }

    protected void STR(int n) {
        int a = virtualStack.popValue();
        virtualStack.insertValue(n, a);
    }

    protected void ALLOC(int m, int n) {
        int k, value, stackPos;

        for (k = 0; k <= n - 1; k++) {
            virtualStack.pushValue(0);
            stackPos = virtualStack.getStackSize() - 1;
            value = virtualStack.getValue(m + k);
            virtualStack.insertValue(stackPos, value);
        }
    }

    protected void DALLOC(int m, int n) {
        int k, value;

        for (k = n - 1; k >= 0; k--) {
            value = virtualStack.popValue();
            virtualStack.insertValue(m + k, value);
        }
    }

    /*
     *  Aritmeticas
     */
    protected void ADD() {
        getOperands();
        result = value2 + value1;
        storeResult();
    }

    protected void SUB() {
        getOperands();
        result = value2 - value1;
        storeResult();
    }

    protected void MULT() {
        getOperands();
        result = value2 * value1;
        storeResult();
    }

    protected void DIVI() {
        getOperands();
        result = value2 / value1;
        storeResult();
    }

    protected void INV() {
        result = -virtualStack.popValue();
        storeResult();
    }

    /*
     *  Logicas
     */
    protected void AND() {
        getOperands();

        if (value2 == 1 && value1 == 1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    protected void OR() {
        getOperands();

        if (value2 == 1 || value1 == 1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    protected void NEG() {
        result = 1 - virtualStack.popValue();
        storeResult();
    }

    /*
     *  Comparação
     */
    protected void CME() {
        getOperands();

        if (value2 < value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    protected void CMA() {
        getOperands();

        if (value2 > value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    protected void CEQ() {
        getOperands();

        if (value2 == value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    protected void CDIF() {
        getOperands();

        if (value2 != value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    protected void CMEQ() {
        getOperands();

        if (value2 <= value1) {
            result = 1;
        } else {
            result = 0;
        }

        storeResult();
    }

    protected void CMAQ() {
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
    protected void START() {
        virtualStack.resetMemory();
    }

    protected void HLT() {
        virtualStack = null;
    }

    protected int RETURN() {
        return virtualStack.popValue();
    }

    protected int RETURNF(int m, int n) {
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
    protected void RD(Integer input) {
        virtualStack.pushValue(input);
    }

    protected int PRN() {
        return virtualStack.popValue();
    }

    /*
     *  Saltos 
     */
    protected int JMP(ArrayList<Command> list, String jmpLabel) {
        return getLabel(list, jmpLabel);
    }

    protected int JMPF(ArrayList<Command> list, String jmpfLabel, int currentPC) {
        int labelPos = getLabel(list, jmpfLabel);
        int value = virtualStack.popValue();

        if (value == 0) {
            return labelPos;
        } else {
            return currentPC;
        }
    }

    protected int CALL(ArrayList<Command> list, String callLabel, int currentPC) {
        int callPos = getLabel(list, callLabel);
        virtualStack.pushValue(currentPC);
        return callPos;
    }
}
