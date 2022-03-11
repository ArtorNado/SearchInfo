import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Tokenizer {

    public void tokenize() {
        File directory = new File("sites");
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            tokenizeAndWriteFile(file);
        }
        removeDuplicates(new File(Utils.TOKENS_FILE_PATH));
    }

    private void tokenizeAndWriteFile(File source) {
        List<String> tokens = null;
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\\p{P}"," ").replaceAll("[0-9]"," ");
                tokens = Arrays.asList(line.split("(?=\\p{javaSpaceChar}|(\\p{Lu}\\p{javaLowerCase}))"));
                tokens = tokens.stream()
                        .map(token -> token.replace(" ",""))
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
        Utils.writeToFileLineByLine(Utils.TOKENS_FILE_PATH, tokens);
    }

    private void removeDuplicates(File source) {
        Set<String> noDuplicatesSet = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = br.readLine()) != null) {
                noDuplicatesSet.add(line.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeToFileLineByLine(Utils.TOKENS_FILE_PATH, new ArrayList<>(noDuplicatesSet).stream().sorted().collect(Collectors.toList()));
    }

}
