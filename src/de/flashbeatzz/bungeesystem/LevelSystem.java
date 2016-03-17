package de.flashbeatzz.bungeesystem;

public class LevelSystem {

    public LevelSystem() {
        MySQL.createTable("levelsystem", "" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`uuid` VARCHAR(100) NOT NULL," +
                "`level` INT NOT NULL," +
                "`exp` INT NOT NULL," +
                "PRIMARY KEY (`id`)");
    }

}
