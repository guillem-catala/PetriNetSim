/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package data;

import business.Arc;
import business.Global;
import business.InputArc;
import business.OutputArc;

import business.PetriNet;
import business.Place;
import business.Token;
import business.TokenSet;
import business.Transition;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import presentation.figures.AbstractArcFigure;
import presentation.figures.AbstractFigure;
import presentation.figures.NormalArcFigure;
import presentation.figures.PathPoint;
import presentation.figures.PlaceFigure;
import presentation.figures.TextFigure;
import presentation.figures.TransitionFigure;

/**
 *
 * @author Guillem
 */
public class FileManager {

    private Document dom;
    private Element pnml;

    /** Loads a Petri Net Model from a file*/
    public HashMap loadFile(File file) {
        HashMap figures = new HashMap();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        dom = null;
        Global.petriNet = new PetriNet();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dom = builder.parse(file);
        } catch (SAXException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }


        NamedNodeMap attributes;
        NodeList nodeList, childNodes;

        // Loading PetriNet name
        nodeList = dom.getElementsByTagName("name");
        if (nodeList.getLength() > 0) {
            Node component = nodeList.item(0);
            Global.petriNet.setLabel(component.getTextContent().trim());
        }
        // Loading imports
        nodeList = dom.getElementsByTagName("import");
        if (nodeList.getLength() > 0) {
            Node component = nodeList.item(0);
            Global.petriNet.setImportText(component.getTextContent().trim());
        }

        // Loading implements
        nodeList = dom.getElementsByTagName("implement");
        if (nodeList.getLength() > 0) {
            Node component = nodeList.item(0);
            Global.petriNet.setImplementText(component.getTextContent().trim());
        }

        // Loading declarations
        nodeList = dom.getElementsByTagName("declaration");
        if (nodeList.getLength() > 0) {
            Node component = nodeList.item(0);
            Global.petriNet.setDeclarationText(component.getTextContent().trim());
        }

        // Loading places
        nodeList = dom.getElementsByTagName("place");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node component = nodeList.item(i);
            Place place;
            attributes = component.getAttributes();
            place = new Place(attributes.getNamedItem("id").getNodeValue());
            Global.petriNet.addPlace(place);
            //get position
            Point2D position = new Point2D.Double(0, 0);
            Point2D offset = new Point2D.Double(0, 0);
            childNodes = component.getChildNodes();
            for (int k = 0; k < childNodes.getLength(); k++) {
                Node node = childNodes.item(k);
                if (node.getNodeName().equals("graphics")) {
                    NodeList positions = node.getChildNodes();
                    for (int l = 0; l < positions.getLength(); l++) {
                        node = positions.item(l);
                        if (node.getNodeName().equals("position")) {
                            attributes = node.getAttributes();
                            int x, y;
                            x = Integer.parseInt(attributes.getNamedItem("x").getNodeValue());
                            y = Integer.parseInt(attributes.getNamedItem("y").getNodeValue());
                            position = new Point2D.Double(x, y);
                        }
                    }
                } else if (node.getNodeName().equals("name")) {
                    NodeList positions = node.getChildNodes();
                    for (int l = 0; l < positions.getLength(); l++) {
                        node = positions.item(l);
                        if (node.getNodeName().equals("text")) {
                            place.setLabel(node.getTextContent());
                        } else if (node.getNodeName().equals("graphics")) {
                            positions = node.getChildNodes();
                            for (int m = 0; m < positions.getLength(); m++) {
                                node = positions.item(m);
                                if (node.getNodeName().equals("offset")) {
                                    attributes = node.getAttributes();
                                    int x, y;
                                    x = Integer.parseInt(attributes.getNamedItem("x").getNodeValue());
                                    y = Integer.parseInt(attributes.getNamedItem("y").getNodeValue());
                                    offset = new Point2D.Double(x, y);
                                }
                            }
                        }
                    }
                } else if (node.getNodeName().equals("initialMarking")) {
                    NodeList positions = node.getChildNodes();
                    for (int l = 0; l < positions.getLength(); l++) {
                        node = positions.item(l);
                        if (node.getNodeName().equals("text")) {
                            Token token;
                            token = new Token(null);
                            token.setInitialMarkingtExpression(node.getTextContent());
                            place.addToken(new TokenSet(token));

                        }
                    }
                } else if (node.getNodeName().equals("capacity")) {
                    NodeList positions = node.getChildNodes();
                    for (int l = 0; l < positions.getLength(); l++) {
                        node = positions.item(l);
                        if (node.getNodeName().equals("text")) {
                            place.setCapacity(Integer.parseInt(node.getTextContent()));
                        }
                    }
                }
            }
            PlaceFigure placeFigure = new PlaceFigure(place.getId(), position);
            figures.put(place.getId(), placeFigure);
            placeFigure.getLabel().setOffsetToParent(offset);
            placeFigure.getLabel().setRelativePosition(placeFigure.getPosition());
            figures.put(place.getId() + "label", placeFigure.getLabel());
        }

        // Loading transitions
        nodeList = dom.getElementsByTagName("transition");
        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node component = nodeList.item(i);
            Transition transition;
            attributes = component.getAttributes();
            transition = new Transition(attributes.getNamedItem("id").getNodeValue());
            Global.petriNet.addTransition(transition);
            // get position

            Point2D position = new Point2D.Double(0, 0);
            Point2D offset = new Point2D.Double(0, 0);
            childNodes = component.getChildNodes();
            for (int k = 0; k < childNodes.getLength(); k++) {
                Node node = childNodes.item(k);
                if (node.getNodeName().equals("graphics")) {
                    NodeList positions = node.getChildNodes();
                    for (int l = 0; l < positions.getLength(); l++) {
                        node = positions.item(l);
                        if (node.getNodeName().equals("position")) {
                            attributes = node.getAttributes();
                            int x, y;
                            x = Integer.parseInt(attributes.getNamedItem("x").getNodeValue());
                            y = Integer.parseInt(attributes.getNamedItem("y").getNodeValue());
                            position = new Point2D.Double(x, y);
                        }
                    }
                } else if (node.getNodeName().equals("name")) {

                    NodeList positions = node.getChildNodes();
                    for (int l = 0; l < positions.getLength(); l++) {
                        node = positions.item(l);
                        if (node.getNodeName().equals("text")) {
                            transition.setLabel(node.getTextContent());
                        } else if (node.getNodeName().equals("graphics")) {
                            positions = node.getChildNodes();
                            for (int m = 0; m < positions.getLength(); m++) {
                                node = positions.item(m);
                                if (node.getNodeName().equals("offset")) {
                                    attributes = node.getAttributes();
                                    int x, y;
                                    x = Integer.parseInt(attributes.getNamedItem("x").getNodeValue());
                                    y = Integer.parseInt(attributes.getNamedItem("y").getNodeValue());
                                    offset = new Point2D.Double(x, y);
                                }
                            }
                        }
                    }

                } else if (node.getNodeName().equals("guard")) {
                    transition.setGuardText(node.getTextContent().trim());
                }
            }

            TransitionFigure transitionFigure = new TransitionFigure(transition.getId(), position);
            figures.put(transition.getId(), transitionFigure);
            transitionFigure.getLabel().setOffsetToParent(offset);
            transitionFigure.getLabel().setRelativePosition(transitionFigure.getPosition());
            figures.put(transition.getId() + "label", transitionFigure.getLabel());
        }
        // Loading arcs
        nodeList = dom.getElementsByTagName("arc");
        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node component = nodeList.item(i);

            attributes = component.getAttributes();

            String id = attributes.getNamedItem("id").getNodeValue();
            String source = attributes.getNamedItem("source").getNodeValue();
            String target = attributes.getNamedItem("target").getNodeValue();
            NormalArcFigure arcFigure = new NormalArcFigure();
            arcFigure.setConnectionStart((AbstractFigure) figures.get(source));
            arcFigure.setConnectionEnd((AbstractFigure) figures.get(target));
            String expression = "";
            // get position
            Point2D position = new Point2D.Double(0, 0);
            childNodes = component.getChildNodes();
            for (int k = 0; k < childNodes.getLength(); k++) {
                Node node = childNodes.item(k);
                if (node.getNodeName().equals("graphics")) {
                    NodeList positions = node.getChildNodes();
                    for (int l = 0; l < positions.getLength(); l++) {
                        node = positions.item(l);
                        if (node.getNodeName().equals("position")) {
                            attributes = node.getAttributes();
                            int x, y;
                            x = Integer.parseInt(attributes.getNamedItem("x").getNodeValue());
                            y = Integer.parseInt(attributes.getNamedItem("y").getNodeValue());
                            position = new Point2D.Double(x, y);
                            arcFigure.addPoint(position);
                        }
                    }
                } else if (node.getNodeName().equals("expression")) {
                    expression = (node.getTextContent().trim());
                }
            }
            AbstractFigure start = arcFigure.getStartConnector();
            AbstractFigure end = arcFigure.getEndConnector();
            if (Global.petriNet.getNetElement(start.getElementId()) instanceof Place) {
                Place p = (Place) Global.petriNet.getNetElement(start.getElementId());
                Transition t = (Transition) Global.petriNet.getNetElement(end.getElementId());
                InputArc arc = new InputArc(id, p, t, expression);
                Global.petriNet.addInputArc(arc);
            } else {
                Place p = (Place) Global.petriNet.getNetElement(end.getElementId());
                Transition t = (Transition) Global.petriNet.getNetElement(start.getElementId());
                OutputArc arc = new OutputArc(id, p, t, expression);
                Global.petriNet.addOutputArc(arc);
            }
            arcFigure.setElementId(id);
            figures.put(id, arcFigure);
            Iterator it = arcFigure.getPoints().iterator();
            int k = 0;
            while (it.hasNext()) {
                PathPoint pathPoint = (PathPoint) it.next();
                if (k != 0 && k != arcFigure.getPoints().size() - 1) {
                    pathPoint.setElementId(arcFigure.getElementId() + "_pathpoint_" + k);
                    figures.put(pathPoint.getElementId(), pathPoint);
                }
                k++;
            }
        }
        return figures;
    }

    /** Generates the xml file with the Petri Net Model*/
    public void generateXML(HashMap figures, File file) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader;

        loader = factory.newDocumentBuilder();
        dom = loader.newDocument();

        pnml = dom.createElement("pnml");
        Element net = dom.createElement("net");
        net.setAttribute("id", Global.petriNet.getId());

        Element node;
        Element text;

        // Petri Net Label
        node = dom.createElement("name");
        text = dom.createElement("text");
        text.appendChild(dom.createTextNode(Global.petriNet.getLabel()));
        node.appendChild(text);
        net.appendChild(node);


        if (!Global.petriNet.getImportText().equals("")) {
            node = dom.createElement("import");
            text = dom.createElement("text");
            text.appendChild(dom.createTextNode(Global.petriNet.getImportText()));
            node.appendChild(text);
            net.appendChild(node);
        }

        if (!Global.petriNet.getImplementText().equals("")) {
            node = dom.createElement("implement");
            text = dom.createElement("text");
            text.appendChild(dom.createTextNode(Global.petriNet.getImplementText()));
            node.appendChild(text);
            net.appendChild(node);
        }


        if (!Global.petriNet.getDeclarationText().equals("")) {
            node = dom.createElement("declaration");
            text = dom.createElement("text");
            text.appendChild(dom.createTextNode(Global.petriNet.getDeclarationText()));
            node.appendChild(text);
            net.appendChild(node);
        }
        Iterator it = figures.values().iterator();
        while (it.hasNext()) {
            AbstractFigure figure = (AbstractFigure) it.next();

            if (figure instanceof PlaceFigure) {
                Element l = dom.createElement("place");
                Place place = (Place) Global.petriNet.getNetElement(figure.getElementId());
                l.setAttribute("id", place.getId());
                l.appendChild(createGraphic(figure));
                l.appendChild(saveLabel((TextFigure) figures.get(place.getId() + "label"), "name"));


                if (!place.getTokens().isEmpty()) {
                    Element m = dom.createElement("initialMarking");
                    Iterator it2 = place.getTokens().iterator();
                    while (it2.hasNext()) {
                        Token token = (Token) it2.next();
                        text = dom.createElement("text");
                        text.appendChild(dom.createTextNode(token.getInitialMarkingtExpression()));
                        m.appendChild(text);
                    }
                    l.appendChild(m);
                }
                net.appendChild(l);

                if (place.getCapacity() > 0) {
                    Element m = dom.createElement("capacity");
                    text = dom.createElement("text");
                    text.appendChild(dom.createTextNode("" + place.getCapacity()));
                    m.appendChild(text);
                    l.appendChild(m);
                }
                net.appendChild(l);


            } else if (figure instanceof TransitionFigure) {
                Element l = dom.createElement("transition");
                Transition transition = (Transition) Global.petriNet.getNetElement(figure.getElementId());
                l.setAttribute("id", transition.getId());
                l.appendChild(createGraphic(figure));
                l.appendChild(saveLabel((TextFigure) figures.get(transition.getId() + "label"), "name"));
                node = dom.createElement("guard");
                text = dom.createElement("text");
                text.appendChild(dom.createTextNode(transition.getGuardText()));
                node.appendChild(text);
                l.appendChild(node);
                net.appendChild(l);
            } else if (figure instanceof NormalArcFigure) {
                Element l = dom.createElement("arc");
                Arc arc = (Arc) Global.petriNet.getNetElement(figure.getElementId());
                l.setAttribute("id", arc.getId());
                if (arc instanceof InputArc) {
                    l.setAttribute("source", arc.getPlace().getId());
                    l.setAttribute("target", arc.getTransition().getId());
                    node = dom.createElement("expression");
                    text = dom.createElement("text");
                    text.appendChild(dom.createTextNode(((InputArc) arc).getEvaluateText()));
                    node.appendChild(text);
                    l.appendChild(node);

                    node = dom.createElement("expression");
                    text = dom.createElement("text");
                    text.appendChild(dom.createTextNode(((InputArc) arc).getExecuteText()));
                    node.appendChild(text);
                    l.appendChild(node);

                } else {
                    l.setAttribute("source", arc.getTransition().getId());
                    l.setAttribute("target", arc.getPlace().getId());
                    node = dom.createElement("expression");
                    text = dom.createElement("text");
                    text.appendChild(dom.createTextNode(((OutputArc) arc).getExecuteText()));
                    node.appendChild(text);
                    l.appendChild(node);
                }
                l.appendChild(createGraphic(figure));
                net.appendChild(l);
            }
        }

        pnml.appendChild(net);

        dom.appendChild(pnml);
        Source source = new DOMSource(dom);

        Result result = new StreamResult(file);
        Transformer tf = TransformerFactory.newInstance().newTransformer();

        tf.setOutputProperty(OutputKeys.INDENT, "yes");

        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                "2");
        tf.transform(source, result);

    }

    /** Creates a xml node to represent a common label */
    private Element saveLabel(TextFigure figure, String name) {
        Element node = dom.createElement(name);
        Element text = dom.createElement("text");
        text.appendChild(dom.createTextNode(figure.getTextLabel()));
        Element graphics = dom.createElement("graphics");
        Element offset = dom.createElement("offset");

        Point2D p = figure.getOffsetToParent();
        offset.setAttribute("x", "" + (int) p.getX());
        offset.setAttribute("y", "" + (int) p.getY());
        graphics.appendChild(offset);

        node.appendChild(text);
        node.appendChild(graphics);
        return node;
    }

    /** Creates a xml node to represent a common graphic figure */
    private Element createGraphic(AbstractFigure figure) {
        Element graphics = dom.createElement("graphics");

        if (figure instanceof AbstractArcFigure) {
            AbstractArcFigure arcFigure = (AbstractArcFigure) figure;
            Iterator it = arcFigure.getPoints().iterator();
            while (it.hasNext()) {
                PathPoint pathPoint = (PathPoint) it.next();
                Element position = dom.createElement("position");
                position.setAttribute("x", "" + (int) pathPoint.getPosition().getX());
                position.setAttribute("y", "" + (int) pathPoint.getPosition().getY());
                graphics.appendChild(position);
            }
        } else {
            Element position = dom.createElement("position");
            Point2D p = figure.getPosition();
            position.setAttribute("x", "" + (int) p.getX());
            position.setAttribute("y", "" + (int) p.getY());
            graphics.appendChild(position);
        }

        return graphics;
    }

    /** Saves the bufferedImage to the specified file given.*/
    public void savePNG(BufferedImage bufferedImage, File file) {
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
