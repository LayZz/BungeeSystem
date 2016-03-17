package de.flashbeatzz.bungeesystem;

public class GuildeSystem {

    public GuildeSystem() {
        MySQL.createTable("guildes", "" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`name` VARCHAR(100) NOT NULL," +
                "`money` DOUBLE NOT NULL," +
                "PRIMARY KEY (`id`)");
    }

}
