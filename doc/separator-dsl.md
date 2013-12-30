separator DSL
=============

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

HTTP:

    $action @end \n $header \n $item : @end \r\r body

Java:

    $block@rec { } $expression ; @esc " ^\" " @esc // \n @esc /* */ @trim \s \n

JSON:


formal syntax
--

    separator := (\s* head \s* marker \s*)+ 
    head := tag | tag@keyword | @keyword
    marker := [^\s]+
    tag := alpha*
    keyword := esc | rec | trim

formal syntax in separator
--

pseudo:

    $def $ $def @ $item \s*