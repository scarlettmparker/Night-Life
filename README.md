# NightLife Plugin

- **Current Version**: `1.0.1`  
- **API Version**: `1.15` (Compatible with 1.21.4)
- **Main Class**: `com.scarlettparker.nightlife.Plugin`  
- **Description**: Night Life plugin created for the **Video Games Society** Night Life Server.
---

## Features

### Custom Enchantment System
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

---

### Commands

| Command           | Description                       | Aliases        | Usage                             |
|-------------------|-----------------------------------|----------------|----------------------------------|
| `/help`           | Displays the Night Life help menu. | None           | `/help`                          |
| `/plugins`        | Lists all plugins on the server.  | `pl`           | `/plugins`                       |
| `/tell`           | Sends a private message to a player. | `msg`, `w`, `tellraw` | `/tell <player> <message>` |
| `/boogey`         | Sets the boogeyman role.          | None           | `/boogey <players>`              |
| `/cure`           | Cures a specific player.          | None           | `/cure <player>`                 |
| `/punish`         | Punishes a specific player.       | None           | `/punish <player>`               |
| `/startlife`      | Starts the life series.           | None           | `/startlife`                     |
| `/setlife`        | Sets a player's life count.       | None           | `/setlife <player> <lives>`      |

---

### Game Rules

Specific game rules are applied to each dimension for balanced and challenging gameplay:

- `logAdminCommands`: **false** – Prevents logging of admin commands for cleaner logs.
- `sendCommandFeedback`: **false** – Prevents players from seeing command feedback.
- `keepInventory`: **true** – Players do not lose their inventory upon death.
- `doInsomnia`: **false** – Prevents **phantoms** from spawning.
- `difficulty`: **hard** – Sets the game difficulty to **Hard** for a more challenging experience.

**Example Command Execution**:
```java
Bukkit.dispatchCommand(console, "execute in <dimension> run gamerule <rule> <value>");
```

---

## Configuration

The NightLife plugin includes configurable options to adjust the life system rules. These settings can be customized in the plugin's configuration file (`config.yml`).

### Default Configuration

```yaml
starting_lives: 6
max_lives: 6
kill_life_increment: true
kill_life_increment_threshold: 4
```

### Configuration Options

| Option                          | Description                                                                 | Default Value |
|---------------------------------|---------------------------------------------------------------------------|---------------|
| `starting_lives`                | The number of lives each player starts with when `/startlife` is executed. | `6`           |
| `max_lives`                     | The maximum number of lives a player can have.                           | `6`           |
| `kill_life_increment`           | Determines if players gain a life when killing another player.           | `true`        |
| `kill_life_increment_threshold` | Minimum lives a killed player needs for the killer to gain a life.       | `4`           |

---

### How to Use

1. Open the plugin's `config.yml` file in your server's `plugins/NightLife/` directory.
2. Edit the values to fit your server's needs.
3. Save the file and restart your server for the changes to take effect.

**Example:**
- To prevent players from gaining lives on kills, set:
  ```yaml
  kill_life_increment: false
  ```

--- 

### Recommendations

The following plugins are recommended for optimal server performance on VGS:
1. **[Chunky](https://www.spigotmc.org/resources/chunky.81534/)** – For efficient world generation and chunk management.

---

### Installation

1. **Download** the NightLife plugin `.jar` file.
2. Place it into your server's `plugins` folder.
3. Reload your server

---

## License

This project is licensed under the [**MIT License**](LICENSE).