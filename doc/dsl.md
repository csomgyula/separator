Separator DSL
=============

status: working draft  
date: 2013-12-30

goal
--

Define a simple parser language based on separators: 

* _simple_ that is simple such as regular expressions and simpler than BNF.
* _based on separators_ that is similar but more sophisticated than [`String.split()`](http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#split%28java.lang.String%29) built into Java.

separators
--

### Simple separator ###

Simple separators are used to separate a list of elements. It behaves similarly  as [`String.split()`](http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#split%28java.lang.String%29). When defining such a separator you should give it a *tagname* and the separator *pattern* itself.

*Sample*: 

    line \n 

In the above sample the tagname is `line` and the separator is the new line character `\n`. If you apply the above rule then it will extract each line from the original text, each of them will be tagged with the given tagname, ie.:

    Separator.separate("line \n", "simple separator\nseparates\na list of elements");

will produce the following tree:

    @ROOT:
      line(1): simple separator
      line(2): separates
      line(3): a list of elements

Tags are useful when there are many separators, see more samples below.

*Syntax*:

    simple := tag pattern

Where 

* *tag* is a word `[a-zA-Z]+`
* *pattern* is a regular expression, however it cannot be word, sample: `sentence \.|!|?` will extract sentences from the given text, ie.:

the following code snippet

    Separator.separate("sentence \.|!|?", "Separator is an enchanced form of split. It can be used to parse simple structures.");

will produce the following tree:

    @ROOT:
      sentence(1): Separator is an enchanced form of split
      sentence(2): It can be used to parse simple structures

### Nesting simple separators ###

Since Java has a built in [`split`](http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#split%28java.lang.String%29) method, simple separators are not extremely useful themselves. However you can nest such separators.

*Sample*: The following defines two nested separators

    record \n field ;

If you apply the above against a string read from a CSV file, for instance a CSV file of the US presidents:

    George Washington; 1789; 1797
    John Adams; 1797; 1801
    Thomas Jefferson; 1801; 1809
    ...

then separator will parse the file, ie.:

    Separator.separate("record \n field ;", csv);

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

*Syntax*: simply concatenate the simple separator definitions one after the other:

    simple1 simple2... <=> tag1 pattern1 tag2 pattern2...

You should give the topmost separator first, the one below the topmost next, etc.

When nesting simple separators the result tree will always have a constant depth: the depth is the number of separators (not counting the root node). For instance in the above CSV example the depth is always 2 (not counting the root).

### simple block ###

    $quote " "

Block separators separate internal content (the content within the start separator char and the end separator char) from external content. In this case the quotian mark separates the quoted string from the rest of the content.

### recursive block ###

    paren@recursive ( )
    block@rec { }

A recursive block is a special block where blocks can be nested within each other.

`recursive` is a special keyword. Such keywords are prefixed with the `@` symbol. TODO: a better syntax?

### no separator ###

    body

In this case the content will be treated as whole, ie. with no separators.

### escape ###

    @escape // \n
    @esc /* */

An escape escapes from normal parsing rules. That is to say separator rules are 'suspended' within an escape sequence. 

One can define escape within an escape itself with th `^` character:

    @escape " ^\" "

### trim ###

    @trim \s \n

You can trim the content with the `trim` keyword.

### end of parsing ###

    @end \r\r

The `end` keyword terminates the previous separator rules. This is useful in two cases: if you do not want to parse the whole string or if you want to apply different separator rules for the different part of the string. For instance you can define the following rule to parse HTTP:

    $header \n @end \r\r $body

This will extract lines from the string until it finds two carriage returns, then it treats the rest as a whole.

samples
--

**CSV**

    $record \n $field ;

This shows how to nest simple separators. The resulting tree will have the following structure:

    - record1
     - field11
     - field12
     ...
    - record2
     - field21
     - field22
     ...

**HTTP**

    $action @end \n $header \n $item : @end \r\r body

This shows how to concatenate parsing rules, using the `@end` construct. The resulting tree will have the following structure:

    - action
    - header1
      - item11 
      - item12
    - header2
      - item21 
      - item22
    ...
    - body

**Java**

    $block@rec { } $expression ; @esc " ^\" " @esc // \n @esc /* */ @trim \s \n

This sample shows how to nest recursive blocks and simple separators. It illustrates how to use escapes as well. TODO: this is not a true nesting.

**JSON**  
TODO


syntax 
--

pseudo specs:

**in BNF**

    separator := \s* (head (\s* marker)* \s*)+ 
    head := $tag | $tag@keyword | @keyword
    marker := [^\s]+
    tag := alpha*
    keyword := esc | rec | trim

**in separator**  
The language can be expressed in itself:-)

    $def $ $def @ $item \s*

This sample also shows that one can use the same *tag* repeatedly.


