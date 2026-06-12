package amazon.webcrawler;

import java.util.List;

/**
 * Interface for fetching page content and extracting links.
 * Can be swapped with real HTTP implementation.
 */
public interface IPageFetcher {
    PageContent fetch(String url);
}

