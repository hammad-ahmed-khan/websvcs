package oracle.retail.sim.client.swing.layout;

import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;

/********************************************************************************************************
 * HierarchyConstraints
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public final class HierarchyConstraints implements Serializable, Cloneable {
    private static final long serialVersionUID = 7513122075783148598L;

    private Component parentNode;
    private int level = -1;
    private List<Component> children = new ArrayList<>();

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public HierarchyConstraints() {
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param parentNode The parent node.
     ***************************************************************************************************/
    public HierarchyConstraints(Component parentNode) {
        this.parentNode = parentNode;
    }

    /****************************************************************************************************
     * If parentNode is null, this constraint will be considered to be the root node and the level will
     * be ignored.
     * <p>
     * @param Component parentNode
     * @param int level
     ***************************************************************************************************/
    public HierarchyConstraints(Component parentNode, int level) {
        this.parentNode = parentNode;
        this.level = level;
    }

    /****************************************************************************************************
     * Gets the parent node component.
     * <p>
     * @return the parent node
     ***************************************************************************************************/
    public Component getParentNode() {
        return parentNode;
    }

    /****************************************************************************************************
     * Sets the parent node.
     * <p>
     * @param parentNode
     ***************************************************************************************************/
    public void setParentNode(Component parentNode) {
        this.parentNode = parentNode;
    }

    /****************************************************************************************************
     * @return <code>true</code> if constraint has no parent
     ***************************************************************************************************/
    public boolean isRootNode() {
        return parentNode == null;
    }

    /****************************************************************************************************
     ***************************************************************************************************/
    public void setLevel(int newLevel) {
        level = newLevel;
    }

    /****************************************************************************************************
     ***************************************************************************************************/
    public int getLevel() {
        return level;
    }

    /****************************************************************************************************
     ***************************************************************************************************/
    void addChild(Component child) {
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    /****************************************************************************************************
     ***************************************************************************************************/
    boolean removeChild(Component child) {
        return children.remove(child);
    }

    /****************************************************************************************************
     ***************************************************************************************************/
    Component[] getChildren() {
        return children.toArray(new Component[children.size()]);
    }

    /****************************************************************************************************
     ***************************************************************************************************/
    void clearChildren() {
        children.clear();
    }

    /****************************************************************************************************
     * @return <code>true</code> if constraint refers to same parent
     ***************************************************************************************************/
    public boolean equals(Object object) {
        if (object instanceof HierarchyConstraints) {
            HierarchyConstraints other = (HierarchyConstraints) object;
            if (other.getParentNode() == null) {
                return parentNode == null;
            }
            return other.getParentNode().equals(getParentNode());
        }
        return false;
    }

    /****************************************************************************************************
     * @return <code>true</code> if constraint refers to same parent
     ***************************************************************************************************/
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(parentNode).toHashCode();
    }

    /****************************************************************************************************
     * @return a clone of this object
     ***************************************************************************************************/
    public Object clone() {
        HierarchyConstraints clone = new HierarchyConstraints(parentNode);
        clone.setLevel(getLevel());
        clone.children = children;
        return clone;
    }
}
