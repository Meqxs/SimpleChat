# AntiSwear Plugin

A Minecraft Paper plugin that filters swear words from chat and logs violations.

## Features

- Blocks messages containing swear words
- Logs all violations with timestamps
- Configurable list of swear words
- Case-insensitive filtering
- Player notification when messages are blocked

## Installation

1. Download the latest release from the releases page
2. Place the .jar file in your server's `plugins` folder
3. Restart your server or reload plugins
4. The plugin will create a default configuration file

## Configuration

The plugin creates two main files:

1. `config.yml` - Contains the list of swear words to filter
2. `swear_logs.yml` - Contains logs of all violations

### Adding/Removing Swear Words

To modify the list of filtered words, edit the `config.yml` file in the plugin's folder. Add or remove words under the `swear-words` section.

## Logs

All violations are logged in `swear_logs.yml` with the following information:
- Timestamp
- Player name
- Blocked message

## Requirements

- Paper 1.21.4 or higher
- Java 17 or higher

## Support

If you encounter any issues or have suggestions, please open an issue on the GitHub repository. 