XMLSerializer = function(){
};
XMLSerializer.prototype.type = "XMLSerializer";
XMLSerializer.prototype.toXML = function(document){
    var xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
    xml = xml + "<form>\n";
    
    /* alle Zeichenobjekte in eine ArrayList holen */
    var figures = document.getFigures();
    for (var i = 0; i < figures.getSize(); i++) {
    
        /* Jedes Zeichenobjekt einzeln ansprechen */
        var figure = figures.get(i);
        xml = xml + "<" + figure.type + " x=\"" + figure.getX() + "\" y=\"" + figure.getY() + "\" id=\"" + figure.getId() + "\">\n";
        
        /* Falls ein Objekte Ports hat, werden diese in eine ArrayList geholt*/
        if (figure.getPorts().getSize() > 0) {
            var ports = figure.getPorts();
            xml = xml + "<ports>\n"
            for (var j = 0; j < ports.getSize(); j++) {
                /* Auf Ports einzeln zugreifen*/
                var port = ports.get(j);
                /* Connections = Kanten ermitteln */
                if (port.getConnections().getSize() > 0) {
                    var connections = port.getConnections();
                    for (var k = 0; k < connections.getSize(); k++) {
                        var connection = connections.get(k);
                        xml = xml + "<" + port.type + " targetId=\"" + connection.getTarget().getParent().getId() + "\">\n";
                    }
                }
            }
            xml = xml + "</ports>\n";
        }
        
        xml = xml + "</" + figure.type + ">\n";
    }
    xml = xml + "</form>\n";
    return xml;
};
