package de.decidr.ui.view.client.ui;



import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;


public class VModelingTool extends de.decidr.modelingtool.client.ModelingToolWidget implements Paintable {

    /** Set the tagname used to statically resolve widget from UIDL. */
    public static final String TAGNAME = "modelingtool";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-" + TAGNAME;

    /** Component identifier in UIDL communications. */
    String uidlId;

    /** Reference to the server connection object. */
    ApplicationConnection client;
    
    //private UIDirector uiDirector = UIDirector.getInstance();
    //private SiteFrame siteFrame = uiDirector.getTemplateView();

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VModelingTool() {
    	super();
        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first. Ensure correct implementation,
        // and let the containing layout manage caption, etc.
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the UIDL identifier for the component
        uidlId = uidl.getId();

        //Gets the table from the workflow models component and gets the selected
        //item from the table. From this item the workflow model id is extracted
        //and passed through to the init method. So the modeling tool can be loaded
        //with the adequate worklfow model.
        //WorkflowModelsComponent component = (WorkflowModelsComponent)siteFrame.getContent();
        //CurrentTenantModelTable table = component.getCurrentTenantTable();
        
        //Item item = table.getItem(table.getValue());
        //Long workflowModelId = (Long)item.getItemProperty("id").getValue();
        
        init(10L);
        
    }

}
