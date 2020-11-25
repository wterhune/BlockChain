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
d) blockBOutput.json //TODO change the name

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

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.security.*;

//Creating a basic block with fields blockId,uuid,first name, and last name
//has getters and setters with equals, toString, and hash
//This Block class basically contains the data of individual block
class BlockFieldB {
    String blockid;
    UUID uuid;
    String PreviousHash;
    String firstName;
    String lastName;
    String randomNumber;
    String winner;
    String SSN;

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

    @Override
    public String toString() {
        return "Block{" +
                "blockid='" + blockid + '\'' +
                ", uuid=" + uuid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", randomNumber='" + randomNumber + '\'' +
                ", winner='" + winner + '\'' +
                ", SSN='" + SSN + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockFieldB block = (BlockFieldB) o;
        return Objects.equals(blockid, block.blockid) &&
                Objects.equals(uuid, block.uuid) &&
                Objects.equals(firstName, block.firstName) &&
                Objects.equals(lastName, block.lastName) &&
                Objects.equals(randomNumber, block.randomNumber) &&
                Objects.equals(winner, block.winner) &&
                Objects.equals(SSN, block.SSN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockid, uuid, firstName, lastName, randomNumber, winner, SSN);
    }
}


//This is the implementation of block A mini project.
//We will be creating a basic block chain with Block fields
//However, each chain will be accompanied by a cryptographic hash of the previous block and timeStamp
public class BlockChainB {

    public static ArrayList<BlockChainB> BLOCK_CHAIN = new ArrayList<BlockChainB>();
    public String prevHash; //cryptographic String of the previous hash
    public String hash;
    private String info;
    private long time;
    private int nonce; //stands for "number only used once"

    //constructor for our BlockChainB where we take in information data and the previous crypto hash
    public BlockChainB(String info, String prevHash) {
        this.info = info;
        this.prevHash = prevHash;
        this.time = new Date().getTime();
        this.hash = doHash(); //calls the doHash() method to compute the hash every time the constructor is called
    }

    //This is the main method where we create four nodes of the same information (at least first and last name) and added to our block chain.
    //The information are set in the blockData and would mine the blocks and add to the chain.
    public static void main(String[] args) {

        //We will now create 4 blocks and each will be added to the BLOCK_CHAIN arrayList
        //They will individually call doHash() method whenever the BlockChainB constructor is called
        BlockChainB originalBlock = new BlockChainB("Original Block", "0");
        originalBlock.mine(1);
        BLOCK_CHAIN.add(originalBlock);
        BlockChainB block1 = new BlockChainB("Block 1", originalBlock.hash);
        block1.mine(1);
        BLOCK_CHAIN.add(block1);
        BlockChainB block2 = new BlockChainB("Block 2!", block1.hash);
        block2.mine(1);
        BLOCK_CHAIN.add(block2);
        BlockChainB block3 = new BlockChainB("Block 3!", block2.hash);
        block3.mine(1);
        BLOCK_CHAIN.add(block3);
        //for debugging purposes we print the block chain in JSON format
        String JSONBlockChain = new GsonBuilder().setPrettyPrinting().create().toJson(BLOCK_CHAIN);
        System.out.println("JSON Block Chain B is " + JSONBlockChain);

        //calls the checkChain() method to see if our block chain was built appropriately
        if(checkChain()) {
            System.out.println("BLOCK_CHAIN built successfully");
        } else System.out.println("BLOCK_CHAIN built unsuccessfully");

        //calling the writeToJSON() method so that our output will be in a json file
        writeToJSON();

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

    //This method will return a true or false if the block chain was built successfully or not.
    public static boolean checkChain() {
        BlockChainB thisBlock;
        BlockChainB beforeBlock;

        //Will go through each block in the chain and check the equality of the hashes.
        for (int i = 1; i < BLOCK_CHAIN.size(); i++) {
            thisBlock = BLOCK_CHAIN.get(i);
            beforeBlock = BLOCK_CHAIN.get(i - 1);
            if (!beforeBlock.hash.equals(thisBlock.prevHash)) {
                return false; //previous hashes do not match
            }
            if (!thisBlock.hash.equals(thisBlock.doHash())) {
                return false; //current hashes do not match
            }
        }
        return true; //every block in the chain checks out :D
    }

    public static void writeToJSON() {

        //randomly create a uuid for blockId
        UUID uuid = UUID.randomUUID();
        String blockID = uuid.toString();

        //We will be creating a basic BlockFieldB where we set the first name, last name, ssn, blockID, and uuid.
        BlockFieldB block = new BlockFieldB();
        block.setFirstName("Scooby");
        block.setLastName("Doo");
        block.setSSN("111-22-3333");
        block.setBlockid(blockID);
        block.setUuid(uuid);

        //we will create a random number from a set upper bound and set the random number output to the block field
        Random randomNumber = new Random();
        int randomValue = randomNumber.nextInt(16777215);

        String randomHexString = Integer.toHexString(randomValue);
        block.setRandomNumber(randomHexString);

        //creating a long string record by adding all strings in the block together
        String stringRecord = block.getBlockId() + block.getPreviousHash() + block.getFirstName() + block.getLastName() + block.getSSN() + block.getRandomNumber();

        String SHA256String = "";

        //Now we are hashing the stringRecord...this will be the hash value
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringRecord.getBytes());
            byte byteData[] = messageDigest.digest();

            //iterates through byteData size and creates a SHA256 string
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA256String = sb.toString();
        } catch (NoSuchAlgorithmException x) {
        }
        //we will set the SHA265String to the block as the winner
        block.setWinner(SHA256String);

        //Using GSON to write to JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String JSON = gson.toJson(block);
        System.out.println("GSON => JSON... " + JSON); //this is for debugging purposes

        //We are now writing the record/JSON fields to a json file
        try (FileWriter fileWriter = new FileWriter("blockBOutput.json")) {
            gson.toJson(block, fileWriter); //changing from GSON to JSON
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
            e.getCause();
        }
    }
}


