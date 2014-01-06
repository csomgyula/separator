Separator implementation
=========
status: working draft  
date: 2013-01-06


Roadmap
--

Prototype:

* simple separator + nesting - DONE
* simple block + nesting - DONE
* recursive block
* escape
* trim
* skip

Beta and RFE:

* engineering
    * error reporting
    * maybe logging
    * machine friendliness (fast, memory efficient)
* output
    * more sophisticated navigation
    * (SAX-like) callbacks, maybe Java 8 functors
* DSL
    * concatenation operator
    * siwtch operator
    * DSL dialects
    * rules by examples

API
--

### Separator ###

`Separator` can separate the given text according to the given rules. Has one method: `separate`:

* `Separator(String rules)` - initializes the Separator (compiles the rules)
    * separation `rules` must be given in the form of the [separator DSL](dsl.md)
* `Node separate(String text)` - parses the given text according to the given rules:
    * `text` can be any string
    * on successfull parsing it returns the root node of the tagged tree (see below)

Sample: parsing a CSV file

    Separator separator = new Separator();
    
    String csv;
    // read csv file...

    Node root = separator.separate("record \n field ;", csv)

    
### Node ###

The output of separation is a tagged tree which is represented by `Node`s. 

A node

* has a tag that represents its type: `Tag getTag()`
* has a kind which can be either `ROOT` or `BRANCH` or `LEAF`
* except root has parent: `Node getParent()`
* may have content if it is a leaf node: `String getContent()`
* may have child nodes if it is a root or branch node: `List<Node> getChildren()`

### Main flow ###

The prototype implementation builds upon a simple pipeline architecture:

    String =Char=> Tokenizer =Token=> NodeBuilder

The `Tokenizer` read characters from the input string and emit tokens to the `NodeBuilder`. The builder builds tree nodes accoring to the tokens received and the input string. That is to say the input string is shared between the tokenizer and the builder.

*Note*: Technically it is the `NodeBuilder` who instantiates and reads the `Tokenizer`. Hence while builder has an active role, tokenizer has a passive role.

Both `Tokenizer` and `NodeBuilder` relies upon compiled rules. It is the `Compiler` who creates this compiled form.

---

The main flow is the following:

*init*:

1. The `Separator` object compiles the given rules upon initialization by invoking the `compile()` method of the `Compiler`.

*separate*:

2. When `separate()` is called the `Separator` object instantiates a new `NodeBuilder` instance.
    1. The builder instantiates a new `Tokenizer` instance.
3. The separator then invokes the `build()` method of the builder. 
    1. The builder loops until there's more token. The main loop is the following:
        1. read the next token by invoking the `next()` method of the `Tokenizer`
        2. build the next node according to the next token just read
    2. If there's no more token return the root node.


