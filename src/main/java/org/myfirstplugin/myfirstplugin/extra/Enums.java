package org.myfirstplugin.myfirstplugin.extra;

import org.bukkit.Material;

public class Enums {
    public enum SpecialItems {
        ANVIL, RESURRECT, SNIPER
    }

    public enum UniqueEnchants {
        ANVIL("Anvil Fall"),
        RESURRECT("Resurrect"),
        SNIPER("Sniper");

        //
        private final String name;

        UniqueEnchants(String name) {
            this.name = name;
        }

        public String Name() {
            return "\"" + name + "\"";
        }

    }

    public enum Sacrifices {
        DiamondRain("Diamond Rain", 120, Material.NETHER_STAR, Material.DIAMOND_BLOCK);

        //
        private final String lore;
        // timeout in seconds (20 tps = 1s)
        private final int timeout;
        private final Material item;
        private final Material blockToFall;


        Sacrifices(String lore, int timeout, Material item, Material blockToFall) {
            this.lore = lore;
            this.timeout = timeout;
            this.item = item;
            this.blockToFall = blockToFall;
        }

        public int getTimeout() {
            return timeout;
        }

        public String getLore() {
            return "\"" + lore + "\"";
        }

        public Material getItem() {
            return item;
        }

        public Material getBlockToFall() {
            return blockToFall;
        }
    }
}
