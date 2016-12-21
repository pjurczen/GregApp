package greg.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.Button;

public class UIController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(UIController.class);

    @FXML
    private TextArea inputField;
    @FXML
    private TextArea outputField;
    @FXML
    private Button parseButton;

    @FXML
    private TextField outputField1;

    @FXML
    private TextField outputField2;

    @FXML
    private TextField outputField3;

    @FXML
    private TextField color;

    @FXML
    private TextField location;

    @FXML
    private TextField website;

    @FXML
    private Button cleanButton;

    @FXML
    public void parseText() {
        String TERMINATOR_STRING = "done";
        String SEPARATOR = ";";

        StringBuilder strBuilder = new StringBuilder();
        StrBuilder outputString = new StrBuilder();

        for (String line : inputField.getText().split("\\n")) {
            if (!TERMINATOR_STRING.equals(line)) {
                strBuilder.append(line.trim());
                strBuilder.append(SEPARATOR);
            } else {
                break;
            }
        }

        /* Declaring all needed variables */
        String name = null;
        String orderNumber = null;
        String option = null;
        String disc = null;
        String lip = null;
        String dimensions = null;
        String frontDim = null;
        String rearDim = null;
        String year = null;
        String make = null;
        String model = null;
        String color = this.color.getText();
        String dealer = null;
        String location = this.location.getText();
        String web = this.website.getText();

        /* Spliting whole line in 4 sections */

        String[] section = strBuilder.toString()
                .split(";WHEEL SPECIFICATIONS:;|;VEHICLE SPECIFICATIONS:;|;AUTHORIZED DEALER:;");
        section[0] = section[0].replace("ORDER NUMBER;", "");

        /* Working with 1st section Order number */

        String[] order = section[0].split(":");
        if (order[0].contains("Order #")) {
            orderNumber = order[1].trim();
        }

        /* Working with 2nd section Wheel Information */

        String[] wheelData = section[1]
                .split(";"); /* Splitting section into data */

        name = wheelData[0]; /* Name is ALWAYS provided first */

        /*
         * Next for-loop goes through rest of Wheel section data and collects
         * data
         */

        for (int i = 1; i < wheelData.length; i++) {
            String[] fragment = wheelData[i].split(":");
            if (fragment.length == 1) {
                if (!fragment[0].contains("|")) {
                    option = fragment[0].trim();
                } else {
                    dimensions = fragment[0].trim();
                }
            } else if (fragment.length == 2) {
                if (fragment[0].toString().equals("Finish Disc")) {
                    disc = fragment[1].trim();
                }
                if (fragment[0].toString().equals("Finish Lip")) {
                    lip = fragment[1].trim();
                }

            } else {
                outputField.setText("Something went wrong");
                return;
            }
        }

        /* Working with 3rd section Vehicle information */

        String[] vehicleData = section[2].split(";");

        for (int i = 0; i < vehicleData.length; i++) {
            String[] fragment = vehicleData[i].split(":");
            if (fragment[0].contains("Vehicle Year")) {
                year = fragment[1].trim();
            } else if (fragment[0].contains("Make")) {
                make = fragment[1].trim();
            } else if (fragment[0].contains("Model")) {
                model = fragment[1].trim();
            }
        }

        /* Working with 4th section Dealer information */

        String[] dealerData = section[3].split(":");
        if (dealerData[0].contains("Dealer")) {
            dealer = dealerData[1].trim().replace(";", "");
        }

        /* Separating dimensions in front and rear dimension */

        String[] dimension = dimensions.split(" \\| ");
        frontDim = dimension[0].trim();
        rearDim = dimension[1].trim();

        /* Printing out everything in correct format */
        outputString.appendln(
                "<div class=\"adv__cols adv__cols--3\">\n<div class=\"adv__cols--col\">\n<div class=\"gallery-item-font\"><strong><span style=\"color: #333333;\">WHEEL SPECIFICATIONS</span></strong></div>");

        outputString.appendln(
                "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Wheels:</span></strong> "
                        + name + "</div>");
        outputString.appendln(
                "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Sizes:</span></strong> "
                        + frontDim + " Front <span style=\"color: #e41111;\">|</span> " + rearDim + " Rear</div>");

        if (disc != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><span style=\"color: #e41111;\"><strong>Finish Disc:</strong></span> "
                            + disc + "</div>");
        }

        if (lip != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><span style=\"color: #e41111;\"><strong>Finish Lips:</strong></span> "
                            + lip + "</div>");
        }

        if (option != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><span style=\"color: #e41111;\"><strong>Hardware Option:</strong></span> "
                            + option + "</div>");
        }

        outputString.appendln("</div>\n" + "<div class=\"adv__cols--col\">\n"
                + "<div class=\"gallery-item-font\"><strong><span style=\"color: #333333;\">VEHICLE SPECIFICATIONS</span></strong></div>");

        if (year != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Vehicle Year:</span></strong> "
                            + year + "</div>");
        }

        if (make != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Vehicle Make:</span></strong> "
                            + make + "</div>");
        }

        if (model != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Vehicle Model:</span></strong> "
                            + model + "</div>");
        }

        if (color != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Vehicle Color:</span></strong> "
                            + color + "</div>");
        }

        outputString.appendln("</div>\n" + "<div class=\"adv__cols--col\">\n"
                + "<div class=\"gallery-item-font\"><span style=\"color: #333333;\"><strong>AUTHORIZED DEALERSHIP INFORMATION</strong></span></div>");

        if (orderNumber != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Order Number:</span></strong> "
                            + orderNumber + "</div>");
        }

        if (dealer != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Dealer name:</span></strong> "
                            + dealer + "</div>");
        }

        if (location != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Dealer location:</span></strong> "
                            + location + "</div>");
        }

        if (web != null) {
            outputString.appendln(
                    "<div class=\"gallery-item-font\"><strong><span style=\"color: #e41111;\">Website:</span></strong> <a href=\"http://"
                            + web + "\" rel=\"nofollow\">" + web + "</a></div>");
        }

        outputString.appendln("</div>\n" + "</div>\n"
                + "<div class=\"quote-form-gallery\">[contact-form-7 id=\"25680\" title=\"Portfolio Pricing\"]</div>\n"
                + "\n" + "[contact-form-7 id=\"25680\" title=\"Portfolio Pricing\"]");

        outputField.setText(outputString.toString());
        outputField1.setText("Title of post: " + "<b>" + make + "-Benz " + model + " AMG</b> - " + name + " Wheels");
        outputField2.setText("Title of image: " + make + "-Benz " + model + " AMG - " + name + " Wheels");
        outputField3
                .setText("Alt of image: " + color + " " + make + "-Benz " + model + " AMG with " + name + " Wheels");
    }

    @FXML
    public void cleanTexts() {
        inputField.clear();
        outputField.clear();
        outputField1.clear();
        outputField2.clear();
        outputField3.clear();
        color.clear();
        location.clear();
        website.clear();
    }

    public void initialize(URL location, ResourceBundle resources) {
        parseButton.disableProperty().bind(inputField.textProperty().isEmpty().or(color.textProperty().isEmpty()
                .or(this.location.textProperty().isEmpty().or(website.textProperty().isEmpty()))));

    }

}
