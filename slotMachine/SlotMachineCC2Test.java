import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Acceptance tests for the cycle 2 methods of SlotMachine:
 * swap, lock, unlock, spin(wheel, steps) and spin(configuration).
 * Every test checks the value of ok() and also verifies the resulting
 * state of the machine (what should or should not have happened).
 *
 * @author Juan Diego Cardozo Beltrán
 * @author Diego Alejandro Díaz Boada
 * @version 21/09/26
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;

    /**
     * Default constructor for test class SlotMachineCC2Test.
     */
    public SlotMachineCC2Test()
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

    // ---------------------------------------------------------------- swap

    /**
     * Two existing, unlocked wheels should exchange their symbols.
     */
    @Test
    public void shouldSwapDifferentWheels() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "yellow");

        machine.swap(1, 2);

        assertTrue(machine.ok());
        String[] conf = machine.configuration();
        assertEquals("yellow", conf[0]);
        assertEquals("red", conf[1]);
    }

    /**
     * Swapping with a wheel that does not exist should fail and leave
     * the existing wheel untouched.
     */
    @Test
    public void shouldNotSwapAWheelThatDoesNotExist() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);

        machine.swap(1, 2);

        assertFalse(machine.ok());
        String[] conf = machine.configuration();
        assertEquals(1, conf.length);
        assertEquals("red", conf[0]);
    }

    /**
     * Swapping when one of the wheels is locked should fail and neither
     * wheel should change its symbol.
     */
    @Test
    public void shouldNotSwapALockedWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "yellow");
        machine.lock(1);

        machine.swap(1, 2);

        assertFalse(machine.ok());
        String[] conf = machine.configuration();
        assertEquals("red", conf[0]);
        assertEquals("yellow", conf[1]);
    }

    // ---------------------------------------------------------------- lock

    /**
     * An existing wheel should be locked, so its symbol can no longer
     * be changed.
     */
    @Test
    public void shouldLockAWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);

        machine.lock(1);
        assertTrue(machine.ok());

        machine.placeSymbol(1, "yellow");
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Locking an already locked wheel should fail, and the wheel should
     * remain locked.
     */
    @Test
    public void shouldNotLockAnAlreadyLockedWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.lock(1);

        machine.lock(1);
        assertFalse(machine.ok());

        machine.placeSymbol(1, "yellow");
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Locking a wheel that does not exist should fail and should not
     * create any wheel.
     */
    @Test
    public void shouldNotLockANotCreatedWheel() {
        machine.lock(1);

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    // -------------------------------------------------------------- unlock

    /**
     * A locked wheel should be unlocked, so its symbol can be changed
     * again.
     */
    @Test
    public void shouldUnlockALockedWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.lock(1);

        machine.unlock(1);
        assertTrue(machine.ok());
<<<<<<< HEAD
    }
    
    @Test
    public void shouldNotUnlockAnAlreadyUnlockedWheel(){
        machine.addWheel(1);
        machine.unlock(1);
        assertFalse(machine.ok());
    }
    
    @Test
    public void shouldNotUnlockAnNotCreatedWheel(){
        machine.unlock(1);
        assertFalse(machine.ok());
    }
    
    //spin steps
    @Test
    public void shouldSpinAnUnlockedWheel(){
        machine.addWheel(1);
        machine.addSymbol(1,"red");
        machine.addSymbol(2,"yellow");
        machine.addSymbol(3,"pink");
        machine.addSymbol(4,"blue");
        machine.spin(1,3);
=======

        machine.placeSymbol(1, "yellow");
>>>>>>> 4f76094920d50006c6be111a20e7f33bb3fbbfce
        assertTrue(machine.ok());
        assertEquals("yellow", machine.configuration()[0]);
    }

    /**
     * Unlocking a wheel that is not locked should fail, and the wheel
     * should keep working normally.
     */
    @Test
    public void shouldNotUnlockAnAlreadyUnlockedWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);

        machine.unlock(1);
        assertFalse(machine.ok());

        machine.placeSymbol(1, "yellow");
        assertTrue(machine.ok());
        assertEquals("yellow", machine.configuration()[0]);
    }
<<<<<<< HEAD
    
    @Test
    public void shouldNotSpinAWheelZeroTimes() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "green");
        machine.addWheel(1);
 
        machine.spin(1, 0);
 
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }
    
    @Test
    public void shouldNotSpinAWheelWithLessThanTwoSymbols() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
 
        machine.spin(1, 3);
 
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }
    
    //spin configuration
=======

    /**
     * Unlocking a wheel that does not exist should fail and should not
     * create any wheel.
     */
>>>>>>> 4f76094920d50006c6be111a20e7f33bb3fbbfce
    @Test
    public void shouldNotUnlockANotCreatedWheel() {
        machine.unlock(1);

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    // ---------------------------------------------------------- spin steps

    /**
     * An unlocked wheel should advance one symbol per step.
     */
    @Test
    public void shouldSpinAnUnlockedWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "pink");
        machine.addSymbol(4, "blue");

        machine.spin(1, 3);

        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Spinning zero times should fail and leave the wheel unchanged.
     */
    @Test
    public void shouldNotSpinAWheelZeroTimes() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "green");
        machine.addWheel(1);

        machine.spin(1, 0);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Spinning with fewer than two symbols should fail and leave the
     * wheel unchanged.
     */
    @Test
    public void shouldNotSpinAWheelWithLessThanTwoSymbols() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);

        machine.spin(1, 3);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Spinning a wheel that does not exist should fail and should not
     * create any wheel.
     */
    @Test
    public void shouldNotSpinANotCreatedWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");

        machine.spin(1, 3);

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    /**
     * Spinning a locked wheel should fail and leave its symbol unchanged.
     */
    @Test
    public void shouldNotSpinALockedWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.lock(1);

        machine.spin(1, 3);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    // -------------------------------------------------- spin configuration

    /**
     * The machine should take exactly the configuration it receives.
     */
    @Test
    public void shouldAssignADefinedConfiguration() {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "blue");

        machine.spin(new String[]{"yellow", "blue", "red"});

        assertTrue(machine.ok());
        String[] conf = machine.configuration();
        assertEquals("yellow", conf[0]);
        assertEquals("blue", conf[1]);
        assertEquals("red", conf[2]);
    }

    /**
     * A configuration whose size differs from the number of wheels should
     * be rejected, leaving every wheel as it was.
     */
    @Test
    public void shouldNotAssignADefinedConfigurationWhenTheSizesDontMatch() {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");

        machine.spin(new String[]{"yellow", "red", "red"});

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());
    }

    /**
     * If at least one wheel is locked the configuration should be
     * rejected, leaving every wheel as it was.
     */
    @Test
    public void shouldNotSpinWhenAtLeastOneWheelIsLocked() {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "blue");
        machine.lock(1);

        machine.spin(new String[]{"yellow", "blue", "red"});

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "red", "red"}, machine.configuration());
    }
}