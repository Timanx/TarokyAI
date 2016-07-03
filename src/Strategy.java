import java.util.Random;

public enum Strategy {
    PLAY_POSSUM(-60, Integer.MAX_VALUE),
    RECKLESS(0, Integer.MAX_VALUE),
    CAREFUL(-40, Integer.MAX_VALUE),
    GOLDEN_MEAN(-20, Integer.MAX_VALUE);

    private int preferancIndex = Integer.MAX_VALUE;
    private int secondDutyIndex = Integer.MAX_VALUE;

    Strategy(int preferancIndex, int secondDutyIndex) {
        this.preferancIndex = preferancIndex;
        this.secondDutyIndex = secondDutyIndex;
    }

    public int getPreferancIndex() {
        return preferancIndex;
    }

    public void setPreferancIndex(int preferancIndex) {
        this.preferancIndex = preferancIndex;
    }

    public int getSecondDutyIndex() {
        return secondDutyIndex;
    }

    public void setSecondDutyIndex(int secondDutyIndex) {
        this.secondDutyIndex = secondDutyIndex;
    }

    public boolean shallIPlayPreferanc(int totalTarokCount, int totalTarokStrength) {
        //TODO: Test the preferanc function and different strategies
        int myIndex = (int) Math.pow(totalTarokCount, 2) + totalTarokStrength/3;
        Random randomGenerator = new Random();
        int probability = randomGenerator.nextInt(100)+1;
        boolean preferanc = Game.preferanc_function[Math.min(Math.max(myIndex - preferancIndex, 0), Game.preferanc_function.length-1)] >= probability;
        return preferanc;
    }
}
