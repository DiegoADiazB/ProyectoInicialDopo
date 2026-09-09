

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class SlotMachineCC2Test.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;
    /**
     * Default constructor for test class SlotMachineCC2Test
     */
    public SlotMachineCC2Test()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {  
        machine = new SlotMachine();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        machine = null;
    }
    //Swap
    /**
     * This test verifies that 2 different wheels should be able to swap.
     */
    @Test
    public void shouldSwapDifferentWheels(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "yellow");   

        machine.swap(1, 2);

        String[] conf = machine.configuration();
        assertEquals("yellow", conf[0]);
        assertEquals("red", conf[1]);
    }
    
    /**
     * This test verifies that the same wheel shouldn't be able to swap with itself.
     */
    @Test
    public void shouldNotSwapTheSameWheel(){
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.swap(1,1);
        assertFalse(machine.ok());
    }
    
    //lock
    @Test
    public void shouldLockAWheel(){
        machine.addWheel(1);
        machine.lock(1);
        assertTrue(machine.ok());
    }
    
    @Test
    public void shouldNotLockAnAlreadyLockedWheel(){
        machine.addWheel(1);
        machine.lock(1);
        machine.lock(1);
        assertFalse(machine.ok());
    }
    
    //unlock
    @Test
    public void shouldUnlockAWheelLocked(){
        machine.addWheel(1);
        machine.lock(1);
        machine.unlock(1);
        assertTrue(machine.ok());
    }
    
    @Test
    public void shouldNotUnlockAWheelRecentlyCreated(){
        machine.addWheel(1);
        machine.unlock(1);
        assertFalse(machine.ok());
    }
    
    //spin steps
    @Test
    public void shouldSpinAnUnlockedWheel(){
        machine.addWheel(1);
        machine.addSymbol(1,"red");
        machine.spin(1,3);
        assertTrue(machine.ok());
    }
    
    @Test
    public void shouldNotSpinAnLockedWheel(){
        machine.addWheel(1);
        machine.addSymbol(1,"red");
        machine.lock(1);
        machine.spin(1,3);
        assertFalse(machine.ok());
    }
    
    //spin configuration
    @Test
    public void shouldAssignADefinedConfiguration(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1,"red");
        machine.addSymbol(2,"yellow");
        machine.addSymbol(3,"blue");
        machine.spin(new String[]{"yellow","blue","red"});
        String[] conf = machine.configuration();
        assertEquals("yellow", conf[0]);
        assertEquals("blue", conf[1]);
        assertEquals("red", conf[2]);
    }
    
    @Test
    public void shouldNotAssignADefinedConfigurationWhenTheSizesDontMatch(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1,"red");
        machine.addSymbol(2,"yellow");
        machine.spin(new String[]{"yellow","red","red"});
        assertFalse(machine.ok());
    }
}