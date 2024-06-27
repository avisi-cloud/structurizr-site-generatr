<!-- TOC -->
* [Structurizr Site Generatr](#structurizr-site-generatr)
  * [Features](#features)
  * [Getting Started](#getting-started)
    * [Installation using Homebrew - Recommended](#installation-using-homebrew---recommended)
    * [Manual installation](#manual-installation)
    * [Docker](#docker)
      * [[Optional] Verify the Structurizr Site Generatr image with CoSign](#optional-verify-the-structurizr-site-generatr-image-with-cosign)
  * [Usage](#usage)
    * [Help](#help)
    * [Version](#version)
    * [Generate a website](#generate-a-website)
      * [From a C4 Workspace](#from-a-c4-workspace)
      * [For those taking the Docker approach](#for-those-taking-the-docker-approach)
      * [Generate a website from a Git repository](#generate-a-website-from-a-git-repository)
    * [Start a development web server around the generated website](#start-a-development-web-server-around-the-generated-website)
      * [For those taking the Docker approach](#for-those-taking-the-docker-approach-1)
  * [Customizing the generated website](#customizing-the-generated-website)
  * [Contributing](#contributing)
  * [Background](#background)
<!-- TOC -->

# Structurizr Site Generatr

A static site generator for [C4 architecture models](https://c4model.com/) created with [Structrizr DSL](https://docs.structurizr.com/dsl).
See [Background](#background) for the story behind this tool.

[Click here to see an example of a generated site](https://avisi-cloud.github.io/structurizr-site-generatr) based on
the [Big Bank plc example](https://structurizr.com/dsl?example=big-bank-plc) from <https://structurizr.com>. This site
is generated from the example workspace in this repository.

## Features

- Generate a static HTML site, based on a Structurizr DSL workspace.
- Generates diagrams in SVG, PNG and PlantUML format, which can be viewed and downloaded from the generated site.
- Easy browsing through the site by clicking on software system and container elements in the diagrams. Note that
  external software systems are excluded from the menu. A software system is considered external when it lives outside
  the (deprecated) enterprise boundary or when it contains a specific tag, see [Customizing the generated website](#customizing-the-generated-website). 
- Start a development server which generates a site, serves it and updates the site automatically whenever a file that's
  part of the Structurizr workspace changes.
- Include documentation (in Markdown or AsciiDoc format) in the generated site. Both workspace level documentation and software
  system level documentation are included in the site.
- Include ADR's in the generated site. Again, both workspace level ADR's and software system level ADR's are included in
  the site.
- Include static assets in the generated site, which can be used in ADR's and documentation.
- Generate a site from a Structurizr DSL model in a Git repository. Supports multiple branches, which makes it possible
  to for example maintain an actual state in `master` and one or more future states in feature branches. The generated
  site includes diagrams for all valid configured or detected branches.
- Include a version number in the generated site.

## Getting Started

To get started with the Structurizr Site Generatr, you can either:

- Install it to your local machine (recommended for the best experience), or
- Execute it on your local machine via a container (requires Docker)

**Please note**: The intended use of the Docker image is to generate a site from a CI pipeline. Using it for model
development is possible, but not a usage scenario that's actively supported.

### Installation using Homebrew - Recommended

As this approach relies on [Homebrew](https://brew.sh/), ensure this is already installed. For Windows and other
operating systems not supported by Homebrew, please use the [Docker approach](#docker) instead.

To install Structurizr Site Generatr execute the following commands in your terminal:

```shell
brew tap avisi-cloud/tools
brew install structurizr-site-generatr

structurizr-site-generatr --help
```

Periodically, you would have to update your local installation to take advantage of any new
[Structurizr Site Generatr releases](https://github.com/avisi-cloud/structurizr-site-generatr/releases).

### Manual installation

If using Homebrew is not an option for you, it's also possible to install Structurizr Site Generatr manually. This can
be done as follows:

- Consult the
  [Structurizr Site Generatr releases](https://github.com/avisi-cloud/structurizr-site-generatr/releases) and choose
  the version you wish to use
- Download the `.tar.gz` or `.zip` distribution
- Extract the archive using your favourite tool
- For ease of use, it's recommended to add Structurizr Site Generatr's `bin` directory to your `PATH`

### Docker

Though local installation is recommended for development where possible, Structurizr Site Generatr is also a packaged
as a Docker image. Therefore, to use this approach, ensure [Docker](https://www.docker.com/) is already installed.
Additionally, for Windows 10+ users, you may want to take advantage of
[WSL2](https://docs.microsoft.com/en-us/windows/wsl/install) (Windows Subsystem for Linux). Both Docker and WSL2
are topics too vast to repeat here, so you are invited to study these as prerequisite learning for this approach.

Then to download our packaged image, consider the
[Structurizr Site Generatr releases](https://github.com/avisi-cloud/structurizr-site-generatr/releases) and choose
the version you wish to use. Then, in your terminal, execute the following:

```shell
docker pull ghcr.io/avisi-cloud/structurizr-site-generatr
```

Once downloaded, you can execute Structurizr Site Generatr via a temporary Docker container by executing the
following in your terminal:

```shell
docker run -it --rm ghcr.io/avisi-cloud/structurizr-site-generatr --help
```

#### [Optional] Verify the Structurizr Site Generatr image with [CoSign](https://github.com/sigstore/cosign)

```shell
cat cosign.pub
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzezKl0vAWSHosQ0JLEsDzNBd2nGm
08KqX+imYqq2avlbH+ehprJFMqKK0/I/bY0q5W9hQC8SLzTRJ9Q5dB9UiQ==
-----END PUBLIC KEY-----
cosign verify --key cosign.pub ghcr.io/avisi-cloud/structurizr-site-generatr
```

Or by using the Github repo url:

```shell
 cosign verify --key https://github.com/avisi-cloud/structurizr-site-generatr ghcr.io/avisi-cloud/structurizr-site-generatr
 ```

## Usage

These examples use the [example workspace](docs/example) in this repository.

Once installed, Structurizr Site Generatr is operated via your terminal by issuing commands. Each command is
explained here:

### Help

To learn about available commands, or parameters for individual commands, call Structurizr Site Generatr with the
`--help` argument.

```shell
installed> structurizr-site-generatr --help

   docker> docker run -it --rm ghcr.io/avisi-cloud/structurizr-site-generatr --help

Usage: structurizr-site-generatr options_list
Subcommands:
    serve - Start a development server
    generate-site - Generate a site for the selected workspace.
    version - Print version information

Options:
    --help, -h -> Usage info
```

### Version

To query the version of Structurizr Site Generatr installed / used.

```shell
installed> structurizr-site-generatr version

   docker> docker run -it --rm ghcr.io/avisi-cloud/structurizr-site-generatr version

Structurizr Site Generatr v1.1.3
```

### Generate a website

#### From a [C4 Workspace](https://docs.structurizr.com/dsl)

This is the primary use case of Structurizr Site Generatr -- to generate a website from a
[C4 Workspace](https://docs.structurizr.com/dsl).

```shell
installed> structurizr-site-generatr generate-site -w workspace.dsl

   docker> docker run -it --rm -v c:/projects/c4:/var/model ghcr.io/avisi-cloud/structurizr-site-generatr generate-site -w workspace.dsl
```

Here, the `--workspace-file` or `-w` parameter specifies the input
[C4 Workspace DSL file](https://docs.structurizr.com/dsl) to the `generate-site` command. Additional
parameters that affect website generation can be reviewed using the `--help` operator.

By default, the generated website will be placed in `./build`, which is overwritten if it already exisits.

#### For those taking the Docker approach

When using the Docker approach, the local file system must be made available to the temporary Structurizr Site
Generatr container via a
[Docker file system volume mount](https://docs.docker.com/engine/reference/commandline/run/#mount-volume--v---read-only).
This is achieved by specifying the `-v` parameter with a linux-like **absolute** path to the folder containing
the .dsl file specified via `-w`. See how `C:\Projects\C4` has become `-v c:/projects/c4:/var/model` in the above
example?

#### Generate a website from a Git repository

Instead of relying on local .dsl files only, the `generate-site` command can also retrieve input files from a
Git repository as follows. This is particularly advantageous for demos, documentation, or CI/CD pipelines.

To explicitly name the branches that you want to build sites from you can use the --branches option.

```shell
structurizr-site-generatr generate-site
    --git-url https://github.com/avisi-cloud/structurizr-site-generatr.git
    --workspace-file docs/example/workspace.dsl
    --branches main,future,old
    --default-branch main
```

or you can choose to build all branches that are found in the repository and exclude specific ones by using the --all-branches and --exclude-branches options.

```shell
structurizr-site-generatr generate-site
    --git-url https://github.com/avisi-cloud/structurizr-site-generatr.git
    --workspace-file docs/example/workspace.dsl
    --all-branches
    --exclude-branches gh-pages
    --default-branch main
```

Both the --branches and --exclude-branches options are comma separated lists and can contain multiple branch names.

### Start a development web server around the generated website

To aid composition of [C4 Workspace DSL files](https://docs.structurizr.com/dsl), the `serve` command will
generate a website from the input .dsl specified with `-w` _and_ start a web server to view it. **Default port** for the web server is **8080**.
A different port for the web server can be specified with `-p PORT`. Additional parameters that affect website generation and the development
web server can be reviewed using the `--help` operator.

```shell
installed> structurizr-site-generatr serve -w workspace.dsl

   docker> docker run -it --rm -v c:/projects/c4:/var/model -p 8080:8080 ghcr.io/avisi-cloud/structurizr-site-generatr serve -w workspace.dsl
```

By default, a development web server will be started and accessible at http://localhost:8080/ (if available).

#### For those taking the Docker approach

However, when using the Docker approach, this development web server is within the temporary Structurizr Site
Generatr container. So
[Docker port mapping](https://docs.docker.com/engine/reference/commandline/run/#publish-or-expose-port--p---expose)
is needed to expose the container's port 8080 to the host (web browser). In the example above, the
`-p 8080:8080` argument tells Docker to bind the local machine / host's port 8080 to the container's port 8080.

## Customizing the generated website

By default, the site generator uses the
[C4PlantUmlExporter](https://docs.structurizr.com/export/plantuml#c4plantumlexporter)
to generate the diagrams. When using this exporter, all properties available for the C4PlantUMLExporter, e.g. `c4plantuml.tags`, can be applied
and affect the diagrams in the generate site. See also [Diagram notation](https://docs.structurizr.com/export/comparison) for an overview of supported features
and limitations for this exporter.

The look and feel of the generated site can be customized with several additional view properties in the C4
architecture model:

| Property name                           | Description                                                                                                                                                                                                                                                                                                                                      | Default                        | Example                                              |
|-----------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|------------------------------------------------------|
| `generatr.style.colors.primary`         | Primary site color, used for header bar background and active menu background.                                                                                                                                                                                                                                                                   | `#333333`                      | `#485fc7`                                            |
| `generatr.style.colors.secondary`       | Secondary site color, used for font color in header bar and for active menu.                                                                                                                                                                                                                                                                     | `#cccccc`                      | `#ffffff`                                            |
| `generatr.style.faviconPath`            | Site logo location relative to the configured `assets` folder. When configured, the logo image will be place on the left side in the header bar. This requires the `--assets-dir` switch when generating the site and the corresponding file to be available in the `assets` folder.                                                             |                                | `site/favicon.ico`                                   |
| `generatr.style.logoPath`               | Site favicon location relative to the configured `assets` folder. When configured, the favicon will be set for all generated pages. This requires the `--assets-dir` switch when generating the site and the corresponding file to be available in the `assets` folder.                                                                          |                                | `site/logo.png`                                      |
| `generatr.style.customStylesheet`       | URL to hosted custom stylesheet or path to custom stylesheet file (location relative to the configured `assets` folder). When configured this css file will be loaded for all pages. When using a path to a file the `--assets-dir` switch must be used when generating the site and the corresponding file is available in the `assets` folder. |                                | `site/custom.css` or 'https://uri.example/custom.css |
| `generatr.search.language`              | Indexing/stemming language for the search index. See [Lunr language support](https://github.com/olivernn/lunr-languages)                                                                                                                                                                                                                         | `en`                           | `nl`                                                 |
| `generatr.markdown.flexmark.extensions` | Additional extensions to the markdown generator to add new markdown capabilities. [More Details](https://avisi-cloud.github.io/structurizr-site-generatr/main/extended-markdown-features/)                                                                                                                                                       | Tables                         | `Tables,Admonition`                                  |
| `generatr.svglink.target`               | Specifies the link target for element links in the exported svg                                                                                                                                                                                                                                                                                  | `_top`                         | `_self`                                              |
| `generatr.site.exporter`                | Specifies the UML exporter, can be `c4` (uses the `C4PlantUMLExporter`) or `structurizr` (uses the `StructurizrPlantUMLExporter`)                                                                                                                                                                                                                | `c4`                           | `structurizr`                                        |
| `generatr.site.externalTag`             | Software systems containing this tag will be considered external                                                                                                                                                                                                                                                                                 |                                |                                                      |
| `generatr.site.nestGroups`              | Will show software systems in the left side navigator in collapsable groups                                                                                                                                                                                                                                                                      | `false`                        | `true`                                               |
| `generatr.site.cdn`                     | Specifies the CDN base location for fetching NPM packages for browser runtime dependencies. Defaults to jsDelivr, but can be changed to e.g. an on-premise location.                                                                                                                                                                             | `https://cdn.jsdelivr.net/npm` | `https://cdn.my-company/npm`                         |
| `generatr.site.darkMode`                | Will show the dark mode button to switch between light and dark mode on the website. If turned off, the site will be shown in light mode and this button will not be shown. Defaults to true/on.                                                                                                                                                 | `true`                         | `false`                                              |

See the included example for usage of some those properties in the
[C4 architecture model example](https://github.com/avisi-cloud/structurizr-site-generatr/blob/main/docs/example/workspace.dsl#L163).

## Contributing

We welcome contributions! Please refer to [CONTRIBUTING.md](CONTRIBUTING.md) for more information on how you can help.

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
