package eu.trentorise.opendata.traceprov.tracel;

public abstract class TraceExpr {
    /**
     * Returns the textual representation of the expression, i.e.  "myProp.myArray[5].doSomething()"
     */
    public abstract String toText();
}
