# Structurizr Site Generatr

A static site generator for C4 architecture models created with [Structrizr DSL](https://github.com/structurizr/dsl).
See [Background](#background) for the story behind this tool.

[Click here](https://avisi-cloud.github.io/structurizr-site-generatr) to see an example of a generated site. This site
is generated from the example workspace in this repository. This example is based on
the [Big Bank plc example](https://structurizr.com/dsl?example=big-bank-plc) from <https://structurizr.com>.

## Features

* Generate a static HTML site, based on a Structurizr DSL workspace.
* Generates diagrams in SVG, PNG and PlantUML format, which can be viewed and downloaded from the generated site.
* Include workspace-level documentation (in Markdown format) in the generated site.
* Include system-level ADR's in the generated site.
* Include static assets in the generated site, which can be used in ADR's and workspace-level documentation.
* Generate a site from a Structurizr DSL model in a Git repository. Supports multiple branches, which makes it possible
  to for example maintain an actual state in `master` and one or more future states in feature branches. The generated
  site includes diagrams for all configured branches.
* Include a version number in the generated site.

## Installation

You can install structurizr-site-generatr using Homebrew:

```shell
brew tap avisi-cloud/tools
brew install structurizr-site-generatr
structurizr-site-generatr --help
```

## Docker image

You can also use the Docker image:

```shell
docker pull ghcr.io/avisi-cloud/structurizr-site-generatr:1.0.1
docker run -it --rm ghcr.io/avisi-cloud/structurizr-site-generatr:1.0.1 --help
```

## Example usage

These examples use the [example workspace](docs/example) in this repository.

### Start a development server

```shell
structurizr-site-generatr serve --workspace-file docs/example/workspace.dsl
```

### Generate a site from a Git repository

```shell
structurizr-site-generatr generate-site \
    --git-url https://github.com/avisi-cloud/structurizr-site-generatr.git \
    --workspace-file docs/example/workspace.dsl \
    --branches main \
    --default-branch main
```

## Background

At Avisi, we're big fans of the [C4 model](https://c4model.com). We use it in many projects, big and small, to document
the architecture of the systems and system landscapes we're working on.

We started out by using PlantUML, combined with the [C4 extension](https://github.com/plantuml-stdlib/C4-PlantUML). This
works well for small systems. However, for larger application landscapes, maintained by multiple development teams, this
lead to duplication and inconsistency across diagrams.

To solve this problem, we needed a model based approach. Rather than maintaining separate diagrams and trying to keep
them consistent, we needed to have a single model, from which diagrams could be generated. This is why we started using
the [Structurizr for Java](https://github.com/structurizr/java) library. About a year later, we migrated our models to
Structurizr DSL.

We created custom tooling to generate diagrams and check them in to our Git repository. All diagrams could be seen by
navigating to our GitLab repository. This is less than ideal, because we like to make these diagrams easily accessible
to everyone, including our customers. This is why we decided that we needed a way of publishing our models to an easily
accessible website.

This tool is the result. It's still a work in progress, but it has already proven to be very useful to us.
