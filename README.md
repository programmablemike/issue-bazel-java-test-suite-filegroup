# issue-bazel-java-test-suite-filegroup

This is an example Bazel project to demonstrate a test suffix searching issue
between Bazel and the bazel-contrib/rule-jvm package.

## issue explanation

The issue we're running into is that the `filegroup` rule cannot be used as source in `srcs` when defining a `java_test_suite`. As a workaround you have to use a `glob()` expression or explicit file name list instead.

There is a weird situation that occurred during the original creation of this example that I still don't understand. The issue is if you specify two `java_test_suite`s where one is using the `glob()` expression, it will load the source files for *both* of them.
