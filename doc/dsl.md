Separator DSL
=============

status: working draft  
date: 2013-01-04

Goal
--

Define a simple parser language based on separators: 

* _simple_ that is simple such as regular expressions and simpler than BNF.
* _based on separators_ that is similar but more sophisticated than [`String.split()`](http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#split%28java.lang.String%29) built into Java.

Separators
--

### Simple separator ###

Simple separators are used to separate a list of elements. It behaves similarly  as [`String.split()`](http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#split%28java.lang.String%29). When defining such a separator you should give it a *tagname* and the separator *pattern* itself.

**Sample**: 

    s line \n 

In the above sample the tagname is `line` and the separator is the new line character `\n`. If you apply the above rule then it will extract each line from the original text, each of them will be tagged with the given tagname, ie.:

    separator.separate("line \n", "simple separator\nseparates\na list of elements");

will produce the following tree:

    @ROOT:
      line(1): simple separator
      line(2): separates
      line(3): a list of elements

Tags are useful when there are many separators, see more samples below.

**Syntax**:

    tag pattern

Where 

* `tag` is a word: `[a-zA-Z]+`, sample: `line`

* `pattern` is a regular expression, however it cannot be word, sample: 

    sentence \.|!|\? 

will extract sentences from the given text, ie.: the following code snippet

    separator.separate("sentence \\.|!|\\?", "What is separator? Separator is an enchanced form of split. It can be used to parse simple structures.");

will produce the following tree:

    @ROOT:
      sentence(1): What is separator
      sentence(2): Separator is an enchanced form of split
      sentence(3): It can be used to parse simple structures

### Nesting simple separators ###

Since Java has a built in [`split`](http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#split%28java.lang.String%29) method, simple separators are not extremely useful themselves. However you can nest such separators.

**Sample**: The following defines two nested separators

    record \n field ;

If you apply the above against a string read from a CSV file, for instance a CSV file of the US presidents:

    George Washington; 1789; 1797
    John Adams; 1797; 1801
    Thomas Jefferson; 1801; 1809
    ...

then separator will parse the file, ie.:

    separator.separate("record \n field ;", csv);

will produce the following tree:

    @ROOT:
      record(1):
	    field(1): George Washington
	    field(2): 1789
	    field(3): 1797
      record(2):
	    field(1): John Adams
	    field(2): 1797
	    field(3): 1801
      record(3):
	    field(1): Thomas Jefferson
	    field(2): 1801
	    field(3): 1809

**Syntax**: simply concatenate the simple separator definitions one after the other:
    
    (tag pattern)+

ie.:

    tag1 pattern1 tag2 pattern2...

You should give the topmost separator first, the one below the topmost next, etc.

When nesting simple separators the result tree will always have a constant depth: the depth is the number of separators (not counting the root node). For instance in the above CSV example the depth is always 2 (not counting the root).

### Simple block ###

Simple blocks separate blocks of contents. Besides this they separate external contents (contents outside of blocks) as well. *Simple* here means that blocks are not "recursive": you cannot nest a simple block inside a block of the same type. If you need recursivity then use recursive blocks (see below).

**Sample**: In the following sample the block separates content enclosed in double paranthesis:

    [variable] {{ }}

This could be used to implement simple templating, ie.:

    separator.separate("[variable] {{ }}", "Hello {{name}}!");

will produce the following tree:

    @ROOT:
      variableExt(1): Hello
      variable(2):    name
      variableExt(3): !

Now you can implement templating like this:

1./ implement a template function:

    public String template(List<Node> nodes, Hashtable<String, String> vars){
        StringBuilder sb = new StringBuilder();

        // iterate through nodes
        for (Node node : nodes){
            String content = node.getContent();

            // if node represents a variable then get the dynamic value
            if ( node.isA("variable") ){
               content = vars.get(content);
            }
               
            sb.append(content);
        }

        return sb.toString();
    }

2./ parse the template:

    Node root = separator.separate("[variable] {{ }}", "Hello {{name}}!");

3./ get the dynamic values, for instance:

    Hashtable<String, String> vars = new Hashtable<String, String>();
    vars.put("name", "world");

4./ pass the parsed template and the vars to the above function

    template(root.getChildren(), vars);

which will then produce:

    Hello world!

The above simple definition will name external nodes as `variableExt`. That is nodes outside of blocks will have a tag with `variableExt` as tagname. Generally the name is autogenerated from the block name: `${blockName}Ext`. If you do not want to use this convention, you can explicitely name the external content, like this:

    [variable]constant ${ }

then

    separator.separate("[variable]constant ${ }", "Hello ${name}!");

will produce the following tree:

    @ROOT:
      constant(1): Hello
      variable(2): name
      constant(3): !
 
**Syntax**:

    "["tag"]"(extName)? openPattern closePattern

* `[...]` represents the separator type, ie.: simple block

* `tag` is the tagname, must be a word: `[a-zA-Z]+`

* `extName` is the tagname for external content, must be a word: `[a-zA-Z]+`

* `openPattern` and `closePattern` represents the opening and closing patterns respectively, must be valid regular expressions, however cannot be words


### Nesting simple blocks ###

You cannot nest a simple block inside a block of the same  type. However you can freely nest blocks of different types and simple separators.

For instance if you do not want variables to span multiple lines, then you may use:

    line \n [var] {{ }}


### recursive block ###

A recursive block is a special block where blocks can be nested within each other.

**TODO**

### escape ###

An escape escapes from normal parsing rules. That is to say separator rules are 'suspended' within an escape sequence. For instance quoted strings...

**TODO**

### skip ###

A skip does not just escape from normal parsing rules, its content is exluded form the result tree as well. For instance comments...

**TODO**

    #comment# // \n /* */
    comment## // \n
    skip comment: // \n

### trim ###

A trim trims the given patterns from the content (useful typically to exlude leading/trailing white spaces).

**TODO**

syntax 
--

    separator := (simple | simpleBlock)*

    simple := tag pattern

    simpleBlock := "["tag"]"blockExt? openPattern closePattern


