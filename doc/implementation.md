Separator implementation
=========
status: working draft  
date: 2013-01-01


Roadmap
--

Prototype:

1. simple separator + nesting
1. recursive block
1. escape separator
1. end separator
1. simple block, skip

Beta and RFE:

* error reporting
* maybe logging
* machine friendliness (fast, memory efficient)
* more sophisticated navigation
* DSL dialects
* (SAX-like) callbacks, maybe Java 8 functors
* typesafe builders

Public API
--

The prototype API consists of two classes: `Separator` and `Node`:

* `Separator` is the main class that exposes the separator interface
* `Node` represents a node in the tagged tree that is the output of separation

### Separator ###

`Separator` can separate the given text according to the given rules. Has one method: `separate`:

 * `Node separate(String rules, String text)` - parses the given text according to the given rules:
    * separation `rules` must be given in the form of the [separator DSL](separator-dsl.md)
    * `text` can be any string
    * on successfull parsing it returns the root node of the tagged tree (see below)

Sample: parsing a CSV file

    Separator separator = new Separator();
    
    String csv;
    // read csv file...

    Node root = separator.separate("record \\n field ;", csv)

    
### Node ###

The output of separation is a tagged tree which is represented by `Node`s. 

A node

* has a tag (name): `String getTag()`
* has a kind which can be either `ROOT` or `BRANCH` or `LEAF`
* except root has parent: `Node getParent()`
* may have content: `String getContent()`
* may have child nodes if it is a branch node: `List<Node> getChildren()`

Prototype implementation
--

### Core ###

The prototype implementation builds upon a simple **pipeline architecture**:

    String =Char=> Tokenizer =Token=> NodeBuilder

The `Tokenizer` read characters from the input string and emit tokens to the `NodeBuilder`. The builder builds tree nodes accoring to the tokens received and the input string. That is to say the input string is shared between the tokenizer and the builder.

*Note*: Technically it is the `NodeBuilder` who instantiates and reads the `Tokenizer`. Hence while builder has an active role, tokenizer has a passive role.

Both `Tokenizer` and `NodeBuilder` relies upon **compiled rules**. It is the `Compiler` who creates this compiled form.

### Flow ###

`Separator` delegates to `Parser` which in turn delegates to `Compiler` and `NodeBuilder`. The main flow is the following:

1. The `Separator` object instantiates a new `Parser` instance. 
    1. The parser in turn compiles the rules by invoking the compile method of `Compiler`.
2. The separator then invokes the `parse()` method of the parser. 
    1. The parser instantiates and configures a new `NodeBuilder` then
    2. pass control to the builder by invoking its `build()` method
        1. The builder then builds the output tree according to the above pipeline architecture
        2. and returns the root `Node`.
    3. The parser returns the root as well.
3. Finally the separator returns the root node as well.


TODO
--

* Compiled `Separator`

The `Separator` interface has a light weight or scripting style. It is optimal if someone has to parse different, "random" formats. However it is not optimal in use cases when someone has to parse texts of the same format. A stateful design would match such situations better, ie.:

     // compiles the rules
     public Separator(String rules){...}

     // rules given to the constructor are reused, no need to pass
     public Node separate(String text){...}

In this case one can use the same separator to separate texts of the same format, ie.:
 
    Separator csvSeparator = new Separator("record \\n field ;");

    String csv1, csv2,...;

    // read csv files...

    Node node1 = csvSeparator.separate(csv1);
    Node node2 = csvSeparator.separate(csv2);
    ...

* Streaming

Handle input streams, files directly.