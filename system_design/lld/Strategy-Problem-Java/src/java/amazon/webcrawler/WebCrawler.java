package amazon.webcrawler;

import java.util.*;
import java.util.concurrent.*;

/**
 * Multithreaded Web Crawler using thread pool + concurrent data structures.
 *
 * Architecture:
 * - ConcurrentHashMap (visited set) ensures no duplicate crawls
 * - BlockingQueue (URL frontier) holds URLs to be crawled
 * - ExecutorService (thread pool) processes URLs in parallel
 * - CountDownLatch / atomic counter tracks completion
 */
public class WebCrawler {

    private final IPageFetcher pageFetcher;
    private final IUrlFilter urlFilter;
    private final int maxPages;
    private final int numThreads;

    private final Set<String> visited;
    private final BlockingQueue<String> urlFrontier;
    private final List<CrawlResult> results;
    private final ExecutorService executor;

    public WebCrawler(IPageFetcher pageFetcher, IUrlFilter urlFilter, int maxPages, int numThreads) {
        this.pageFetcher = pageFetcher;
        this.urlFilter = urlFilter;
        this.maxPages = maxPages;
        this.numThreads = numThreads;

        this.visited = ConcurrentHashMap.newKeySet();
        this.urlFrontier = new LinkedBlockingQueue<>();
        this.results = Collections.synchronizedList(new ArrayList<>());
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    public List<CrawlResult> crawl(String seedUrl) {
        System.out.println("🕷 Starting crawl from: " + seedUrl);
        System.out.println("  Threads: " + numThreads + " | Max pages: " + maxPages + "\n");

        urlFrontier.add(seedUrl);
        visited.add(seedUrl);

        // Submit worker tasks
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            futures.add(executor.submit(this::crawlWorker));
        }

        // Wait for all workers to finish
        for (Future<?> future : futures) {
            try {
                future.get(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                // timeout or error
            }
        }

        executor.shutdown();

        System.out.println("\n🏁 Crawl complete. Pages crawled: " + results.size());
        return results;
    }

    private void crawlWorker() {
        while (true) {
            // Check if we've hit the limit
            if (results.size() >= maxPages) return;

            String url;
            try {
                url = urlFrontier.poll(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (url == null) return; // no more URLs, worker exits

            if (results.size() >= maxPages) return;

            processUrl(url);
        }
    }

    private void processUrl(String url) {
        long start = System.currentTimeMillis();

        PageContent page = pageFetcher.fetch(url);
        if (page == null) return; // 404 or unreachable

        long elapsed = System.currentTimeMillis() - start;

        // Extract and enqueue new links
        int newLinks = 0;
        for (String link : page.getLinks()) {
            if (urlFilter.shouldCrawl(link) && visited.add(link)) {
                urlFrontier.add(link);
                newLinks++;
            }
        }

        CrawlResult result = new CrawlResult(url, page.getTitle(), page.getBody().length(), page.getLinks().size(), elapsed);
        results.add(result);

        System.out.println("[" + Thread.currentThread().getName() + "] " + result);
    }
}

