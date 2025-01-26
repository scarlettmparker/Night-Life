# NightLife Plugin

- **Current Version**: 1.1.2  
- **API Version**: 1.16.5  
- **Main Class**: com.scarlettparker.nightlife.Plugin  
- **Description**: Night Life plugin created for the **Video Games Society** Night Life Server.
---

## Features

### Enchantment Restrictions
The plugin enforces strict rules on enchantments:
- **Maximum Enchant Level**: Enchantments are capped at **Level 1**.
- **Book Enchantments**: You **cannot enchant books**.
- **Auto-Nerf System**:
  - If a player holds or equips an enchanted book or item with an enchantment above Level 1, it will automatically downgrade to Level 1.
  - Combining enchanted items in an anvil will **not increase** the enchantment level beyond Level 1.
- **Villagers and Structures**:
  - Villagers will **not trade enchanted items**.
  - If enchanted items are somehow obtained (e.g., through structures), holding or equipping them will still enforce the **Level 1 cap**.
- **Bookcases**:
  - Adding bookcases will still show higher enchant levels and rarer enchants (e.g., **Silk Touch**).
  - Bookcases allow for multiple enchantments to be applied to an item during a single enchant action as usual, but only up to a maximum Level 1 enchantment.

### Potion and Firework Star Restrictions
- **Potions and Tipped Arrows**: Potions and tipped arrows above Level 1 are disabled. For example:
  - Maximum strength is **Strength I**.
  - Regeneration is limited to **Regen I**.
- **Firework stars**: Firework stars are **not craftable**.

---

### Commands

| Command           | Description                       | Aliases        | Usage                             |
|-------------------|-----------------------------------|----------------|----------------------------------|
| /help             | Displays the Night Life help menu. | None           | /help                            |
| /plugins          | Lists all plugins on the server.  | pl             | /plugins                         |
| /tell             | Sends a private message to a player. | msg, w, tellraw | /tell <player> <message>         |
| /boogey           | Sets the boogeyman role.          | None           | /boogey <players>                |
| /cure             | Cures a specific player.          | None           | /cure <player>                   |
| /punish           | Punishes a specific player.       | None           | /punish <player>                 |
| /startlife        | Starts the life series.           | None           | /startlife                       |
| /setlife          | Sets a player's life count.       | None           | /setlife <player> <lives>        |

---

### Game Rules

Specific game rules are applied to each dimension upon the starting the life server:

- logAdminCommands: **false** – Prevents logging of admin commands.
- sendCommandFeedback: **false** – Prevents players from seeing command feedback.
- keepInventory: **true** – Players do not lose their inventory upon death.
- doInsomnia: **false** – Prevents **phantoms** from spawning.
- difficulty: **hard** – Sets the game difficulty to **Hard**.

**Example Command Execution**:  
```java
Bukkit.dispatchCommand(console, "execute in <dimension> run gamerule <rule> <value>");
```

---

## Configuration

The NightLife plugin includes configurable options to adjust the life system rules. These settings can be customized in the plugin's configuration file (config.yml).

### Default Configuration
```yaml
life:
  starting_lives: 6
  max_lives: 6
  boogey_transfer_night: true
  kill_life_increment: true
  kill_life_upper_threshold: 4
  kill_life_lower_threshold: 2

explosion:
  explosion_speed: 0.4
  bed_explosion_radius: 5.0
  bed_explosion_dampening: 2.0
  anchor_explosion_radius: 3.0
  anchor_explosion_dampening: 4.0

egg:
  mob_egg_chance: 1.0
  spawner_drop_eggs: false
```

### Configuration Options

#### Life System
| Option                   | Description                                                                 | Default Value |
|--------------------------|---------------------------------------------------------------------------|---------------|
| starting_lives           | The number of lives each player starts with when /startlife is executed. | 6             |
| max_lives                | The maximum number of lives a player can have.                           | 6             |
| boogey_transfer_night    | Determines if the boogeyman curse transfers upon a kill at night.         | true          |
| kill_life_increment      | Determines if players gain a life when killing another player.           | true          |
| kill_life_upper_threshold| Minimum lives a killed player needs for the killer to gain a life.       | 4             |
| kill_life_lower_threshold| Minimum lives a killer needs to gain a life from a kill.                 | 2             |

#### Explosions
| Option                   | Description                                                                 | Default Value |
|--------------------------|---------------------------------------------------------------------------|---------------|
| explosion_speed          | How far bed, end crystal, and respawn anchor explosions will throw you.  | 0.4           |
| bed_explosion_radius     | Explosion radius of bed explosions.                                      | 5.0           |
| bed_explosion_dampening  | Power of which bed explosions are weakened.                             | 2.0           |
| anchor_explosion_radius  | Explosion radius of respawn anchor explosions.                          | 3.0           |
| anchor_explosion_dampening| Dampening power for respawn anchor explosions.                         | 4.0           |

#### Eggs
| Option                   | Description                                                                 | Default Value |
|--------------------------|---------------------------------------------------------------------------|---------------|
| mob_egg_chance           | Chance as a percentage of a mob dropping its own spawn egg.              | 1.0           |
| spawner_drop_eggs        | Whether mobs from spawners drop eggs.                                    | false         |

---

### Custom Recipes

#### Shaped Recipes

**Saddle**
- Ingredients: 5 leather and 1 tripwire hook.
- ![Saddle Recipe](https://media.discordapp.net/attachments/1331309935832731769/1331310473781575814/image.png?ex=679126ff&is=678fd57f&hm=903396723b802921886d0a4366da40f533f0d23d6c98094747f34c627bfe6399&=&format=webp&quality=lossless)

**Spawner**
- Ingredients: 8 iron bars and 1 diamond. You must then right-click the spawner with a spawn egg to make it spawn the mobs.
- ![Spawner Recipe](https://media.discordapp.net/attachments/1331309935832731769/1331310106662670468/image.png?ex=679126a7&is=678fd527&hm=f9debfcb833922cf7da00fbc76c6a04a840023feade96acc72260b1aaa69ae05&=&format=webp&quality=lossless)

**Elytra**
- Ingredients: 2 blaze rods, 3 end stone, a netherite chestplate, and 2 diamonds.
- ![Elytra Recipe](https://media.discordapp.net/attachments/1331309935832731769/1331309948281556992/image.png?ex=67912682&is=678fd502&hm=e62dc2f55388b86613bc2140ec06656f9127d98ab7eae4420355141d4ecad160&=&format=webp&quality=lossless)

#### Shapeless Recipes

**Name Tag**
- Ingredients: 1 iron bar, 1 paper, and 1 string.
- ![Name Tag Recipe](https://media.discordapp.net/attachments/1331309935832731769/1331310373109760010/image.png?ex=679126e7&is=678fd567&hm=e9eb5fa103ad641ae136a1a5f565d883b66573aa1771c886b8ad9371e9c55c63&=&format=webp&quality=lossless)

**TNT**
- Ingredients: 1 gunpowder, 1 paper, and 1 string.
- ![TNT Recipe](https://media.discordapp.net/attachments/1331309935832731769/1331310311889961105/image.png?ex=679126d8&is=678fd558&hm=4dda090429dc1bb349bed99ada47cd2cc9eb1ecbbb689190f029cc66d4e6caab&=&format=webp&quality=lossless)

---

### Recommendations

The following plugins are recommended for optimal server performance on VGS:
1. **[Chunky](https://www.spigotmc.org/resources/chunky.81534/)** – For efficient world generation and chunk management.

---

### Installation

1. **Download** the NightLife plugin .jar file.
2. Place it into your server's plugins folder.
3. Restart your server to load the plugin.

---

## License

This project is licensed under the [**MIT License**](LICENSE).

