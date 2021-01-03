package com.htec.util;

import com.htec.model.RootAttributes;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GraphOfRoutesTest {

    private static final double DELTA = 0.00001d;
    private GraphOfRoutes<String> graphOfRoutes;
    private RootAttributes rootAttributes;

    @Before
    public void initMocks() throws Exception {
        graphOfRoutes = new GraphOfRoutes();
        rootAttributes = mock(RootAttributes.class);
    }

    @Test(expected = NullPointerException.class)
    public void addNode_nullNode() {
        graphOfRoutes.addNode(null);
    }

    @Test
    public void addNode_alreadyContains() {
        boolean expectedFirst = graphOfRoutes.addNode("KZE");
        boolean expectedSame = graphOfRoutes.addNode("KZE");
        assertTrue(expectedFirst);
        assertFalse(expectedSame);
    }

    @Test(expected = NullPointerException.class)
    public void addEdge_nullNode() {
        graphOfRoutes.addEdge(null,"KZE", rootAttributes);
    }

    @Test(expected = NoSuchElementException.class)
    public void addEdge_alreadyContainsOneNode() {
        boolean expectedFirst = graphOfRoutes.addNode("KZE");
        boolean expectedSecond = graphOfRoutes.addNode("AER");
        graphOfRoutes.addEdge("AER","DYU", rootAttributes);
    }

    @Test
    public void addEdge_regular() {
        boolean expectedFirst = graphOfRoutes.addNode("KZE");
        boolean expectedSecond = graphOfRoutes.addNode("AER");
        graphOfRoutes.addEdge("AER","KZE", rootAttributes);
    }

    @Test(expected = NullPointerException.class)
    public void edgesFrom_nullNode() {
        boolean expectedFirst = graphOfRoutes.addNode("KZE");
        boolean expectedSecond = graphOfRoutes.addNode("AER");
        graphOfRoutes.addEdge("AER","KZE", rootAttributes);
        graphOfRoutes.edgesFrom(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void edgesFrom_sourceNodeNonExisting() {
        boolean expectedFirst = graphOfRoutes.addNode("KZE");
        boolean expectedSecond = graphOfRoutes.addNode("AER");
        graphOfRoutes.addEdge("AER","KZE", rootAttributes);
        graphOfRoutes.edgesFrom("DYU");
    }

    @Test()
    public void edgesFrom_regular() {
        boolean expectedFirst = graphOfRoutes.addNode("KZE");
        boolean expectedSecond = graphOfRoutes.addNode("AER");
        graphOfRoutes.addEdge("AER","KZE", rootAttributes);
        Map<String, RootAttributes> edges = graphOfRoutes.edgesFrom("AER");
        assertTrue(edges.containsKey("KZE"));
        assertTrue(rootAttributes.equals(edges.get("KZE")));
    }
}