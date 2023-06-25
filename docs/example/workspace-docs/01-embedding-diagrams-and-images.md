## Embedding diagrams and images

This page showcases the ability to embed diagrams and static images in documentation.

### Embedding diagrams

Diagrams can be embedded using the `embed:` syntax:

```markdown
![System Landscape Diagram](embed:SystemLandscape)
```

See also: <https://www.structurizr.com/help/documentation/diagrams>

#### Example: Embedded diagram

![System Landscape Diagram](embed:SystemLandscape)

### Embedding static images

Static assets can be included in the generated site, using the `--assets-dir` command-line flag. This flag can be used
with the `serve` command and the `generate-site` command.

When, for example, you would like to embed a nice picture which is located in the `pictures` directory under the assets
directory, you can do that as follows:

```markdown
![A nice picture](/pictures/nice-picture.png)
```

#### Example: Embedded picture

[Sun](https://www.flickr.com/photos/schmollmolch/4937297813/), by Christian Scheja

![A nice picture](/pictures/nice-picture.png)

### Embedding mermaid diagrams

Structurizr Site Generatr is supporting mermaid diagrams in markdown pages using the actual mermaid.js version. Therefore every diagram type, supported by mermaid may be used in markdown documentation files.

* flowchart
* sequence diagram
* class diagram
* state diagram
* entity-relationship diagram
* user journey
* gantt chart
* pie chart
* requirement diagram
* and some more

Please find the full list of supported chart types on [mermaid.js.org/intro](https://mermaid.js.org/intro/#diagram-types)

#### Flowchart Diagram Example

````markdown
```mermaid
graph TD;
  A-->B;
  A-->C;
  B-->D;
  C-->D;
```
````

```mermaid
graph TD;
  A-->B;
  A-->C;
  B-->D;
  C-->D;
```

#### Sequence Diagram Example

````markdown
```mermaid
sequenceDiagram
    participant Alice
    participant Bob
    Alice->>John: Hello John, how are you?
    loop Healthcheck
        John->>John: Fight against hypochondria
    end
    Note right of John: Rational thoughts <br/>prevail!
    John-->>Alice: Great!
    John->>Bob: How about you?
    Bob-->>John: Jolly good!
```
````

```mermaid
sequenceDiagram
    participant Alice
    participant Bob
    Alice->>John: Hello John, how are you?
    loop Healthcheck
        John->>John: Fight against hypochondria
    end
    Note right of John: Rational thoughts <br/>prevail!
    John-->>Alice: Great!
    John->>Bob: How about you?
    Bob-->>John: Jolly good!
```
