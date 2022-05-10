package il.co.site_building.performance_course.graph.tests;

import il.co.site_building.performance_course.graph.NeighborsMatrixGraph;

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
  public void testAddVertexEmptyGraph(){
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
}
