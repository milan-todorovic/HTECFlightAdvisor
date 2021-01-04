package com.htec.service;

import com.htec.model.RootAttributes;
import com.htec.util.Configuration;
import com.htec.util.GraphOfRoutes;

import java.util.*;

/**
 * Given a connected graph (graphical representation of all airports and flight routes),
 * finds all routes between any two airports. A DFS methode of graph search is performed here.
 */
public class FindRoutes<T> {

    private final GraphOfRoutes<T> graph;
    private Configuration configuration = Configuration.instance();

    /**
     * Constructor that takes flight routes represented through graph.
     */
    public FindRoutes(GraphOfRoutes<T> graph) {
        if (graph == null) {
            throw new NullPointerException("The input graph that represents flight routes cannot be null.");
        }
        this.graph = graph;
    }

    /**
     * Validation of source and destination node/airport
     * @param source source node/airport
     * @param destination destination node/airport
     */
    private void validate(T source, T destination) {

        if (source == null) {
            throw new NullPointerException("The source airport: " + source + " cannot be null.");
        }
        if (destination == null) {
            throw new NullPointerException("The destination airport: " + destination + " cannot be  null.");
        }
        if (source.equals(destination)) {
            throw new IllegalArgumentException("The source and destination airports: " + source + " cannot be the same.");
        }
    }

    /**
     * Returns the list of routes, where route itself is a list of nodes/airports, based on DFS methode of graph search
     * that is performed here with recursive search of routes/graph paths.
     *
     * @param source      the source node/airport
     * @param destination the destination node/airport
     * @return List of all flight routes
     */
    public Map<List<T>, RootAttributes> getAllRoutsWithCost(T source, T destination) {
        validate(source, destination);

        Map<List<T>, RootAttributes> routeWithCost = new HashMap<List<T>, RootAttributes>();

        List<List<T>> routes = new ArrayList<List<T>>();
        List<RootAttributes> totalCost = new ArrayList<RootAttributes>();
        Double cost = new Double(0);
        Double length = new Double(0);
        recursiveWithCost(source, destination, routes, new LinkedHashSet<T>(), totalCost, cost, length, new HashMap<T, RootAttributes>());
        for (int i = 0; i < routes.size(); i++) {
            routeWithCost.put(routes.get(i), totalCost.get(i));
        }
        return routeWithCost;
    }

    private void recursiveWithCost(T current, T destination, List<List<T>> routes, LinkedHashSet<T> route, List<RootAttributes> totalCost, Double cost, Double length, Map<T, RootAttributes> allEdges) {
        if (route.size() > configuration.NUMBER_OF_TRANSITES) {
            return;
        }
        route.add(current);
        if (allEdges.get(current) != null) {
            cost = cost + allEdges.get(current).getCost();
            length = length + allEdges.get(current).getLength();
        }
        if (current.equals(destination)) {
            routes.add(new ArrayList<T>(route));

            totalCost.add(RootAttributes.builder()
                    .cost(cost)
                    .length(length)
                    .build());
            route.remove(current);
            return;
        }

        allEdges = graph.edgesFrom(current);

        final Set<T> edges = graph.edgesFrom(current).keySet();

        for (T t : edges) {
            if (!route.contains(t)) {
                recursiveWithCost(t, destination, routes, route, totalCost, cost, length, allEdges);
            }
        }

        route.remove(current);
    }
}
