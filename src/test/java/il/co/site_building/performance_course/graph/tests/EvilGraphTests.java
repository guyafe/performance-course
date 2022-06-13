package il.co.site_building.performance_course.graph.tests;

import il.co.site_building.performance_course.graph.UndirectedWeightedNeighborsMatrixGraphEvil;
import il.co.site_building.performance_course.graph.UndirectedWeightedNeighborsMatrixGraphImpl;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EvilGraphTests {

  @Test
  public void testEvilGraphEquivalentToImpl() {
    int SEED = 0;
    int numberOfVertices = 256;
    double loadFactor = 0.5;
    Random implRandom = new Random(SEED);
    Random evilRandom = new Random(SEED);
    UndirectedWeightedNeighborsMatrixGraphImpl graph =
        UndirectedWeightedNeighborsMatrixGraphImpl.generateRandomGraph(implRandom, numberOfVertices, loadFactor);
    UndirectedWeightedNeighborsMatrixGraphEvil evilGraph =
        UndirectedWeightedNeighborsMatrixGraphEvil.generateRandomGraph(evilRandom, numberOfVertices, loadFactor);
    verifyGraphsAreEqual(graph, evilGraph, numberOfVertices);
  }

  private void verifyGraphsAreEqual(UndirectedWeightedNeighborsMatrixGraphImpl graph,
                                    UndirectedWeightedNeighborsMatrixGraphEvil evilGraph,
                                    int numberOfVertices) {
    for (int vertex = 0; vertex < numberOfVertices; vertex++) {
      Assertions.assertEquals(graph.vertexExists(vertex), evilGraph.vertexExists(vertex));
      for (int neighbor = 0; neighbor < numberOfVertices; neighbor++) {
        Assertions.assertEquals(graph.containsEdge(vertex, neighbor), evilGraph.containsEdge(vertex, neighbor));
        Assertions.assertEquals(graph.getEdgeWeight(vertex, neighbor), evilGraph.getEdgeWeight(vertex, neighbor));
      }
    }
  }


}
