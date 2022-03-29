
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Counter {

    public void countIndicators() {
        File wordsToken = new File(Utils.WORDS_TOKEN_DIR);
        File[] tokenFiles = wordsToken.listFiles();
        File lemmasDirectory = new File(Utils.WORDS_LEMMAS_DIR);
        File[] lemmasFiles = lemmasDirectory.listFiles();

        if (tokenFiles == null || lemmasFiles == null) {
            System.out.println("No files found");
            return;
        }

        File idfsTokensFile = new File(Utils.IDFS_TOKENS_FILE_PATH);
        Map<String, Double> idfsForTokens = new HashMap<>();
        if (!idfsTokensFile.exists()) {
            idfsForTokens = countIdfsForTokens(tokenFiles);
            Utils.writeToFileLineByLine(
                    idfsTokensFile,
                    idfsForTokens.entrySet().stream().map(
                            (entry) -> entry.getKey() + " " + entry.getValue()).collect(Collectors.toList()
                    ),
                    false
            );
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(idfsTokensFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] array = line.split(" ");
                    idfsForTokens.put(array[0], Double.valueOf(array[1]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Выписываем токены
        List<String> tokenWithIndicators = new ArrayList<>();
        int index = 1;
        for (File tokenFile : tokenFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(tokenFile))) {
                List<String> allTokens = Files.readAllLines(Paths.get(tokenFile.getPath()));
                String token;
                while ((token = reader.readLine()) != null) {
                    String finalToken = token;
                    if (finalToken.matches("[a-zA-Z+]+")) continue;

                    // TF
                    double amountOfEnters = allTokens.stream().reduce(0, (acc, currToken) -> {
                        if (finalToken.equals(currToken)) {
                            return acc + 1;
                        }
                        return acc;
                    }, Integer::sum);
                    double tfCounter = amountOfEnters / allTokens.size();

                    // IDF
                    if (idfsForTokens.get(token) == null) continue;
                    double idfCounter = idfsForTokens.get(token);

                    // TF-IDF
                    double tfIdfCounter = tfCounter * idfCounter;
                    tokenWithIndicators.add("" + token + " " + idfCounter + " " + tfIdfCounter);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utils.writeToFileLineByLine(
                    new File(Utils.generateFileName(Utils.TOKENS_COUNTERS_DIR, index)),
                    tokenWithIndicators,
                    false
            );
            index++;
        }

        File idfsLemmasFile = new File(Utils.IDFS_LEMMAS_FILE_PATH);
        Map<String, Double> idfsForLemmas = new HashMap<>();
        if (!idfsLemmasFile.exists()) {
            idfsForLemmas = countIdfsForLemmas(lemmasFiles);
            Utils.writeToFileLineByLine(
                    idfsLemmasFile,
                    idfsForLemmas.entrySet().stream().map(
                            (entry) -> entry.getKey() + " " + entry.getValue()).collect(Collectors.toList()
                    ),
                    false
            );
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(idfsLemmasFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] array = line.split(" ");
                    idfsForLemmas.put(array[0], Double.valueOf(array[1]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Выписываем леммы
        tokenWithIndicators = new ArrayList<>();
        index = 1;
        for (File lemmaFile : lemmasFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(lemmaFile))) {
                List<String> allLemmas = Files.readAllLines(Paths.get(lemmaFile.getPath()));
                String token;
                while ((token = reader.readLine()) != null) {
                    String finalToken = token;
                    if (finalToken.matches("[a-zA-Z+]+")) continue;

                    // TF
                    double amountOfEnters = allLemmas.stream().reduce(0, (acc, currToken) -> {
                        if (finalToken.equals(currToken)) {
                            return acc + 1;
                        }
                        return acc;
                    }, Integer::sum);
                    double tfCounter = amountOfEnters / allLemmas.size();

                    // IDF
                    if (idfsForTokens.get(token) == null) continue;
                    double idfCounter = idfsForLemmas.get(token);

                    // TF-IDF
                    double tfIdfCounter = tfCounter * idfCounter;
                    tokenWithIndicators.add("" + token + " " + idfCounter + " " + tfIdfCounter);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utils.writeToFileLineByLine(
                    new File(Utils.generateFileName(Utils.LEMMAS_COUNTERS_DIR, index)),
                    tokenWithIndicators,
                    false
            );
            index++;
        }
    }

    private Map<String, Double> countIdfsForTokens(File[] tokenFiles) {
        Map<String, Double> idfs = new HashMap<>();
        File tokenFile = new File(Utils.TOKENS_FILE_PATH);
        try (BufferedReader tokenReader = new BufferedReader(new FileReader(tokenFile))) {
            String token;
            while ((token = tokenReader.readLine()) != null) {
                // убираем английские токены
                if (token.matches("[a-zA-Z]+")) continue;
                if (!idfs.containsKey(token)) idfs.put(token, 0.0);
                int amountOfSites = 0;
                for (File checkSiteFile : tokenFiles) {
                    try (BufferedReader siteReader = new BufferedReader(new FileReader(checkSiteFile))) {
                        String wordFromToken;
                        while ((wordFromToken = siteReader.readLine()) != null) {
                            if (token.equals(wordFromToken.toLowerCase())) {
                                amountOfSites++;
                            }
                        }
                    }
                }
                idfs.put(token, Math.log((double) tokenFiles.length / amountOfSites));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idfs;
    }

    private Map<String, Double> countIdfsForLemmas(File[] lemmasFiles) {
        Map<String, Double> idfs = new HashMap<>();
        File tokenFile = new File(Utils.TOKENS_LEMMATIZED_FILE_PATH);
        try (BufferedReader tokenReader = new BufferedReader(new FileReader(tokenFile))) {
            String token;
            while ((token = tokenReader.readLine()) != null) {
                // убираем английские токены
                if (token.matches("[a-zA-Z]+")) continue;
                if (!idfs.containsKey(token)) idfs.put(token, 0.0);
                int amountOfSites = 0;
                for (File checkSiteFile : lemmasFiles) {
                    try (BufferedReader siteReader = new BufferedReader(new FileReader(checkSiteFile))) {
                        String wordFromToken;
                        while ((wordFromToken = siteReader.readLine()) != null) {
                            if (token.equals(wordFromToken.toLowerCase())) {
                                amountOfSites++;
                            }
                        }
                    }
                }
                idfs.put(token, Math.log((double) lemmasFiles.length / amountOfSites));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idfs;
    }
}