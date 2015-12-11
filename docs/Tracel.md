[TOC]

### Tracel

TraceProv Expression Language


Simple subset of Javascript ES6 with features of functional languages.

#### TracEL Parser
TraceEL parsing is done by <a href="http://www.typescriptlang.org/">typescript compiler</a> running on <a href="https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino" target="_blank">Rhino interpreter</a>, launching exceptions on unsupported language elements we find in <a href="https://github.com/Microsoft/TypeScript/blob/master/src/compiler/types.ts" target=_"blank"> typescript AST (_Abstract Syntax Tree_)</a>.

#### TraceEL Grammar

To have a general idea of language features we would like to support here is an attempt at a simplified grammar:

todo check
```

Expr
|
|- Token
|
Path
|
PropertyPath     myProp.otherProp     $["my Prop"]["Other Prop"]

Expr examples:
let x = 5; props["some field"].makeNice(someParams, x)[4]

Token examples:
ID:			myId
INDEX:		[i]
                            (x[e]) x : array,  e : integer)
PROPERTY: 		[e]
							(x[e]) x : object,  e : string)
			|	.ID

DEF:		let ID = EXPR
ENTRY:		(ID | STRING_LITERAL) : EXPR
ENVEXPR:  	DEF; EXPR
ARRAY:		[EXPRs]
OBJECT:		{ENTRYs}

FUNCALL:  	ID(EXPRs)           makeNice()
LAMBDA:   	(IDs)=>EXPR		    (x)=>x*2
STRING_LITERAL:		"my String"
INT_LITERAL:		5
DOUBLE_LITERAL:		3.4
LiTERAL:  	   	INT_LITERAL
			|	DOUBLE_LITERAL
			|  	STRING_LITERAL

```

### TraceQuery

There is no widely used standard query language for json / javascript objects. `XPath` is the most well known tree query language, but it is only for xml. There is an equivalent `JsonPath` language, but it has no formal specs and many implementations. Also, it looks like json/javascript but it is not valid javascript. For this reason we adopt JsonPath but changing some symbols so it it remains valid Javascript. The expressions can be made parseable by using <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Proxy" target="_blank">ES6 Proxies</a>. 

todo write more.

| JsonPath                  |TraceQuery| Description                                                        ||
| :------------------------ | |:----------------------------------------------------------------- |
| `$`                       | `S` |The root element to query. This starts all query expressions.       |
| `@`                       | `NODE`|The current node being processed by a filter predicate.            |
| `*`                       | `ALL`|Wildcard. Available anywhere a name or numeric are required.       |
| `..`                      | `.DEEP.`|Deep scan. Available anywhere a name is required.                  |
| `.<name>`                 |  `.<name>`|Dot-notated child                                                  |
| `['<name>' (, '<name>')]` | better not use comma (although with some wizardry we could use Javascript comma operator)|Bracket-notated child or children                                  |
| `[<number> (, <number>)]` |  better not use comma (although with some wizardry we could use Javascript comma operator)|Array index or indexes                                             |
| `[start:end]`             | `[SLICE(start,end)]`|Array slice operator                                               |
| `[?(<expression>)]`       |`[FILTER(<expression>)]` |Filter expression. Expression must evaluate to a boolean value.    |

### Modellings CSVs

  Suppose we have an original CSV table like this:
  
  ```
       h1,h2
       aa,ab
       ba,bb
  ```
  
  its JSON view format is supposed to be like this:
 
  ```json
   [
       ["h1","h2"],
       ["aa","ab"],
       ["ba","bb"]
   ]
  ```
  <p>
  We don't use an array of records as original header names may be empty or
  duplicated. Thus cell `ba` can be pinpointed with the TracePath expression
  `S[2][0]` (preferred) or `2.0` . First one is preferred as it is clearer and
  closer in syntax to Javascript<br/>
  The first column can be selected with JsonPath expression `S[ALL][0]` (preferred)
  or `ALL.0`
  </p>
  
  Once CSV is correctly loaded and transformed in a proper common tree
  representation, then we can afford to have a more user friendly version with
  records like this:

```json
       [
           {
               "h1":"aa",
               "h2":"ab"
           },
           {
               "h1":"ba",
               "h2":"bb"
           }
       ]
```

  First column can be selected with TracePath `S[ALL].h1` (preferred) or `ALL.h1`
  
