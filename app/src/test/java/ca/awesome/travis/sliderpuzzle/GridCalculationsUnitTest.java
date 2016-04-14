package ca.awesome.travis.sliderpuzzle;

import org.junit.Test;

import ca.awesome.travis.sliderpuzzle.GridCalculations;

import static org.junit.Assert.*;

public class GridCalculationsUnitTest {

    private static final float TILE_WIDTH = 10.0f;
    private static final float TILE_HEIGHT = 10.0f;

    @Test
    public void initializeGridCalculations() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);
        assertNotEquals(gc, null);
    }


    @Test
    public void testConstrainTemporaryMovementDown() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);

        GridCalculations.Direction dir = GridCalculations.Direction.DOWN;
        float[] expected1 = {0.0f, TILE_HEIGHT};
        float[] deltas1 = {10.0f, 50.0f};
        float[] results1 =  gc.constrainTemporaryMovement(dir, deltas1[0], deltas1[1]);
        assertArrayEquals(expected1, results1, 0.1f);

        float[] expected2 = {0.0f, 0.0f};
        float[] deltas2 = {-10.0f, -50.0f};
        float[] results2 =  gc.constrainTemporaryMovement(dir, deltas2[0], deltas2[1]);
        assertArrayEquals(expected2, results2, 0.1f);
    }
    @Test
    public void testConstrainTemporaryMovementUp() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);

        GridCalculations.Direction dir = GridCalculations.Direction.UP;
        float[] expected1 = {0.0f, -TILE_HEIGHT};
        float[] deltas1 = {10.0f, -50.0f};
        float[] results1 =  gc.constrainTemporaryMovement(dir, deltas1[0], deltas1[1]);
        assertArrayEquals(expected1, results1, 0.1f);

        float[] expected2 = {0.0f, 0.0f};
        float[] deltas2 = {-10.0f, 50.0f};
        float[] results2 =  gc.constrainTemporaryMovement(dir, deltas2[0], deltas2[1]);
        assertArrayEquals(expected2, results2, 0.1f);
    }
    @Test
    public void testConstrainTemporaryMovementLeft() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);

        GridCalculations.Direction dir = GridCalculations.Direction.LEFT;
        float[] expected1 = {-TILE_WIDTH, 0.0f};
        float[] deltas1 = {-50.0f, 10.0f};
        float[] results1 =  gc.constrainTemporaryMovement(dir, deltas1[0], deltas1[1]);
        assertArrayEquals(expected1, results1, 0.1f);

        float[] expected2 = {0.0f, 0.0f};
        float[] deltas2 = {50.0f, -10.0f};
        float[] results2 =  gc.constrainTemporaryMovement(dir, deltas2[0], deltas2[1]);
        assertArrayEquals(expected2, results2, 0.1f);
    }
    @Test
    public void testConstrainTemporaryMovementRight() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);

        GridCalculations.Direction dir = GridCalculations.Direction.RIGHT;
        float[] expected1 = {TILE_WIDTH, 0.0f};
        float[] deltas1 = {50.0f, 10.0f};
        float[] results1 =  gc.constrainTemporaryMovement(dir, deltas1[0], deltas1[1]);
        assertArrayEquals(expected1, results1, 0.1f);

        float[] expected2 = {0.0f, 0.0f};
        float[] deltas2 = {-50.0f, -10.0f};
        float[] results2 =  gc.constrainTemporaryMovement(dir, deltas2[0], deltas2[1]);
        assertArrayEquals(expected2, results2, 0.1f);
    }


    @Test
    public void testSnapFinalMovementDown() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);
        GridCalculations.Direction dir = GridCalculations.Direction.DOWN;

        //Not enough to trigger movement
        float[] expected1 = {0.0f, 0.0f};
        float[] results1 =  gc.snapFinalMovement(dir, 0.0f, 2.0f);
        assertArrayEquals(expected1, results1, 0.1f);

        //Enough to trigger movement
        float[] expected2 = {0.0f, 10.0f};
        float[] results2 =  gc.snapFinalMovement(dir, 0.0f, 6.0f);
        assertArrayEquals(expected2, results2, 0.1f);

    }
    @Test
    public void testSnapFinalMovementUp() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);
        GridCalculations.Direction dir = GridCalculations.Direction.UP;

        //Not enough to trigger movement
        float[] expected1 = {0.0f, 0.0f};
        float[] results1 =  gc.snapFinalMovement(dir, 0.0f, -2.0f);
        assertArrayEquals(expected1, results1, 0.1f);

        //Enough to trigger movement
        float[] expected2 = {0.0f, -10.0f};
        float[] results2 =  gc.snapFinalMovement(dir, 0.0f, -6.0f);
        assertArrayEquals(expected2, results2, 0.1f);

    }
    @Test
    public void testSnapFinalMovementLeft() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);
        GridCalculations.Direction dir = GridCalculations.Direction.LEFT;

        //Not enough to trigger movement
        float[] expected1 = {0.0f, 0.0f};
        float[] results1 =  gc.snapFinalMovement(dir, -2.0f, 0.0f);
        assertArrayEquals(expected1, results1, 0.1f);

        //Enough to trigger movement
        float[] expected2 = {-10.0f, 0.0f};
        float[] results2 =  gc.snapFinalMovement(dir, -6.0f, 0.0f);
        assertArrayEquals(expected2, results2, 0.1f);

    }
    @Test
    public void testSnapFinalMovementRight() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);
        GridCalculations.Direction dir = GridCalculations.Direction.RIGHT;

        //Not enough to trigger movement
        float[] expected1 = {0.0f, 0.0f};
        float[] results1 =  gc.snapFinalMovement(dir, 2.0f, 0.0f);
        assertArrayEquals(expected1, results1, 0.1f);

        //Enough to trigger movement
        float[] expected2 = {10.0f, 0.0f};
        float[] results2 =  gc.snapFinalMovement(dir, 6.0f, 0.0f);
        assertArrayEquals(expected2, results2, 0.1f);

    }


    @Test
    public void testMovementTriggeredTileUpdate() throws Exception {
        GridCalculations gc = new GridCalculations(TILE_WIDTH, TILE_HEIGHT);

        boolean expectedResults = false;
        boolean movement = gc.movementTriggeredTileUpdate(0.0f, 0.0f);
        assertEquals(movement, expectedResults);

        expectedResults = true;
        movement = gc.movementTriggeredTileUpdate(0.0f, 1.0f);
        assertEquals(movement, expectedResults);

        expectedResults = true;
        movement = gc.movementTriggeredTileUpdate(1.0f, 0.0f);
        assertEquals(movement, expectedResults);
    }



}