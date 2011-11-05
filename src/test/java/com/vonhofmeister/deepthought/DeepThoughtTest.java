package com.vonhofmeister.deepthought;

import junit.framework.TestCase;

/**
 *
 * @author Henrik Hofmeister <henrik@newdawn.dk>
 */
public class DeepThoughtTest extends TestCase {

    public DeepThoughtTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test_simple_answer() {
        DeepThought<Cowboy,CowboyType> dt = new DeepThought<Cowboy,CowboyType>();
        dt.is(CowboyType.BAD,CowboyType.GOOD).when(tall);
        dt.is(CowboyType.BAD,CowboyType.UGLY).when(angry);
        dt.is(CowboyType.BAD,CowboyType.UGLY).when(packingHeat);
        dt.is(CowboyType.GOOD).whenNot(packingHeat);

        assertEquals("Good cowboy found",CowboyType.GOOD,dt.getAnswer(new Cowboy(20,0,1)).get(0));
        assertEquals("Bad cowboy found",CowboyType.BAD,dt.getAnswer(new Cowboy(20,3,10)).get(0));
        assertEquals("Ugly cowboy found",CowboyType.UGLY,dt.getAnswer(new Cowboy(5,3,10)).get(0));
    }

    public void test_chained_answer() {
        DeepThought<Cowboy,CowboyType> dt = new DeepThought<Cowboy,CowboyType>();

        dt.is(CowboyType.BAD,CowboyType.UGLY)
                .when(packingHeat)
                .and(angry);
        dt.is(CowboyType.BAD,CowboyType.GOOD)
                .when(tall);
        dt.is(CowboyType.GOOD)
                .whenNot(packingHeat);


        assertEquals("Good cowboy found",CowboyType.GOOD,dt.getAnswer(new Cowboy(20,0,1)).get(0));
        assertEquals("Bad cowboy found",CowboyType.BAD,dt.getAnswer(new Cowboy(20,3,10)).get(0));
        assertEquals("Ugly cowboy found",CowboyType.UGLY,dt.getAnswer(new Cowboy(5,3,10)).get(0));
    }


    public void test_answer_ulimate_question_of_life_universe_and_everything() {
        DeepThought<Boolean,Integer> dt = new DeepThought<Boolean,Integer>();

        //Thread.sleep(7500000*YEAR);

        dt.is(42).when(new Condition<Boolean>() {

            public int evaluate(Boolean value) {
                return value ? 1 : -1;
            }
        });

        assertEquals("Ultimate answer is 42",new Integer(42),dt.getAnswer(true).get(0));
    }

    private static class Cowboy {
        private int height;
        private int numGuns;
        private int angerLevel;

        public Cowboy(int height, int numGuns, int angerLevel) {
            this.height = height;
            this.numGuns = numGuns;
            this.angerLevel = angerLevel;
        }

        public int getAngerLevel() {
            return angerLevel;
        }

        public int getHeight() {
            return height;
        }

        public int getNumGuns() {
            return numGuns;
        }
    }

    private static enum CowboyType {
        GOOD,BAD,UGLY
    }

    private static Condition<Cowboy> tall = new Condition<DeepThoughtTest.Cowboy>() {

        public int evaluate(Cowboy value) {
            return value.getHeight() > 10 ? 1 : -1;
        }
    };
    private static Condition<Cowboy> angry = new Condition<DeepThoughtTest.Cowboy>() {

        public int evaluate(Cowboy value) {
            return value.getAngerLevel() > 10 ? 1 : -1;
        }
    };
    private static Condition<Cowboy> packingHeat = new Condition<DeepThoughtTest.Cowboy>() {

        public int evaluate(Cowboy value) {
            return value.getNumGuns() > 0 ? 1 : -1;
        }
    };
}
