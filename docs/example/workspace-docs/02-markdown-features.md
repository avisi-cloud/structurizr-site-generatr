## Extended Markdown features

This page showcases the ability to use extended Markdown formating features in workspace documentation files. The full list of available extensions to standard commonmark markdown features is documented in the flexmark wiki [Extensions page](https://github.com/vsch/flexmark-java/wiki/Extensions).

Most of these extended features have to be activated in your architecture model as a property in workspace views.

```DSL
workspace {
    ...
    views {
        ...
        properties {
            ...
            // full list of available "generatr.markdown.flexmark.extensions":
            // - Abbreviation
            // - Admonition
            // - AnchorLink
            // - Aside
            // - Attributes
            // - Autolink
            // - Definition
            // - Emoji
            // - EnumeratedReference
            // - Footnotes
            // - GfmIssues
            // - GfmStrikethroughSubscript
            // - GfmTaskList
            // - GfmUsers
            // - GitLab
            // - Ins
            // - Macros
            // - MediaTags
            // - ResizableImage
            // - Superscript
            // - Tables
            // - TableOfContents
            // - SimulatedTableOfContents
            // - Typographic
            // - WikiLinks
            // - XWikiMacro
            // - YAMLFrontMatter
            // - YouTubeLink
            // see https://github.com/vsch/flexmark-java/wiki/Extensions
            // ATTENTION:
            // * "generatr.markdown.flexmark.extensions" values must be separated by comma
            // * it's not possible to use "GitLab" and "ResizableImage" extensions together
            // default behaviour, if no generatr.markdown.flexmark.extensions property is specified, is to load the Tables extension only
            "generatr.markdown.flexmark.extensions" "Abbreviation,Admonition,AnchorLink,Attributes,Autolink,Definition,Emoji,Footnotes,GfmTaskList,GitLab,MediaTags,Tables,TableOfContents,Typographic"
            ...
        }
        ...
    }
    ...
}
```

### Table of Contents

`[TOC]` element which renders a table of contents

```markdown
[TOC]
```

will render into

[TOC]

#### Usage info

"TableOfContents" is an optional feature, that has to be activated in workspace views properties

```DSL
    "generatr.markdown.flexmark.extensions" "TableOfContents"
```

### Render Tables

Standard tables can be embedded in markdown text syntax:

```markdown
| header1 | header2 |
| ------- | ------- |
| content | content |
```

This will be rendered as

| header1 | header2 |
| ------- | ------- |
| content | content |

#### Usage info

"Render Tables" is an optional feature, that is enabled by default. If other optional markdown features are activated in workspace views properties, than you have to ensure, that Tables is listed in the extension list as well.

```DSL
    "generatr.markdown.flexmark.extensions" "Tables"
```

### Admonition Blocks

Admonitions create block-styled side content.

```markdown
!!! faq "FAQ"
    This is a FAQ.
!!! attention "Warning"
    This is a warning message

!!! info "information"
    this is an additional information
```

This will be rendered as

!!! faq "FAQ"
    This is a FAQ.
!!! attention "Warning"
    This is a warning message

!!! info "information"
    this is an additional information

#### Usage info

"Admonition Blocks" is an optional feature, that has to be activated in workspace views properties

```DSL
    "generatr.markdown.flexmark.extensions" "Admonition"
```

### GitLab flavored markdown extensions

Please see [GitLab flavored markdown features](https://docs.gitlab.com/ee/user/markdown.html?tab=Rendered+Markdown) for a detailed description.
Unfortunately only the following features are supported by Flexmark markdown renderer, that is used here.

#### Multiline Block quote delimiters

```markdown
>>>
If you paste a message from somewhere else

that spans multiple lines,

you can quote that without having to manually prepend `>` to every line!
>>>
```

>>>
If you paste a message from somewhere else

that spans multiple lines,

you can quote that without having to manually prepend `>` to every line!
>>>

#### Inline diff (deletions and additions)

With inline diff tags, you can display `{+ additions +}` or `[- deletions -]`.

The wrapping tags can be either curly braces or square brackets:

```markdown
- {+ addition 1 +}
- [+ addition 2 +]
- {- deletion 3 -}
- [- deletion 4 -]
```

- {+ addition 1 +}
- [+ addition 2 +]
- {- deletion 3 -}
- [- deletion 4 -]

#### Math

Math written in LaTeX syntax is rendered with [KaTeX](https://github.com/KaTeX/KaTeX).
_KaTeX only supports a [subset](https://katex.org/docs/supported.html) of LaTeX._

Math written between dollar signs with backticks (``$`...`$``) or single dollar signs (`$...$`)
is rendered inline with the text.

Math written between double dollar signs (`$$...$$`) or in a code block with
the language declared as `math` is rendered on a separate line:

````markdown
This math is inline: $`a^2+b^2=c^2`$.

This math is on a separate line using a ```` ```math ```` block:

```math
a^2+b^2=c^2
```
````

This math is inline: $`a^2+b^2=c^2`$.

This math is on a separate line using a ```` ```math ```` block:

```math
a^2+b^2=c^2
```

#### Usage info

"GitLab Flavored Markdown" is an optional feature, that has to be activated in workspace views properties

```DSL
    "generatr.markdown.flexmark.extensions" "GitLab"
```

### AnchorLink

Automatically adds anchor links to headings, using GitHub id generation algorithm

#### Usage info

"AnchorLink" is an optional feature, that has to be activated in workspace views properties

```DSL
    "generatr.markdown.flexmark.extensions" "AnchorLink"
```

### Definition Lists

Converts definition syntax of Php Markdown Extra Definition List to `<dl></dl>` HTML and corresponding AST nodes.

```markdown
Definition Term
: Definition of above term
: Another definition of above term
```

Definition Term
: Definition of above term
: Another definition of above term

#### Usage info

"Definition Lists" is an optional feature, that has to be activated in workspace views properties

```DSL
    "generatr.markdown.flexmark.extensions" "Definition"
```

### Emoji

Allows to create image link to emoji images from emoji shortcuts using [Emoji-Cheat-Sheet.com](https://www.webfx.com/tools/emoji-cheat-sheet) and optionally to replace with its unicode equivalent character with mapping by Mark Wunsch found at [mwunsch/rumoji](https://github.com/mwunsch/rumoji)

```markdown
thumbsup :thumbsup:  
calendar :calendar:  
warning :warning:  
```

thumbsup :thumbsup:  
calendar :calendar:  
warning :warning:  

#### Usage info

"Emoji" is an optional feature, that has to be activated in workspace views properties

```DSL
    "generatr.markdown.flexmark.extensions" "Emoji"
```

### GfmTaskList

Enables list items based task lists whose text begins with: `[ ]`, `[x]` or `[X]`

```markdown
- [x] Completed task
- [ ] Incomplete task
  - [x] Sub-task 1
  - [ ] Sub-task 3

1. [x] Completed task
1. [ ] Incomplete task
   1. [x] Sub-task 1
   1. [ ] Sub-task 3
```

will be rendered as

- [x] Completed task
- [ ] Incomplete task
  - [x] Sub-task 1
  - [ ] Sub-task 3

1. [x] Completed task
1. [ ] Incomplete task
   1. [x] Sub-task 1
   1. [ ] Sub-task 3

#### Usage info

"GfmTaskList" is an optional feature, that has to be activated in workspace views properties

```DSL
    "generatr.markdown.flexmark.extensions" "GfmTaskList"
```
