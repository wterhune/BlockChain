/*--------------------------------------------------------
1. Name / Date:
Wisa Terhune-Prahruettam, 11/4/2020

2. Java version used, if not the official version for the class:
 I used Java version jdk1.8.0_181.

3. Precise examples / instructions to run this program:
> I just ran the code in my intelliJ pressing Play. I ran into some issues running on the terminal.

4. List of files needed for running the program.

a) BlockChainB.java
b) gson-2.8.2.jar
c) BlockchainLog.txt
d) blockBOutput.json

6.Credits
//TODO add more credits here
1) https://www.tutorialspoint.com/gson/gson_overview.htm
2) https://mkyong.com/java/how-to-parse-json-with-gson/
3) https://www.baeldung.com/sha-256-hashing-java
4) https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/
5) https://www.youtube.com/watch?v=SSo_EIwHSd4
----------------------------------------------------------*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.security.*;

//Creating a basic block with fields blockId,uuid,first name, and last name
//also include SSN and DOB
//has getters and setters with equals, toString, and hash
//This Block class basically contains the data of individual block
class BlockFieldC {
    String blockid;
    UUID uuid;
    String PreviousHash;
    String firstName;
    String lastName;
    String randomNumber;
    String winner;
    String SSN;
    String DOB;

    public String getBlockId() {
        return blockid;
    }

    public void setBlockid(String blockid) {
        this.blockid = blockid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPreviousHash() {
        return PreviousHash;
    }

    public void setPreviousHash(String previousHash) {
        PreviousHash = previousHash;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public String getWinner() {
        return winner;
    }

    public String getSSN() {
        return SSN;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    @Override
    public String toString() {
        return "BlockFieldC{" +
                "blockid='" + blockid + '\'' +
                ", uuid=" + uuid +
                ", PreviousHash='" + PreviousHash + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", randomNumber='" + randomNumber + '\'' +
                ", winner='" + winner + '\'' +
                ", SSN='" + SSN + '\'' +
                ", DOB='" + DOB + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockFieldC that = (BlockFieldC) o;
        return Objects.equals(blockid, that.blockid) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(PreviousHash, that.PreviousHash) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(randomNumber, that.randomNumber) &&
                Objects.equals(winner, that.winner) &&
                Objects.equals(SSN, that.SSN) &&
                Objects.equals(DOB, that.DOB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockid, uuid, PreviousHash, firstName, lastName, randomNumber, winner, SSN, DOB);
    }
}


//This is the implementation of block A mini project.
//We will be creating a basic block chain with Block fields
//However, each chain will be accompanied by a cryptographic hash of the previous block and timeStamp
public class BlockChainC {

    public static ArrayList<BlockChainC> BLOCK_CHAIN = new ArrayList<BlockChainC>();
    public String prevHash; //cryptographic String of the previous hash
    public String hash;
    private String info;
    private long time;
    private int nonce; //stands for "number only used once"

    //This is to create a file call FILE and to create an integer position for each of the data field
    //...essentially preparing for JSON input
    public static String FILE;

    private static final int JSONInputFirstName = 0; //integer placement for first  name
    private static final int JSONInputLastName = 1; //integer placement for last name
    private static final int JSONInputDOB = 2; //integer placement for dob
    private static final int JSONInputSSN = 3; //integer placement for ssn

    //constructor for our BlockChainB where we take in information data and the previous crypto hash
    public BlockChainC(String info, String prevHash) {
        this.info = info;
        this.prevHash = prevHash;
        this.time = new Date().getTime();
        this.hash = doHash(); //calls the doHash() method to compute the hash every time the constructor is called
    }

    //This is the beginning of the class at main. We will create a new BlockChainC and it will direct to run method. The run method will print out a statement.
    //Then we will write json and read from file the block fields
    public static void main(String[] args) {
        BlockChainC process = new BlockChainC(args);
        process.run(args);
    }
    //This method is from utilities provided by the class
    public void run(String argv[]) {
        System.out.println("Running Block Chain C process now...\n");
        try {
            writeToJSON(argv); //start running writeToJson method

        } catch (Exception exception) {
        }
        readingJSONFILE(); //after writing json, read the file
    }

    //This doHash() method will be called every time the BlockChainA constructor is called.
    //We are also computing SHA-265 encryption
    public String doHash() {
        String hashString = prevHash + Long.toString(time) + Integer.toString(nonce) + info;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(hashString.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString(); //this is the result for hex in string
        } catch (Exception error) {
            error.getMessage();
            error.getCause();
            throw new RuntimeException(error);
        }
    }

    //We are setting the difficulty level to 1...as easy.
    public void mine(int level) {
        String string = new String(new char[level]).replace('\0', '0');
        while (!hash.substring(0, level).equals(string)) {
            nonce++;
            hash = doHash(); //calls doHash() method to compute SHA265
        }
    }

    //this is similar to writeToJSON method in the previous mini block chain implementation
    //Some of the code is from the course
    public void writeToJSON(String args[]) throws Exception {

        LinkedList<BlockFieldC> blocks = new LinkedList<BlockFieldC>(); //creating a new linkedList of blocks
        int number;

        //checking what the file name will be...will look at the argument length and set a number
        if (args.length < 1) {
            number = 0;
        } else if (args[0].equals("0")) { //check the num of argument
            number = 0;
        } else if (args[0].equals("1")) { //check the num of argument
            number = 1;
        } else if (args[0].equals("2")) { //check the num of argument
            number = 2;
        } else {
            number = 0;
        }

        System.out.println(number);
        //whatever the number is, we will set the FILE to that "txt"
        //this is to keep track of what "number" is too
        switch (number) {
            case 1:
                FILE = "BlockInput1.txt";
                break;
            case 2:
                FILE = "BlockInput2.txt";
                break;
            default:
                FILE = "BlockInput0.txt";
                break;
        }
        System.out.println("FILE NAME: " + FILE);

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            String[] x;
            String inputLineString;

            UUID uuid;
            String blockid;

            while ((inputLineString = br.readLine()) != null) { //as long as the next line is not null, keep reading in fields

                BlockFieldC blockFieldC = new BlockFieldC(); //creating a brand new BlockFieldC object

                uuid = UUID.randomUUID(); //creating a random UUID
                blockid = uuid.toString();
                //A1. using string array to put in our data into manageable tokens
                x = inputLineString.split(" +");

                //We are now setting each field into a block
                blockFieldC.setBlockid(blockid); //set the block id field
                blockFieldC.setUuid(uuid); //set the uuid field
                blockFieldC.setFirstName(x[JSONInputFirstName]); //set the first name field from json
                blockFieldC.setLastName(x[JSONInputLastName]); //set the last name field from json
                blockFieldC.setSSN(x[JSONInputSSN]); //set the ssn field from json
                blockFieldC.setDOB(x[JSONInputDOB]); //set the dob field from the json
                Random rr = new Random(); //generate a random number

                int randomValue = rr.nextInt(16777215); //we will generate a random value with a certain set bound
                String random = Integer.toHexString(randomValue); //now generating a random number to string
                blockFieldC.setRandomNumber(random); //set the random number field

                blocks.add(blockFieldC); //add each new block to the linked list call blocks
            }

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            fileNotFoundException.getMessage();
            fileNotFoundException.getCause();
        } catch (IOException x) {
            x.getCause();
            x.getMessage();
            x.printStackTrace();
        }

        System.out.println("Now writing to JSON");

        //Using GSON to write to JSON file
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        String JSON = GSON.toJson(blocks);

        System.out.println("GSON to JSON file: " + JSON); //This is for debugging purposes

        try (FileWriter fileWriter = new FileWriter("blockCOutput.json")) {
            GSON.toJson(blocks, fileWriter); //this will enable GSON to write to JSON

        } catch (IOException exception) {
            exception.getMessage();
            exception.getCause();
            exception.printStackTrace();
        }
    }

    public void readingJSONFILE() {
        System.out.println("Now reading JSON file"); //notification for reading json file
        Gson gson = new Gson(); //create a new GSON object

        try {
            Reader reader = Files.newBufferedReader(Paths.get("blockCOutput.json")); //identify the same output C file as write JSON method

            //This will change the JSON file output into objects. We will create an array list of BlockFieldC objects for json to fill in
            ArrayList<BlockFieldC> records = gson.fromJson(reader, new TypeToken<ArrayList<BlockFieldC>>() {
            }.getType());

            //we will output the records to the screen for reading json
            //the output will be for each block in the list
            for (BlockFieldC field : records) {
                System.out.println("First Name: " + field.firstName);
                System.out.println("Last Name: " + field.lastName);
                System.out.println("SSN: " + field.SSN);
                System.out.println("DOB: " + field.DOB);
            }

            //This is to catch out exception if the reading json file method did not work.
        } catch (FileNotFoundException e) {
            e.getCause();
            e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            e.getCause();
            e.getMessage();
            e.printStackTrace();
        }
    }

    public BlockChainC(String argv[]) {
        System.out.println("Preparing files to read...");
    }

}


