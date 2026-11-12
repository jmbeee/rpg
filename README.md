# Journey to the Demon King

A single-player turn-based RPG made with Java Swing. The player chooses three
heroes and fights six demons before facing the Demon King.

## Project Title and Team

- **Project:** Journey to the Demon King
- **Course:** Object-Oriented Programming
- **School:** University of San Jose - Recoletos
- **Instructor:** Engr. Violdan E. Bayocot
- **Student:** Replace with complete name
- **GitHub:** [@jmbeee](https://github.com/jmbeee)
- **Submission date:** Add submission date

## How to Build and Run

The project requires JDK 17 or newer.

```bash
git clone git@github.com:jmbeee/oop-rpg.git
cd oop-rpg

rm -rf build
mkdir -p build/classes

javac -d build/classes \
  src/game/Main.java \
  src/game/SelectionPanel.java \
  src/game/model/*.java \
  src/game/view/*.java \
  src/game/controller/*.java \
  src/game/io/*.java \
  src/game/exceptions/*.java

java -cp build/classes game.Main
```

Create the runnable JAR with a standard JDK:

```bash
printf 'Main-Class: game.Main\n' > build/manifest.mf
jar cfm build/JourneyToDemonKing.jar build/manifest.mf -C build/classes .
java -jar build/JourneyToDemonKing.jar
```

Run the game from the project root so it can find the `assets` folder.

## Controls and Gameplay

The game is controlled with the mouse.

1. Choose **New Game** or load one of the three save slots.
2. Select three heroes from Frieren, Himmel, Heiter, and Eisen.
3. At camp, view the party, manage inventory, save, or start the next battle.
4. Defeat all six enemies to win the game.

Battle actions:

- **Attack:** Every living hero attacks in speed order.
- **Ability:** Every living hero uses a class-specific ability.
- **Defend:** The party gives up its attack and the targeted hero takes less damage.
- **Use Potion:** Restores HP to one living hero and consumes a Health Potion.

After the party acts, the enemy automatically attacks a random living hero and
may use a special ability. The party loses when all selected heroes are knocked
out. After a victory, the player may continue immediately or return to camp.

The shared inventory contains a Health Potion, Iron Sword, and Ancient Key. The
game saves progress in both readable `.txt` and binary `.sav` files. Test saves
are included as `saves/slot1.txt` and `saves/slot1.sav`.

## Feature Checklist

| Requirement | Status | Implementation |
| --- | --- | --- |
| Abstract classes | Complete | `GameCharacter`, `Hero`, `Enemy`, and `Item` |
| Two interfaces | Complete | `Attackable` and `Healable` |
| Two-level inheritance | Complete | `GameCharacter -> Hero -> Frieren` and other subclasses |
| Private instance fields | Complete | Model fields are private and accessed through methods |
| Method overriding | Complete | `useAbility()`, `getDescription()`, and `paintComponent()` |
| Method overloading | Complete | `attack()` and `takeDamage()` |
| Polymorphism | Complete | `BattleEngine` uses `List<Hero>` and overridden abilities |
| Two collection types | Complete | `ArrayList` and `HashMap` |
| Custom generic code | Complete | `GameSlot<T>` and `Inventory.findItem()` |
| Comparator sorting | Complete | Hero speed order in `BattleEngine` |
| Stream API | Complete | Injured-hero selection and party defeat check |
| Two custom exceptions | Complete | `InvalidTargetException` and `SaveGameException` |
| File cleanup | Complete | `SaveService` uses try-with-resources |
| No empty catch blocks | Complete | Exceptions are handled or rethrown |
| User-facing errors | Complete | Swing dialogs display save, item, and target errors |
| Text save | Complete | `SaveService.saveText()` and `loadText()` |
| Binary save | Complete | `ObjectOutputStream` and `ObjectInputStream` |
| Missing/corrupted saves | Complete | Handled using `SaveGameException` |
| Three save slots | Complete | Slots 1, 2, and 3 |
| Swing GUI | Complete | `JFrame`, `JPanel`, and standard Swing components |
| Three or more screens | Complete | Menu, selection, camp, party, inventory, battle, and ending |
| Two layout managers | Complete | `BorderLayout`, `GridLayout`, and `BoxLayout` |
| Inner, anonymous, and lambda handlers | Complete | Battle and Swing Timer handlers |
| Graphics2D rendering | Complete | `BattlefieldPanel.paintComponent()` |
| Nearest-neighbor scaling | Complete | Rendering hint in `BattlefieldPanel` |
| Logical packages | Complete | `model`, `view`, `controller`, `io`, and `exceptions` |
| Classes near 300 lines or less | Complete | Battle drawing is separated into `BattlefieldPanel` |
| Javadoc on classes/public methods | Needs final review | Some public constructors and methods still need comments |
| Named balance constants | Needs final review | Some character statistics remain constructor literals |

## Who Did What

### [@jmbeee](https://github.com/jmbeee)

This is a solo project. I worked on `Main`, `SelectionPanel`, the
`GameCharacter`, `Hero`, `Enemy`, and `Item` hierarchies, the hero and enemy
classes, `Inventory`, `GameState`, `GameSlot`, and `EncounterFactory`.

I worked on the combat flow in `BattleEngine`, the Swing screens in `game.view`,
the custom drawing and animations in `BattlefieldPanel`, the text and binary save
system in `SaveService`, the custom exceptions, UML diagram, testing, assets, and
documentation.

## AI Usage Disclosure

I used ChatGPT extensively during development. It assisted with planning the
class structure, creating and revising portions of the Java code, explaining Java
and Swing concepts, debugging errors, reviewing the requirements, creating the
UML diagram, writing documentation, and generating some game artwork.

I also used AntiGravity for code autocomplete. I reviewed the project and take
responsibility for understanding and explaining the submitted work during the
defense.

## External Assets and Attribution

- Hero, enemy, item, and battle background images were created for this project
  with AI assistance.
- `frieren.gif` came from the
  [Frieren Pixel GIF on Tenor](https://tenor.com/view/frieren-pixel-pixel-frieren-gif-333636461155836075),
  posted by Tenor user **akatou**. The page does not display a specific reuse
  license for the GIF.
- Character names are inspired by *Frieren: Beyond Journey's End*. This is an
  educational fan project and is not affiliated with the original rights holders.

## Known Issues

- The game must be launched from the project root because it uses relative asset paths.
- The assets are stored beside the JAR instead of inside it.
- The Ancient Key remains in the inventory after its animation.
