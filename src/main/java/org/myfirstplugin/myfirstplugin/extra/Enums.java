package org.myfirstplugin.myfirstplugin.extra;

public class Enums {
    public enum SpecialItems {
        ANVIL, RESURRECT
    }

    public enum UniqueEnchants {
        Anvil("Anvil Fall"),
        Resurrect("Resurrect"),
        Sacrifice("Sacrifice");

        private final String name;

        UniqueEnchants(String name) {
            this.name = name;
        }

        public String Name() {
            return "\"" + name + "\"";
        }

    }
}
