import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Random;

public class SlotMachineCC3Test
{
    private SlotMachineContest contest;
    private Random random;
    private int n;

    public SlotMachineCC3Test()
    {
    }

    @BeforeEach
    public void setUp()
    {
        contest = new SlotMachineContest();
        random = new Random();
        n = random.nextInt(10) + 1;
    }

    @AfterEach
    public void tearDown()
    {
        contest = null;
        random = null;
    }

    @Test
    public void shouldSolveAMachineWithOneWheel() {
        List<int[]> actions = contest.solve(1);

        assertEquals(1, contest.getMachine().distinctSymbols());
        assertTrue(actions.isEmpty());
    }

    @Test
    public void shouldSolveAnyRandomMachine() {
        for (int attempt = 0; attempt < 10; attempt++) {
            SlotMachineContest other = new SlotMachineContest();
            int size = random.nextInt(10) + 1;
            other.solve(size);
            assertEquals(1, other.getMachine().distinctSymbols());
        }
    }

    @Test
    public void shouldReturnActionsOnValidWheels() {
        List<int[]> actions = contest.solve(n);

        for (int[] action : actions) {
            assertTrue(action[0] >= 1 && action[0] <= n);
        }
    }

    @Test
    public void shouldReturnActionsWithValidSteps() {
        List<int[]> actions = contest.solve(n);

        for (int[] action : actions) {
            assertTrue(action[1] >= 1 && action[1] <= n);
        }
    }

    @Test
    public void shouldReturnEveryActionAsAPair() {
        List<int[]> actions = contest.solve(n);

        for (int[] action : actions) {
            assertEquals(2, action.length);
        }
    }

    @Test
    public void shouldRecordActionsWhenSolving() {
        List<int[]> actions = contest.solve(random.nextInt(9) + 2);

        assertFalse(actions.isEmpty());
    }
}