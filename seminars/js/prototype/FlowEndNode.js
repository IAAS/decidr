function FlowEndNode(parent){
    ToolGeneric.call(this, parent);
};
FlowEndNode.prototype = new ToolGeneric();
FlowEndNode.prototype.type = "FlowEndNode";
FlowEndNode.prototype.execute = function(x, y){
	
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
        
    this.palette.workflow.getCommandStack().execute(new CommandAdd(this.palette.workflow, node, x, y));
    ToolGeneric.prototype.execute.call(this, x, y);
};