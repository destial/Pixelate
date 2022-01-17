package xyz.destiall.pixelate.score;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.utils.FormatterUtils;

public class Scoreboard {
    private static Scoreboard instance = null;
    private static final String fileName = "scoreboard";
    LinkedHashMap<String, Float> entries;

    private Scoreboard()
    {
        entries = new LinkedHashMap<String, Float>();
        //Read from File
        Context context = Pixelate.getContext();
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            String fileContents = "#Scoreboard Storage\n";
            try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
                fos.write(fileContents.getBytes(StandardCharsets.UTF_8));
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                if(!line.startsWith("#"))
                {
                    System.out.println("Parsing line: " + line);
                    String entryName;
                    Long time;
                    Float entryScore;
                    String split[] = line.split(";");
                    if(split.length == 3) //score;entryname;time
                    {
                        entryName = split[0];
                        entryScore = Float.parseFloat(split[1]);
                        time = Long.parseLong(split[2]);
                        entries.put(entryName+";"+time, entryScore);
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToLeaderboard(String entryName, Float entryValue, Long time)
    {
        entries.put(entryName+";"+time, entryValue);
        LinkedHashMap<String, Float> newEntries = entries.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, LinkedHashMap::new));
        ;
        entries = newEntries;
        Context context = Pixelate.getContext();
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            String toAdd = entryName + ";" + entryValue + ";" + time + '\n';
            try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND)) {
                fos.write(toAdd.getBytes());
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (FileNotFoundException e) {

        }


        System.out.println("Sorted Values");
        int index = 0;
        for(Map.Entry<String, Float> entry : entries.entrySet())
        {
            String split[] = entry.getKey().split(";");
            String entryName2 = split[0];
            Long time2 = Long.parseLong(split[1]);
            Float entryScore2 = entry.getValue();
            System.out.println("Entry " + ++index + ": " + entryName2 + " received score " + entryScore2 + " at time " + FormatterUtils.formatTime(time2, "EST") + " time time " + time2);
        }
    }

    public void clearLeaderboard()
    {
        entries.clear();
        Context context = Pixelate.getContext();
        FileInputStream fis = null;
        try {
            context.deleteFile(fileName);
        }catch(Exception e) { }
    }




    public static Scoreboard getInstance()
    {
        if(instance == null)
            instance = new Scoreboard();
        return instance;
    }

}
