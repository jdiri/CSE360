package application;

import java.text.DecimalFormat;
import javafx.scene.Group;


public class BuyerView {
	private Group buyerGroup;
	private DecimalFormat df;
	
	public BuyerView() {
        df = new DecimalFormat("####0.00");
        buyerGroup = new Group();
        initializeBuyerView();
    }
	
	private void initializeBuyerView() {
		
	}
	
	public Group getBuyerGroup() {
        return buyerGroup;
    }
}
