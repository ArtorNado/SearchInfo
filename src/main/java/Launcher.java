class Launcher {

    public static final int NUM_OF_SITES = 200;
    public static final String SITE = "https://tproger.ru";

    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        crawler.crawl();
    }
}