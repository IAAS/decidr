function ToolBar(){
    ToolPalette.call(this, "ToolBar");
    
    this.buttonStart = new FlowStartNode(this);
	this.buttonStart.setDimension(24, 24);
    this.buttonStart.setPosition(5, 5);
    this.addChild(this.buttonStart);
    
    this.buttonNode = new FlowNode(this);
	this.buttonNode.setDimension(24, 24);
    this.buttonNode.setPosition(35, 5);
    this.addChild(this.buttonNode);
	

	this.buttonEnd = new FlowEndNode(this);
	this.buttonEnd.setDimension(24, 24);
    this.buttonEnd.setPosition(65, 5);
    this.addChild(this.buttonEnd);

}

ToolBar.prototype = new ToolPalette();
