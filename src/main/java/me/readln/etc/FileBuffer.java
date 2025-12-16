package me.readln.etc;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

@Data
public class FileBuffer implements WordStat {
    private byte[] buffer;
    Environment environment;
    public FileBuffer(String filename, Environment environment) {
        this.environment = environment;
        buffer = loadBufferFromFile(filename);
    }

    public FileBuffer(URL url, Environment environment)
    {
        this.environment = environment;
        Document page = null;
        try {
            //page = Jsoup.connect(String.valueOf(url)).get();
            page = Jsoup.parse(url, 3000);
        } catch (IOException e) {
            environment.getUi().displayMessageLn("URL can not be opened: " + environment.getUrl());
        }
        buffer = loadBufferFromPage(page);
    }

    private byte[] loadBufferFromPage (Document page)
    {
        byte[] buffer = new byte[0];
        buffer = addStringToBuffer(buffer, page.body().text());
        return buffer;
    }

    private byte[] addStringToBuffer (byte[] buffer, String string)
    {
        byte[] newBuffer = new byte[buffer.length + string.length()];
        int bufferIndex = 0;
        for (; bufferIndex < buffer.length; bufferIndex++) {
            newBuffer[bufferIndex] = buffer[bufferIndex];
        }
        for (int stringIndex = 0; stringIndex < string.length(); bufferIndex++, stringIndex++) {
            newBuffer[bufferIndex] = (byte)string.charAt(stringIndex);
        }

        return newBuffer;
    }

    private byte[] loadBufferFromFile (String filename)
    {
        byte[] buff = null;
        try (FileInputStream fileIn = new FileInputStream(filename))
        {
            int estimateSize = fileIn.available();
            if (estimateSize > MAX_SIZE_FILE) {
                throw new IllegalStateException("Available part of the File " + filename +
                        " is too long: " + estimateSize + " bytes");
            }
            buff = new byte[estimateSize];
            fileIn.read(buff, 0, buff.length);
        }
        catch(IOException ex){
            System.out.println("Can't open file: " + ex.getMessage());
        }

        return buff;
    }

}
