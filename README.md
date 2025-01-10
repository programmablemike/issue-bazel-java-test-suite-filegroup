# issue-bazel-java-test-suite-filegroup

This is an example Bazel project to demonstrate a test suffix searching issue
between Bazel and the `bazel-contrib/rules_jvm` package.

This issue was filed against the `bazel-contrib/rules_jvm` repository [here](https://github.com/bazel-contrib/rules_jvm/issues/316).

## issue explanation

The issue we're running into is that the `filegroup` rule cannot be used as source in `srcs` when defining a `java_test_suite`. As a workaround you have to use a `glob()` expression or explicit file name list instead.

There is a weird situation that occurred during the original creation of this example that I still don't understand. The issue is if you specify two `java_test_suite`s where one is using the `glob()` expression, it will load the source files and run the tests correctly for *both* of them.

## seeing the behavior

To see the sources not found behavior you can run the following command:

```shell
$: bazel test //:tests_filegroup --nocache_test_results
INFO: Found 0 test targets...
INFO: Elapsed time: 0.071s, Critical Path: 0.00s
INFO: 1 process: 1 internal.
INFO: Build completed successfully, 1 total action
ERROR: No test targets were found, yet testing was requested
```

To see the sources found if there's a second test suite using `glob()` behavior uncomment the second test suite in the [BUILD.bazel](./BUILD.bazel) file and run the following commands:

```shell
####
# Run the glob() specified java_test_suite
####
$: bazel test //:tests_glob --nocache_test_results
INFO: Analyzed 2 targets (0 packages loaded, 7 targets configured).
INFO: Found 2 test targets...
INFO: Elapsed time: 0.425s, Critical Path: 0.31s
INFO: 3 processes: 1 internal, 2 darwin-sandbox.
INFO: Build completed successfully, 3 total actions
//:src/test/java/com/example/foo/FirstTest                               PASSED in 0.3s
//:src/test/java/com/example/foo/SecondTest                              PASSED in 0.3s

Executed 2 out of 2 tests: 2 tests pass.
There were tests whose specified size is too big. Use the --test_verbose_timeout_warnings command line option to see which ones these are.

####
# Run the filegroup specified java_test_suite. This should work now.
####
$: bazel test //:tests_filegroup --nocache_test_results
INFO: Analyzed 2 targets (0 packages loaded, 0 targets configured).
INFO: Found 2 test targets...
INFO: Elapsed time: 0.376s, Critical Path: 0.30s
INFO: 3 processes: 1 internal, 2 darwin-sandbox.
INFO: Build completed successfully, 3 total actions
//:src/test/java/com/example/foo/FirstTest                               PASSED in 0.3s
//:src/test/java/com/example/foo/SecondTest                              PASSED in 0.3s

Executed 2 out of 2 tests: 2 tests pass.
There were tests whose specified size is too big. Use the --test_verbose_timeout_warnings command line option to see which ones these are.
```
