package nl.rug.oop.rts.model;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Node {
    private int id;

    private String name;

    private Set<Edge> edges;

    
}
