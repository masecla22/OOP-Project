package nl.rug.oop.rts.protocol.adapters;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonArray;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

public class GameMapTypeAdapter extends TypeAdapter<Map> {

    @Override
    public JsonElement serialize(Map object) {
        JsonObject result = new JsonObject();

        result.put("nodes", this.getTreeSerializer().toJson(object.getNodes()));

        JsonArray array = new JsonArray();
        for (Edge edge : object.getEdges()) {
            JsonObject edgeSerialized = new JsonObject();

            // We need to serialize the nodes as their id, not as the node itself
            edgeSerialized.put("from", this.getTreeSerializer().toJson(edge.getPointA().getId()));
            edgeSerialized.put("to", this.getTreeSerializer().toJson(edge.getPointB().getId()));
            edgeSerialized.put("id", this.getTreeSerializer().toJson(edge.getId()));
            edgeSerialized.put("armies", this.getTreeSerializer().toJson(edge.getArmies()));
            edgeSerialized.put("events", this.getTreeSerializer().toJson(edge.getEvents()));

            array.add(edgeSerialized);
        }

        result.put("edges", array);
        result.put("offset", this.getTreeSerializer().toJson(object.getOffset()));

        return result;
    }

    @Override
    public Map deserialize(JsonElement consumer, Class<Map> clazz, List<Class<?>> genericTypes) {
        if (!(consumer instanceof JsonObject)) {
            throw new IllegalArgumentException("Expected JsonObject, got " + consumer.getClass().getSimpleName());
        }

        JsonObject object = consumer.asJsonObject();

        Set<Node> nodes = this.getTreeSerializer().fromJson(object.get("nodes"), Set.class, Node.class);

        java.util.Map<Integer, Node> nodeMap = new HashMap<>();
        for (Node node : nodes)
            nodeMap.put(node.getId(), node);

        JsonArray edges = object.get("edges").asJsonArray();
        Set<Edge> resultingEdges = new HashSet<>();
        for (JsonElement element : edges) {
            if (!(element instanceof JsonObject)) {
                throw new IllegalArgumentException("Expected JsonObject, got " + element.getClass().getSimpleName());
            }

            JsonObject edge = element.asJsonObject();

            int from = (int) edge.get("from").asJsonNumber().asInt();
            int to = (int) edge.get("to").asJsonNumber().asInt();
            int id = (int) edge.get("id").asJsonNumber().asInt();

            List<Army> armies = this.getTreeSerializer().fromJson(edge.get("armies"), List.class, Army.class);
            List<Event> events = this.getTreeSerializer().fromJson(edge.get("events"), List.class, Event.class);

            Node fromNode = nodeMap.get(from);
            Node toNode = nodeMap.get(to);

            Edge edgeObject = new Edge(id, fromNode, toNode, armies, events);
            fromNode.getEdges().add(edgeObject);
            toNode.getEdges().add(edgeObject);

            resultingEdges.add(edgeObject);
        }

        Point offset = this.getTreeSerializer().fromJson(object.get("offset"), Point.class);

        Map map = new Map();
        map.setOffset(offset);
        map.setEdges(resultingEdges);
        map.setNodes(nodes);

        return map;
    }

}
