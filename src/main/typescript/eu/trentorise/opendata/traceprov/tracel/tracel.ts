
///<reference path='./node_modules/typescript/lib/typescriptServices' />

// ------------------------
// usual module garbage to make all Typescript, Visual Studio Code and Rhino happy

declare var require: any;

var ts;
if (!ts) {    
    ts = require("typescript");
}

var exports: any;
if (!exports) {    
    exports = {}
}

var tracel = exports;


// ------------------------

let scriptTarget = ts.ScriptTarget.ES5;

/*var content = 
    "import {f} from \"foo\"\n" +
    "export var x = f()";
*/
    
  
/* 
var content = 
    "a.c]"; 
*/

let compilerOptions = { module: ts.ModuleKind.System };


/**
 * to centralize the stupid 'A const enum can only ba accessed using a string literal' error
 * There is also a ts.tokenToString(SyntaxKind) but always return undefined :-/
 */
function kindToString(kind: ts.SyntaxKind): string {
    return ts.SyntaxKind[kind];
}


/**
 * Expresses provided obj as json. Cyclic properties by default are turned to null. 
 * 
 * @param replacer either the replace or a single value to use for replacing circular deps.
 * @param includeInherited if true inherited props will be included in the output. 
 * todo Warning: mildy modifies provided node by reporting variables in the prototype chain 
 * inside the object. Taken from http://stackoverflow.com/a/8779611
 
function toJson(x: any,
    replacer: (key: string, value: any) => any | any,
    includeInherited: boolean): string {

    let fixObj = function(obj) {
        for (var i in obj) {
            if (!obj.hasOwnProperty(i)) {
                // weird as it might seem, this actually does the trick! - adds parent property to self
                obj[i] = obj[i];
            }
        }
        if (obj && obj.kind) {
            obj.__kindLabel = kindToString(obj.kind);
        }
    }    
    
    // To prevent circular deps 
    let replacer = function(key, value) {
        if (key === "parent" || key === "_children" || key === "file") {
            return null;
        } else {
            fixObj(value);
            return value;
        }
    }

    if (includeInherited) {
        fixObj(x);
    }

    return JSON.stringify(x, replacer, 4);

}
*/

/**
 * Cyclic properties are turned to null. Also  
 * So we jsoninze the inherited node kind as well... 
 * 
 * warning: mildy modifies provided node by reporting variables in the prototype chain inside the object. 
 * 
 * Taken from http://stackoverflow.com/a/8779611
 */
function nodeToJson(x: ts.Node): string {

    let fixObj = function(obj) {
        for (var i in obj) {
            if (!obj.hasOwnProperty(i)) {
                // weird as it might seem, this actually does the trick! - adds parent property to self
                obj[i] = obj[i];
            }
        }
        if (obj && obj.kind) {
            obj.__kindLabel = kindToString(obj.kind);
        }
    }    
    
    /** To prevent circular deps */
    let replacer = function(key, value) {
        if (key === "parent" || key === "_children" || key === "file") {
            return null;
        } else {
            fixObj(value);
            return value;
        }
    }

    fixObj(x);
    return JSON.stringify(x, replacer, 4);
}

/*
var res1 = ts.transpile(
				content, 
				compilerOptions,  
				 undefined,  ///fileName 
				 undefined, // diagnostics 
				 "myModule1");  //moduleName
				
console.log(res1); 

console.log("============");
*/
/*
var res2 = ts.transpileModule(content, {compilerOptions: compilerOptions, moduleName: "myModule2"});
console.log(res2.outputText);
*/

// ts.SyntaxKind[kind]
// ts.syntaxKindToName(node.kind)

let a = {}
a["\""];


let internalTracel = {

    printAllChildren: function(sourceCode: string, node: any, depth = 0) {
        console.log(new Array(depth + 1).join('----'), kindToString(node.kind), node.pos, node.end, "-> ",
            sourceCode.substr(node.pos, node.end - node.pos));
        depth++;
        node.getChildren().forEach(c=> internalTracel.printAllChildren(c, depth));
    }
}


/* 
var sourceCode = `
var foo = 123;
`.trim();
*/



// let sourceFile = ts.createSourceFile('foo.ts', sourceCode, scriptTarget, true);
//printAllChildren(sourceFile);


function checkKind(expected: ts.SyntaxKind, actual: ts.SyntaxKind) {
    if (expected !== actual) {

        throw new Error("Expected " + ts.tokenToString(expected) + ", found instead " + kindToString(actual));
    }
}

function checkLength(expectedNumber: number, actualChildren: ts.Node[]) {

    if (!actualChildren) {
        throw new Error("Discovered null/undefined children! Found: " + actualChildren);
    }

    if (expectedNumber !== actualChildren.length) {

        throw new Error("Expected exactly " + expectedNumber + ", found instead " + actualChildren.length
            + " nodes. Nodes are: " + actualChildren.forEach((c) => nodeToJson(c)));
    }
}

function getText(text: string, node: ts.Node) {
    return text.substring(node.pos, node.end - node.pos);

}


// *********************************************************
//                  STUFF TO *REALLY* EXPORT
// *********************************************************


    
/**
* Parses the provided string which must be a valid Javascript property path, 
* starting with an identifier and followed by any number of accessor
* <pre>
    * Valid paths: 
    * a.b
    * a["b"].c
    * a[b][c] 
    * a["b c"].d
    * a[3]
    * b["3"]
    * c["3.4"]
    * 
    * Invalid:
    * "a"
    * a[f(b)]
    * [c]
    * f(a)[b]
* </pre>
*/
exports.parsePropertyPath = function(text: string): string[] {
    if (!text) {
        throw new Error("Provided text must not be empty, found instead " + text);
    }
    let ret = [];
    let sf = ts.createSourceFile('foo.ts', text, scriptTarget, false);

    let parseDiagnostics = (<any>sf).parseDiagnostics;
    if (parseDiagnostics.length > 0) {
        throw new Error("Found parse errors! " + nodeToJson(parseDiagnostics));
    }

    let curNode: ts.Node;
    try {
        curNode = sf;

        checkKind(ts.SyntaxKind.SourceFile, curNode.kind);
        let ch = curNode.getChildren();
        checkLength(2, ch);

        curNode = ch[0];
        checkKind(ts.SyntaxKind.SyntaxList, curNode.kind);
        ch = curNode.getChildren();
        checkLength(1, ch);

        curNode = ch[0];
        checkKind(ts.SyntaxKind.ExpressionStatement, curNode.kind);

    } catch (e) {
        console.error("ERROR AT INTERVAL [" + curNode.pos + ","
            + curNode.end + ") with text -->" + getText(text, curNode) + "<--");
        throw new Error(e);
    }

    let curExpr = (<ts.ExpressionStatement>curNode).expression;

    while (curExpr) {
        try {
            if (!(ts.SyntaxKind.Identifier === curExpr.kind
                || ts.SyntaxKind.PropertyAccessExpression === curExpr.kind
                || ts.SyntaxKind.ElementAccessExpression === curExpr.kind
                || ts.SyntaxKind.FirstLiteralToken === curExpr.kind)) {
                throw new Error("Found invalid expression kind: " + kindToString(curExpr.kind));
            }

            if (ts.SyntaxKind.Identifier === curExpr.kind) {
                let id = <ts.Identifier>curExpr;
                ret.unshift(id.text);
                curExpr = null;
            } else if (ts.SyntaxKind.PropertyAccessExpression === curExpr.kind) {
                let propExpr = <ts.PropertyAccessExpression>curExpr;
                console.log("propExpr.name = ", propExpr.name);
                ret.unshift(propExpr.name.text);
                curExpr = propExpr.expression;
            } else if (ts.SyntaxKind.ElementAccessExpression === curExpr.kind) {
                let accExpr = <ts.ElementAccessExpression>curExpr;
                let argExpr = accExpr.argumentExpression;
                let text: string; 
                if (ts.SyntaxKind.Identifier === argExpr.kind) {
                    text = (<ts.Identifier>argExpr).text;
                } else if (ts.SyntaxKind.FirstLiteralToken === argExpr.kind) {
                    text = (<ts.LiteralExpression>argExpr).text;
                } else if (ts.SyntaxKind.StringLiteral === argExpr.kind) {
                    text = (<ts.StringLiteral>argExpr).text;
                } else {
                    throw new Error("Found invalid expression kind: " + kindToString(argExpr.kind));
                }
                ret.unshift(text);
                curExpr = accExpr.expression;
            } else {
                curExpr = null;
            }
        } catch (e) {
            console.error("ERROR AT INTERVAL [" + curExpr.pos + "," + curExpr.end + ") with text -->" + getText(text, curExpr) + "<--");
            throw (e);
        }
    }


    return ret;
}




// console.log("sourceFile = ", sourceFile);
// console.log("jsonized ast = ", nodeToJson(sourceFile));

// console.log("PropertyPath = ", tracel.parsePropertyPath(sourceCode));

