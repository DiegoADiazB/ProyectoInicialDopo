
import javax.swing.JOptionPane;
import java.util.List;

/**
 * Visual acceptance test for cycle 3 of SlotMachine.
 * <p>
 * This class is not a unit test: it uses SlotMachineContest to solve the
 * marathon problem on real machines, so that the user can observe on
 * screen how the wheels are moved until every one of them shows the same
 * symbol.
 * <p>
 * The script is split into two runs, and each one builds everything it
 * needs, so they can be executed in any order and on their own. The
 * first one shows the simulation of a normal machine and the second one
 * shows the special cases. A message describing what is about to happen
 * is displayed before each step, so the execution advances at the user's
 * own pace.
 *
 * @author Juan Diego Cardozo Beltrán
 * @author Diego Alejandro Díaz Boada
 * @version 21/09/26
 */
public class CC3AcceptenceTest
{
    /**
     * Runs the complete script: first the simulation of a normal machine
     * and then the special cases.
     */
    public void runAll()
    {
        runValidOperations();
        runSpecialCases();
    }

    /**
     * First run: the simulation of a normal machine.
     * Creates a machine of 4 wheels and 4 random symbols and solves it on
     * screen, showing the configuration before and after the process.
     */
    public void runValidOperations()
    {
        narrate("simulate(4): se crea una máquina VISIBLE de 4 ruedas\n"
              + "y 4 símbolos aleatorios, y se resuelve en pantalla.\n"
              + "Observe cómo giran las ruedas hasta el jackpot.");

        SlotMachineContest contest = new SlotMachineContest();
        contest.simulate(4);
        showResult(contest, "Máquina de 4 ruedas resuelta");

        narrate("Las 4 ruedas muestran el mismo símbolo,\n"
              + "así que distinctSymbols() debe valer 1\n"
              + "y la máquina debe lucir como ganadora.");

        narrate("simulate(6): se repite con una máquina más grande.\n"
              + "Al ser aleatoria, la configuración inicial\n"
              + "es distinta cada vez, pero el resultado es el mismo.");

        SlotMachineContest bigger = new SlotMachineContest();
        bigger.simulate(6);
        showResult(bigger, "Máquina de 6 ruedas resuelta");

        narrate("solve(4): el mismo algoritmo, pero INVISIBLE.\n"
              + "No debe aparecer ninguna máquina en pantalla:\n"
              + "solo se devuelve la secuencia de movimientos.");

        SlotMachineContest silent = new SlotMachineContest();
        List<int[]> actions = silent.solve(4);
        showActions(actions);
        showResult(silent, "Máquina resuelta sin mostrarse");
    }

    /**
     * Second run: special cases.
     * Shows the limits of the simulation: a machine with a single wheel,
     * which already starts as a winner, and the sizes that the machine
     * adjusts by itself.
     */
    public void runSpecialCases()
    {
        narrate("CASOS ESPECIALES.\n"
              + "Se prueban los límites del simulador.\n"
              + "En todos ellos la máquina debe terminar ganadora.");

        narrate("simulate(1): una sola rueda.\n"
              + "Ya está ganadora desde que se crea,\n"
              + "así que no debe hacerse ningún movimiento.");

        SlotMachineContest one = new SlotMachineContest();
        one.simulate(1);
        showResult(one, "Máquina de una sola rueda");

        narrate("simulate(2): el caso más pequeño que sí\n"
              + "necesita mover ruedas.");

        SlotMachineContest two = new SlotMachineContest();
        two.simulate(2);
        showResult(two, "Máquina de dos ruedas");

        narrate("solve(0): un tamaño inválido.\n"
              + "La máquina lo ajusta al mínimo permitido,\n"
              + "así que se resuelve como si fuera de una rueda.");

        SlotMachineContest zero = new SlotMachineContest();
        List<int[]> none = zero.solve(0);
        showActions(none);
        showResult(zero, "Máquina creada con n = 0");

        narrate("solve(60): un tamaño mayor al máximo.\n"
              + "La máquina lo ajusta al máximo permitido.\n"
              + "Se resuelve invisible porque son muchas ruedas.");

        SlotMachineContest big = new SlotMachineContest();
        big.solve(60);
        showResult(big, "Máquina creada con n = 60");

        narrate("En todos los casos distinctSymbols() terminó en 1:\n"
              + "el algoritmo resuelve cualquier máquina,\n"
              + "sin importar su tamaño ni sus colores.");
    }

    /**
     * Displays the state of the machine after being solved.
     *
     * @param contest the contest that solved the machine
     * @param title   the description of the case being shown
     */
    private void showResult(SlotMachineContest contest, String title)
    {
        SlotMachine machine = contest.getMachine();
        String text = title + ":\n"
                    + "  distinctSymbols() = " + machine.distinctSymbols() + "\n"
                    + "  ok() = " + machine.ok();
        JOptionPane.showMessageDialog(null, text);
    }

    /**
     * Displays the movements performed to win, each one as the wheel that
     * was spun and the number of steps it was spun.
     *
     * @param actions the movements returned by solve
     */
    private void showActions(List<int[]> actions)
    {
        String text = "Movimientos realizados: " + actions.size() + "\n";
        for (int[] action : actions) {
            text = text + "  rueda " + action[0] + " -> " + action[1] + " pasos\n";
        }
        JOptionPane.showMessageDialog(null, text);
    }

    /**
     * Displays a message describing the step that is about to be
     * executed. Execution resumes once the user closes the dialog, which
     * allows every change to be observed calmly.
     *
     * @param text the description of the step
     */
    private void narrate(String text)
    {
        JOptionPane.showMessageDialog(null, text);
    }
}