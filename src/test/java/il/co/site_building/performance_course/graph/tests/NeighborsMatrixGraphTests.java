package il.co.site_building.performance_course.graph.tests;

import il.co.site_building.performance_course.graph.JGraphSimpleGraphImpl;
import il.co.site_building.performance_course.graph.NeighborsMatrixGraph;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NeighborsMatrixGraphTests {

  @Test
  public void testEmptyGraph() {
    NeighborsMatrixGraph graph = new NeighborsMatrixGraph();
    Assertions.assertFalse(graph.edgeExists(0, 2));
    Assertions.assertFalse(graph.edgeExists(3, 2));
    Assertions.assertFalse(graph.edgeExists(1000, 2));
    Assertions.assertFalse(graph.edgeExists(0, 1000));
    Assertions.assertFalse(graph.edgeExists(0, 2));
    Assertions.assertFalse(graph.vertexExists(0));
    Assertions.assertFalse(graph.vertexExists(1));
    Assertions.assertFalse(graph.vertexExists(2));
    Assertions.assertFalse(graph.vertexExists(3));
    Assertions.assertFalse(graph.vertexExists(4));
    Assertions.assertFalse(graph.vertexExists(5));
    Assertions.assertFalse(graph.vertexExists(6));
    Assertions.assertFalse(graph.vertexExists(7));
    Assertions.assertFalse(graph.vertexExists(8));
    Assertions.assertFalse(graph.vertexExists(1000));
  }

  @Test
  public void testAddVertexEmptyGraph() {
    NeighborsMatrixGraph graph = new NeighborsMatrixGraph();
    graph.addVertex(0);
    graph.addVertex(7);
    graph.addVertex(1000);
    Assertions.assertTrue(graph.vertexExists(0));
    Assertions.assertTrue(graph.vertexExists(7));
    Assertions.assertFalse(graph.vertexExists(1));
    Assertions.assertFalse(graph.vertexExists(8));
    Assertions.assertFalse(graph.vertexExists(999));
    Assertions.assertFalse(graph.vertexExists(1001));
    Assertions.assertFalse(graph.vertexExists(10_000));
  }

  @Test
  public void testAddVertexGraphWithVertices() {
    NeighborsMatrixGraph graph = new NeighborsMatrixGraph(8);
    graph.addVertex(1000);
    Assertions.assertTrue(graph.vertexExists(0));
    Assertions.assertTrue(graph.vertexExists(7));
    Assertions.assertTrue(graph.vertexExists(1));
    Assertions.assertFalse(graph.vertexExists(8));
    Assertions.assertFalse(graph.vertexExists(9));
    Assertions.assertFalse(graph.vertexExists(130));
    Assertions.assertTrue(graph.vertexExists(1000));
    graph.addVertex(130);
    Assertions.assertTrue(graph.vertexExists(130));
    Assertions.assertFalse(graph.vertexExists(10_000));
  }

  @Test
  public void testRemoveVertexGraphWithVertices() {
    NeighborsMatrixGraph graph = new NeighborsMatrixGraph(70);
    Assertions.assertTrue(graph.vertexExists(0));
    Assertions.assertTrue(graph.vertexExists(30));
    Assertions.assertTrue(graph.vertexExists(64));
    Assertions.assertTrue(graph.vertexExists(66));
    Assertions.assertTrue(graph.vertexExists(69));
    Assertions.assertFalse(graph.vertexExists(70));
    graph.removeVertex(30);
    graph.removeVertex(66);
    graph.removeVertex(72);
    Assertions.assertTrue(graph.vertexExists(0));
    Assertions.assertFalse(graph.vertexExists(30));
    Assertions.assertTrue(graph.vertexExists(64));
    Assertions.assertFalse(graph.vertexExists(66));
    Assertions.assertTrue(graph.vertexExists(69));
    Assertions.assertFalse(graph.vertexExists(70));
  }

  @Test
  public void testRemoveVertexEmptyGraph() {
    NeighborsMatrixGraph graph = new NeighborsMatrixGraph();
    graph.addVertex(0);
    graph.addVertex(6);
    graph.addVertex(7);
    graph.addVertex(1000);
    graph.removeVertex(6);
    Assertions.assertTrue(graph.vertexExists(0));
    Assertions.assertFalse(graph.vertexExists(6));
    Assertions.assertTrue(graph.vertexExists(7));
    Assertions.assertTrue(graph.vertexExists(1000));
    graph.removeVertex(0);
    Assertions.assertFalse(graph.vertexExists(0));
    Assertions.assertFalse(graph.vertexExists(6));
    Assertions.assertTrue(graph.vertexExists(7));
    Assertions.assertTrue(graph.vertexExists(1000));
    graph.removeVertex(1000);
    Assertions.assertFalse(graph.vertexExists(0));
    Assertions.assertFalse(graph.vertexExists(6));
    Assertions.assertTrue(graph.vertexExists(7));
    Assertions.assertFalse(graph.vertexExists(1000));
  }

  @Test
  public void testAddEdge() {
    NeighborsMatrixGraph graph = new NeighborsMatrixGraph(130);
    graph.addEdge(0, 0);
    graph.addEdge(0, 1);
    graph.addEdge(2, 1);
    graph.addEdge(129, 1);
    graph.addEdge(150, 1000);
    graph.addEdge(1, 2);
    graph.addEdge(80, 90);
    graph.addEdge(77, 77);
    Assertions.assertTrue(graph.edgeExists(0, 0));
    Assertions.assertTrue(graph.edgeExists(0, 1));
    Assertions.assertFalse(graph.edgeExists(1, 1));
    Assertions.assertTrue(graph.edgeExists(2, 1));
    Assertions.assertTrue(graph.edgeExists(1, 2));
    Assertions.assertTrue(graph.edgeExists(1, 129));
    Assertions.assertTrue(graph.edgeExists(129, 1));
    Assertions.assertFalse(graph.edgeExists(150, 1000));
    Assertions.assertFalse(graph.edgeExists(1000, 150));
    Assertions.assertTrue(graph.edgeExists(80, 90));
    Assertions.assertTrue(graph.edgeExists(90, 80));
    Assertions.assertTrue(graph.edgeExists(77, 77));
    Assertions.assertFalse(graph.edgeExists(8, 8));
    Assertions.assertFalse(graph.edgeExists(80, 8));
    Assertions.assertFalse(graph.edgeExists(80, 18));
    Assertions.assertFalse(graph.edgeExists(18, 129));
    Assertions.assertFalse(graph.edgeExists(129, 129));
    Assertions.assertFalse(graph.edgeExists(95, 95));
    Assertions.assertFalse(graph.edgeExists(95, 100));
  }

  @Test
  public void testRemoveEdge() {
    NeighborsMatrixGraph graph = new NeighborsMatrixGraph(130);
    graph.addEdge(0, 0);
    graph.addEdge(0, 1);
    graph.addEdge(2, 1);
    graph.addEdge(129, 1);
    graph.addEdge(150, 1000);
    graph.addEdge(80, 90);
    graph.addEdge(77, 77);
    Assertions.assertTrue(graph.edgeExists(1, 2));
    Assertions.assertTrue(graph.edgeExists(2, 1));
    graph.removeEdge(2, 1);
    Assertions.assertFalse(graph.edgeExists(1, 2));
    Assertions.assertFalse(graph.edgeExists(2, 1));
    Assertions.assertFalse(graph.edgeExists(150, 1000));
    graph.removeEdge(1, 129);
    Assertions.assertFalse(graph.edgeExists(129, 1));
  }

  @Test
  public void testRandomGraphs() {
    Random jGraphRandom = new Random(0);
    Random neighborMatrixRandom = new Random(0);
    int numberOfVertices = 1000;
    double loadFactor = 0.3;
    JGraphSimpleGraphImpl jGraphSimpleGraph =
        JGraphSimpleGraphImpl.createRandomGraph(jGraphRandom, numberOfVertices, loadFactor);
    NeighborsMatrixGraph neighborsMatrixGraph =
        NeighborsMatrixGraph.createRandomGraph(neighborMatrixRandom, numberOfVertices, loadFactor);
    Assertions.assertTrue(neighborsMatrixGraph.equalsJGraphImpl(jGraphSimpleGraph));
  }
}
