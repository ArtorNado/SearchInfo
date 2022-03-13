import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Tokenizer {

    public void tokenize(boolean writeIntoSingleFile) {
        File directory = new File("sites");
        File[] files = directory.listFiles();
        if (files == null) return;
        Integer i = writeIntoSingleFile ? null : 1;
        for (File file : files) {
            tokenizeAndWriteFile(file, writeIntoSingleFile, i);
            if (!writeIntoSingleFile) {
                File tokenFile = new File(Utils.generateFileName(Utils.WORDS_TOKEN_DIR, i));
                removeDuplicates(tokenFile, true);
                i++;
            }
        }
        if (writeIntoSingleFile) removeDuplicates(new File(Utils.TOKENS_FILE_PATH), false);
    }

    private void tokenizeAndWriteFile(File source, boolean needToWriteInSingleFile, Integer index) {
        List<String> tokens = null;
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\\p{P}", " ").replaceAll("[0-9]", " ");
                tokens = Arrays.asList(line.split("(?=\\p{javaSpaceChar}|(\\p{Lu}\\p{javaLowerCase}))"));
                tokens = tokens.stream()
                        .map(token -> token.replace(" ", ""))
                        .filter(token -> !token.equals(""))
                        // Убираем одинокие буквы
                        .filter(token -> token.length() != 1)
                        // Убираем союзы
                        .filter(token -> !token.toLowerCase().matches(Utils.UNIONS_REGEX_PATTERN))
                        // Убираем предлоги
                        .filter(token -> !token.toLowerCase().matches(Utils.PREPOSITIONS_REGEX_PATTERN))
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (needToWriteInSingleFile) {
            Utils.writeToFileLineByLine(
                    new File(Utils.TOKENS_FILE_PATH),
                    tokens,
                    false
            );
        } else {
            Utils.writeToFileLineByLine(
                    new File(Utils.generateFileName(Utils.WORDS_TOKEN_DIR, index)),
                    tokens,
                    false
            );
        }
    }

    private void removeDuplicates(File source, boolean shouldDelete) {
        Set<String> noDuplicatesSet = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = br.readLine()) != null) {
                noDuplicatesSet.add(line.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeToFileLineByLine(
                source,
                new ArrayList<>(noDuplicatesSet).stream().sorted().collect(Collectors.toList()),
                shouldDelete
        );
    }

}
