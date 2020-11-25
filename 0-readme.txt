Direction:
Put a 0-readme.txt file in the subdirectory with a brief description of what the mini-project achieves.

I have completed mini project block A-C.
Description:
This is for course CS435 mini block chain project. The purpose of the project is to complete a series of
block chain implementations, including to gain a rudimentary understanding of how block chain works
and basic steps on how to create a block...and later, a block chain.

The professor has highlighted about 18 steps in the mini block chain project.
This repository will only include the first 4 steps of block chain implementation out of the 18 steps.
The professor did include sample utility code to help us get started.

BlockChainA:
I created a dummy block object contain basic information like first name and last name. Then I created more
blocks with the same information. However, each block will contain a hash of added String and added to a block chain arraylist.
There is a verify method in the main method to check if the current hash matches.

BlockChainB:
For the second mini project, BlockChainB, I copied the code from BlockChain A and refactored the object's name and methods.
However, I added a method writeToJson() that will write the block chain fields into a json file call blockBOutput.json.
I thought that writing to a separate file than one BlockchainLedgerSample.json would help me troubleshoot each block chain mini project.

BlockChainC:
For the third mini project, BockChainC, I copied the code from BlockChainB but refactored writeToJson() and read from json method.
the writing to json identifies the file name from BlockInput0 to BlockInput2 for block fields. Then, the read from json method
will print the fields already stored in "blockCOutput.json" file.
