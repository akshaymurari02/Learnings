package amazon.webcrawler;

import java.util.List;

public class WebCrawlerDemo {

    public static void main(String[] args) {

        // Setup
        IPageFetcher fetcher = new MockPageFetcher();
        IUrlFilter filter = new SameDomainFilter("https://example.com");

        // Crawl with 4 threads, max 10 pages
        WebCrawler crawler = new WebCrawler(fetcher, filter, 10, 4);
        List<CrawlResult> results = crawler.crawl("https://example.com");

        // Summary
        System.out.println("\n=== Crawl Summary ===");
        System.out.println("Pages crawled: " + results.size());
        long totalTime = results.stream().mapToLong(CrawlResult::getCrawlTimeMs).sum();
        System.out.println("Total fetch time: " + totalTime + "ms (parallel, so wall time is less)");
        System.out.println("\nAll URLs crawled:");
        results.forEach(r -> System.out.println("  " + r.getUrl()));
    }
}

