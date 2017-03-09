
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class CSCI2020uAssignment1 extends Application {
    public static HashMap<String, Float> trainHam(File d) {
        String ham1 = d + "\\train\\ham";
        String ham2 = d + "\\train\\ham2";

        ArrayList<String> dirList = new ArrayList<String>();
        dirList.add(ham1);
        dirList.add(ham2);
        
        HashMap<String, Float> hash = new HashMap<String, Float>();
        float fileCount = 0f;
        
        for(String elem : dirList) {
            File dir = new File(elem);
            File[] directoryListing = dir.listFiles();

            for (File file : directoryListing) {
                fileCount += 1;
                
                try {
                    Scanner scan = new Scanner(file);
                    ArrayList<String> temp = new ArrayList<String>();

                    while(scan.hasNext()) {
                        String current = scan.next();
                        if(!temp.contains(current)) {
                            temp.add(current);
                        }
                    }

                    for(String word: temp) {    
                        if(hash.containsKey(word)) {
                            hash.replace(word, (hash.get(word) + 1));
                        } else {
                            hash.put(word,1f);
                        }
                    }
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CSCI2020uAssignment1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        for(String elem : hash.keySet()) {           
            hash.replace(elem, hash.get(elem)/fileCount);
        }
        return hash;
    }
    
    public static HashMap<String, Float> trainSpam(File d) {
        String spam = d + "\\train\\spam";

        ArrayList<String> dirList = new ArrayList<String>();
        dirList.add(spam);
        
        HashMap<String, Float> hash = new HashMap<String, Float>();
        float fileCount = 0f;
        
        for(String elem : dirList) {
            File dir = new File(elem);
            File[] directoryListing = dir.listFiles();

            for (File file : directoryListing) {
                fileCount += 1;
                
                try {
                    Scanner scan = new Scanner(file);
                    ArrayList<String> temp = new ArrayList<String>();

                    while(scan.hasNext()) {
                        String current = scan.next();
                        if(!temp.contains(current)) {
                            temp.add(current);
                        }
                    }

                    for(String word: temp) {    
                        if(hash.containsKey(word)) {
                            hash.replace(word, (hash.get(word) + 1));
                        } else {
                            hash.put(word,1f);
                        }
                    }
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CSCI2020uAssignment1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        for(String elem : hash.keySet()) {
            hash.replace(elem, hash.get(elem)/fileCount);
        }
        return hash;
    }
    
    public static ArrayList<TestFile> testHam(HashMap<String,Float> pSpamWord, File d) {
        String ham = d + "\\test\\ham";
        
        File dir = new File(ham);
        File[] dirListing = dir.listFiles();
        
        ArrayList<TestFile> results = new ArrayList<TestFile>();
        
        for(File file : dirListing) {
            try {
                Scanner scan = new Scanner(file);
                float pSpam = 0f;
                float eta = 0f;
                ArrayList<String> contains = new ArrayList<String>();
                
                while(scan.hasNext()) {
                    String current = scan.next();
                    
                    if((pSpamWord.containsKey(current)) && (pSpamWord.get(current) != 0f)) {
                        contains.add(current);
                    }
                }
                
                for(String elem : contains) {                  
                    eta += ((Math.log(1-pSpamWord.get(elem)))-((Math.log(pSpamWord.get(elem)))));                 
                }
                
                pSpam = (float) (1/(1+Math.pow(Math.E, eta)));
                
                if(pSpam > 0.50f) {
                    results.add(new TestFile(file.getName(), pSpam, "Ham", "Spam"));
                } else {
                    results.add(new TestFile(file.getName(), pSpam, "Ham", "Ham"));
                }
                        
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CSCI2020uAssignment1.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        return results;
    }
    
    public static ArrayList<TestFile> testSpam(HashMap<String,Float> pSpamWord, File d) {
        String spam = d + "\\test\\spam";
        
        File dir = new File(spam);
        File[] dirListing = dir.listFiles();
        
        ArrayList<TestFile> results = new ArrayList<TestFile>();
        
        for(File file : dirListing) {
            try {
                Scanner scan = new Scanner(file);
                float pSpam = 0f;
                float eta = 0f;
                ArrayList<String> contains = new ArrayList<String>();
                
                while(scan.hasNext()) {
                    String current = scan.next();
                    
                    if((pSpamWord.containsKey(current)) && (pSpamWord.get(current) != 0f)) {
                        contains.add(current);
                    }
                }
                
                for(String elem : contains) {
                    eta += ((Math.log(1-pSpamWord.get(elem)))-((Math.log(pSpamWord.get(elem)))));
                }              
                pSpam = (float) (1/(1+Math.pow(Math.E, eta)));
                
                if(pSpam > 0.50f) {
                    results.add(new TestFile(file.getName(), pSpam, "Spam", "Spam"));
                } else {
                    results.add(new TestFile(file.getName(), pSpam, "Spam", "Ham"));
                }
                        
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CSCI2020uAssignment1.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        return results;
    }
    
    public static float calcAccuracy(ArrayList<TestFile> data) {
        float accuracy = 0f;
        int correct = 0;
        
        for(TestFile elem : data) {
            if(elem.getActualClass().equals(elem.getPredictedClass())) {
                correct += 1;
            }
        }
        
        accuracy = (float) correct/data.size();
        
        return accuracy;
    }
    
    public static float calcPrecision(ArrayList<TestFile> data) {
        float precision = 0f;
        float truePos = 0;
        float falsePos = 0;
        
        for(TestFile elem : data) {
            if(elem.getActualClass().equals("Spam")){
                if(elem.getActualClass().equals(elem.getPredictedClass())) {
                    truePos += 1f;
                } else {
                    falsePos += 1f;
                }
            }
        }
        
        precision = (truePos/(falsePos+truePos));
        
        return precision;
    }
    
    public static ArrayList<TestFile> filterSpam(File dir) {
        HashMap<String, Float> pWordHam = new HashMap<String, Float>();
        pWordHam = trainHam(dir);
        
        HashMap<String, Float> pWordSpam = new HashMap<String, Float>();
        pWordSpam = trainSpam(dir);
        
        HashMap<String, Float> pSpamWord = new HashMap<String, Float>();
        
        //Iterate through the ham frequency map, calculating P(S|W) if the word
        //occurs in both maps, otherwise setting it to 0.
        for(String elem : pWordHam.keySet()) {
            if(pWordSpam.containsKey(elem)) {
                pSpamWord.put(elem, (pWordSpam.get(elem)/(pWordSpam.get(elem)+pWordHam.get(elem))));
            } else {
                pSpamWord.put(elem, 0f);
            }
        }
        
        //Iterates through the spam frequency map to catch the elements that do
        //not occur in the ham map.
        for(String elem : pWordSpam.keySet()) {
            if(!pWordHam.containsKey(elem)) {
                pSpamWord.put(elem, pWordSpam.get(elem));
                //pSpamWord.put(elem, 1f);
            }
        }
        
        ArrayList<TestFile> hamTest = new ArrayList<TestFile>();
        hamTest = testHam(pSpamWord, dir);
        
        ArrayList<TestFile> spamTest = new ArrayList<TestFile>();
        spamTest = testSpam(pSpamWord, dir);
        
        ArrayList<TestFile> results = new ArrayList<TestFile>();
        results.addAll(hamTest);
        results.addAll(spamTest);
        
        return results;
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Spam Slam 6000");
        
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);
        
        TableView<TestFile> table = new TableView<TestFile>();
        
        ArrayList<TestFile> rawData = filterSpam(mainDirectory);
        
        ObservableList<TestFile> data = FXCollections.observableList(rawData);
        table.setItems(data);
        
        TableColumn<TestFile, String> name = new TableColumn<TestFile, String>("File Name");
        name.setCellValueFactory(new PropertyValueFactory<TestFile, String>("filename"));
        
        TableColumn<TestFile, String> actual = new TableColumn<TestFile, String>("Actual Class");
        actual.setCellValueFactory(new PropertyValueFactory<TestFile, String>("actualClass"));
        
        TableColumn<TestFile, String> predicted = new TableColumn<TestFile, String>("Predicted Class");
        predicted.setCellValueFactory(new PropertyValueFactory<TestFile, String>("predictedClass"));
        
        TableColumn<TestFile, Float> prob = new TableColumn<TestFile, Float>("Spam Probability");
        prob.setCellValueFactory(new PropertyValueFactory<TestFile, Float>("spamProbability"));
        
        table.getColumns().add(name);
        table.getColumns().add(actual);
        table.getColumns().add(predicted);
        table.getColumns().add(prob);
                
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        
        Label accLabel = new Label("Accuracy: ");
        TextField acc = new TextField(String.valueOf(calcAccuracy(rawData)));
        
        Label presLabel = new Label("Precision: ");
        TextField pres = new TextField(String.valueOf(calcPrecision(rawData)));
        
        
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(hbox, accLabel, acc, presLabel, pres, table);
        
        Scene scene = new Scene(vbox, 500, 475);
        
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
