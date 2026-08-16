package oracle.retail.sim.client.swing.core;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutFocusTraversalPolicy;
import oracle.retail.sim.client.swing.panel.RPanel;

/******************************************************************************************
 * This class is the keyboard focus traversal policy for all applications. Any method of the
 * FocusTraversalPolicy that does not supply a component with its own code will then try
 * to use LayoutFocusTraversalPolicy to find the component. The default order of components
 * in a container is determined by getComponents() unless the container is a RPanel, then
 * it uses getFocusCycleComponents().
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class OracleFocusPolicy extends FocusTraversalPolicy {

    private LayoutFocusTraversalPolicy defaultPolicy = new LayoutFocusTraversalPolicy();
    private Component[] comparray;
    private Component[] temparray;
    private Component matchcomp;
    private int globalIndex = -1;

    /******************************************************************************************
     * Creates a new OracleFocusPolicy.
     ******************************************************************************************/
    public OracleFocusPolicy() {
    }

    /******************************************************************************************
     * Returns the default Component to focus. This Component will be the first to receive
     * focus when traversing down into a new focus traversal cycle rooted at focusCycleRoot.
     * <p>
     * NOTE: This method should be altered to scan the components in the buildFullCycle()
     * method and return a "default" component such as a default button if one is found.
     * <p>
     * @param focusCycleRoot The focus cycle root whose default Component is to be returned
     * @return The default Component in the traversal cycle when focusCycleRoot is the focus
     * 		cycle root, or null if no suitable Component can  be found.
     * @throws IllegalArgumentException If focusCycleRoot is null.
     ******************************************************************************************/
    public Component getDefaultComponent(Container focusCycleRoot) {
        return getFirstComponent(focusCycleRoot);
    }

    /******************************************************************************************
     * Returns the first Component in the traversal cycle. This method is used to determine the
     * next Component to focus when traversal wraps in the forward direction.
     * <p>
     * @param focusCycleRoot The focus cycle root whose first Component is to be returned.
     * @return The first Component in the traversal cycle when focusCycleRoot is the focus cycle
     * 		root, or null if no suitable Component can be found.
     * @throws IllegalArgumentException If focusCycleRoot is null.
     ******************************************************************************************/
    public Component getFirstComponent(Container focusCycleRoot) {
        if (focusCycleRoot == null) {
            throw new IllegalArgumentException("focusCycleRoot cannot be null!");
        }
        buildFullCycle(focusCycleRoot, null);
        if (comparray.length > 0) {
            return comparray[0];
        }
        return defaultPolicy.getFirstComponent(focusCycleRoot);
    }

    /******************************************************************************************
     * Returns the last Component in the traversal cycle. This method is used to determine the
     * next Component to focus when traversal wraps in the reverse direction.
     * <p>
     * @param focusCycleRoot The focus cycle root whose last Component is to be returned.
     * @return The last Component in the traversal cycle when focusCycleRoot is the focus cycle
     * 		root, or null if no suitable Component can be found.
     * @throws IllegalArgumentException If focusCycleRoot is null
     ******************************************************************************************/
    public Component getLastComponent(Container focusCycleRoot) {
        if (focusCycleRoot == null) {
            throw new IllegalArgumentException("focusCycleRoot cannot be null!");
        }
        buildFullCycle(focusCycleRoot, null);
        if (comparray.length > 0) {
            return comparray[comparray.length - 1];
        }
        return defaultPolicy.getLastComponent(focusCycleRoot);
    }

    /******************************************************************************************
     * Returns the Component that should receive the focus after component. focusCycleRoot must
     * be a focus cycle root of component.
     * <p>
     * @param focusCycleRoot A focus cycle root of component.
     * @param component A (possibly indirect) child of focusCycleRoot, or focusCycleRoot itself.
     * @return The Component that should receive the focus after aComponent, or null if no
     * 		suitable Component can be found.
     * @throws IllegalArgumentException If focusCycleRoot is not a focus cycle root of component,
     * 		 or if either focusCycleRoot or component is null.
     ******************************************************************************************/
    public Component getComponentAfter(Container focusCycleRoot, Component component) {
        if (focusCycleRoot == null || component == null) {
            throw new IllegalArgumentException("focusCycleRoot and component cannot be null");
        }

        buildFullCycle(focusCycleRoot, component);

        int length = comparray.length;
        if (length > 0 && globalIndex != -1) {
            if (globalIndex == length - 1) {
                return comparray[0];
            }
            return comparray[globalIndex + 1];
        }
        return defaultPolicy.getComponentAfter(focusCycleRoot, component);
    }

    /******************************************************************************************
     * Returns the Component that should receive the focus before component. focusCycleRoot
     * must be a focus cycle root of component.
     * <p>
     * @param focusCycleRoot A focus cycle root of component.
     * @param component A (possibly indirect) child of focusCycleRoot, or focusCycleRoot itself.
     * @return The Component that should receive the focus before component, or null if no
     * 		suitable Component can be found.
     * @throws IllegalArgumentException If focusCycleRoot is not a focus cycle root of component,
     * 		or if either focusCycleRoot or component is null.
     ******************************************************************************************/
    public Component getComponentBefore(Container focusCycleRoot, Component component) {
        if (focusCycleRoot == null || component == null) {
            throw new IllegalArgumentException("focusCycleRoot and component cannot be null");
        }

        buildFullCycle(focusCycleRoot, component);

        int length = comparray.length;
        if (length > 0 && globalIndex != -1) {
            if (globalIndex == 0) {
                return comparray[length - 1];
            }
            return comparray[globalIndex - 1];
        }
        return defaultPolicy.getComponentBefore(focusCycleRoot, component);
    }

    /******************************************************************************************
     * Builds a full cycle of components for the container, attempting to set the global index
     * of the component passed in.
     * <p>
     * @param container A container to build the global focus cycle for.
     * @param component A component to find in the cycle and set the global index.`
     ******************************************************************************************/
    protected void buildFullCycle(Container container, Component component) {
        comparray = new Component[0];
        matchcomp = component;
        globalIndex = -1;

        Component[] components = null;

        if (container instanceof RPanel) {
            components = ((RPanel) container).getFocusCycleComponents();
        } else {
            components = container.getComponents();
        }

        for (Component componentx : components) {
            processCycleComponent(componentx);
        }
    }

    /******************************************************************************************
     * Processes a component to determine if it should be added to the global cycle array.
     * JComboBox and JTable are automatically added to the global array. JScrollPane has its
     * view added to the global array. RPanels have their focus cycle components processed.
     * Containers have their components processed. The container's getComponents() method sorts
     * the components in the order they are added to the container.
     * <p>
     * @param component The component to process.
     ******************************************************************************************/
    protected void processCycleComponent(Component component) {
        if (component instanceof JComboBox) {
            addComponentToArray(component);
        } else if (component instanceof JList) {
            addComponentToArray(component);
        } else if (component instanceof JTable) {
            addComponentToArray(component);
        } else if (component instanceof JScrollPane) {
            processCycleComponent(((JScrollPane) component).getViewport().getView());
        } else if (component instanceof RPanel) {
            Component[] childArray = ((RPanel) component).getFocusCycleComponents();
            for (Component child : childArray) {
                processCycleComponent(child);
            }
        } else if (component instanceof Container) {
            Component[] childArray = ((Container) component).getComponents();
            if (childArray.length == 0) {
                addComponentToArray(component);
            }
            for (Component child : childArray) {
                processCycleComponent(child);
            }
        } else {
            addComponentToArray(component);
        }
    }

    /******************************************************************************************
     * Adds a component to the global component array. It only adds the component if it passes
     * the accept() test. If the component matches the matchcomp [global match component] the
     * global index is set. This speeds up the after() and before() methods.
     * <p>
     * @param component The component to add.
     ******************************************************************************************/
    protected void addComponentToArray(Component component) {
        if (accept(component)) {
            int length = comparray.length;
            temparray = new Component[length + 1];
            System.arraycopy(comparray, 0, temparray, 0, length);
            comparray = temparray;
            comparray[length] = component;

            if (component == matchcomp) {
                globalIndex = length;
            }
        }
    }

    /******************************************************************************************
     * Accepts or rejects a component. A component is rejected if it is null, not showing, not
     * visible, not displayable, not focusable or not enabled.
     * <p>
     * @param component The component to validate.
     * <p>
     * @return True if the component can receive focus, false if not.
     ******************************************************************************************/
    protected boolean accept(Component component) {
        if (component == null) {
            return false;
        }
        if (!(component.isShowing() && component.isVisible() && component.isDisplayable() && component.isFocusable() && component.isEnabled())) {
            return false;
        }

        // Verify that the Component is recursively enabled. Disabling a heavyweight Container disables its children,
        // whereas disabling a lightweight Container does not.
        if (!(component instanceof Window)) {
            for (Container container = component.getParent(); container != null; container = container.getParent()) {
                if (!(container.isEnabled() || container.isLightweight())) {
                    return false;
                }
                if (container instanceof Window) {
                    break;
                }
            }
        }
        return true;
    }
}
