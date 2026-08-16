package oracle.retail.sim.client.application;

import java.awt.GridBagLayout;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.client.swing.widget.RTextArea;
import oracle.retail.sim.common.configutil.VersionManager;

/********************************************************************************************************
 * About Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AboutPanel extends RPanel {
    private static final long serialVersionUID = -8698815727238667514L;

    private static String ABOUT_PRODUCT = "ABOUT_PRODUCT";
    private static String ABOUT_COPYRIGHT = "ABOUT_COPYRIGHT";
    private static String ABOUT_LICENSE_1 = "ABOUT_LICENSE_1";
    private static String ABOUT_LICENSE_2 = "ABOUT_LICENSE_2";
    private static String ABOUT_GOVERNMENT_1 = "ABOUT_GOVERNMENT_1";
    private static String ABOUT_GOVERNMENT_RIGHTS = "ABOUT_GOVERNMENT_RIGHTS";
    private static String ABOUT_GOVERNMENT_RIGHTS_TITLE = "ABOUT_GOVERNMENT_RIGHTS_TITLE";
    private static String ABOUT_HAZARDS_1 = "ABOUT_HAZARDS_1";
    private static String ABOUT_HAZARDS_2 = "ABOUT_HAZARDS_2";
    private static String ABOUT_THIRD_PARTY_1 = "ABOUT_THIRD_PARTY_1";
    private static String ABOUT_THIRD_PARTY_2 = "ABOUT_THIRD_PARTY_2";
    private static String ABOUT_TRADEMARK = "ABOUT_TRADEMARK";

    private RTextArea productArea = new RTextArea();
    private RTextArea copyrightArea = new RTextArea();
    private RTextArea commentArea = new RTextArea();
    private RScrollPane commentPane = new RScrollPane(commentArea);
    private RTextArea trademarkArea = new RTextArea();

    public AboutPanel() {
        initTextArea(productArea);
        initTextArea(copyrightArea);
        initTextArea(commentArea);
        initTextArea(trademarkArea);

        setLayout(new GridBagLayout());
        setEmptyBorder(5);
        add(productArea, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 10, 0));
        add(copyrightArea, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 10, 0));
        add(commentPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 10, 0));
        add(trademarkArea, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 3, 0, 0, 10, 0));
    }

    private void initTextArea(RTextArea textArea) {
        textArea.setFont(UIManager.getFont(UIThemeName.THEME_LARGE_BOLD_FONT));
        textArea.setDisabledColorSchemeActive(false);
    }

    public void loadAboutInformation() {
        productArea.setText(Translator.getText(ABOUT_PRODUCT) + " " + VersionManager.getVersion(true, true));
        copyrightArea.setText(Translator.getText(ABOUT_COPYRIGHT));
        commentArea.setText(Translator.getText(ABOUT_LICENSE_1));
        commentArea.append("\n\n");
        commentArea.append(Translator.getText(ABOUT_LICENSE_2));
        commentArea.append("\n\n");
        commentArea.append(Translator.getText(ABOUT_GOVERNMENT_1));
        commentArea.append("\n\n");
        commentArea.append(Translator.getText(ABOUT_GOVERNMENT_RIGHTS_TITLE));
        commentArea.append("\n\n");
        commentArea.append(Translator.getText(ABOUT_GOVERNMENT_RIGHTS));
        commentArea.append("\n\n");
        commentArea.append(Translator.getText(ABOUT_HAZARDS_1));
        commentArea.append("\n\n");
        commentArea.append(Translator.getText(ABOUT_HAZARDS_2));
        commentArea.append("\n\n");
        commentArea.append(Translator.getText(ABOUT_THIRD_PARTY_1));
        commentArea.append("\n\n");
        commentArea.append(Translator.getText(ABOUT_THIRD_PARTY_2));
        trademarkArea.setText(Translator.getText(ABOUT_TRADEMARK));
    }
}
