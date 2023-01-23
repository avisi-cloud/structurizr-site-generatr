# How to contribute

We welcome contributions! You can contribute
by [filing an issue](https://github.com/avisi-cloud/structurizr-site-generatr/issues), or by filing a pull request.

For those who'd like to help out by filing a pull request, here's some information to help you get started quickly.

## Recommended development tooling

We recomment using IntelliJ Community or Ultimate edition as your IDE. Additionally, you will need the following tools:

- Git for version control
- Java Development Kit (JDK) version 18 for building the source code
- (optional) [asdf-vm](https://asdf-vm.com/). We have provided a `.tool-versions` file in this repository, which is used
  by `asdf` to help you to install the correct JDK version for building this tool.

## Working from the command line

If you prefer working with a terminal, here's a few commands you can use for some common tasks. For all these commands,
we assume that the current working directory is the root of this repository. For all these common tasks, we use the
Gradle CLI. Please refer to Gradle's [user manual](https://docs.gradle.org/current/userguide/userguide.html) if you're
not familiar with Gradle.

### Running tests

```shell
./gradlew test
```

### Running the application from source

```shell
./gradlew run --args "<program arguments>"
```

#### Example: Start a development server from source

```shell
./gradlew run --args "serve -w docs/example/workspace.dsl"
```

## Working with IntelliJ

For those working with IntelliJ, we've provided some run configurations for running the program from source:

| Name                                              | Description                                                                                         |
|---------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| `all unit tests`                                  | Runs all unit tests                                                                                 |
| `generate site for example model (from git repo)` | Generates a site for the example model in `docs/example` from the remote Git repository on GitHub   |
| `generate site for example model (local)`         | Generates a site for the example model in `docs/example` from the local clone of the Git repository |
| `serve example model`                             | Starts a development server for the example model in `docs/example`                                 |

## Updating documentation

At the time of writing this document, the documentation of this tool is sparse. Please provide updated documentation
with your PR's, where applicable.

This is what's currently available:

- There's some basic documentation available in [README.md](README.md), which describes the basic usage of this tool.
- The default homepage (located in the `HomePageViewModel` class) provides some basic documentation on customizing the
  homepage.
- Finally, there's the example model, which contains some documentation on embedding diagrams in documentation Markdown
  files.
