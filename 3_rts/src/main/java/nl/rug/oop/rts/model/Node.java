package nl.rug.oop.rts.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Node {
    public static final int NODE_SIZE = 64;

    private final int id;

    @NonNull
    private Point position;

    @NonNull
    private String name;

    private Set<Edge> edges = new HashSet<>();
}
