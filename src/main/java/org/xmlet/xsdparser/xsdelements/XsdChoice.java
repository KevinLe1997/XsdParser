package org.xmlet.xsdparser.xsdelements;

import org.w3c.dom.Node;
import org.xmlet.xsdparser.core.XsdParserCore;
import org.xmlet.xsdparser.xsdelements.elementswrapper.ReferenceBase;
import org.xmlet.xsdparser.xsdelements.visitors.XsdAbstractElementVisitor;
import org.xmlet.xsdparser.xsdelements.visitors.XsdAnnotatedElementsVisitor;
import org.xmlet.xsdparser.xsdelements.visitors.XsdChoiceVisitor;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A class representing the xsd:choice element. Since it shares the same attributes as {@link XsdChoice} or
 * {@link XsdSequence} it extends {@link XsdMultipleElements}. For more information check {@link XsdMultipleElements}.
 *
 * @see <a href="https://www.w3schools.com/xml/el_choice.asp">xsd:choice element description and usage at w3c</a>
 */
public class XsdChoice extends XsdMultipleElements {

    public static final String XSD_TAG = "xsd:choice";
    public static final String XS_TAG = "xs:choice";

    /**
     * {@link XsdChoiceVisitor} instance which restricts the children elements to {@link XsdElement}, {@link XsdGroup},
     * {@link XsdChoice}, {@link XsdSequence}.
     * Can also have {@link XsdAnnotation} as children as per inheritance of {@link XsdAnnotatedElementsVisitor}.
     */
    private XsdChoiceVisitor visitor = new XsdChoiceVisitor(this);

    /**
     * Specifies the minimum number of times this element can occur in the parent element. The value can be any
     * number bigger or equal to 0. Default value is 1. This attribute cannot be used if the parent element is the
     * XsdSchema element.
     */
    private Integer minOccurs;

    /**
     * Specifies the maximum number of times this element can occur in the parent element. The value can be any
     * number bigger or equal to 0, or if you want to set no limit on the maximum number, use the value "unbounded".
     * Default value is 1. This attribute cannot be used if the parent element is the XsdSchema element.
     */
    private String maxOccurs;

    private XsdChoice(@NotNull XsdParserCore parser, @NotNull Map<String, String> attributesMap) {
        super(parser, attributesMap);

        this.minOccurs = AttributeValidations.validateNonNegativeInteger(XSD_TAG, MIN_OCCURS_TAG, attributesMap.getOrDefault(MIN_OCCURS_TAG, "1"));
        this.maxOccurs = AttributeValidations.maxOccursValidation(XSD_TAG, attributesMap.getOrDefault(MAX_OCCURS_TAG, "1"));
    }

    @Override
    public void accept(XsdAbstractElementVisitor visitorParam) {
        super.accept(visitorParam);
        visitorParam.visit(this);
    }

    @Override
    public XsdChoiceVisitor getVisitor() {
        return visitor;
    }

    public static ReferenceBase parse(@NotNull XsdParserCore parser, Node node){
        return xsdParseSkeleton(node, new XsdChoice(parser, convertNodeMap(node.getAttributes())));
    }

    @SuppressWarnings("unused")
    public Integer getMinOccurs() {
        return minOccurs;
    }

    @SuppressWarnings("unused")
    public String getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * @return The children elements that are of the type {@link XsdChoice}.
     */
    @SuppressWarnings("unused")
    public Stream<XsdChoice> getChildrenChoices(){
        return getElements()
                .stream()
                .filter(element -> element.getElement() instanceof XsdChoice)
                .map(element -> (XsdChoice) element.getElement());
    }

    /**
     * @return The children elements that are of the type {@link XsdElement}.
     */
    @SuppressWarnings("unused")
    public Stream<XsdElement> getChildrenElements(){
        return getElements()
                .stream()
                .filter(element -> element.getElement() instanceof XsdElement)
                .map(element -> (XsdElement) element.getElement());
    }

    /**
     * @return The children elements that are of the type {@link XsdSequence}.
     */
    @SuppressWarnings("unused")
    public Stream<XsdSequence> getChildrenSequences(){
        return getElements()
                .stream()
                .filter(element -> element.getElement() instanceof XsdSequence)
                .map(element -> (XsdSequence) element.getElement());
    }

    /**
     * @return The children elements that are of the type {@link XsdGroup}.
     */
    @SuppressWarnings("unused")
    public Stream<XsdGroup> getChildrenGroups(){
        return getElements()
                .stream()
                .filter(element -> element.getElement() instanceof XsdGroup)
                .map(element -> (XsdGroup) element.getElement());
    }
}
