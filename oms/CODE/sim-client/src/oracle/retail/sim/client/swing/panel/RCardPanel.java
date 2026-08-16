package oracle.retail.sim.client.swing.panel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.SwingUtilities;
import oracle.retail.sim.client.locale.StringUtility;

/********************************************************************************************************
 * RCardPanel is a substitute for a standard panel with a CardLayout manager. It is simpler to use and
 * has alternate methods for calculating its preferred size based on all of the "cards" that it contains.
 * A "Card Panel contains a list of components where only the currently selected component (often a
 * panel) is displayed and all the other components (or "cards") remain hidden.
 * <p>
 * Use the <code>addCard</code> method to add a card (Component) to the panel. Use the
 * <code>showCard</code> method to make a card visible. Cards are ordered by the sequence they are
 * added to the panel. nextCard and previousCard work based on this.
 * <p>
 * Do not use the normal Container add() methods with this component!
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RCardPanel extends RPanel {
    private static final long serialVersionUID = 7441199874470693963L;

    private String[] nameArray = new String[0];
    private Component[] cardArray = new Component[0];
    private int currentIndex = -1;

    private static final String METHOD_UNAVAILABLE = "Method is unavilable. Please use addCard()";

    /****************************************************************************************************
     * Creates a CardPanel
     ***************************************************************************************************/
    public RCardPanel() {
        setLayout(new RCardLayout());
        setOpaque(false);
    }

    /****************************************************************************************************
     * Augments superclass to update the UI for any cards that are not currently parented
     ***************************************************************************************************/
    public void updateUI() {
        super.updateUI();

        if (cardArray != null) {
            int size = cardArray.length;

            Component currentCard = null;
            if (currentIndex != -1) {
                currentCard = cardArray[currentIndex];
            }
            for (int i = 0; i < size; i++) {
                if (cardArray[i] != currentCard) {
                    SwingUtilities.updateComponentTreeUI(cardArray[i]);
                }
            }
            revalidate();
        }
    }

    /****************************************************************************************************
     * Retrieves the currently displayed card.
     * <p>
     * @return The currently displayed card (or null if none is displayed).
     ***************************************************************************************************/
    public Component getCard() {
        if (currentIndex < 0 || currentIndex >= cardArray.length) {
            return null;
        }
        return cardArray[currentIndex];
    }

    /****************************************************************************************************
     * Adds the specified card (Component) to the list of cards.
     * <p>
     * @param card A component.
     ***************************************************************************************************/
    public void addCard(Component card) {
        String name = card.getName();

        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Component must contain a name.");
        }
        for (Component element : cardArray) {
            if (element == card) {
                throw new IllegalArgumentException("Component has already been added to panel.");
            }
        }
        addCard(name, card);
    }

    /****************************************************************************************************
     * Adds the specified card (Component) to the list of cards. If the name matches a previous name,
     * then the card (Component) will replace the previous component of the same name. Cards with
     * original names are added to the end of the card sequence.
     * <p>
     * @param name A name identifier for the component.
     * @param card A component.
     ***************************************************************************************************/
    public void addCard(String name, Component card) {
        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (card == null) {
            throw new IllegalArgumentException("Component cannot be null.");
        }
        for (Component element : cardArray) {
            if (element == card) {
                throw new IllegalArgumentException("Component has already been added to panel.");
            }
        }
        for (int i = 0; i < nameArray.length; i++) {
            if (nameArray[i].equals(name)) {
                cardArray[i] = card;
                if (currentIndex == i) {
                    showCard(i);
                }
                return;
            }
        }

        String[] tempNameArray = new String[nameArray.length + 1];
        Component[] tempCardArray = new Component[cardArray.length + 1];

        System.arraycopy(nameArray, 0, tempNameArray, 0, nameArray.length);
        System.arraycopy(cardArray, 0, tempCardArray, 0, cardArray.length);

        nameArray = tempNameArray;
        cardArray = tempCardArray;

        nameArray[nameArray.length - 1] = name;
        cardArray[cardArray.length - 1] = card;

        if (cardArray.length == 1) {
            super.add(card, name);
            revalidate();
            repaint();
            currentIndex = 0;
        }
    }

    /****************************************************************************************************
     * Removes the card (Component) from the list of cards. If this card is currently showing, the
     * previous card in the list is displayed.
     * <p>
     * @param card The component to remove.
     ***************************************************************************************************/
    public void removeCard(Component card) {
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == card) {
                removeCard(i);
                break;
            }
        }
    }

    /****************************************************************************************************
     * Removes the card (Component) from the list of cards based on the name identifier of the card. If
     * this card is currently showing, the previous card in the list is displayed.
     * <p>
     * @param name The name of the component to remove.
     ***************************************************************************************************/
    public void removeCard(String name) {
        for (int i = 0; i < nameArray.length; i++) {
            if (nameArray[i].equals(name)) {
                removeCard(i);
                break;
            }
        }
    }

    /****************************************************************************************************
     * Removes the card (Component) from the list of cards based on the name identifier of the card. If
     * this card is currently showing, the previous card in the list is displayed.
     * <p>
     * @param card The component to remove.
     ***************************************************************************************************/
    protected void removeCard(int index) {
        if (index == currentIndex) {
            if (cardArray.length > 1) {
                showPreviousCard();
            } else {
                remove(cardArray[currentIndex]);
                currentIndex = -1;
            }
        }

        String[] tempNameArray = new String[nameArray.length - 1];

        for (int i = 0; i < nameArray.length; i++) {
            if (i != index) {
                tempNameArray[i++] = nameArray[i];
            }
        }

        Component[] tempCardArray = new Component[cardArray.length - 1];

        for (int i = 0; i < cardArray.length; i++) {
            if (i != index) {
                tempCardArray[i++] = cardArray[i];
            }
        }

        nameArray = tempNameArray;
        cardArray = tempCardArray;

        revalidate();
        repaint();
    }

    /****************************************************************************************************
     * Displays the specified card.
     * <p>
     * @param card A component to display.
     ***************************************************************************************************/
    public void showCard(Component card) {
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == card) {
                showCard(i);
                break;
            }
        }
    }

    /****************************************************************************************************
     * Displays the card with the specified name.
     * <p>
     * @param name The name of the card to display.
     ***************************************************************************************************/
    public void showCard(String name) {
        for (int i = 0; i < nameArray.length; i++) {
            if (nameArray[i].equals(name)) {
                showCard(i);
                break;
            }
        }
    }

    /****************************************************************************************************
     * Internal method for displaying a card by index.
     * <p>
     * @param index The index of the card to display.
     ***************************************************************************************************/
    protected void showCard(int index) {
        remove(cardArray[currentIndex]);
        super.add(cardArray[index], cardArray[index].getName());

        revalidate();
        repaint();

        currentIndex = index;
    }

    /****************************************************************************************************
     * Displays the card that was added to this CardPanel after the currently visible card. If the
     * currently visible card was added last, then displays the first card. show the first card.
     ***************************************************************************************************/
    public void showNextCard() {
        if (cardArray.length == 0) {
            return;
        }
        int index = currentIndex + 1;
        if (index == cardArray.length) {
            index = 0;
        }
        showCard(index);
    }

    /****************************************************************************************************
     * Displays the card that was added to this CardPanel before the currently visible card. If the
     * currently visible card was added first, then displays the last card.
     ***************************************************************************************************/
    public void showPreviousCard() {
        if (cardArray.length == 0) {
            return;
        }
        int index = currentIndex - 1;
        if (index < 0) {
            index = cardArray.length - 1;
        }
        showCard(index);
    }

    /****************************************************************************************************
     * Displays the oldest card that was added to this CardPanel.
     ***************************************************************************************************/
    public void showFirstCard() {
        if (cardArray.length == 0) {
            return;
        }
        showCard(0);
    }

    /****************************************************************************************************
     * Displays the most recent card that was added to this CardPanel.
     ***************************************************************************************************/
    public void showLastCard() {
        if (cardArray.length == 0) {
            return;
        }
        showCard(cardArray.length - 1);
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. This method has been overridden and
     * made unavailable.
     * <p>
     * @param component The component to be added.
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container. It is strongly advised to use the 1.1 method,
     * add(Component, Object), in place of this method. This method has been overridden and made
     * unavailable.
     * <p>
     * @param name A string name for the component.
     * @param component The component to be added.
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(String name, Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container at the given index. This method has been overridden
     * and made unavailable.
     * <p>
     * @param component The component to be added
     * @param index Position to add the component
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(Component component, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. This method has been overridden and
     * made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ***************************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container with the specified constraints at the specified
     * index. Also notifies the layout manager to add the component to the this container's layout using
     * the specified constraints object.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     * @param index The position in the container's list at which to insert the component. -1 means
     *            insert at the end.
     ***************************************************************************************************/
    public void add(Component component, Object constraints, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     *
     * INNER CLASS RCARDLAYOUT
     *
     ***************************************************************************************************/

    private class RCardLayout implements LayoutManager {

        /************************************************************************************************
         * Creates new RCardLayout.
         ***********************************************************************************************/
        public RCardLayout() {
        }

        /************************************************************************************************
         * Empty implementation of method.
         ***********************************************************************************************/
        public void addLayoutComponent(String name, Component child) {
        }

        /************************************************************************************************
         * Empty implementation of method.
         ***********************************************************************************************/
        public void removeLayoutComponent(Component child) {
        }

        /************************************************************************************************
         * Retrieves the preferredLayoutSize of the parent container. This will return the preferred size
         * of the largest preferred width and largest preferred height.
         * <p>
         * @param parent The parent container (cardPanel).
         * @return The maximum preferred width/height + the parents insets.
         ***********************************************************************************************/
        public Dimension preferredLayoutSize(Container parent) {
            Insets insets = parent.getInsets();
            int width = insets.left + insets.right;
            int height = insets.top + insets.bottom;

            Dimension dim;
            for (Component element : cardArray) {
                dim = element.getPreferredSize();
                if (dim.width > width) {
                    width = dim.width;
                }
                if (dim.height > height) {
                    height = dim.height;
                }
            }
            return new Dimension(width, height);
        }

        /************************************************************************************************
         * Retrieves the minimum layout size.
         * <p>
         * @param parent The parent container (cardPanel).
         * @return The maximum minimum width/height + the parents insets.
         ***********************************************************************************************/
        public Dimension minimumLayoutSize(Container parent) {
            Insets insets = parent.getInsets();
            int width = insets.left + insets.right;
            int height = insets.top + insets.bottom;

            Dimension dim;
            for (Component element : cardArray) {
                dim = element.getMinimumSize();
                if (dim.width > width) {
                    width = dim.width;
                }
                if (dim.height > height) {
                    height = dim.height;
                }
            }
            return new Dimension(width, height);
        }

        /************************************************************************************************
         * Lays out the container.
         * <p>
         * @param parent The parent container.
         ***********************************************************************************************/
        public void layoutContainer(Container parent) {
            if (parent.getComponentCount() > 0) {
                Component component = parent.getComponent(0);
                Rectangle rect = parent.getBounds();
                Insets insets = parent.getInsets();

                int width = rect.width - insets.left + insets.right;
                int height = rect.height - insets.top + insets.bottom;

                component.setBounds(insets.left, insets.top, width, height);
            }
        }
    }
}
