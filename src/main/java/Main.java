import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // get filename from args - if none provided, prompt user and exit
        String filename;
        if(args.length > 0) {
            filename = args[0];
        } else {
            System.out.println("No filename was supplied.");
            return;
        }

        // check filename has extension
        if(!filename.endsWith(".csv")) {
            filename += ".csv";
        }

        // check the file exists
        File file = new File(filename);
        if(!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        // attempt to decode the CSV into a list of arrays
        List<String[]> decodedCSV = new ArrayList<>();
        try {
            decodedCSV = CSVHelper.readCsv(file);
        } catch (IOException e) {
            Logger.error("There was an exception while decoding the CSV - " + e.getMessage());
            e.printStackTrace();
        }

        // check if something is in the decoded csv
        if(decodedCSV.size() == 0) {
            System.out.println("CSV file appears empty.");
            return;
        }

        // convert the csv data to players
        List<Player> players = Player.getPlayersFromCSV(decodedCSV);

        // sort the list by division and points
        List<Player> sortedPlayers = Player.sortPlayers(players);

        // get top three
        List<Player> topThreePlayers = Player.topTreePlayers(sortedPlayers);

        // convert top three to records
        List<Record> records = Record.getRecordsFromPlayers(topThreePlayers);

        // get YAML string
        String recordsAsYAML = "";
        try {
            recordsAsYAML = YAMLHelper.generateYAML(records);
        } catch (JsonProcessingException e) {
            Logger.error("There was an error while generating the YAML - " + e.getMessage());
            e.printStackTrace();
        }

        // output results
        System.out.println("records:");
        System.out.println(recordsAsYAML);
    }
}
