

import java.io.File;
import java.io.*;
import java.util.*;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors


public class Test {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};


    public static final String inputFolder = "input";
    public static final String outputFolder = "output";


    public static void main(String[] args) throws Exception {

	List<File> files = getListFiles(inputFolder);

	for(int i = 0; i < files.size(); i++){
	    File file = files.get(i);

	    // parse
	    String outputString = parseMidi(inputFolder + "/" + file.getName());

	    // write to file
	    writeToFile(outputFolder + "/" + file.getName() + ".txt", outputString);
	}



    }



   public static List<File> getListFiles(String folderName){
	File folder = new File(folderName);
	File[] listOfFiles = folder.listFiles();
    	List<File> files = new ArrayList<File>();

	for (int i = 0; i < listOfFiles.length; i++) {
 	    if (listOfFiles[i].isFile()) {
  	    	//System.out.println("File " + listOfFiles[i].getName());
	    	files.add(listOfFiles[i]);
  	    } else if (listOfFiles[i].isDirectory()) {
   	        //System.out.println("Directory " + listOfFiles[i].getName());
 	    }
	}

	return files;
	// end of function
   }

   public static String parseMidi(String midiFileName) throws Exception{
	
	System.out.println("parsing " + midiFileName + " ...");
        
	Sequence sequence = MidiSystem.getSequence(new File(midiFileName));

        int trackNumber = 0;
	String outputString = "";
        for (Track track :  sequence.getTracks()) {

            trackNumber++;
            //System.out.println("Track " + trackNumber + ": size = " + track.size());
            //System.out.println();

            for (int i=0; i < track.size(); i++) { 

                MidiEvent event = track.get(i);
                //System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();

                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    //System.out.print("Channel: " + sm.getChannel() + " \n");

                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity + "\n");

			// also append to output string
			outputString += event.getTick() + " " + key + " " + velocity + "\n";


                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        //System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    //System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
	    // end of for
        }
	

	return outputString;
	// end of function
   }

    public static void writeToFile(String filename, String outputString){
	try {
   	   FileWriter myWriter = new FileWriter(filename);
   	   //myWriter.write("Files in Java might be tricky, but it is fun enough!");
	   myWriter.write(outputString);
   	   myWriter.close();
    	  System.out.println("Successfully wrote to the file.");
    	} catch (IOException e) {
     	 System.out.println("An error occurred.");
     	 e.printStackTrace();
    	}
   }

}