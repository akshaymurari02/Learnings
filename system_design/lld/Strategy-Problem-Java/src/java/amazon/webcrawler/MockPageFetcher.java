package amazon.webcrawler;

import java.util.*;

/**
 * Mock page fetcher that simulates a web graph for demo/testing.
 */
public class MockPageFetcher implements IPageFetcher {

    private final Map<String, PageContent> pages = new HashMap<>();

    public MockPageFetcher() {
        // Simulate a small web graph
        pages.put("https://example.com", new PageContent(
                "https://example.com", "Home Page", "Welcome to example.com",
                Arrays.asList("https://example.com/about", "https://example.com/products", "https://example.com/blog")));

        pages.put("https://example.com/about", new PageContent(
                "https://example.com/about", "About Us", "We are a tech company",
                Arrays.asList("https://example.com", "https://example.com/team")));

        pages.put("https://example.com/products", new PageContent(
                "https://example.com/products", "Products", "Our product catalog",
                Arrays.asList("https://example.com", "https://example.com/products/item1", "https://example.com/products/item2")));

        pages.put("https://example.com/blog", new PageContent(
                "https://example.com/blog", "Blog", "Latest articles",
                Arrays.asList("https://example.com", "https://example.com/blog/post1")));

        pages.put("https://example.com/team", new PageContent(
                "https://example.com/team", "Our Team", "Meet the team",
                Arrays.asList("https://example.com/about")));

        pages.put("https://example.com/products/item1", new PageContent(
                "https://example.com/products/item1", "Product 1", "Amazing product details",
                Arrays.asList("https://example.com/products")));

        pages.put("https://example.com/products/item2", new PageContent(
                "https://example.com/products/item2", "Product 2", "Another great product",
                Arrays.asList("https://example.com/products", "https://external.com/review")));

        pages.put("https://example.com/blog/post1", new PageContent(
                "https://example.com/blog/post1", "First Post", "Hello World blog post",
                Arrays.asList("https://example.com/blog")));
    }

    @Override
    public PageContent fetch(String url) {
        // Simulate network latency
        try {
            Thread.sleep(50 + new Random().nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return pages.get(url);
    }
}

