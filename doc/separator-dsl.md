separator DSL
=============

status: working draft  
date: 2013-12-30

elements
--

no separator

    $body

simple separator:

    $record \n 
    $field ;

simple block:

    $quote " "

recursive block

    $paren@recursive ( )
    $block@rec { }

escape:

    @escape // \n
    @esc /* */

escape in escape:

    @escape " ^\" "

trim:

    @trim \s \n

end of parsing:

    @end \r\r

samples
--

CSV:

    $record \n $field ;

This shows how to nest separators. The resulting tree will have the following structure:

    - record1
     - field11
     - field12
     ...
    - record2
     - field21
     - field22
     ...

HTTP:

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

Java:

    $block@rec { } $expression ; @esc " ^\" " @esc // \n @esc /* */ @trim \s \n

This sample shows how to nest blocks and simple separators and it illustrates how to use escapes. TODO: this is not a true nesting.

JSON: TODO


formal syntax in BNF
--

    separator := \s* (head (\s* marker)* \s*)+ 
    head := tag | tag@keyword | @keyword
    marker := [^\s]+
    tag := alpha*
    keyword := esc | rec | trim

formal syntax in separator
--

pseudo:

    $def $ $def @ $item \s*