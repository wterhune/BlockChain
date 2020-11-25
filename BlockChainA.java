/*--------------------------------------------------------
1. Name / Date:
Wisa Terhune-Prahruettam, 11/4/2020
2. Java version used, if not the official version for the class:
 I used Java version jdk1.8.0_181.

3. Precise command-line compilation examples / instructions:
> I just ran the code in my intelliJ pressing Play. I ran into some issues running on the terminal.

4. List of files needed for running the program.

a) BlockChainA.java
b) gson-2.8.2.jar

6.Credits
1) https://www.tutorialspoint.com/gson/gson_overview.htm
2) https://mkyong.com/java/how-to-parse-json-with-gson/
3) https://www.baeldung.com/sha-256-hashing-java
4) https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/
5) https://www.youtube.com/watch?v=SSo_EIwHSd4
----------------------------------------------------------*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;
import java.security.*;

//Creating a basic block with fields blockId,uuid,first name, and last name
//has getters and setters with equals, toString, and hash
//This Block class basically contains the data of individual block
class Block {
    String blockid;
    UUID uuid;
    String firstName;
    String lastName;

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

    @Override
    public String toString() {
        return "Block{" +
                "blockid='" + blockid + '\'' +
                ", uuid=" + uuid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Objects.equals(blockid, block.blockid) &&
                Objects.equals(uuid, block.uuid) &&
                Objects.equals(firstName, block.firstName) &&
                Objects.equals(lastName, block.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockid, uuid, firstName, lastName);
    }
}


//This is the implementation of block A mini project.
//We will be creating a basic block chain with Block fields
//However, each chain will be accompanied by a cryptographic hash of the previous block and timeStamp
public class BlockChainA {

    public static ArrayList<BlockChainA> BLOCK_CHAIN = new ArrayList<BlockChainA>();
    public String prevHash; //cryptographic String of the previous hash
    public String hash;
    private String info;
    private long time;
    private int nonce; //stands for "number only used once"

    //constructor for our BlockChainA where we take in information data and the previous crypto hash
    public BlockChainA(String info, String prevHash) {
        this.info = info;
        this.prevHash = prevHash;
        this.time = new Date().getTime();
        this.hash = doHash(); //calls the doHash() method to compute the hash every time the constructor is called
    }

    //This is the main method where we create four nodes of the same information (at least first and last name) and added to our block chain.
    //The information are set in the blockData and would mine the blocks and add to the chain.
    public static void main(String[] args) {

        Block blockData = new Block(); //creating a blank block object before setting the fields

        UUID uuid = UUID.randomUUID();
        String blockId = UUID.randomUUID().toString();

        blockData.setFirstName("Scooby");
        blockData.setLastName("Doo");
        blockData.setBlockid(blockId);
        blockData.setUuid(uuid);

        //need to attach all of the data fields together in one string
        String stringBlockData = blockData.getBlockId() + blockData.getUuid() + blockData.getFirstName() + blockData.getLastName();

        //setting up Gson. This is a library that we downloaded from the class call "gson-2-8.2"
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String JSONRecord = gson.toJson(stringBlockData); //converting the long String of block fields together and convert to JSON from GSON

        //We are creating the original block and sending our long String data fields along with a previous hash of 0 since it is the original
        BlockChainA originalBlock = new BlockChainA(stringBlockData, "0");

        originalBlock.mine(1); //we set the difficult level to 1 for this block

        //The first block has been added to the array list BLOCK_CHAIN
        BLOCK_CHAIN.add(originalBlock);
        System.out.println("Original Block in JSON ==> " + JSONRecord); //This is for debugging purposes

        //The second block gets created with concat string data and hash of the original block
        //Remember, the hash is created right at the constructor level for BlockChainA()
        BlockChainA block1 = new BlockChainA(stringBlockData, originalBlock.hash);
        block1.mine(1); //Setting the level to be easy
        BLOCK_CHAIN.add(block1); //add the block to the arraylist

        //The third block gets created with concat string data and hash of the second block
        //Remember, the hash is created right at the constructor level for BlockChainA()
        BlockChainA block2 = new BlockChainA(stringBlockData, block1.hash);
        block2.mine(1); //Setting the level to be easy
        BLOCK_CHAIN.add(block2); //add the block to the arraylist

        //The fourth block gets created with concat string data and hash of the second block
        //Remember, the hash is created right at the constructor level for BlockChainA()
        BlockChainA block3 = new BlockChainA(stringBlockData, block2.hash);
        block3.mine(1); //Setting the level to be easy
        BLOCK_CHAIN.add(block3); //add the block to the arraylist

        //We need to verify that our block chain has all of the information by
        //using GSON to print out in JSON
        String JSONBlockChain = new GsonBuilder().setPrettyPrinting().create().toJson(BLOCK_CHAIN);
        System.out.println(JSONBlockChain);

        //calls the checkChain() method to see if our block chain was built appropriately
        if(checkChain()) {
            System.out.println("BLOCK_CHAIN built successfully");
        } else System.out.println("BLOCK_CHAIN built unsuccessfully");
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
        BlockChainA thisBlock;
        BlockChainA beforeBlock;

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
}


