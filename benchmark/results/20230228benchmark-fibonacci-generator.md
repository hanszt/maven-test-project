````
"C:\Program Files\Java\jdk-19\bin\java.exe" -javaagent:C:\Users\hzuiderv\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\223.8617.56\lib\idea_rt.jar=65085:C:\Users\hzuiderv\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\223.8617.56\bin -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\23.0.0\annotations-23.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.35\jmh-core-1.35.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.35\jmh-generator-annprocess-1.35.jar benchmark.prefix.GeneratorBenchmark
# JMH version: 1.35
# VM version: JDK 19, OpenJDK 64-Bit Server VM, 19+36-2238
# VM invoker: C:\Program Files\Java\jdk-19\bin\java.exe
# VM options: -javaagent:C:\Users\hzuiderv\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\223.8617.56\lib\idea_rt.jar=65085:C:\Users\hzuiderv\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\223.8617.56\bin -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.GeneratorBenchmark.conventionalFibonacci

# Run progress: 0,00% complete, ETA 00:05:00
# Fork: 1 of 2
# Warmup Iteration   1: 2234493,320 ops/s
# Warmup Iteration   2: 1688299,221 ops/s
Iteration   1: 2288664,840 ops/s
Iteration   2: 2282647,980 ops/s
Iteration   3: 2285498,150 ops/s

# Run progress: 16,67% complete, ETA 00:04:16
# Fork: 2 of 2
# Warmup Iteration   1: 2331033,807 ops/s
# Warmup Iteration   2: 2449833,375 ops/s
Iteration   1: 2177303,224 ops/s
Iteration   2: 2287183,401 ops/s
Iteration   3: 2292669,391 ops/s


Result "benchmark.prefix.GeneratorBenchmark.conventionalFibonacci":
  2268994,498 ±(99.9%) 126308,844 ops/s [Average]
  (min, avg, max) = (2177303,224, 2268994,498, 2292669,391), stdev = 45042,951
  CI (99.9%): [2142685,653, 2395303,342] (assumes normal distribution)


# JMH version: 1.35
# VM version: JDK 19, OpenJDK 64-Bit Server VM, 19+36-2238
# VM invoker: C:\Program Files\Java\jdk-19\bin\java.exe
# VM options: -javaagent:C:\Users\hzuiderv\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\223.8617.56\lib\idea_rt.jar=65085:C:\Users\hzuiderv\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\223.8617.56\bin -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.GeneratorBenchmark.fibonacciFromGenerator

# Run progress: 33,33% complete, ETA 00:03:24
# Fork: 1 of 2
# Warmup Iteration   1: 630,096 ops/s
# Warmup Iteration   2: 627,308 ops/s
Iteration   1: 633,567 ops/s
Iteration   2: 593,776 ops/s
Iteration   3: 625,583 ops/s

# Run progress: 50,00% complete, ETA 00:02:33
# Fork: 2 of 2
# Warmup Iteration   1: 632,941 ops/s
# Warmup Iteration   2: 623,776 ops/s
Iteration   1: 634,722 ops/s
Iteration   2: 634,904 ops/s
Iteration   3: 603,147 ops/s


Result "benchmark.prefix.GeneratorBenchmark.fibonacciFromGenerator":
  620,950 ±(99.9%) 50,482 ops/s [Average]
  (min, avg, max) = (593,776, 620,950, 634,904), stdev = 18,002
  CI (99.9%): [570,468, 671,432] (assumes normal distribution)


# JMH version: 1.35
# VM version: JDK 19, OpenJDK 64-Bit Server VM, 19+36-2238
# VM invoker: C:\Program Files\Java\jdk-19\bin\java.exe
# VM options: -javaagent:C:\Users\hzuiderv\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\223.8617.56\lib\idea_rt.jar=65085:C:\Users\hzuiderv\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\223.8617.56\bin -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.GeneratorBenchmark.fibonacciSequence

# Run progress: 66,67% complete, ETA 00:01:42
# Fork: 1 of 2
# Warmup Iteration   1: 1412278,239 ops/s
# Warmup Iteration   2: 1451657,206 ops/s
Iteration   1: 1448995,134 ops/s
Iteration   2: 1470682,773 ops/s
Iteration   3: 1468631,195 ops/s

# Run progress: 83,33% complete, ETA 00:00:51
# Fork: 2 of 2
# Warmup Iteration   1: 1309306,017 ops/s
# Warmup Iteration   2: 1471422,981 ops/s
Iteration   1: 1445712,432 ops/s
Iteration   2: 1458637,288 ops/s
Iteration   3: 1458615,416 ops/s


Result "benchmark.prefix.GeneratorBenchmark.fibonacciSequence":
  1458545,706 ±(99.9%) 28180,196 ops/s [Average]
  (min, avg, max) = (1445712,432, 1458545,706, 1470682,773), stdev = 10049,329
  CI (99.9%): [1430365,511, 1486725,902] (assumes normal distribution)


# Run complete. Total time: 00:05:06

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark                                   Mode  Cnt        Score        Error  Units
GeneratorBenchmark.conventionalFibonacci   thrpt    6  2268994,498 ± 126308,844  ops/s
GeneratorBenchmark.fibonacciFromGenerator  thrpt    6      620,950 ±     50,482  ops/s
GeneratorBenchmark.fibonacciSequence       thrpt    6  1458545,706 ±  28180,196  ops/s

Process finished with exit code 0

````
