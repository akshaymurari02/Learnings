package amazon.webcrawler;

/**
 * Filters URLs to only crawl within the same domain.
 */
public class SameDomainFilter implements IUrlFilter {

    private final String allowedDomain;

    public SameDomainFilter(String seedUrl) {
        this.allowedDomain = extractDomain(seedUrl);
    }

    @Override
    public boolean shouldCrawl(String url) {
        return extractDomain(url).equals(allowedDomain);
    }

    private String extractDomain(String url) {
        // Simple domain extraction
        String noProtocol = url.replaceFirst("https?://", "");
        int slashIdx = noProtocol.indexOf('/');
        return slashIdx == -1 ? noProtocol : noProtocol.substring(0, slashIdx);
    }
}

