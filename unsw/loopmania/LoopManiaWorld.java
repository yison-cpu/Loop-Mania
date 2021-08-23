package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one entity
 * can occupy the same square.
 */
public class LoopManiaWorld {
    // TODO = add additional backend functionality

    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;

    // TODO = add more lists for other entities, for equipped inventory items,
    // etc...

    // this list of enemies to be spawn in this tick
    private List<BasicEnemy> spawningEnemies;

    // TODO = expand the range of enemies
    private List<BasicEnemy> enemies;

    // TODO = expand the range of cards
    private List<Card> cardEntities;

    // TODO = expand the range of items
    private List<Entity> unequippedInventoryItems;

    // TODO = expand the range of buildings
    private List<Building> buildingEntities;

    // List of Allied Soldiers currently in the world
    private ArrayList<AlliedSoldier> listAlliedSoldiers;

    private boolean spawnedDoggie = false;

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse
     * them
     */
    private List<Pair<Integer, Integer>> orderedPath;

    /**
     * create the world (constructor)
     * 
     * @param width       width of world in number of cells
     * @param height      height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing
     *                    position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        nonSpecifiedEntities = new ArrayList<>();
        character = null;
        enemies = new ArrayList<>();
        cardEntities = new ArrayList<>();
        unequippedInventoryItems = new ArrayList<>();
        this.orderedPath = orderedPath;
        buildingEntities = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity
     * out of the file
     * 
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * add a generic entity (without it's own dedicated method for adding to the
     * world)
     * 
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        // TODO = if more specialised types being added from main menu, add more methods
        // like this with specific input types...
        nonSpecifiedEntities.add(entity);
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     * 
     * @return list of the enemies to be displayed on screen
     */
    public List<BasicEnemy> possiblySpawnEnemies() {
        // TODO = expand this very basic version
        Pair<Integer, Integer> pos = possiblyGetBasicEnemySpawnPosition();
        if (pos != null && character.getCycle() == 20 && spawnedDoggie == false) {
            int indexInPath = orderedPath.indexOf(pos);
            BasicEnemy enemy = new Doggie(new PathPosition(indexInPath, orderedPath), enemies.size()); // unique id
                                                                                                       // should be
                                                                                                       // length of
                                                                                                       // enemies list
            addEnemy(enemy);
            spawningEnemies.add(enemy);
            spawnedDoggie = true;
        } else if (pos != null) {
            int indexInPath = orderedPath.indexOf(pos);
            BasicEnemy enemy = new Slug(new PathPosition(indexInPath, orderedPath), 0);
            addEnemy(enemy);
            spawningEnemies.add(enemy);
        }
        return spawningEnemies;
    }

    /**
     * kill an enemy
     * 
     * @param enemy enemy to be killed
     */
    private void killEnemy(BasicEnemy enemy) {
        enemy.destroy();
        enemies.remove(enemy);
    }

    /**
     * run the expected battles in the world, based on current world state
     * 
     * @return list of enemies which have been killed
     */
    public List<BasicEnemy> runBattles() {

        List<BasicEnemy> defeatedEnemies = new ArrayList<BasicEnemy>();
        boolean isEnemyInSight = false;
        for (BasicEnemy e : enemies) {

            // Pythagoras: a^2+b^2 < radius^2 to see if within radius
            if (Math.pow((character.getX() - e.getX()), 2) + Math.pow((character.getY() - e.getY()), 2) < e
                    .getBattleRadius()) {
                isEnemyInSight = true;
                character.attack(e);
                e.setInBattle(true);
                e.attack(character);
            }
            // if enemy health <= 0 (due to trap or battle with character) then defeated
            if (e.getHealth() <= 0) {
                defeatedEnemies.add(e);
            }
            character.setInBattle(isEnemyInSight);
        }
        for (BasicEnemy e : defeatedEnemies) {
            e.drop(character);
            killEnemy(e);
        }
        return defeatedEnemies;
    }

    /**
     * spawn a vampire castle card in the world and return the card entity
     * 
     * @return a vampire castle card to be spawned in the controller as a JavaFX
     *         node
     */
    public VampireCastleCard loadVampireCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            // TODO = give some cash/experience/item rewards for the discarding of the
            // oldest card
            removeCard(0);
        }
        VampireCastleCard vampireCastleCard = new VampireCastleCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(vampireCastleCard);
        return vampireCastleCard;
    }

    /**
     * spawn a zombiePit card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public ZombiePitCard loadZombiePitCard() {
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        ZombiePitCard zombiePitCard = new ZombiePitCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(zombiePitCard);
        return zombiePitCard;
    }

    /**
     * spawn a tower card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public TowerCard loadTowerCard() {
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        TowerCard towerCard = new TowerCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(towerCard);
        return towerCard;
    }

    /**
     * spawn a village card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public VillageCard loadVillageCard() {
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        VillageCard villageCard = new VillageCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(villageCard);
        return villageCard;
    }

    /**
     * spawn a barracks card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public BarracksCard loadBarracksCard() {
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        BarracksCard barracksCard = new BarracksCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(barracksCard);
        return barracksCard;
    }

    /**
     * spawn a trap card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public TrapCard loadTrapCard() {
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        TrapCard trapCard = new TrapCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(trapCard);
        return trapCard;
    }

    /**
     * spawn a campfire card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public CampfireCard loadCampfireCard() {
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        CampfireCard campfireCard = new CampfireCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(campfireCard);
        return campfireCard;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed
     * cards)
     * 
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index) {
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * spawn a sword in the world and return the sword entity
     * 
     * @return a sword to be spawned in the controller as a JavaFX node
     */
    public Sword addUnequippedSword() {
        // TODO = expand this - we would like to be able to add multiple types of items,
        // apart from swords
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        // now we insert the new sword, as we know we have at least made a slot
        // available...
        Sword sword = new Sword(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(sword);
        return sword;
    }

    /**
     * spawn a stake in the world and return the sword entity
     * 
     * @return a stake to be spawned in the controller as a JavaFX node
     */
    public Stake addUnequippedStake() {
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        Stake stake = new Stake(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(stake);
        return stake;
    }

    /**
     * spawn a staff in the world and return the sword entity
     * 
     * @return a staff to be spawned in the controller as a JavaFX node
     */
    public Staff addUnequippedStaff() {
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        Staff staff = new Staff(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(staff);
        return staff;
    }

    /**
     * spawn an armour in the world and return the sword entity
     * 
     * @return an armour to be spawned in the controller as a JavaFX node
     */
    public Armour addUnequippedArmour() {
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        Armour armour = new Armour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(armour);
        return armour;
    }

    /**
     * spawn a shield in the world and return the sword entity
     * 
     * @return a shield to be spawned in the controller as a JavaFX node
     */
    public Shield addUnequippedShield() {
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        Shield shield = new Shield(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(shield);
        return shield;
    }

    /**
     * spawn a shield in the world and return the sword entity
     * 
     * @return a shield to be spawned in the controller as a JavaFX node
     */
    public TreeStump addUnequippedTreeStump() {
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(treeStump);
        return treeStump;
    }

    /**
     * spawn a helmet in the world and return the sword entity
     * 
     * @return a helmet to be spawned in the controller as a JavaFX node
     */
    public Helmet addUnequippedHelmet() {
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        Helmet helmet = new Helmet(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(helmet);
        return helmet;
    }

    /**
     * spawn a health potion in the world and return the potion
     * 
     * @return a potion to be spawned in the controller as a JavaFX node
     */
    public HealthPotion addUnequippedHealthPotion() {
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        HealthPotion potion = new HealthPotion(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(potion);
        return potion;
    }

    /**
     * spawn a doggie coin in the world and return the doggie coin
     * 
     * @return a doggie coin to be spawned in the controller as a JavaFX node
     */
    public DoggieCoin addUnequippedDoggieCoin() {
        Pair<Integer, Integer> firstAvailableSlot = ensureUnequippedSlot();

        DoggieCoin doggieCoin = new DoggieCoin(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(doggieCoin);
        return doggieCoin;
    }

    public Pair<Integer, Integer> ensureUnequippedSlot() {
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            // TODO = give some cash/experience rewards for the discarding of the oldest
            // sword
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        return firstAvailableSlot;
    }

    /**
     * equip a sword in the world and return the sword entity
     * 
     * @param item a unequipped item to be equipped
     */
    // public void equip(Item item) {
    // removeUnequippedInventoryItemByCoordinates(item.getX(), item.getY());
    // character.equip(item);
    // }

    /**
     * remove an item by x,y coordinates
     * 
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y) {
        Entity item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    /**
     * run moves which occur with every tick without needing to spawn anything
     * immediately
     */
    public void runTickMoves() {

        // damage multiplier and scalar decrease is complicated to maintain, so it'll be
        // easier to reset and recalculate every tick
        character.setDamageMultiplier(1);
        character.setScalarDecrease(0);

        // reset spawningEnemies
        spawningEnemies = new ArrayList<>();

        // notify observers of character of the change in position in previous tick
        character.notifyObservers();

        if (!character.isInBattle()) {
            character.moveDownPath();
        }
        moveBasicEnemies();
    }

    /**
     * remove an item from the unequipped inventory
     * 
     * @param item item to be removed
     */
    private void removeUnequippedInventoryItem(Entity item) {
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates assumes that no 2
     * unequipped inventory items share x and y coordinates
     * 
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    private Entity getUnequippedInventoryItemEntityByCoordinates(int x, int y) {
        for (Entity e : unequippedInventoryItems) {
            if ((e.getX() == x) && (e.getY() == y)) {
                return e;
            }
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list
     * (this is ordered based on age in the starter code)
     * 
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index) {
        Entity item = unequippedInventoryItems.get(index);
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the
     * unequipped inventory
     * 
     * @return x,y coordinate pair
     */
    public Pair<Integer, Integer> getFirstAvailableSlotForItem() {
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available
        // slot defined by looking row by row
        for (int y = 0; y < unequippedInventoryHeight; y++) {
            for (int x = 0; x < unequippedInventoryWidth; x++) {
                if (getUnequippedInventoryItemEntityByCoordinates(x, y) == null) {
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * shift card coordinates down starting from x coordinate
     * 
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x) {
        for (Card c : cardEntities) {
            if (c.getX() >= x) {
                c.x().set(c.getX() - 1);
            }
        }
    }

    /**
     * move all enemies
     */
    private void moveBasicEnemies() {
        // TODO = expand to more types of enemy
        for (BasicEnemy e : enemies) {
            if (!e.isInBattle()) {
                e.notifyObservers();
                e.move();
            }
        }
    }

    /**
     * get a randomly generated position which could be used to spawn an enemy
     * 
     * @return null if random choice is that wont be spawning an enemy or it isn't
     *         possible, or random coordinate pair if should go ahead
     */
    private Pair<Integer, Integer> possiblyGetBasicEnemySpawnPosition() {
        // TODO = modify this

        // has a chance spawning a basic enemy on a tile the character isn't on or
        // immediately before or after (currently space required = 2)...
        Random rand = new Random();
        int choice = rand.nextInt(3);
        // TODO = change based on spec
        int slugSize = 0;
        for (BasicEnemy enemy : enemies) {
            if (enemy instanceof Slug) {
                slugSize++;
            }
        }
        if ((choice == 0) && (slugSize < 2)) {
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size()) % orderedPath.size();
            int endNotAllowed = (indexPosition + 3) % orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i = endNotAllowed; i != startNotAllowed; i = (i + 1) % orderedPath.size()) {
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }

            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates
                    .get(rand.nextInt(orderedPathSpawnCandidates.size()));

            return spawnPosition;
        }
        return null;
    }

    /**
     * remove a card by its x, y coordinates
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }
        Building newBuilding = null;

        for (Building b : buildingEntities) {
            if (b.getX() == buildingNodeX && b.getY() == buildingNodeY) {
                return null;
            }
        }

        if (card.isValidDraggingPosition(orderedPath, buildingNodeX, buildingNodeY)) {
            // now spawn building and make it observer of character/enemy
            if (card instanceof VampireCastleCard) {
                newBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(buildingNodeX),
                        new SimpleIntegerProperty(buildingNodeY));
            } else if (card instanceof CampfireCard) {
                newBuilding = new CampfireBuilding(new SimpleIntegerProperty(buildingNodeX),
                        new SimpleIntegerProperty(buildingNodeY));
            } else if (card instanceof TowerCard) {
                newBuilding = new TowerBuilding(new SimpleIntegerProperty(buildingNodeX),
                        new SimpleIntegerProperty(buildingNodeY));
            } else if (card instanceof ZombiePitCard) {
                newBuilding = new ZombiePitBuilding(new SimpleIntegerProperty(buildingNodeX),
                        new SimpleIntegerProperty(buildingNodeY));
            } else if (card instanceof VillageCard) {
                newBuilding = new VillageBuilding(new SimpleIntegerProperty(buildingNodeX),
                        new SimpleIntegerProperty(buildingNodeY));
            } else if (card instanceof BarracksCard) {
                newBuilding = new BarracksBuilding(new SimpleIntegerProperty(buildingNodeX),
                        new SimpleIntegerProperty(buildingNodeY));
            } else {
                newBuilding = new TrapBuilding(new SimpleIntegerProperty(buildingNodeX),
                        new SimpleIntegerProperty(buildingNodeY));
            }

            // some building is observer of character, some enemies
            if (newBuilding instanceof TrapBuilding) {
                for (BasicEnemy e : enemies) {
                    e.registerObserver(newBuilding);
                }
            } else {
                character.registerObserver(newBuilding);
            }

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            buildingEntities.add(newBuilding);
        }

        return newBuilding;
    }

    public Character getCharacter() {
        return this.character;
    }

    public List<BasicEnemy> getEnemies() {
        return this.enemies;
    }

    public void addEnemy(BasicEnemy enemy) {
        enemies.add(enemy);
        enemy.setWorld(this);
        for (Building b : buildingEntities) {
            if (b instanceof TrapBuilding) {
                enemy.registerObserver(b);
            }
        }
    }

    public List<BasicEnemy> getSpawningEnemies() {
        return this.spawningEnemies;
    }

    public List<Pair<Integer, Integer>> getOrderedPath() {
        return this.orderedPath;
    }

    public List<Building> getBuildings() {
        return this.buildingEntities;
    }

    public void addBuilding(Building building) {
        this.buildingEntities.add(building);
    }

    public void removeBuilding(Building building) {
        this.buildingEntities.remove(building);
    }

    public ArrayList<AlliedSoldier> getAlliedSoldiers() {
        return this.listAlliedSoldiers;
    }

    public void addAlliedSoldier(AlliedSoldier a) {
        listAlliedSoldiers.add(a);
    }

    public List<?> getUnequippedInventoryItems() {
        return this.unequippedInventoryItems;
    }

    public List<Card> getCardEntities() {
        return this.cardEntities;
    }
}
