package amazon.webcrawler;

/**
 * Represents a crawled page result.
 */
public class CrawlResult {
    private final String url;
    private final String title;
    private final int contentLength;
    private final int linksFound;
    private final long crawlTimeMs;

    public CrawlResult(String url, String title, int contentLength, int linksFound, long crawlTimeMs) {
        this.url = url;
        this.title = title;
        this.contentLength = contentLength;
        this.linksFound = linksFound;
        this.crawlTimeMs = crawlTimeMs;
    }

    public String getUrl() { return url; }
    public String getTitle() { return title; }
    public int getContentLength() { return contentLength; }
    public int getLinksFound() { return linksFound; }
    public long getCrawlTimeMs() { return crawlTimeMs; }

    @Override
    public String toString() {
        return "[" + crawlTimeMs + "ms] " + url + " | " + title + " | " + contentLength + " bytes | " + linksFound + " links";
    }
}

