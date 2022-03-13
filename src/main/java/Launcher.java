class Launcher {

    public static final int NUM_OF_SITES = 200;
    public static final String SITE = "https://tproger.ru";

    public static void main(String[] args) {
        // Crawler crawler = new Crawler();
        // crawler.crawl();
        // Задание 2
//        Tokenizer tokenizer = new Tokenizer();
//        tokenizer.tokenize(true);
//        Lemmatizer lemmatizer = new Lemmatizer();
//        lemmatizer.lemmatize();
//        lemmatizer.groupLemmasByTokens();
        // Задание 3
//        Tokenizer tokenizer = new Tokenizer();
//        tokenizer.tokenize(false);
//        Lemmatizer lemmatizer = new Lemmatizer();
//        lemmatizer.lemmatizeMultiple();
//        InvertedIndexer invertedIndexer = new InvertedIndexer();
//        invertedIndexer.execute();
        BooleanSearch booleanSearch = new BooleanSearch();
        booleanSearch.execute();
    }
}