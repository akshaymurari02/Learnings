package amazon.webcrawler;

import java.util.List;

public class PageContent {
    private final String url;
    private final String title;
    private final String body;
    private final List<String> links;

    public PageContent(String url, String title, String body, List<String> links) {
        this.url = url;
        this.title = title;
        this.body = body;
        this.links = links;
    }

    public String getUrl() { return url; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public List<String> getLinks() { return links; }
}

