Scope
=====
status: working draft  
date: 2013-01-02

Syntax and formats
------------------

### Tag names ###

**Why tag names?**

One might build a compact DSL without tag names. For instance, instead of

    record \n field ;

a simpler form could be just

    \n ;

The latter is simpler than the former, which (simplicity) is one of the main design goals. 

So why tag names? 

The reason:

Could a compact form handle blocks? Would an untagged tree be readable? Could the developer determine the meaning or role of the nodes? Take the following DSL and samples:

DSL:

    {  } ()

Sample1:

    head { a }

Sample2:

    call( a )

Could you tell the original code from the output tree?

Yes you can read the tokens from the nodes. But that leads to machine-code-like programming which is not an intention...

But: 

If you want you can implement and use your DSL. See custom syntax...

### Fixed length width ###

**Does the DSL handle fixed length width formats?**

No. It is out-of-scope. Fixed length width formats does not use explicit separators to separate fields. Meanwhile the DSL does use explicit separators only:-)

### Python identations ###

**Does the DSL handle Python-like separators?**
 
No.

Python has complex context-dependent separation format.

The following sample (taken from [java2s.com](http://www.java2s.com/Code/Python/Language-Basics/Forloopcombinedwithinfunction.htm)) prints qudratic numbers:

    1: from math import sqrt
    2:
    2: for n in range(99, 0, -1):
    3:   root = sqrt(n)
    4:   if root == int(root):
    5:     print n
    6:     break

Here line 5 starts a new block because it has more identation then the previous line 4. Meanwhile line 4 does not start a new block beacuse it has the same identation as the previous one, line 3. That is to say whether a separator opens a new block or not is dependent on the previous block. Broadly speaking:

* ident is block-open iff ident length > the previous ident length
* ident is block-close iff ident length = one of the previous ident lengthes

Could one express this in a simple form? If yes this feature will go to the DSL:-)

The problem is not context-dependency. The problem is complexity. The DSL in fact supports context dependent separators through regexp back references. However this is much simpler than Python's form.

Perhaps such forms might be added at the API level?

### Names as separators ###

### XML ###

API
---

### Named rules ###

**Should the technique support named rules?**

For mixed formats... Sample:

    rules:
        csv(;): record \n field ;
        csv(,): record \n field , 
    input: 
        @csv(;) field11; field12\nfield21; field22\n
        @csv(,) field11, field12\nfield21, field22

Problem: how to differentiate between format switch and special values? Ie. the above could be parsed as 

        | @csv(;) field11          | field12 |
        | field21                  | field22 |
        | @csv(,) field11, field12 |
        | field21, field22         |

MIME multiparts or such...?

    input:
        csv(;)@SOS@field11; field12\nfield21; field22@EOS@
        csv(,)@SOS@field11, field12\nfield21, field22@EOS@

### Compiled `Separator` ###

**Should the API support compiled `Separators`?**

The `Separator` interface has a light weight or scripting style. It is optimal if someone has to parse different, "random" formats. However it is not optimal in use cases when someone has to parse texts of the same format. A stateful design would match such situations better, ie.:

     // compiles the rules
     public Separator(String rules){...}

     // rules given to the constructor are reused, no need to pass
     public Node separate(String text){...}

In this case one can use the same separator to separate texts of the same format, ie.:
 
    Separator csvSeparator = new Separator("record \n field ;");

    String csv1, csv2,...;

    // read csv files...

    Node node1 = csvSeparator.separate(csv1);
    Node node2 = csvSeparator.separate(csv2);
    ...

### Streaming ###

**Should the API handle input streams, files directly?**