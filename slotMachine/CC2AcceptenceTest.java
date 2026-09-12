import javax.swing.JOptionPane;

/**
 * Visual acceptance test for cycle 2 of SlotMachine.
 * <p>
 * This class is not a unit test: it builds an actual machine, makes it
 * visible and runs a script of operations so that the user can observe
 * on screen the behaviour of the five new methods of the cycle
 * (lock, unlock, swap, spin(int,int) and spin(String[])).
 * <p>
 * The script is split into two runs. The first one shows the valid
 * operations and the second one shows the special cases, where the
 * machine rejects the operation and reports it through a dialog. A
 * message describing what is about to happen is displayed before each
 * step, so the execution advances at the user's own pace.
 *
 * @author Juan Diego Cardozo Beltrán
 * @author Diego Alejandro Díaz Boada
 * @version 09/09/26
 */
public class CC2AcceptenceTest
{
    /**
     * The machine on which the demonstration is performed.
     */
    private SlotMachine machine;

    /**
     * Constructs the acceptance test.
     * Creates the machine, registers three symbols and three wheels, and
     * leaves everything visible on screen, ready to start the script.
     */
    public CC2AcceptenceTest()
    {
        machine = new SlotMachine();
        machine.makeVisible();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "green");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        narrate("Máquina lista: 3 símbolos (red, yellow, green) y 3 ruedas.\n"
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
        narrate("Fin de la prueba de aceptación del ciclo 2.");
    }

    /**
     * First run: every operation is valid.
     * Assigns a defined configuration, swaps two wheels, locks one wheel,
     * spins another one several steps and finally unlocks it to check
     * that the wheel responds again.
     */
    public void runValidOperations()
    {
        narrate("spin(String[]): se asigna la configuración\n"
              + "{yellow, green, red} de una sola vez.");
        machine.spin(new String[]{"yellow", "green", "red"});
        showConfiguration();

        narrate("swap(1,3): las ruedas 1 y 3 intercambian su símbolo.\n"
              + "El amarillo y el rojo deben cruzarse.");
        machine.swap(1, 3);
        showConfiguration();

        narrate("lock(2): la rueda 2 queda bloqueada.\n"
              + "A partir de ahora debe ignorar cualquier giro.");
        machine.lock(2);

        narrate("spin(1,3): la rueda 1 avanza 3 pasos del ciclo\n"
              + "red -> yellow -> green -> red, con animación.");
        machine.spin(1, 3);
        showConfiguration();

        narrate("spin(2,2): se intenta girar la rueda BLOQUEADA.\n"
              + "La máquina debe rechazarlo y no mover nada.");
        machine.spin(2, 2);
        showConfiguration();

        narrate("unlock(2): se libera la rueda 2.");
        machine.unlock(2);

        narrate("spin(2,1): ahora sí gira. Como está en el último símbolo\n"
              + "del ciclo, debe volver al primero (red).");
        machine.spin(2, 1);
        showConfiguration();

        narrate("spin(String[]): se fuerza {red, red, red}.\n"
              + "Al quedar todas iguales debe activarse el JACKPOT\n"
              + "y el cuerpo de la máquina se pinta de amarillo.");
        machine.spin(new String[]{"red", "red", "red"});
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

        narrate("lock(99) y unlock(99): la rueda 99 no existe.");
        machine.lock(99);
        machine.unlock(99);

        narrate("unlock(1): la rueda 1 nunca fue bloqueada.");
        machine.unlock(1);

        narrate("lock(1) dos veces seguidas:\n"
              + "el primero funciona, el segundo debe avisar\n"
              + "que la rueda ya estaba bloqueada.");
        machine.lock(1);
        machine.lock(1);

        narrate("swap(1,2) y swap(2,1): la rueda 1 está bloqueada,\n"
              + "sin importar en qué posición del intercambio esté.");
        machine.swap(1, 2);
        machine.swap(2, 1);

        narrate("swap(2,99): la segunda rueda no existe.");
        machine.swap(2, 99);

        narrate("spin(1,3): la rueda 1 sigue bloqueada.\n"
              + "spin(99,3): la rueda 99 no existe.");
        machine.spin(1, 3);
        machine.spin(99, 3);

        narrate("spin(String[]) con {green}: hay 3 ruedas y 1 solo símbolo.\n"
              + "Los tamaños no coinciden.");
        machine.spin(new String[]{"green"});

        narrate("spin(String[]) con 3 símbolos correctos,\n"
              + "pero la rueda 1 sigue bloqueada.");
        machine.spin(new String[]{"green", "red", "yellow"});

        showConfiguration();
        narrate("La configuración es la misma con la que terminó\n"
              + "el primer recorrido: ninguna operación inválida\n"
              + "alteró el estado de la máquina.");

        machine.unlock(1);
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