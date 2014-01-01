Separator DSL
=============

status: working draft  
date: 2013-12-30

goal
--

Define a simple parser language based on separators. Simple that is simple such as regular expressions and simpler than BNF.

elements
--

### simple separator ###

    $line \n 

Simple separators are used to separate a list of elements. You should define a *tag tag* for the elements and the separator itself. *Tag names* are prefixed with the `$` symbol. TODO: a better syntax?

In the above case the tagname is `line` and the separator is the new line character `\n`. If you apply the above rule then it will extract each line from the original text, each of them will be tagged with the given tag tag. Tags are useful when there are many separators, see samples below.

**simple block**

    $quote " "

Block separators separate internal content (the content within the start separator char and the end separator char) from external content. In this case the quotian mark separates the quoted string from the rest of the content.

**recursive block**

    $paren@recursive ( )
    $block@rec { }

A recursive block is a special block where blocks can be nested within each other.

`recursive` is a special keyword. Such keywords are prefixed with the `@` symbol. TODO: a better syntax?

**no separator**

    $body

In this case the content will be treated as whole, ie. with no separators.

**escape**

    @escape // \n
    @esc /* */

An escape escapes from normal parsing rules. That is to say separator rules are 'suspended' within an escape sequence. 

One can define escape within an escape itself with th `^` character:

    @escape " ^\" "

**trim**

    @trim \s \n

You can trim the content with the `trim` keyword.

**end of parsing**

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

This sample also shows that one can use the same *tag tag* repeatedly.

TODO
--

* nesting simple separators within blocks
