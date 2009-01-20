function FlowNode(parent){
    ToolGeneric.call(this, parent);
};
FlowNode.prototype = new ToolGeneric();
FlowNode.prototype.type = "FlowNode";
FlowNode.prototype.execute = function(x, y){
	
	/* Node wird erstellt */
    var node = new Node();
    node.setDimension(100, 60);
    node.setBackgroundColor(new Color(255, 255, 255));
    
	/* Input-Port, eingehende Kante muendet hier */
    var ip = new InputPort();
    ip.setWorkflow(this.palette.workflow);
    ip.setBackgroundColor(new Color(255, 0, 0));
    ip.setColor(null);
    node.addPort(ip, node.width / 2, 0);
    
	/* Output-Port, ausgehende Kante faengt hier an */
    var op = new OutputPort();
    op.setWorkflow(this.palette.workflow);
    op.setBackgroundColor(new Color(0, 255, 0));
    op.setColor(null);
	op.setId(node.getId);
    node.addPort(op, node.width / 2, node.height);
    
    this.palette.workflow.getCommandStack().execute(new CommandAdd(this.palette.workflow, node, x, y));
    ToolGeneric.prototype.execute.call(this, x, y);
};
