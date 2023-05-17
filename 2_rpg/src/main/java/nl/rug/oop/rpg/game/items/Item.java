package nl.rug.oop.rpg.game.items;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Item {
    private String name;
    private String description;
}
