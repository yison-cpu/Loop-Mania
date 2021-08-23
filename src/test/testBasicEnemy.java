package test;

import org.junit.Test;

import unsw.loopmania.LoopManiaWorld;

public class testBasicEnemy {
    
    // test each constructor behaves appropriately alongside getters and setters
    // test any other functions within the class here as we create them (i.e. sorting lists of enemies, testing positions)

    @Test
    public void testNewZombie() {
        LoopManiaWorld newWorld = makePathAndWorld();

        Zombie zombie = new Zombie(10,2,3,100,0,5);
        assertEquals(10, zombie.getDamage(), "zombie's damage should be 10");
        assertEquals(2, zombie.getBattleRadius(), "zombie's battle radius should be 2");
        assertEquals(3, zombie.getSupportRadius(), "zombie's support radius should be 3");
        assertEquals(100, zombie.getHealth(), "zombie's health should be 100");
        assertEquals(0.5, zombie.getSpeed(), "zombie's speed should be 0.5 tile/second");

        newWorld.incrementLoopCounter();
        assertEquals(20, zombie.getDamage(), "zombie's damage should now be 20");

        newWorld.delete();
        zombie.delete();
    }

    @Test
    public void testNewSlug() {
        LoopManiaWorld newWorld = makePathAndWorld();

        Slug slug = new Slug(5,1,1,50,1);
        assertEquals(5, slug.getDamage(), "slug's damage should be 5");
        assertEquals(1, slug.getBattleRadius(), "slug's battle radius should be 1");
        assertEquals(1, slug.getSupportRadius(), "slug's support radius should be 1");
        assertEquals(50, slug.getHealth(), "slug's health should be 50");
        assertEquals(1, slug.getSpeed(), "slug's speed should be 1 tile/second");

        newWorld.incrementLoopCounter();
        assertEquals(10, slug.getDamage(), "slug's damage should now be 10");

        newWorld.delete();
        slug.delete();
    }

    @Test
    public void testNewVampire() {
        LoopManiaWorld newWorld = makePathAndWorld();

        Vampire vampire = new Vampire(20,3,4,150,2);
        assertEquals(20, vampire.getDamage(), "vampire's damage should be 20");
        assertEquals(3, vampire.getBattleRadius(), "vampire's battle radius should be 3");
        assertEquals(4, vampire.getSupportRadius(), "vampire's support radius should be 4");
        assertEquals(150, vampire.getHealth(), "vampire's health should be 150");
        assertEquals(2, vampire.getSpeed(), "vampire's speed should be 2 tile/second");

        newWorld.incrementLoopCounter();
        assertEquals(40, vampire.getDamage(), "vampire's damage should now be 40");

        newWorld.delete();
        vampire.delete();
    }

}