package game.controller;

import game.exceptions.InvalidTargetException;
import game.model.EncounterFactory;
import game.model.Enemy;
import game.model.GameCharacter;
import game.model.GameState;
import game.model.Heiter;
import game.model.Hero;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/** handles one turn-based encounter. */
public class BattleEngine {

    private static final Random RANDOM = new Random();
    private GameState state;
    private Enemy enemy;
    private List<Hero> turnOrder;

    public BattleEngine(GameState state) {
        this.state = state;
        enemy = EncounterFactory.createEnemy(state.getEncounterIndex());
        turnOrder = new ArrayList<>(state.getParty());
        turnOrder.sort(Comparator.comparingInt(Hero::getSpeed).reversed());
    }

    /** Returns the current enemy. */
    public Enemy getEnemy() {
        return enemy;
    }

    /** Runs a normal party attack followed by the enemy turn. */
    public List<String> attackRound() throws InvalidTargetException {
        List<String> messages = new ArrayList<>();
        for (Hero hero : turnOrder) {
            if (hero.isAlive() && enemy.isAlive()) {
                int damage = hero.attack(enemy);
                messages.add(hero.getName() + " dealt " + damage + " damage.");
            }
        }
        addEnemyTurn(messages);
        return messages;
    }

    /** Runs every living hero's ability followed by the enemy turn. */
    public List<String> abilityRound() throws InvalidTargetException {
        List<String> messages = new ArrayList<>();
        for (Hero hero : turnOrder) {
            if (!hero.isAlive() || !enemy.isAlive()) {
                continue;
            }
            GameCharacter target = hero instanceof Heiter ? getMostInjuredHero() : enemy;
            messages.add(hero.useAbility(target));
        }
        addEnemyTurn(messages);
        return messages;
    }

    /** Gives up the party attack and reduces the enemy's damage. */
    public List<String> defendRound() throws InvalidTargetException {
        List<String> messages = new ArrayList<>();
        Hero target = getRandomLivingHero();
        int damage = enemy.attack(target, true);
        messages.add(target.getName() + " defended and took " + damage + " damage.");
        return messages;
    }

    /** Runs the enemy turn after a potion is used. */
    public String enemyTurnAfterItem() throws InvalidTargetException {
        List<String> messages = new ArrayList<>();
        addEnemyTurn(messages);
        return messages.isEmpty() ? "The enemy was defeated." : messages.get(0);
    }

    /** Returns true when the current enemy is knocked out. */
    public boolean isVictory() {
        return !enemy.isAlive();
    }

    /** Returns true when every party member is knocked out. */
    public boolean isPartyDefeated() {
        return state.getParty().stream().noneMatch(Hero::isAlive);
    }

    private void addEnemyTurn(List<String> messages) throws InvalidTargetException {
        if (!enemy.isAlive()) {
            return;
        }
        Hero target = getRandomLivingHero();
        if (RANDOM.nextInt(4) == 0) {
            messages.add(enemy.useAbility(target));
        } else {
            int damage = enemy.attack(target);
            messages.add(enemy.getName() + " dealt " + damage
                    + " damage to " + target.getName() + ".");
        }
    }

    private Hero getRandomLivingHero() throws InvalidTargetException {
        List<Hero> livingHeroes = new ArrayList<>();
        for (Hero hero : state.getParty()) {
            if (hero.isAlive()) {
                livingHeroes.add(hero);
            }
        }
        if (livingHeroes.isEmpty()) {
            throw new InvalidTargetException("There are no living heroes to target.");
        }
        return livingHeroes.get(RANDOM.nextInt(livingHeroes.size()));
    }

    private Hero getMostInjuredHero() throws InvalidTargetException {
        return state.getParty().stream()
                .filter(Hero::isAlive)
                .sorted(Comparator.comparingDouble(hero ->
                        (double) hero.getHealth() / hero.getMaxHealth()))
                .findFirst()
                .orElseThrow(() -> new InvalidTargetException(
                        "There is no living hero to heal."));
    }
}
