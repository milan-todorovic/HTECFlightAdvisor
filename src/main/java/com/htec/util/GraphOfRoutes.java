package com.htec.util;

import com.htec.model.RootAttributes;

import java.util.*;

/**
 * Provides structure to store between airports based on flight rotes.
 * @param <T>
 */
public class GraphOfRoutes<T> implements Iterable<T> {

    public final Map<T, Map<T, RootAttributes>> graph = new HashMap<T, Map<T, RootAttributes>>();

    /**
     *  Adds a new node/airport to the graph. If the node/airport already exists then
     *  duplicate is not inderted
     *
     * @param node  Adds to a graph/airport. If node/airport is null then it is not stored.
     * @return      true if node/airport is added, false otherwise.
     */
    public boolean addNode(T node) {
        if (node == null) {
            throw new NullPointerException("The input node cannot be null.");
        }
        if (graph.containsKey(node)) {
            return false;
        }
        graph.put(node, new HashMap<T, RootAttributes>());
        return true;
    }

    /**
     * Given the source and destination node/airport it would add an arc/flight route from source
     * to destination node/airport. If an arc/flight route already exists then the value would be
     * updated the new value.
     *
     * @param source                    the source node/airport.
     * @param destination               the destination node/airport.
     * @param rootAtributes             combined values of route cost and length
     * @throws NullPointerException     if source or destination is null.
     * @throws NoSuchElementException   if either source of destination does not exists.
     */
    public void addEdge (T source, T destination, RootAttributes rootAtributes) {
        if (source == null || destination == null) {
            throw new NullPointerException("Source and Destination, both should be non-null.");
        }
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            throw new NoSuchElementException("Source and Destination, both should be part of graph");
        }
        graph.get(source).put(destination, rootAtributes);
    }

    /**
     * Given a node/airport, returns the edges/routes going from that node/airport.
     *
     * @param node The node/airport whose edges/routes should be provided.
     * @return <code>Map</code> of the edges/routes leaving that node/airport.
     * @throws NullPointerException   If input node/airport is null.
     * @throws NoSuchElementException If node/airport is not in graph.
     */
    public Map<T, RootAttributes> edgesFrom(T node) {
        if (node == null) {
            throw new NullPointerException("The node should not be null.");
        }
        Map<T, RootAttributes> edges = graph.get(node);
        if (edges == null) {
            throw new NoSuchElementException("Source node does not exist.");
        }
        return Collections.unmodifiableMap(edges);
    }

    /**
     * Returns the iterator that travels the nodes of a graph.
     *
     * @return an iterator that travels the nodes of a graph.
     */
    @Override public Iterator<T> iterator() {
        //System.out.println(graph.keySet().iterator());
        return graph.keySet().iterator();
    }
}


