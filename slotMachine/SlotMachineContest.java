
/**
 * Write a description of class SlotMachineContest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.List;
import java.util.ArrayList;

public class SlotMachineContest{
    private List<int[]> actions = new ArrayList<>();
    private int[] position;
    private boolean[] identified;
    private SlotMachine machine;

    public SlotMachine getMachine() {
        return machine;
    }
    
    public List<int[]> solve(int n){
        n = adjust(n);
        machine = new SlotMachine(n);
        position = new int[n + 1];
        identified = new boolean[n + 1];
        makeSymbolsDistinct(n);
        identifySymbols(n);
        alignWheels(n);
        return actions;
    }

    
    public void simulate(int n){
       n = adjust(n);
       machine = new SlotMachine(n);
       machine.makeVisible();
       position = new int[n + 1];
       identified = new boolean[n + 1];
       makeSymbolsDistinct(n);
       identifySymbols(n);
       alignWheels(n);
    }
    
    private int adjust(int n) {
        if (n < 1) {
            n = 1;
        }
        if (n > SlotMachine.WHEELS_NUMBER) {
            n = SlotMachine.WHEELS_NUMBER;
        }
        return n;
    }
    
    private void makeSymbolsDistinct(int n){
        int i = 2;
        for(;i<=n;i++){
            int symDist = machine.distinctSymbols();
            int j = 0;
            boolean found = false;
            while (j < n && !found) {
                machine.spin(i,1);
                j++;
                
                if (machine.distinctSymbols() > symDist){
                    found = true;
                }
            }
            if (found) {
                actions.add(new int[]{i, j});
            }
        }
    }

    private void identifySymbols(int n){
        int l = 1;
        for (;l<n;l++){
            machine.spin(1,1);
            actions.add(new int[]{1, 1});
            int k = 2;
            for (;k<=n;k++){
                if (!identified[k]) {
                    machine.spin(k,n-1);
                    actions.add(new int[]{k, n-1});
                    if (machine.distinctSymbols() == n){
                        position[k] = l-1;
                        identified[k] = true;
                        break;
                    } else {
                        machine.spin(k,1);
                        actions.add(new int[]{k, 1});
                    }
                }
            }
        }
        position[1] = n-1;
    }

    private void alignWheels(int n){
        int m = 1;
        for (;m<=n;m++){
            int steps = (n - position[m]) % n;
            if (steps > 0) {
                machine.spin(m,steps);
                actions.add(new int[]{m, steps});
            }
        }
    }
}