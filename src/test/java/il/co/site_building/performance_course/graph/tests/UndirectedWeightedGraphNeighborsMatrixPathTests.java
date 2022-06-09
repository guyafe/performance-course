package il.co.site_building.performance_course.graph.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import il.co.site_building.performance_course.graph.PathResult;
import il.co.site_building.performance_course.graph.UndirectedWeightedNeighborsMatrixGraph;
import il.co.site_building.performance_course.graph.UndirectedWeightedNeighborsMatrixGraphImpl;

public class UndirectedWeightedGraphNeighborsMatrixPathTests {

  @Test
  public void testOneNodeGraph() {
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraphImpl();
    graph.addVertex(0);
    PathResult pathResult = graph.findShortestPath(0, 0);
    Assertions.assertEquals(0, pathResult.distances);
    Assertions.assertArrayEquals(new int[]{0}, pathResult.path.toArray());
  }

  @Test
  public void testTwoNodesNotConnectedGraph() {
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraphImpl();
    graph.addVertex(0);
    graph.addVertex(1);
    PathResult pathResult1 = graph.findShortestPath(1, 1);
    Assertions.assertEquals(0, pathResult1.distances);
    Assertions.assertArrayEquals(new int[]{1}, pathResult1.path.toArray());
    PathResult pathResult0 = graph.findShortestPath(0, 0);
    Assertions.assertEquals(0, pathResult0.distances);
    Assertions.assertArrayEquals(new int[]{0}, pathResult0.path.toArray());
    PathResult pathResult01 = graph.findShortestPath(0, 1);
    Assertions.assertEquals(Double.POSITIVE_INFINITY, pathResult01.distances);
    Assertions.assertArrayEquals(new int[]{}, pathResult01.path.toArray());
    PathResult pathResult10 = graph.findShortestPath(0, 1);
    Assertions.assertEquals(Double.POSITIVE_INFINITY, pathResult10.distances);
    Assertions.assertArrayEquals(new int[]{}, pathResult10.path.toArray());
  }

  @Test
  public void testTwoNodesConnectedGraph() {
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraphImpl();
    graph.addVertex(0);
    graph.addVertex(1);
    graph.setEdge(0, 1, 100.0);
    PathResult pathResult = graph.findShortestPath(0, 1);
    Assertions.assertEquals(100.0, pathResult.distances);
    Assertions.assertArrayEquals(new int[]{1, 0}, pathResult.path.toArray());
    PathResult pathResultReverse = graph.findShortestPath(1, 0);
    Assertions.assertEquals(100.0, pathResultReverse.distances);
    Assertions.assertArrayEquals(new int[]{0, 1}, pathResultReverse.path.toArray());
  }

  @Test
  public void testThreeNodesLong() {
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraphImpl();
    graph.addVertex(0);
    graph.addVertex(1);
    graph.addVertex(2);
    graph.setEdge(0, 1, 1.0);
    graph.setEdge(1, 2, 2.0);
    PathResult pathResult = graph.findShortestPath(0, 2);
    Assertions.assertEquals(3.0, pathResult.distances);
    Assertions.assertArrayEquals(new int[]{2, 1, 0}, pathResult.path.toArray());
    graph.setEdge(0, 2, 0.5);
    pathResult = graph.findShortestPath(0, 2);
    Assertions.assertEquals(0.5,pathResult.distances);
    Assertions.assertArrayEquals(new int[]{2,0}, pathResult.path.toArray());
    graph.setEdge(0, 2, 1.5);
    pathResult = graph.findShortestPath(0, 2);
    Assertions.assertEquals(1.5,pathResult.distances);
    Assertions.assertArrayEquals(new int[]{2,0}, pathResult.path.toArray());
  }

  @Test
  public void testLargeGraph(){
    UndirectedWeightedNeighborsMatrixGraph graph = new UndirectedWeightedNeighborsMatrixGraphImpl(9);
    graph.setEdge(0,1,4);
    graph.setEdge(0,7,8);
    graph.setEdge(1,7,11);
    graph.setEdge(2,3,7);
    graph.setEdge(2,5,4);
    graph.setEdge(2,8,2);
    graph.setEdge(3,4,9);
    graph.setEdge(3,5,14);
    graph.setEdge(4,5,10);
    graph.setEdge(5,6,2);
    graph.setEdge(6,8,6);
    graph.setEdge(6,7,1);
    graph.setEdge(7,8,7);
    PathResult pathResult = graph.findShortestPath(0, 4);
    Assertions.assertEquals(21.0, pathResult.distances);
    Assertions.assertArrayEquals(new int[]{4,5,6,7,0}, pathResult.path.toArray());
  }
}
