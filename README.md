# 🧭 Manhunt+

**Manhunt+** is a powerful Minecraft manhunt plugin featuring compass tracking, exciting twists, and full control over the hunt!

---

## ⚙️ Features & Usage

- ⚡ Easy and fast setup
- 🧭 Compass tracking across all dimensions
- 🌀 Enrich the game with twists
- 🛠️ Create your own twists *(coming soon!)*

### 🕹️ Commands

#### 🧍 Basic Setup
- `/manhunt speedrunner add|remove <player>` – Add/remove a speedrunner
- `/manhunt hunter add|remove <player>` – Add/remove a hunter
- `/manhunt start` – Start the manhunt
- `/manhunt stop` – Stop the manhunt
- `/manhunt list` – Lists all hunters and speedrunners
- `/compass <player:speedrunner>` – Track a speedrunner

#### 🧪 Extras
- `/manhunt prepare` – Waits for a speedrunner to hit a hunter to start
- `/surround <speedrunner>` – Surrounds the speedrunner with all hunters

#### ⚙️ Settings
- `/manhunt countdown <minutes>` – Set a max time limit ⏱️ (**Set to 0 to disable**)
- `/manhunt twist <twist>` – Apply a twist to the manhunt (default is `DEFAULT`)
- `/manhunt help` – Open the help menu 📖
- Config:
  


---

### 🌀 Twists
- `DEFAULT` – Standard manhunt rules
- `PIG_OP_LOOT` – Pigs drop OP loot (only for the speedrunner 🐷💎)
- **More twists coming soon! 🔧**

---

## 🛠️ Configuration

🗂️ Navigate to your server folder → `plugins/ManhuntPlus/config.yml`

- Calibration
    - `auto-calibration`: Enable/Disable auto compass calibration
    - `auto-calibration-interval` - Set the interval on which the compass updates. Recommended to increase this if used on larger servers or even completely disable it, restart server to apply changes
- Broadcasting
    - `broadcast-time`: true
    - `broadcast-time-every` - Broadcast time interval(in seconds)
- `surround-radius: DECIMAL` – Distance between speedrunner and hunters when using `/surround`. Default: `3`
- `name-tracking-compass`: Name the compass to which player its tracking
---

## 🚧 Coming Soon
- 🛠️ Custom Twist Tweaker
- 🎯 Lootpool Modifier

---

## 💬 Support, Tracking & More

- 💬 Join our [**Discord**](https://discord.tutla.net) for sneak peeks & community support
- ⭐ Star us on [**GitHub**](https://github.com/TutlaMC/manhunt-plus)
- 🌐 Visit our [**Website**](https://tutla.net) for updates

Think we’re missing something? **Contribute on [GitHub](https://github.com/TutlaMC/manhunt-plus)**

---

## 🧪 Other Tutla Studio Projects

- 🛡️ [**LeaveBind!**](https://modrinth.com/mod/leavebind) – Leave the server with a click of a button
- 🧾 [**Leave This, GoBack!**](https://modrinth.com/mod/leave-this-go-back) – Pressing on a slot twice returns you back to the previous slot. Useful for attribute swapping / quick tool swapping / mace pvp 
