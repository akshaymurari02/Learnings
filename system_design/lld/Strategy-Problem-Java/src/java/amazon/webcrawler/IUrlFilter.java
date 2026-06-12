package amazon.webcrawler;

/**
 * URL filter to decide which URLs to crawl.
 */
public interface IUrlFilter {
    boolean shouldCrawl(String url);
}

