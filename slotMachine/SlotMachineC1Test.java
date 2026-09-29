import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Acceptance tests for the cycle 1 methods of SlotMachine.
 * For every method there is one test that checks the expected behavior
 * and one test for each case in which the method should not work.
 * Every test checks ok() (when it applies) and the resulting state of
 * the machine.
 *
 * @author Juan Diego Cardozo Beltrán
 * @author Diego Alejandro Díaz Boada
 * @version 21/09/26
 */
public class SlotMachineC1Test
{
    private SlotMachine machine;

    /**
     * Default constructor for test class SlotMachineC1Test.
     */
    public SlotMachineC1Test()
    {
    }

    /**
     * Sets up the test fixture. Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        machine = new SlotMachine();
    }

    /**
     * Tears down the test fixture. Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        machine = null;
    }

    // --------------------------------------------------------- SlotMachine

    /**
     * A new machine should start without wheels and without symbols.
     */
    @Test
    public void shouldCreateAnEmptySlotMachine() {
        assertEquals(0, machine.configuration().length);
        assertEquals(0, machine.symbols().length);
        assertTrue(machine.ok());
    }

    // ------------------------------------------------------------ addWheel

    /**
     * A wheel should be added and should show the first symbol.
     */
    @Test
    public void shouldAddAWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");

        machine.addWheel(1);

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    /**
     * A wheel should not be added in an occupied position.
     */
    @Test
    public void shouldNotAddAWheelInAnOccupiedPosition() {
        machine.addWheel(1);

        machine.addWheel(1);

        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    /**
     * A wheel should not be added in a position lower than 1.
     */
    @Test
    public void shouldNotAddAWheelInAPositionLowerThanOne() {
        machine.addWheel(0);

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    /**
     * A wheel should not be added in a position greater than the maximum.
     */
    @Test
    public void shouldNotAddAWheelInAPositionGreaterThanTheMaximum() {
        machine.addWheel(SlotMachine.WHEELS_NUMBER + 1);

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    // ------------------------------------------------------------ delWheel

    /**
     * An existing wheel should be removed.
     */
    @Test
    public void shouldDeleteAWheel() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.addWheel(2);

        machine.delWheel(1);

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    /**
     * A wheel that does not exist should not be removed and the other
     * wheels should remain.
     */
    @Test
    public void shouldNotDeleteANotCreatedWheel() {
        machine.addWheel(1);

        machine.delWheel(2);

        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    // ----------------------------------------------------------- addSymbol

    /**
     * A symbol should be added.
     */
    @Test
    public void shouldAddASymbol() {
        machine.addSymbol(1, "red");

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    /**
     * A symbol should not be added in an occupied position.
     */
    @Test
    public void shouldNotAddASymbolInAnOccupiedPosition() {
        machine.addSymbol(1, "red");

        machine.addSymbol(1, "yellow");

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    /**
     * A symbol with a color that already exists should not be added.
     */
    @Test
    public void shouldNotAddARepeatedSymbol() {
        machine.addSymbol(1, "red");

        machine.addSymbol(2, "red");

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    /**
     * A symbol should not be added in a position lower than 1.
     */
    @Test
    public void shouldNotAddASymbolInAPositionLowerThanOne() {
        machine.addSymbol(0, "red");

        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    // ----------------------------------------------------------- delSymbol

    /**
     * A symbol should be removed and the wheels that showed it should
     * move to the next symbol.
     */
    @Test
    public void shouldDeleteASymbol() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);

        machine.delSymbol("red");

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"yellow"}, machine.symbols());
        assertArrayEquals(new String[]{"yellow"}, machine.configuration());
    }

    /**
     * A symbol that does not exist should not be removed.
     */
    @Test
    public void shouldNotDeleteANotCreatedSymbol() {
        machine.addSymbol(1, "red");

        machine.delSymbol("blue");

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    // --------------------------------------------------------- placeSymbol

    /**
     * An existing symbol should be placed on an existing wheel.
     */
    @Test
    public void shouldPlaceASymbol() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.addWheel(2);

        machine.placeSymbol(2, "yellow");

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "yellow"}, machine.configuration());
    }

    /**
     * A symbol should not be placed on a wheel that does not exist.
     */
    @Test
    public void shouldNotPlaceASymbolOnANotCreatedWheel() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);

        assertThrows(NullPointerException.class, () -> machine.placeSymbol(2, "red"));

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    /**
     * A symbol that does not exist should not be placed on a wheel.
     */
    @Test
    public void shouldNotPlaceANotCreatedSymbol() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);

        machine.placeSymbol(1, "blue");

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    // ---------------------------------------------------------- spin(int)

    /**
     * A wheel should move to the next symbol.
     */
    @Test
    public void shouldSpinAWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);

        machine.spin(1);

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"yellow"}, machine.configuration());
    }

    /**
     * A wheel that does not exist should not spin.
     */
    @Test
    public void shouldNotSpinANotCreatedWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);

        assertThrows(NullPointerException.class, () -> machine.spin(2));

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    /**
     * A wheel should not spin if there are no symbols.
     */
    @Test
    public void shouldNotSpinAWheelWithoutSymbols() {
        machine.addWheel(1);

        machine.spin(1);

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"white"}, machine.configuration());
    }

    // -------------------------------------------------------------- spin()

    /**
     * All the wheels should move to the next symbol.
     */
    @Test
    public void shouldSpinAllTheWheels() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "yellow");

        machine.spin();

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"yellow", "red"}, machine.configuration());
    }

    /**
     * Spinning should fail when there are no wheels.
     */
    @Test
    public void shouldNotSpinWithoutWheels() {
        machine.addSymbol(1, "red");

        machine.spin();

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    // ------------------------------------------------------------ symbols

    /**
     * The symbols should be returned ordered by position.
     */
    @Test
    public void shouldConsultTheSymbols() {
        machine.addSymbol(3, "blue");
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");

        String[] symbols = machine.symbols();

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "yellow", "blue"}, symbols);
    }

    // ---------------------------------------------------- distinctSymbols

    /**
     * The number of different symbols shown by the wheels should be
     * counted.
     */
    @Test
    public void shouldCountTheDistinctSymbols() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(3, "yellow");

        int distinct = machine.distinctSymbols();

        assertTrue(machine.ok());
        assertEquals(2, distinct);
    }

    // ------------------------------------------------------ configuration

    /**
     * The symbols of the wheels should be returned from left to right.
     */
    @Test
    public void shouldConsultTheConfiguration() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(2);
        machine.addWheel(1);
        machine.placeSymbol(2, "yellow");

        String[] conf = machine.configuration();

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "yellow"}, conf);
    }

    // ---------------------------------------------------------- isJackpot

    /**
     * There should be a jackpot when every wheel shows the same symbol.
     */
    @Test
    public void shouldBeJackpot() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.addWheel(2);
        assertTrue(machine.isJackpot());
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());
    }

    /**
     * There should not be a jackpot when the wheels show different
     * symbols.
     */
    @Test
    public void shouldNotBeJackpotWithDifferentSymbols() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "yellow");
        assertFalse(machine.isJackpot());
        assertArrayEquals(new String[]{"red", "yellow"}, machine.configuration());
    }

    /**
     * There should not be a jackpot when there are no wheels.
     */
    @Test
    public void shouldNotBeJackpotWithoutWheels() {
        machine.addSymbol(1, "red");
        assertFalse(machine.isJackpot());
        assertArrayEquals(new String[]{}, machine.configuration());
    }
}