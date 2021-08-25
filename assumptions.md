# **Assumptions**

## **Character**:

Movement:

    - The main character will only move through the loop clockwise.
    - 0.3 seconds to move.

Attack action:

    - The battle will start automatically when the main character is in the attack range of enemies around.
    - When the battle starts, the nearby monsters will join the battle as long as the main character is in their attack range.
    - The maximum number of monsters in a single battle is three
    - The priority of the nearby monsters joining the battle depends on their distance from the main character.

Default Attribute:

| HP  | Strength | Defense |    Speed    | Attack speed |
| :-: | :------: | :-----: | :---------: | :----------: |
| 250 |    15    |    8    | 1 block/sec | 1 per second |

Experience:

    - The main character can gain extra property by upgrading. Experience can be collected by killing monsters.
    - The experience required to upgrade is 200.
    - The HP, Strength and Defense of the main character will increase by 30% for each upgrade.

## **Enemy:**

### **Slug:**

Movement:

    - move randomly (Clockwise/Anti-clockwise) after it is spawned

Attribute:

| HP  | Strength | Defense |     Speed     |  Attack speed  | battle radius | support radius | exp |
| :-: | :------: | :-----: | :-----------: | :------------: | :-----------: | :------------: | :-: |
| 15  |    11    |    3    | 0.5 block/sec | 0.6 per second |    1 grids    |    1 grids     | 25  |

attack behaviour:

    - normal attack

### **Zombie:**

Movement:

    - move randomly (Clockwise/Anti-clockwise) after it is spawned

Attribute:

| HP  | Strength | Defense |     Speed     | battle radius | support radius | exp |
| :-: | :------: | :-----: | :-----------: | :-----------: | :------------: | :-: |
| 40  |    20    |    8    | 0.2 block/sec |    2 grids    |    1 grids     | 50  |

attack behaviour:

    - normal attack
    - critical bite. (have higher damage when biting the character. Special: A critical bite from a zombie against an allied soldier (which has a random chance of occurring) will transform the allied soldier into a zombie, which will then proceed to fight against the Character until it is killed. )

### **Vampire:**

Movement:

    - move randomly (Clockwise/Anti-clockwise) after it is spawned

Attribute:

| HP  | Strength | Defense |    Speed    |  Attack speed  | battle radius | support radius | exp |
| :-: | :------: | :-----: | :---------: | :------------: | :-----------: | :------------: | :-: |
| 60  |    16    |   12    | 1 block/sec | 0.6 per second |    2 grids    |    3 grids     | 100 |

attack behaviour:

    - normal attack
    - critical bite: A critical bite (which has a random chance of occurring) from a vampire causes random additional damage with every vampire attack, for a random number of vampire attacks

### **Thief:**

Movement:

    - move randomly (Clockwise/Anti-clockwise) after it is spawned

Attribute:

| HP  | Strength | Defense |    Speed    | battle radius | support radius | exp |
| :-: | :------: | :-----: | :---------: | :-----------: | :------------: | :-: |
| 50  |    1     |    8    | 1 block/sec |    1 grids    |    2 grids     | 50  |

attack behaviour:

    - normal attack
    - Thief Steal: A steal (which has a random chance of occurring) from a Thief causes random item lost from character and random number of gold lost from character

### **Doggie:**

(Boss, spawns after 20 cycles)

Movement:

    - move randomly (Clockwise/Anti-clockwise) after it is spawned 

Attribute:

| HP  | Strength | Defense |    Speed    | battle radius | support radius | exp |
| :-: | :------: | :-----: | :---------: | :-----------: | :------------: | :-: |
| 85  |    22    |    13   | 1 block/sec |    1 grids    |    2 grids     | 200 |

attack behaviour:

    - normal attack
    - Doggie Stun: can stun the character, which prevents the character from making an attack temporarily

Special:
Character can get doggieCoin once defeated doggie

### **ElanMuske:**

(Boss, Spawns after 40 cycles, and the player has reached 10000 experience points)

Movement:

    - move randomly (Clockwise/Anti-clockwise) after it is spawned

Attribute:

| HP  | Strength | Defense |    Speed    | battle radius | support radius | exp |
| :-: | :------: | :-----: | :---------: | :-----------: | :------------: | :-: |
| 100 |    24    |    14   | 1 block/sec |    1 grids    |    2 grids     | 500 |

attack behaviour:

    - normal attack
    - Elan healing: recover all the lost Hp for other enemy NPCs 

Special:
When ElanMuske spawns, the price of doggieCoin will be increased 1-500$ randomly in every tick, while the price will drop to
250-500 when ElanMuske is defeated.

## **Building**:

### **Tower**:

    - Attack damage: 9% of the maximum health of enemies
    - Attack speed: every tick in game
    - battle radius: 2 blocks
    - Only on non-path tiles adjacent to the path

### **Village**:

    - Refills health: 10%
    - Only on path tiles

### **campfire:**
    - Refills health: 10%
    - battle radius: 3 blocks
    - Any non-path tile

### **Trap**:

    - Attack damage: 25% of maximum health
    - Only on path tiles
    
### **Barracks**:
    - Provide one allied soldier each time
    - Only on path tiles

### **Zombie pit**:

    - Produces zombies every cycle of the path completed by the Character, spawning nearby on the path
    - Only on non-path tiles adjacent to the path

### **Vampire castle**:

    - Produces vampires every 5 cycles of the path completed by the Character
    - spawning nearby on the path
    - Only on non-path tiles adjacent to the path

### **Magic Shop**:

    - very low chance to get from killing enemy
    - when character pass the Magic Shop, he will have a chance to buy items
    - Only on path tiles adjacent to the path

## **Item**:

    - Each equip has a level, the actual property of a equip is calculated by default property \* level
    - The level of the equip that the main character can achieve is depend on the loopNumber, and it will not be bigger than (loopNumber + 1)

### **Sword:**

    - Increase Strength: 10
    - Sword can be sell for 50 gold
    - Sword need 100 gold to buy

### **Stake:**

    - Increase Strength: 5
    - Special property: when attacking a vampire, damage from the character will become true damage (the defense of the vampire will be ignored).
    - Stake can be sell for 50 gold
    - Stake need 100 gold to buy

### **Staff**:

    - Increase Strength: 2
    - Special property: have 15% chance to inflict a trance, which transforms the attacked enemy into an allied soldier for 10 seconds. If the trance ends during the fight, the affected enemy reverts back to acting as an enemy which fights the Character. If the fight ends whilst the enemy is in a trance, the enemy dies
    - Staff can be sell for 50 gold
    - Staff need 100 gold to buy

### **Armour:**

    - Increase health: 50
    - Armour can be sell for 50 gold
    - Armour need 100 gold to buy

### **Shield**:

    - Increase armour: 10
    - Shield can be sell for 50 gold
    - Shield need 100 gold to buy

### **Helmet:**

    - Increase armour: 5
    - increase health: 25
    - Helmet can be sell for 50 gold
    - Helmet need 100 gold to buy

### **The One Ring:**

    - rare item
    - Ability: If the Character is killed, it respawns with full health up to a single time

### **Anduril:**

    - rare item
    - Increase Strength: 10
    - Ability: A very high damage sword which causes triple damage against bosses

### **TreeStump:**

    - rare item
    - Increase armour: 10
    - Ability:An especially powerful shield, which provides higher defence against bosses

### **Gold**:

    - integer, no maximum
    - 4% chance will spawn 10 gold on path
    - Maximum number on path: 3

### **DoggieCoin**:

    - integer, no maximum, the price will fluctuate in every tick. Normal price will be in range (250, 500)

### **Potion:**

    - Refills health: 30%
    - Maximum number on path: 4
    - 2% chance will spawn potion on path
    - potion can be sell for 50 gold
    - potion need 100 gold to buy

### **Rare item:**

    - Every time the Character wins a battle, there is a small chance(15%) of winning a "Rare Item"
    - TheOneRing can revive character
    - ring can not be buy, but can be sold for 200 gold

### **allied soldier:**
    - Enemies will first attack the allied soldier if the character has allies.
    - allied soldier: (maximum 5 in battle)
    - HP: 50
    - Strength: 15
    - defense: 10
    - attack speed: 0.6 per second

## **Game**:

### **Statistics**:

    - The statistics of enemies and soldiers will upgrade in every loop.
    - The statistics include damage, HP and defense.

### **Formula**:

$` Base \cdot n \cdot (1 + (n-1) \cdot 0.03 ) \cdot \text{DifficultyConstant} `$

- $`n`$: the number of loops that the character has gone through
- $`Base`$: the initial value of every attribute.

$`\text{DifficultyConstant}`$:

      - standard mode: 1
      - survival mode: 1.05
      - Berseka mode: 1.1

### **Shop**:

    - Shop will automatically pop out when the character arrives at the hero’s castle and meets the loop condition.
    - Shop item will be stronger than the one receive by killing monster, which for the formula, item that can be buy in shop will be n + 1 instead of n

### **Rewards for killing enemies**:
    - For each enemy killed, character will definitely acquire a card, but have 40% chance getting item, otherwise it will get 50 gold.

### **Goal**:
    - Three chapter is made.
    - Chapter 1 need 10000 gold or 7000 experience or 20 cycle to win.
    - Chapter 2 need 7000 gold and 10000 experience to win.
    - Chapter 3 need 7000 gold and 10000 experience and 100 cycle to win.
    - If character died, then the game will be over.

### **Mode**:
    - There is three mode: Standard mode, Survival mode and Berserker mode.
    - Standard mode no restriction.
    - Survival mode can only buy one potion in shop.
    - Berserker mode can only buy one item in shop.
