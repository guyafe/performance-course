package il.co.site_building.performance_course.graph;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Utility class to for the statistics of graph benchmarking.
 * Build and connected components.
 */
public record GraphBenchmarkStatistics(DescriptiveStatistics buildStatistics, DescriptiveStatistics connectedComponents) {
}
