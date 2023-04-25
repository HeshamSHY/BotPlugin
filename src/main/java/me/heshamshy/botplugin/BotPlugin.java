/*
 *     BotPlugin
 *     Copyright (C) 2023  HeshamSHY
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.heshamshy.botplugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class BotPlugin extends JavaPlugin {

    private final Timer timer = new Timer();

    int lastMessageIndex = 0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        final ChatColor messageColor = ChatColor.valueOf(getConfig().getString("messageColor"));
        final ArrayList<String> messages = (ArrayList<String>) getConfig().getStringList("messages");
        final boolean isRandom = getConfig().getBoolean("isRandom");
        final int frequency = getConfig().getInt("frequency") * 60 * 1000;

        if (!messages.isEmpty()) {
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            getServer().broadcastMessage(
                                    messageColor + chooseMessage(messages, isRandom)
                            );
                        }
                    },
                    frequency, frequency

            );
        }

        getServer().getLogger().info("Plugin is enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        timer.cancel();

        getServer().getLogger().info("Plugin is disabled!");
    }

    private String chooseMessage(List<String> messages, boolean isRandom) {
        if (isRandom) {
            Random randomizer = new Random();
            return messages.get(randomizer.nextInt(messages.size()));
        }

        if (lastMessageIndex >= messages.size()-1) lastMessageIndex = 0;

        return messages.get(lastMessageIndex);
    }
}
