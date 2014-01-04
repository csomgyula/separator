`NodeBuilder` implementation
============================

status: working draft  
date: 2013-01-04


Main flow
---------

1. create root node
2. execute the main loop:
    1. read the next token from tokenizer
    2. handle next token

The rest of this doc deals with the last step, ie. how `NodeBuilder` handles the next token.


States
------

### Cursor ###

Node builder maintains a "cursor" to the list of open nodes, it points to the deepest open node: 

     @ROOT
       node
         node
           ...  
             node <- cursor points here

NodeBuilder can navigate within the cursor upwards, through `Node.getParent()`.
 
### Invariants ###

`NodeBuilder` provides the following invariants:

* After each round cursor points to the deepest node not yet closed
* The cursor of a running builder always has at least one open block, ie. root


Inputs
------

### Possible inputs ###

Input is a token associated with a 

1. simple separator** or
2. block open (not SOS) or 
3. block close (can be EOS)

*Note*: SOS and EOS is associated with the root node. SOS is the Start-of-Source, EOS is the End-of-Source.

### Invariants ###

`Tokenizer` provides the following invariants:

* If there's an open block, then incoming normal separators are lower in hierarchy
* An incoming block close is always associated with the lowest open block

Since the cursor of a running builder always has open block(s) we can refine what an input token can be. The next token of a running builder could be a

* normal token below open blocks 
    * either a simple separator or
    * an open block (not SOS)
* close token of the deepest open block
    * either a block close under root or
    * EOS


Handles
-------

### Initial state ###

Create the root node on the first round.

*TODO*: Currently it is implemented at `NodeBuilder`. However perhaps some logic could be delegated to `Tokenizer`: it might emit a SOS on its first invocation.

### Next token ###

1. depending on the token kind execute
2. simple separator or
3. simple block open or
4. simple block close

### Simple separator ###

If next token is a simple separator then do the following:

1. add a new content node (as leaf)
2. close the node associated with the input token and nodes below it

### Simple block open ###

If next token is a simple block open (not SOS):

1. if there's content add a new content node (as leaf)
2. close the node associated with the input token and nodes below it
3. open the block associated with the block open

### Simple block close ###

If next token is a simple block close (can be EOS):

1. add a new content node (as leaf)
2. close the node associated with the input token and nodes below it


### Invariants ###

`NodeBuilder` provides the following invariants:

* Content nodes are always added as leaves
* Node associated with the incoming token's tag is always closed just as nodes below it

Utils
-----

### New content node ###

Adds a new content node.

*triggered by*: each token

*logic*:

1. create content node according the previous and the current token
2. add content node to the cursor

### Close node ###

Closes the token associated with the input token and everything below it.

*triggered by*: each token, especially the block open will trigger it when content node added

*logic*:

* close the open block associated with the token's tag and everything below it

### Open block node ###

Open a simple block (not SOS).

*triggered by*: simple block open

*logic*:

1. create block node
2. add block node

### Add node ###

Addition of both content node and open block node behaves similalry:

1. possibly create its parents up till the deepest open node
2. for blocks create an external node (block-external) 

Summary
-------

`NodeBuilder` could be thought as a finite automata, where:

*States*:

* the state is the node cursor (ie. a linked list of nodes)

*State changes*:

* the cursor changes on the next token

*Special states*:

* initial state is `{root}`, ie. only the root is in the cursor
* last state is empty cursor `{}`

Sample
------

rules:

    line \n [var:const] {{ }} part (\s)+

source:

    constant11 constant12 {{var11 var12}} constant13
    {{var21}} constant21
    constant31

1. add constant11 to block-ext ie. const block
2. add constant12 to block-ext + open var11 var block
3. add var11 to var block
4. add var12 to var block and close block
4. add constant13 to block-ext