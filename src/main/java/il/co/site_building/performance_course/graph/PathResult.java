package il.co.site_building.performance_course.graph;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

public class PathResult {

  public final TIntList path = new TIntArrayList();
  public double distances = Double.POSITIVE_INFINITY;

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PathResult that = (PathResult) o;

    if (Double.compare(that.distances, distances) != 0) return false;
    return path != null ? path.equals(that.path) : that.path == null;
  }

  @Override public int hashCode() {
    int result;
    long temp;
    result = path != null ? path.hashCode() : 0;
    temp = Double.doubleToLongBits(distances);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
