package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.util.Duration;
import java.util.EnumMap;

import java.io.File;
import java.io.IOException;

/**
 * the draggable types. If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE {
    CARD, ITEM
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application
 * thread:
 * https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 * Note in
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html
 * under heading "Threading", it specifies animation timelines are run in the
 * application thread. This means that the starter code does not need locks
 * (mutexes) for resources shared between the timeline KeyFrame, and all of the
 * event handlers (including between different event handlers). This will make
 * the game easier for you to implement. However, if you add time-consuming
 * processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend: using Task
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by
 * itself or within a Service
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 * Tasks ensure that any changes to public properties, change notifications for
 * errors or cancellation, event handlers, and states occur on the JavaFX
 * Application thread, so is a better alternative to using a basic Java Thread:
 * https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm The Service class
 * is used for executing/reusing tasks. You can run tasks without Service,
 * however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need
 * to implement locks on resources shared with the application thread (i.e.
 * Timeline KeyFrame and drag Event handlers). You can check whether code is
 * running on the JavaFX application thread by running the helper method
 * printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and
 * https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using
 * Platform.runLater
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 * This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass,
     * buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot
     * stretches over the entire game world, so we can detect dragging of
     * cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    @FXML
    private Label goldLabel;

    @FXML
    private Label xpLabel;

    @FXML
    private Label cycleLabel;

    @FXML
    private Label damageLabel;

    @FXML
    private StackPane healthBar;

    @FXML
    private Button shopButton;

    @FXML
    private Label continueLabel;

    // all image views including tiles, character, enemies, cards... even though
    // cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here
     * and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through
     * maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    // card
    private Image vampireCastleCardImage;
    private Image zombiePitCardImage;
    private Image towerCardImage;
    private Image villageCardImage;
    private Image barracksCardImage;
    private Image trapCardImage;
    private Image campfireCardImage;
    // enemy
    private Image slugImage;
    private Image zombieImage;
    private Image vampireImage;
    private Image doggieImage;
    // items
    private Image swordImage;
    private Image stakeImage;
    private Image staffImage;
    private Image armourImage;
    private Image shieldImage;
    private Image helmetImage;
    private Image potionImage;
    private Image doggieCoinImage;
    // building
    private Image vampireCastleBuildingImage;
    private Image zombiePitBuildingImage;
    private Image towerBuildingImage;
    private Image villageBuildingImage;
    private Image barracksBuildingImage;
    private Image trapBuildingImage;
    private Image campfireBuildingImage;

    /**
     * the image currently being dragged, if there is one, otherwise null. Holding
     * the ImageView being dragged allows us to spawn it again in the drop location
     * if appropriate.
     */
    // TODO = it would be a good idea for you to instead replace this with the
    // building/item which should be dropped
    private ImageView currentlyDraggedImage;

    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged into the boundaries of its appropriate
     * gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged outside of the boundaries of its
     * appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    /**
     * object handling switching to the shop interface
     */
    private MenuSwitcher shopSwitcher;

    /**
     * @param world           world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be
     *                        loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        entityImages = new ArrayList<>(initialEntities);
        // card
        vampireCastleCardImage = new Image((new File("src/images/vampire_castle_card.png")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit_card.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower_card.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village_card.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks_card.png")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap_card.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire_card.png")).toURI().toString());
        // enemy
        slugImage = new Image((new File("src/images/slug.png")).toURI().toString());
        zombieImage = new Image((new File("src/images/zombie.png")).toURI().toString());
        vampireImage = new Image((new File("src/images/vampire.png")).toURI().toString());
        doggieImage = new Image((new File("src/images/doggie.png")).toURI().toString());
        // item
        swordImage = new Image((new File("src/images/basic_sword.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        potionImage = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
        doggieCoinImage = new Image((new File("src/images/doggie_coin.png")).toURI().toString());
        // building
        vampireCastleBuildingImage = new Image(
                (new File("src/images/vampire_castle_building_purple_background.png")).toURI().toString());
        zombiePitBuildingImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        towerBuildingImage = new Image((new File("src/images/tower.png")).toURI().toString());
        villageBuildingImage = new Image((new File("src/images/village.png")).toURI().toString());
        barracksBuildingImage = new Image((new File("src/images/barracks.png")).toURI().toString());
        trapBuildingImage = new Image((new File("src/images/trap.png")).toURI().toString());
        campfireBuildingImage = new Image((new File("src/images/campfire.png")).toURI().toString());

        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    @FXML
    public void initialize() {
        // TODO = load more images/entities during initialization

        Image pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the
        // twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages) {
            squares.getChildren().add(entity);
        }

        // add the ground underneath the cards
        for (int x = 0; x < world.getWidth(); x++) {
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x = 0; x < LoopManiaWorld.unequippedInventoryWidth; x++) {
            for (int y = 0; y < LoopManiaWorld.unequippedInventoryHeight; y++) {
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // initialize gold, xp and health
        goldLabel.textProperty().bind(world.getCharacter().goldProperty().asString());
        xpLabel.textProperty().bind(world.getCharacter().experienceProperty().asString());
        cycleLabel.textProperty().bind(world.getCharacter().cycleProperty().asString());
        damageLabel.textProperty().bind(world.getCharacter().damageProperty().asString());
        shopButton.visibleProperty().bind(world.getCharacter().inCastleProperty());

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);
    }

    /**
     * create and run the timer
     */
    public void startTimer() {
        // TODO = handle more aspects of the behaviour required by the specification
        System.out.println("starting timer");
        isPaused = false;
        // trigger adding code to process main game logic to queue. JavaFX will target
        // framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {
            Character character = world.getCharacter();
            world.runTickMoves();
            if (character.isInCastle()) {
                pause();
            }
            List<BasicEnemy> defeatedEnemies = world.runBattles();
            for (BasicEnemy e : defeatedEnemies) {
                reactToEnemyDefeat(e);
            }
            List<BasicEnemy> newEnemies = world.possiblySpawnEnemies();
            for (BasicEnemy newEnemy : newEnemies) {
                if (newEnemy instanceof Slug) {
                    onLoad((Slug) newEnemy);
                } else if (newEnemy instanceof Zombie) {
                    onLoad((Zombie) newEnemy);
                } else if (newEnemy instanceof Vampire) {
                    onLoad((Vampire) newEnemy);
                } else if (newEnemy instanceof Doggie) {
                    onLoad((Doggie) newEnemy);
                }
            }
            // set the width of health bar
            Double health = character.getHealth();
            if (health <= 0) {
                terminate();
            }
            double widthPercentage = health / Character.MAX_HEALTH;
            int healthBarWidth = 80;
            healthBar.setPrefWidth(widthPercentage * healthBarWidth);
            printThreadingNotes("HANDLED TIMER");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * pause the execution of the game loop the human player can still drag and drop
     * items during the game pause
     */
    public void pause() {
        isPaused = true;
        System.out.println("pausing");
        continueLabel.setVisible(true);
        timeline.stop();
    }

    public void terminate() {
        pause();
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * 
     * @param entity backend entity to be paired with view
     * @param view   frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a vampire card from the world, and pair it with an image in the GUI
     */
    private void loadVampireCard() {
        VampireCastleCard vampireCastleCard = world.loadVampireCard();
        onLoad(vampireCastleCard);
    }

    /**
     * load a zombie pit card from the world, and pair it with an image in the GUI
     */
    private void loadZombiePitCard() {
        ZombiePitCard zombiePitCard = world.loadZombiePitCard();
        onLoad(zombiePitCard);
    }

    /**
     * load a tower card from the world, and pair it with an image in the GUI
     */
    private void loadTowerCard() {
        TowerCard towerCard = world.loadTowerCard();
        onLoad(towerCard);
    }

    /**
     * load a village card from the world, and pair it with an image in the GUI
     */
    private void loadVillageCard() {
        VillageCard villageCard = world.loadVillageCard();
        onLoad(villageCard);
    }

    /**
     * load a barracks card from the world, and pair it with an image in the GUI
     */
    private void loadBarracksCard() {
        BarracksCard barracksCard = world.loadBarracksCard();
        onLoad(barracksCard);
    }

    /**
     * load a trap card from the world, and pair it with an image in the GUI
     */
    private void loadTrapCard() {
        TrapCard trapCard = world.loadTrapCard();
        onLoad(trapCard);
    }

    /**
     * load a campfire card from the world, and pair it with an image in the GUI
     */
    private void loadCampfireCard() {
        CampfireCard campfireCard = world.loadCampfireCard();
        onLoad(campfireCard);
    }

    /**
     * load a sword from the world, and pair it with an image in the GUI
     */
    private void loadSword() {
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Sword sword = world.addUnequippedSword();
        onLoad(sword);
    }

    /**
     * load a stake from the world, and pair it with an image in the GUI
     */
    private void loadStake() {
        Stake stake = world.addUnequippedStake();
        onLoad(stake);
    }

    /**
     * load a staff from the world, and pair it with an image in the GUI
     */
    private void loadStaff() {
        Staff staff = world.addUnequippedStaff();
        onLoad(staff);
    }

    /**
     * load an armour from the world, and pair it with an image in the GUI
     */
    private void loadArmour() {
        Armour armour = world.addUnequippedArmour();
        onLoad(armour);
    }

    /**
     * load a shield from the world, and pair it with an image in the GUI
     */
    private void loadShield() {
        Shield shield = world.addUnequippedShield();
        onLoad(shield);
    }

    /**
     * load a helmet from the world, and pair it with an image in the GUI
     */
    private void loadHelmet() {
        Helmet helmet = world.addUnequippedHelmet();
        onLoad(helmet);
    }

    /**
     * load a health potion from the world, and pair it with an image in the GUI
     */
    private void loadHealthPotion() {
        HealthPotion potion = world.addUnequippedHealthPotion();
        onLoad(potion);
    }

    /**
     * load a doggie Coin from the world, and pair it with an image in the GUI
     */
    private void loadDoggieCoin() {
        DoggieCoin doggieCoin = world.addUnequippedDoggieCoin();
        onLoad(doggieCoin);
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning
     * items/experience/gold
     * 
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(BasicEnemy enemy) {
        // react to character defeating an enemy
        // in starter code, spawning extra card/weapon...
        boolean isDropItem = enemy.dropItem();
        int randomItem = (new Random()).nextInt(7); // 0 to 6
        if (isDropItem) {
            if (enemy instanceof Doggie) {
                loadDoggieCoin();
            } else if (randomItem == 0) {
                loadSword();
            } else if (randomItem == 1) {
                loadStaff();
            } else if (randomItem == 2) {
                loadArmour();
            } else if (randomItem == 3) {
                loadShield();
            } else if (randomItem == 4) {
                loadHelmet();
            } else if (randomItem == 5) {
                loadStake();
            } else if (randomItem == 6) {
                loadHealthPotion();
            }
        }
        boolean isDropCard = enemy.dropCard();
        int randomCard = (new Random()).nextInt(5); // 0 to 4
        if (isDropCard) {
            if (randomCard == 0) {
                loadCampfireCard();
            } else if (randomCard == 1) {
                loadTrapCard();
            } else if (randomCard == 2) {
                loadBarracksCard();
            } else if (randomCard == 3) {
                loadVillageCard();
            } else if (randomCard == 4) {
                loadTowerCard();
            }
        }
    }

    /**
     * load a vampire castle card into the GUI.
     * 
     * @param vampireCastleCard
     */
    private void onLoad(VampireCastleCard vampireCastleCard) {
        ImageView view = new ImageView(vampireCastleCardImage);

        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(vampireCastleCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a zombiePit card into the GUI.
     * 
     * @param zombiePitCard
     */
    private void onLoad(ZombiePitCard zombiePitCard) {
        ImageView view = new ImageView(zombiePitCardImage);

        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(zombiePitCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a tower card into the GUI.
     * 
     * @param towerCard
     */
    private void onLoad(TowerCard towerCard) {
        ImageView view = new ImageView(towerCardImage);

        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(towerCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a village card into the GUI.
     * 
     * @param villageCard
     */
    private void onLoad(VillageCard villageCard) {
        ImageView view = new ImageView(villageCardImage);

        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(villageCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a barracks card into the GUI.
     * 
     * @param barracksCard
     */
    private void onLoad(BarracksCard barracksCard) {
        ImageView view = new ImageView(barracksCardImage);

        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(barracksCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a trap card into the GUI.
     * 
     * @param trapCard
     */
    private void onLoad(TrapCard trapCard) {
        ImageView view = new ImageView(trapCardImage);

        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(trapCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a campfire card into the GUI.
     * 
     * @param campfireCard
     */
    private void onLoad(CampfireCard campfireCard) {
        ImageView view = new ImageView(campfireCardImage);

        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(campfireCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a sword into the GUI.
     * 
     * @param sword
     */
    private void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(sword, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a stake into the GUI.
     * 
     * @param stake
     */
    private void onLoad(Stake stake) {
        ImageView view = new ImageView(stakeImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(stake, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a potion into the GUI. Particularly, we must connect to the drag
     * detection event handler, and load the image into the unequippedInventory
     * GridPane.
     * 
     * @param staff
     */
    private void onLoad(Staff staff) {
        ImageView view = new ImageView(staffImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(staff, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a staff into the GUI.
     * 
     * @param potion
     */
    private void onLoad(HealthPotion potion) {
        ImageView view = new ImageView(potionImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(potion, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a staff into the GUI.
     * 
     * @param potion
     */
    private void onLoad(DoggieCoin doggieCoin) {
        ImageView view = new ImageView(doggieCoinImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(doggieCoin, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load an armour into the GUI
     * 
     * @param armour
     */
    private void onLoad(Armour armour) {
        ImageView view = new ImageView(armourImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(armour, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a shield into the GUI.
     * 
     * @param shield
     */
    private void onLoad(Shield shield) {
        ImageView view = new ImageView(shieldImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(shield, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a helmet into the GUI.
     * 
     * @param helmet
     */
    private void onLoad(Helmet helmet) {
        ImageView view = new ImageView(helmetImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(helmet, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a slug into the GUI
     * 
     * @param slub
     */
    private void onLoad(Slug slug) {
        ImageView view = new ImageView(slugImage);
        addEntity(slug, view);
        squares.getChildren().add(view);
    }

    /**
     * load a zombie into the GUI
     * 
     * @param zombie
     */
    private void onLoad(Zombie zombie) {
        ImageView view = new ImageView(zombieImage);
        addEntity(zombie, view);
        squares.getChildren().add(view);
    }

    /**
     * load an vampire into the GUI
     * 
     * @param vampire
     */
    private void onLoad(Vampire vampire) {
        ImageView view = new ImageView(vampireImage);
        addEntity(vampire, view);
        squares.getChildren().add(view);
    }

    /**
     * load an doggie into the GUI
     * 
     * @param vampire
     */
    private void onLoad(Doggie doggie) {
        ImageView view = new ImageView(doggieImage);
        addEntity(doggie, view);
        squares.getChildren().add(view);
    }

    /**
     * load a vampire building into the GUI
     * 
     * @param building
     */
    private void onLoad(VampireCastleBuilding building) {
        ImageView view = new ImageView(vampireCastleBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a zombiePit building into the GUI
     * 
     * @param building
     */
    private void onLoad(ZombiePitBuilding building) {
        ImageView view = new ImageView(zombiePitBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a tower building into the GUI
     * 
     * @param building
     */
    private void onLoad(TowerBuilding building) {
        ImageView view = new ImageView(towerBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a village building into the GUI
     * 
     * @param building
     */
    private void onLoad(VillageBuilding building) {
        ImageView view = new ImageView(villageBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a barracks building into the GUI
     * 
     * @param building
     */
    private void onLoad(BarracksBuilding building) {
        ImageView view = new ImageView(barracksBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a trap building into the GUI
     * 
     * @param building
     */
    private void onLoad(TrapBuilding building) {
        ImageView view = new ImageView(trapBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a campfire building into the GUI
     * 
     * @param building
     */
    private void onLoad(CampfireBuilding building) {
        ImageView view = new ImageView(campfireBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the
     * background, dropping over the background. These are not attached to invidual
     * items such as swords/cards.
     * 
     * @param draggableType  the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to
     *                       (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane,
            GridPane targetGridPane) {

        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                // TODO = for being more selective about where something can be dropped,
                // consider applying additional if-statement logic
                /*
                 * you might want to design the application so dropping at an invalid location
                 * drops at the most recent valid location hovered over, or simply allow the
                 * card/item to return to its slot (the latter is easier, as you won't have to
                 * store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType) {
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    // Data dropped
                    // If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    boolean dropped = false;
                    if (node != targetGridPane && db.hasImage()) {

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        // Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        switch (draggableType) {
                            case CARD:
                                Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                // if drag position is not valid for card dragging position
                                if (newBuilding != null) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    if (currentlyDraggedImage.getImage().equals(vampireCastleCardImage)) {
                                        onLoad((VampireCastleBuilding) newBuilding);
                                    } else if (currentlyDraggedImage.getImage().equals(zombiePitCardImage)) {
                                        onLoad((ZombiePitBuilding) newBuilding);
                                    } else if (currentlyDraggedImage.getImage().equals(towerCardImage)) {
                                        onLoad((TowerBuilding) newBuilding);
                                    } else if (currentlyDraggedImage.getImage().equals(villageCardImage)) {
                                        onLoad((VillageBuilding) newBuilding);
                                    } else if (currentlyDraggedImage.getImage().equals(barracksCardImage)) {
                                        onLoad((BarracksBuilding) newBuilding);
                                    } else if (currentlyDraggedImage.getImage().equals(trapCardImage)) {
                                        onLoad((TrapBuilding) newBuilding);
                                    } else if (currentlyDraggedImage.getImage().equals(campfireCardImage)) {
                                        onLoad((CampfireBuilding) newBuilding);
                                    }
                                    dropped = true;
                                }
                                break;
                            case ITEM:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                // TODO = spawn an item in the new location. The above code for spawning a
                                // building will help, it is very similar
                                removeItemByCoordinates(nodeX, nodeY);
                                targetGridPane.add(image, x, y, 1, 1);
                                dropped = true;
                                break;
                            default:
                                break;
                        }
                        if (dropped) {
                            draggedEntity.setVisible(false);
                            draggedEntity.setMouseTransparent(false);
                            // remove drag event handlers before setting currently dragged image to null
                            currentlyDraggedImage = null;
                            currentlyDraggedType = null;
                            printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                        } else {
                            currentlyDraggedImage.setVisible(true);
                            draggedEntity.setVisible(false);
                            draggedEntity.setMouseTransparent(false);
                            // remove drag event handlers before setting currently dragged image to null
                            removeDraggableDragEventHandlers(draggableType, targetGridPane);
                            currentlyDraggedImage = null;
                            currentlyDraggedType = null;
                            printThreadingNotes("INVALID DRAG DROPPED ON GRIDPANE HANDLED");
                        }
                        node.setOpacity(1.0);
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a
                // sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read
                // https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for
        // dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>() {
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType) {
                    if (event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null) {
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for
        // dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType) {
                    // Data dropped
                    // If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node != anchorPaneRoot && db.hasImage()) {
                        // Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);

                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                // let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        return world.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove an item from the unequipped inventory by its x and y coordinates in
     * the unequipped inventory gridpane
     * 
     * @param nodeX x coordinate from 0 to unequippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to unequippedInventoryHeight-1
     */
    private void removeItemByCoordinates(int nodeX, int nodeY) {
        world.removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
    }

    /**
     * add drag event handlers to an ImageView
     * 
     * @param view           the view to attach drag event handlers to
     * @param draggableType  the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be
     *                       dragged
     * @param targetGridPane the relevant gridpane to which the entity would be
     *                       dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane,
            GridPane targetGridPane) {
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can
                                              // detect it...
                currentlyDraggedType = draggableType;
                // Drag was detected, start drap-and-drop gesture
                // Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);

                // Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                draggedEntity.setImage(currentlyDraggedImage.getImage());

                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED,
                        anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n : targetGridPane.getChildren()) {
                    // events for entering and exiting are attached to squares children because that
                    // impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = be more selective about whether highlighting changes - if it cannot be
                        // dropped in the location, the location shouldn't be highlighted!
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType) {
                                // The drag-and-drop gesture entered the target
                                // show the user that it is an actual gesture target
                                if (event.getGestureSource() != n && event.getDragboard().hasImage()) {
                                    n.setOpacity(0.7);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = since being more selective about whether highlighting changes, you
                        // could program the game so if the new highlight location is invalid the
                        // highlighting doesn't change, or leave this as-is
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType) {
                                n.setOpacity(1);
                            }

                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }

        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events this is
     * particularly important for slower machines such as over VLAB.
     * 
     * @param draggableType  either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane) {
        // remove event handlers from nodes in children squares, from anchorPaneRoot,
        // and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n : targetGridPane.getChildren()) {
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys. Specifically, we should pause when
     * pressing SPACE
     * 
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        // TODO = handle additional key presses, e.g. for consuming a health potion
        switch (event.getCode()) {
            case SPACE:
                if (isPaused) {
                    continueLabel.setVisible(false);
                    startTimer();
                } else {
                    pause();
                }
                break;
            default:
                break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher) {
        // TODO = possibly set other menu switchers
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * 
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        // TODO = possibly set other menu switchers
        pause();
        mainMenuSwitcher.switchMenu();
    }

    public void setShopSwitcher(MenuSwitcher shopSwitcher) {
        // TODO = possibly set other menu switchers
        this.shopSwitcher = shopSwitcher;
    }

    /**
     * this method is triggered when click button to go to shop in FXML
     * 
     * @throws IOException
     */
    @FXML
    private void switchToShop() throws IOException {
        shopSwitcher.switchMenu();
    }

    /**
     * Set a node in a GridPane to have its position track the position of an entity
     * in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the model
     * will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we
     * need to track positions of spawned entities such as enemy or items which
     * might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So
     * it is vital this is handled in this Controller class
     * 
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        // TODO = tweak this slightly to remove items from the equipped inventory?
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from
        // equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                .onAttach((o, l) -> o.addListener(xListener)).onDetach((o, l) -> {
                    o.removeListener(xListener);
                    entityImages.remove(node);
                    squares.getChildren().remove(node);
                    cards.getChildren().remove(node);
                    equippedItems.getChildren().remove(node);
                    unequippedInventory.getChildren().remove(node);
                }).buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                .onAttach((o, l) -> o.addListener(yListener)).onDetach((o, l) -> {
                    o.removeListener(yListener);
                    entityImages.remove(node);
                    squares.getChildren().remove(node);
                    cards.getChildren().remove(node);
                    equippedItems.getChildren().remove(node);
                    unequippedInventory.getChildren().remove(node);
                }).buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here,
        // position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is
     * running on the application thread. By running everything on the application
     * thread, you will not need to worry about implementing locks, which is outside
     * the scope of the course. Always writing code running on the application
     * thread will make the project easier, as long as you are not running
     * time-consuming tasks. We recommend only running code on the application
     * thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel) {
        System.out.println("\n###########################################");
        System.out.println("current method = " + currentMethodLabel);
        System.out.println("In application thread? = " + Platform.isFxApplicationThread());
        System.out.println("Current system time = " + java.time.LocalDateTime.now().toString().replace('T', ' '));
    }
}
