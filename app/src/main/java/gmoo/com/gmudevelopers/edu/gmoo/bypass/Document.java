package gmoo.com.gmudevelopers.edu.gmoo.bypass;

public class Document {

    Element[] elements;

    public Document(Element[] elements) {
        this.elements = elements;
    }

    public int getElementCount() {
        return elements.length;
    }

    public Element getElement(int pos) {
        return elements[pos];
    }
}
