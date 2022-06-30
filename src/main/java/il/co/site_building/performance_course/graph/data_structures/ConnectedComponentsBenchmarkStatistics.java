package il.co.site_building.performance_course.graph.data_structures;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public record ConnectedComponentsBenchmarkStatistics(DescriptiveStatistics jgraphBuildStatistics,
                                                     DescriptiveStatistics jgraphCalculationStatistics,
                                                     DescriptiveStatistics neighborsMatrixBuildStatistics,
                                                     DescriptiveStatistics neighborsMatrixCalculationStatistics) {
}
