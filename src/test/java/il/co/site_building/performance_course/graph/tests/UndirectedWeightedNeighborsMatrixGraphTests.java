package il.co.site_building.performance_course.graph.tests;

import il.co.site_building.performance_course.graph.UndirectedWeightedNeighborsMatrixGraph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UndirectedWeightedNeighborsMatrixGraphTests {

  @Test
  public void testEmptyConstructor() {
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraph();
    Assertions.assertFalse(graph.vertexExists(0));
    Assertions.assertFalse(graph.vertexExists(1));
    Assertions.assertFalse(graph.vertexExists(10));
    Assertions.assertFalse(graph.containsEdge(0, 0));
    Assertions.assertFalse(graph.containsEdge(0, 1));
    Assertions.assertFalse(graph.containsEdge(10, 5));
    Assertions.assertFalse(graph.containsEdge(100, 1000));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(0, 0));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(0, 1));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(10, 5));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(100, 1000));
  }

  @Test
  public void testConstructorWithVertices() {
    int NUMBERS_OF_VERTICES = 10;
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraph(NUMBERS_OF_VERTICES);
    Assertions.assertTrue(graph.vertexExists(0));
    Assertions.assertTrue(graph.vertexExists(1));
    Assertions.assertTrue(graph.vertexExists(9));
    Assertions.assertFalse(graph.vertexExists(10));
    Assertions.assertFalse(graph.vertexExists(20));
    Assertions.assertEquals(0, graph.getEdgeWeight(0, 0));
    Assertions.assertEquals(0, graph.getEdgeWeight(5, 5));
    Assertions.assertEquals(0, graph.getEdgeWeight(9, 9));
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(0, 1));
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(1, 0));
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(9, 1));
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(5, 8));
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(8, 9));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(10, 10));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(10, 1));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(1, 10));
  }

  @Test
  public void testAddVertex() {
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraph();
    Assertions.assertFalse(graph.vertexExists(0));
    Assertions.assertFalse(graph.vertexExists(1));
    Assertions.assertFalse(graph.vertexExists(10));
    graph.addVertex(0);
    Assertions.assertTrue(graph.vertexExists(0));
    graph.addVertex(10);
    graph.addVertex(0);
    Assertions.assertTrue(graph.vertexExists(0));
    Assertions.assertFalse(graph.vertexExists(1));
    Assertions.assertTrue(graph.vertexExists(10));
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(0, 10));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(0, 1));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(1, 10));
    Assertions.assertEquals(0, graph.getEdgeWeight(10, 10));
    Assertions.assertEquals(0, graph.getEdgeWeight(0, 0));
  }

  @Test
  public void testRemoveVertex() {
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraph(3);
    graph.removeVertex(3);
    graph.removeVertex(1);
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(0, 2));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(0, 1));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(1, 2));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(3, 3));
  }

  @Test
  public void testSetEdge(){
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraph(10);
    graph.setEdge(0, 0, 100.0);
    graph.setEdge(10, 10, 100.0);
    graph.setEdge(1, 10, 100.0);
    graph.setEdge(10, 2, 100.0);
    graph.setEdge(2, 2, 100.0);
    graph.setEdge(2, 5, 100.0);
    graph.setEdge(6, 1, 100.0);
    graph.setEdge(8, 4, 100.0);
    Assertions.assertEquals(0, graph.getEdgeWeight(0, 0));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(10, 10));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(1, 10));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(2, 10));
    Assertions.assertEquals(Double.NaN, graph.getEdgeWeight(20, 3));
    Assertions.assertEquals(0, graph.getEdgeWeight(2,2));
    Assertions.assertEquals(100.0, graph.getEdgeWeight(2,5));
    Assertions.assertEquals(100.0, graph.getEdgeWeight(6,1));
    Assertions.assertEquals(100.0, graph.getEdgeWeight(8,4));
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(8,3));
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(2,9));
    graph.setEdge(2, 5, 200.0);
    Assertions.assertEquals(200.0, graph.getEdgeWeight(2,5));
    graph.setEdge(2, 5, Double.POSITIVE_INFINITY);
    Assertions.assertEquals(Double.POSITIVE_INFINITY, graph.getEdgeWeight(2,5));
    graph.setEdge(6, 1, Double.NaN);
    Assertions.assertEquals(100.0, graph.getEdgeWeight(1,6));
  }
}
