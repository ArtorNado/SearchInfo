import java.io.*;
import java.util.List;

public class Utils {

    public static final String UNIONS_REGEX_PATTERN = "(а|абы|аж|ан|благо|буде|будто|вроде|да|дабы|даже|едва|ежели|если|же|затем|зато|и|ибо|или|итак|кабы|как|когда|коли|коль|ли|либо|лишь|нежели|но|пока|покамест|покуда|поскольку|притом|причем|пускай|пусть|раз|разве|ровно|сиречь|словно|так|также|тоже|только|точно|хоть|хотя|чем|чисто|что|чтоб|чтобы|чуть|якобы)";
    public static final String PREPOSITIONS_REGEX_PATTERN = "(без|близ|в|вместо|вне|для|до|за|из|из-за|из-под|к|ко|кроме|между|меж|на|над|о|об|обо|от|ото|перед|передо|пред|предо|по|под|подо|при|про|ради|с|со|сквозь|у|через|чрез)";

    public static final String TOKENS_FILE_PATH = "words/tokens.txt";
    public static final String TOKENS_LEMMATIZED_FILE_PATH = "words/tokens_lemmatized.txt";
    public static final String GROUPED_TOKENS_BY_LEMMAS_FILE_PATH = "words/tokens_grouped_by_lemmas.txt";

    public static void writeToFileLineByLine(String path, List<String> lines) {
        File outputFile = new File(path);
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
        } else {
            outputFile.delete();
            outputFile.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}