package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.Workspace

import com.vladsch.flexmark.util.misc.Extension
import com.vladsch.flexmark.util.data.DataHolder
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.ext.admonition.AdmonitionExtension
import com.vladsch.flexmark.ext.aside.AsideExtension
import com.vladsch.flexmark.ext.attributes.AttributesExtension
import com.vladsch.flexmark.ext.autolink.AutolinkExtension
import com.vladsch.flexmark.ext.definition.DefinitionExtension
import com.vladsch.flexmark.ext.emoji.EmojiExtension
import com.vladsch.flexmark.ext.emoji.EmojiImageType
import com.vladsch.flexmark.ext.enumerated.reference.EnumeratedReferenceExtension
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension
import com.vladsch.flexmark.ext.gitlab.GitLabExtension
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.parser.Parser

import nl.avisi.structurizr.site.generatr.site.GeneratorContext

import mu.KotlinLogging

data class FlexmarkConfig(
    val flexmarkExtensionsProperty: String,
    val selectedExtensionMap: Map<String, Extension>,
    val flexmarkOptions: DataHolder
)

val availableExtensionMap: MutableMap<String, Extension> = mutableMapOf<String, Extension>(
    "Abbreviation" to com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension.create(),
    "Admonition" to com.vladsch.flexmark.ext.admonition.AdmonitionExtension.create(),
    "AnchorLink" to com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension.create(),
    "Aside" to com.vladsch.flexmark.ext.aside.AsideExtension.create(),
    "Attributes" to com.vladsch.flexmark.ext.attributes.AttributesExtension.create(), 
    "Autolink" to com.vladsch.flexmark.ext.autolink.AutolinkExtension.create(),
    "Definition" to com.vladsch.flexmark.ext.definition.DefinitionExtension.create(),
    // Docx Converter: render doxc from markdown
    "Emoji" to com.vladsch.flexmark.ext.emoji.EmojiExtension.create(),
    "EnumeratedReference" to com.vladsch.flexmark.ext.enumerated.reference.EnumeratedReferenceExtension.create(),
    "Footnotes" to com.vladsch.flexmark.ext.footnotes.FootnoteExtension.create(),
    "GfmIssues" to com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension.create(),
    "GfmStrikethrough" to com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension.create(),
    "GfmSubscript" to com.vladsch.flexmark.ext.gfm.strikethrough.SubscriptExtension.create(),
    "GfmStrikethroughSubscript" to com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension.create(),
    "GfmTaskList" to com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension.create(),
    "GfmUsers" to com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension.create(),
    "GitLab" to com.vladsch.flexmark.ext.gitlab.GitLabExtension.create(),
    // Html To Markdown: render markdown from html
    "Ins" to com.vladsch.flexmark.ext.ins.InsExtension.create(),
    // Jekyll Tags: render jekyll tags from markdown
    // Jira-Converter: render jira markup from markdown
    "Macros" to com.vladsch.flexmark.ext.macros.MacrosExtension.create(),
    "MediaTags" to com.vladsch.flexmark.ext.media.tags.MediaTagsExtension.create(),
    "ResizableImage" to com.vladsch.flexmark.ext.resizable.image.ResizableImageExtension.create(),
    // "SpecExample" to com.vladsch.flexmark.ext.spec.example.SpecExampleExtension.create(),
    "Superscript" to com.vladsch.flexmark.ext.superscript.SuperscriptExtension.create(),
    "Tables" to com.vladsch.flexmark.ext.tables.TablesExtension.create(),
    "TableOfContents" to com.vladsch.flexmark.ext.toc.TocExtension.create(),
    "SimulatedTableOfContents" to com.vladsch.flexmark.ext.toc.SimTocExtension.create(),
    "Typographic" to com.vladsch.flexmark.ext.typographic.TypographicExtension.create(),
    "WikiLinks" to com.vladsch.flexmark.ext.wikilink.WikiLinkExtension.create(),
    "XWikiMacro" to com.vladsch.flexmark.ext.xwiki.macros.MacroExtension.create(),
    "YAMLFrontMatter" to com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension.create(),
    // "YouTrackConverter" to com.vladsch.flexmark.ext.youtrack.converter.YouTrackConverterExtension.create(),
    "YouTubeLink" to com.vladsch.flexmark.ext.youtube.embedded.YouTubeLinkExtension.create(),
)

fun buildFlexmarkConfig(generatorContext: GeneratorContext): FlexmarkConfig {
    var flexmarkExtensionString: String = "Tables"

    if (generatorContext.workspace.properties.containsKey("flexmark.extensions")) {
        flexmarkExtensionString = generatorContext.workspace.properties["flexmark.extensions"] as String
    }

    val flexmarkExtensionNames = flexmarkExtensionString.split(",")

    // Create a Mutable List to store Extension objects
    val selectedExtensionMap: MutableMap<String, Extension> = mutableMapOf<String, Extension>();
    
    // Add each extension to the MutableDataSet
    flexmarkExtensionNames.forEach { it ->
        val extensionName = it.trim()
        if (availableExtensionMap.containsKey(extensionName)) {
            selectedExtensionMap.put(extensionName, availableExtensionMap[extensionName] as Extension)
        } else {
            println("Flexmark extension $extensionName not found. Skipping.")
        }
    }
    
    // Create a MutableDataSet to store the parser options
    val flexmarkOptions = MutableDataSet()
    flexmarkOptions.set(Parser.EXTENSIONS, selectedExtensionMap.values)

    if (selectedExtensionMap.containsKey("Emoji")) {
        flexmarkOptions.set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.UNICODE_ONLY)
    }

    return FlexmarkConfig(flexmarkExtensionString, selectedExtensionMap, flexmarkOptions)
}

