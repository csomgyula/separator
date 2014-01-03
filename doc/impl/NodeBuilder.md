`NodeBuilder` implementation
============================

status: working draft  
date: 2013-01-04

Main flow
---------

1. read the next token from tokenizer
2. handle next token

The rest of this doc deals with the last step, ie. how `NodeBuilder` handles the next token.

States
------

### Cursor ###

Node builder maintains a "cursor" to the open nodes, it points to the deepest open node. Then you can move up in the cursor, through `Node.getParent()`.

### Cursor invariants ###

* Cursor points to the deepest not yet closed node
* The cursor of a running builder always has at least one open block, ie. root

### Possible inputs ###

Input is a token associated with a 

1. normal separator
    1. simple separator  
    2. block open
2. block close

### Input invariants ###

* If there's an open block, then incoming normal separators must be lower in hierarchy
* The block close must be associated with the lowest open block

### Possible inputs (2) ###

Since the cursor of a running builder always has open block(s) the next token could be a

* normal token below open blocks 
    * simple separator or
    * open block (not SOS)
* close token of the deepest open block
    * block close token under root or
    * EOS


Handles
-------

### Handle invariants ###

* content nodes are always added as leaves

### Simple separator ###

if input token is a simple separator:

* new nodes are added as leaves, their tag is always the lowest in hiearchy that is independent from the tag of the token
    * if the node cursor is higher in the hierarchy (grandpa or higher) create the necessary parent nodes of the new node
        * for blocks create an external node (block-external) 
* if the token is
    * **INVALID** since root is a block token: the root tag then close nodes below it by moving the cursor to the root
    * the leaf tag then move the cursor to the new node's parent
    * is not the leaf tag then close the node associated with the token and the nodes below it by moving the cursor to its parent

### Simple block open ###
if input token is a simple block open:

* If there's content add a new leaf node to it, create its parents if necessary (just as with simple separators above), then close nodes associated with or below this tag
* Open block, possibly build its parents (just as with simple separators)


### Simple block close ###

* Add content as a new leaf node to the block, create the new node's parents if necessary (just as with simple separators above)
* Close the block (and everything below it) by moving to its parent

### EOS ###

Sames as simple block close.


Utils
-----

### content node ###

triggered by: each token

logic:

* create content node
* add content node
* possibly create its parents up till the deepest open node
* for blocks create an external node (block-external) 

### close block ###

triggered by: each token, especially the block open will trigger it when content node added

logic:

* close the open block associated with the token and everything below it

### open block ###

triggered by: block open

logic:

* create block node
* possibly create its parents up till the deepest open node
* for blocks create an external node (block-external) 

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