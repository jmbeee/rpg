package game.model;

/** creates enemies for each campaign encounter. */
public final class EncounterFactory {

    private static final int ENCOUNTER_COUNT = 6;

    private EncounterFactory() {
    }

    public static Enemy createEnemy(int encounterIndex) {
        switch (encounterIndex) {
            case 0:
                return new DemonScout();
            case 1:
                return new DemonSoldier();
            case 2:
                return new Qual();
            case 3:
                return new Aura();
            case 4:
                return new Macht();
            default:
                return new DemonKing();
        }
    }

    public static int getEncounterCount() {
        return ENCOUNTER_COUNT;
    }
}
