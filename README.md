# issue-bazel-java-test-suite-filegroup

This is an example Bazel project to demonstrate a test suffix searching issue
between Bazel and the bazel-contrib/rule-jvm package.

## issue explanation

The issue we're running into is that the `filegroup` rule cannot be used as source in `srcs` when defining a `java_test_suite`. As a workaround you have to use a `glob()` expression or explicit file name list instead.

There is a weird situation that occurred during the original creation of this example that I still don't understand. The issue is if you specify two `java_test_suite`s where one is using the `glob()` expression, it will load the source files for *both* of them.

## seeing the behavior

To see the sources not found behavior you can run the following command:

```shell
$: bazel test //:tests_filegroup
INFO: Found 0 test targets...
INFO: Elapsed time: 0.152s, Critical Path: 0.00s
INFO: 1 process: 1 internal.
INFO: Build completed successfully, 1 total action
ERROR: No test targets were found, yet testing was requested
```

To see the sources found if there's a second test suite using `glob()` behavior uncomment the second test suite in the [BUILD.bazel](./BUILD.bazel) file and run the following commands:

```shell
####
# Run the glob() specified java_test_suite
####
$: bazel test //:tests_glob
INFO: Analyzed 2 targets (123 packages loaded, 5887 targets configured).
INFO: Multiplexer process for Javac has closed its output stream
INFO: From Building external/contrib_rules_jvm~/java/src/com/github/bazel_contrib/contrib_rules_jvm/junit5/liballow.jar (1 source file):
warning: [options] source value 8 is obsolete and will be removed in a future release
warning: [options] target value 8 is obsolete and will be removed in a future release
warning: [options] To suppress warnings about obsolete options, use -Xlint:-options.
INFO: From Building external/contrib_rules_jvm~/java/src/com/github/bazel_contrib/contrib_rules_jvm/junit5/libjunit5-compile-class.jar (19 source files):
warning: [options] source value 8 is obsolete and will be removed in a future release
warning: [options] target value 8 is obsolete and will be removed in a future release
warning: [options] To suppress warnings about obsolete options, use -Xlint:-options.
INFO: Found 2 test targets...
INFO: Elapsed time: 9.893s, Critical Path: 8.61s
INFO: 63 processes: 4 internal, 50 darwin-sandbox, 9 worker.
INFO: Build completed successfully, 63 total actions
//:src/test/java/com/example/foo/FirstTest                               PASSED in 0.3s
//:src/test/java/com/example/foo/SecondTest                              PASSED in 0.3s

Executed 2 out of 2 tests: 2 tests pass.
There were tests whose specified size is too big. Use the --test_verbose_timeout_warnings command line option to see which ones these are.

####
# Run the filegroup specified java_test_suite. This should work now.
####
❯ bazel test //:tests_filegroup
INFO: Analyzed 2 targets (123 packages loaded, 5887 targets configured).
INFO: From Building external/contrib_rules_jvm~/java/src/com/github/bazel_contrib/contrib_rules_jvm/junit5/liballow.jar (1 source file):
warning: [options] source value 8 is obsolete and will be removed in a future release
warning: [options] target value 8 is obsolete and will be removed in a future release
warning: [options] To suppress warnings about obsolete options, use -Xlint:-options.
INFO: From Building external/contrib_rules_jvm~/java/src/com/github/bazel_contrib/contrib_rules_jvm/junit5/libjunit5-compile-class.jar (19 source files):
warning: [options] source value 8 is obsolete and will be removed in a future release
warning: [options] target value 8 is obsolete and will be removed in a future release
warning: [options] To suppress warnings about obsolete options, use -Xlint:-options.
INFO: Found 2 test targets...
INFO: Elapsed time: 5.420s, Critical Path: 4.77s
INFO: 81 processes: 13 internal, 59 darwin-sandbox, 9 worker.
INFO: Build completed successfully, 81 total actions
//:src/test/java/com/example/foo/FirstTest                               PASSED in 0.5s
//:src/test/java/com/example/foo/SecondTest                              PASSED in 0.6s

Executed 2 out of 2 tests: 2 tests pass.
There were tests whose specified size is too big. Use the --test_verbose_timeout_warnings command line option to see which ones these are.
```
