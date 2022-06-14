package il.co.site_building.performance_course.matrices.benchmarking;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public record MatrixMultiplicationBenchmarkResult(DescriptiveStatistics multiplicationResult,
                                                  DescriptiveStatistics transposeMultiplicationResult,
                                                  DescriptiveStatistics blocksMatrixResult) {
}
