package eu.trentorise.opendata.traceprov.tracel;

public abstract class Token {
           
    /**     
     * Returns the textual representation of the expression, i.e.  "myProp.myArray[5].doSomething()"
     */     
    public abstract String toText();
    
}
