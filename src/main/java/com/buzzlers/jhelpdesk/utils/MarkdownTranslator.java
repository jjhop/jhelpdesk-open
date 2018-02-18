package com.buzzlers.jhelpdesk.utils;

import org.pegdown.PegDownProcessor;

import org.springframework.stereotype.Component;

@Component
public class MarkdownTranslator {

    private PegDownProcessor markdownProcessor;

    public MarkdownTranslator() {
        markdownProcessor = new PegDownProcessor();
    }

    public String processMarkdown(String source) {
        return markdownProcessor.markdownToHtml(source);
    }
}
