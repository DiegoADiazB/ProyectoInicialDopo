
import javax.swing.JOptionPane;

/**
 * Visual acceptance test for cycle 1 of SlotMachine.
 * <p>
 * This class is not a unit test: it builds an actual machine, makes it
 * visible and runs a script of operations so that the user can observe
 * on screen the behaviour of every method of the cycle (addWheel,
 * delWheel, addSymbol, delSymbol, placeSymbol, spin(int), spin(),
 * symbols, distinctSymbols, configuration, isJackpot, makeVisible,
 * makeInvisible, ok and exit).
 * <p>
 * The script is split into two runs. The first one shows the valid
 * operations and the second one shows the special cases, where the
 * machine rejects the operation and reports it through a dialog. A
 * message describing what is about to happen is displayed before each
 * step, so the execution advances at the user's own pace.
 *
 * @author Juan Diego Cardozo Beltrán
 * @author Diego Alejandro Díaz Boada
 * @version 21/09/26
 */
public class CC1AcceptenceTest
{
    /**
     * The machine on which the demonstration is performed.
     */
    private SlotMachine machine;

    /**
     * Constructs the acceptance test.
     * Creates an empty machine and leaves it visible on screen, ready to
     * start the script.
     */
    public CC1AcceptenceTest()
    {
        machine = new SlotMachine();
        machine.makeVisible();
        machine.addSymbol(1, "red");
         machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        narrate("Máquina lista: 3 símbolos (red, yellow, blue) y 3 ruedas.\n"
          + "Todas las ruedas arrancan con el primer símbolo registrado.");
    }

    /**
     * Runs the complete script: first the valid operations and then the
     * special cases.
     */
    public void runAll()
    {
        runValidOperations();
        runSpecialCases();
    }

    /**
     * First run: every operation is valid.
     * Registers symbols and wheels, places symbols, spins one wheel and
     * all of them, consults the state of the machine, reaches the
     * jackpot and hides and shows the machine again.
     */
    public void runValidOperations()
    {

        narrate("placeSymbol: la rueda 2 pasa a yellow\n"
              + "y la rueda 3 a blue.");
        machine.placeSymbol(2, "yellow");
        machine.placeSymbol(3, "blue");
        showConfiguration();
        
        narrate("addSymbol: añadimos un nuevo simbolo de\n"
            + "de color green a la maquina.");
        machine.addSymbol(4, "green");
        showSymbols();
        
        narrate("distinctSymbols(): las tres ruedas muestran\n"
              + "símbolos diferentes, así que debe valer 3.");
        JOptionPane.showMessageDialog(null, "distinctSymbols() = " + machine.distinctSymbols());

        narrate("spin(3): la rueda 3 avanza un símbolo del ciclo\n"
              + "red -> yellow -> blue -> red -> green.");
        machine.spin(3);
        showConfiguration();

        narrate("spin(): todas las ruedas avanzan un símbolo.");
        machine.spin();
        showConfiguration();

        narrate("delWheel(3): se elimina la rueda 3.\n"
              + "Debe desaparecer de la pantalla.");
        machine.delWheel(3);
        showConfiguration();

        narrate("delSymbol(blue): se elimina el símbolo blue.\n"
              + "Las ruedas que lo mostraban avanzan al siguiente.");
        machine.delSymbol("blue");
        showSymbols();
        showConfiguration();

        narrate("placeSymbol: se dejan las dos ruedas en red.\n"
              + "Al quedar todas iguales debe activarse el JACKPOT\n"
              + "y el cuerpo de la máquina se pinta de amarillo.");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        showConfiguration();

        narrate("isJackpot() = " + machine.isJackpot());

        narrate("makeInvisible(): la máquina desaparece de la pantalla,\n"
              + "pero sigue funcionando.");
        machine.makeInvisible();

        narrate("makeVisible(): la máquina vuelve a aparecer\n"
              + "exactamente como estaba.");
        machine.makeVisible();
        showConfiguration();
    }

    /**
     * Second run: special cases.
     * Every operation in this block must be rejected by the machine,
     * showing the corresponding dialog and leaving the configuration
     * untouched.
     */
    public void runSpecialCases()
    {
        narrate("CASOS ESPECIALES.\n"
              + "Cada operación siguiente debe ser rechazada.\n"
              + "Observe el mensaje de error y que la máquina no cambia.");

        narrate("addWheel(1): ya existe una rueda en esa posición.");
        machine.addWheel(1);

        narrate("addWheel(0) y addWheel(51): posiciones fuera\n"
              + "del rango permitido de ruedas.");
        machine.addWheel(0);
        machine.addWheel(SlotMachine.WHEELS_NUMBER + 1);

        narrate("addSymbol(1, green): ya hay un símbolo\n"
              + "en la posición 1.");
        machine.addSymbol(1, "green");

        narrate("addSymbol(5, red): el color red ya está\n"
              + "registrado en otra posición.");
        machine.addSymbol(5, "red");

        narrate("addSymbol(0, green): la posición 0 no es válida.");
        machine.addSymbol(0, "green");

        narrate("delWheel(9): esa rueda no existe.");
        machine.delWheel(9);

        narrate("delSymbol(green): ese símbolo no existe.");
        machine.delSymbol("green");

        narrate("placeSymbol(9, red): esa rueda no existe.");
        machine.placeSymbol(9, "red");

        narrate("placeSymbol(1, green): ese símbolo no existe.");
        machine.placeSymbol(1, "green");

        narrate("spin(9): esa rueda no existe.");
        machine.spin(9);

        showConfiguration();
        narrate("La configuración es la misma con la que terminó\n"
              + "el primer recorrido: ninguna operación inválida\n"
              + "alteró el estado de la máquina.");
    }

    /**
     * Ends the demonstration by closing the simulator. Since exit()
     * terminates the virtual machine, this is always the last step.
     */
    public void finish()
    {
        narrate("exit(): se termina el simulador.");
        machine.exit();
    }

    /**
     * Displays the symbols currently registered in the machine, ordered
     * by position.
     */
    private void showSymbols()
    {
        String[] symbols = machine.symbols();
        String text = "Registered symbols:\n";
        for (int i = 0; i < symbols.length; i++) {
            text = text + "  " + (i + 1) + " -> " + symbols[i] + "\n";
        }
        text = text + "ok() = " + machine.ok();
        JOptionPane.showMessageDialog(null, text);
    }

    /**
     * Displays the current configuration of the machine in a dialog,
     * that is, the symbol of every wheel ordered by position.
     */
    private void showConfiguration()
    {
        String[] conf = machine.configuration();
        String text = "Current configuration:\n";
        for (int i = 0; i < conf.length; i++) {
            text = text + "  wheel " + (i + 1) + " -> " + conf[i] + "\n";
        }
        text = text + "ok() = " + machine.ok();
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