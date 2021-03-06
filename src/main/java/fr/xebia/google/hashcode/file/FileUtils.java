package fr.xebia.google.hashcode.file;

import fr.xebia.google.hashcode.model.DataCenter;
import fr.xebia.google.hashcode.model.Row;
import fr.xebia.google.hashcode.model.Server;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class FileUtils {

    public static DataCenter readFileInDataCenter(String filePath) {
        DataCenter dataCenter;

        Path path = FileSystems.getDefault().getPath(filePath);
        String[] firstLine = readFirstLine(path);
        int rowNumber = parseInt(firstLine[0]);
        int rowSize = parseInt(firstLine[1]);
        int notAvailableNumber = parseInt(firstLine[2]);
        int groupCount = parseInt(firstLine[3]);

        dataCenter = new DataCenter();
        dataCenter.groupCount = groupCount;

        for (int i = 0; i < rowNumber; i++) {
            dataCenter.addRow(new Row(i, rowSize));
        }

        List<String[]> notAvalableLines = readNotAvailableLines(path, notAvailableNumber);
        for (String[] notAvalableLine : notAvalableLines) {
            int rowIndex = parseInt(notAvalableLine[0]);
            int column = parseInt(notAvalableLine[1]);
            dataCenter.setUnavailableAt(rowIndex, column);
        }

        List<String[]> serverLines = readServerLines(path, 1 + notAvailableNumber);
        int i = 0;
        for (String[] serverLine : serverLines) {
            int size = parseInt(serverLine[0]);
            int capacity = parseInt(serverLine[1]);
            dataCenter.addServer(new Server(i, capacity, size));
            i++;
        }

        return dataCenter;
    }

    public static void writeServersInFile(DataCenter dataCenter, String filePath) {

        List<Server> servers = dataCenter.getServers();
        servers.sort(INDEX_CONPARATOR);

        Path path = FileSystems.getDefault().getPath(filePath);
        try {
            Files.write(path, servers.stream().map(Object::toString).collect(Collectors.toList()), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Comparator INDEX_CONPARATOR = new Comparator<Server>() {

        @Override
        public int compare(Server s1, Server s2) {
            return s1.getIndice().compareTo(s2.getIndice());
        }
    };

    public static List<String> readFileInString(String filePath) {
        Path path = FileSystems.getDefault().getPath(filePath);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String[] readFirstLine(Path path) {
        try (Stream<String> fileLines = Files.lines(path)) {
            return fileLines.findFirst()
                    .map(s -> s.split(" "))
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String[]> readNotAvailableLines(Path path, int n) {
        try (Stream<String> fileLines = Files.lines(path)) {
            return fileLines.skip(1).limit(n).map(s -> s.split(" ")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String[]> readServerLines(Path path, int begin) {
        try (Stream<String> fileLines = Files.lines(path)) {
            return fileLines.skip(begin).map(s -> s.split(" ")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
