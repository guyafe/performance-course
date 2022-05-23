package il.co.site_building.performance_course.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

/**
 * JGraphT based implementation.
 * Delegates all functionality to JGraphT's.
 */
public class JGraphSimpleGraphImpl implements SimpleGraph {

  private final Graph<Integer, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);

  @Override public void addVertex(int vertex) {
    graph.addVertex(vertex);
  }

  @Override public void removeVertex(int vertex) {
    graph.removeVertex(vertex);
  }

  @Override public boolean vertexExists(int vertex) {
    return graph.containsVertex(vertex);
  }

  @Override public void addEdge(int v1, int v2) {
    graph.addEdge(v1, v2);
  }

  @Override public void removeEdge(int v1, int v2) {
    graph.removeEdge(v1, v2);
  }

  @Override public boolean edgeExists(int v1, int v2) {
    return v1 == v2 || graph.containsEdge(v1, v2);
  }

  @Override public Collection<SimpleGraph> createConnectedComponents() {
    ConnectivityInspector<Integer, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(graph);
    List<Set<Integer>> connectivitySets = connectivityInspector.connectedSets();
    Collection<SimpleGraph> connectedComponents = new HashSet<>(connectivitySets.size());
    connectivitySets.forEach(connectivitySet -> connectedComponents.add(createConnectedComponent(connectivitySet)));
    return connectedComponents;
  }

  @Override public Set<Integer> vertexSet() {
    return graph.vertexSet();
  }

  private SimpleGraph createConnectedComponent(Set<Integer> connectivitySet) {
    SimpleGraph connectedComponent = new JGraphSimpleGraphImpl();
    for (Integer integer : connectivitySet) {
      connectedComponent.addVertex(integer);
    }
    graph.edgeSet().forEach(edge -> {
      if(graph.containsEdge(edge)) {
        connectedComponent.addEdge(graph.getEdgeSource(edge), graph.getEdgeTarget(edge));
      }
    });
    return connectedComponent;
  }

  public static JGraphSimpleGraphImpl createRandomGraph(Random random, int numberOfVertices, double loadFactor){
    JGraphSimpleGraphImpl graph = new JGraphSimpleGraphImpl();
    for (int vertex = 0; vertex < numberOfVertices; vertex++) {
      for(int neighbor = vertex + 1; neighbor < numberOfVertices; neighbor++){
        double lucky = random.nextDouble();
        if(loadFactor <= lucky) {
          graph.addVertex(vertex);
          graph.addVertex(neighbor);
          graph.addEdge(vertex, neighbor);
        }
      }
    }
    return graph;
  }

}
