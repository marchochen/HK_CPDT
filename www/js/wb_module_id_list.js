function ArrayList() {
    this.length = 0;
    this.elements = [];
    
    this.add = function(element){
        if (element[0]!==undefined || element.elements!==undefined) {
            for (var n = 0; n < element.length; n++) {
				var targetElement = (element[n]!==undefined)?element[n]:element.get(n);
                this.elements[this.length] = targetElement;
                this.length++;
            }
        } else {
            this.elements[this.length] = element;
            this.length++;
        }
    }
    
    this.pop = function(){
        if (this.length == 0) {
            alert('ArrayIndexOutOfException');
            return;
        }
        this.elements[this.length - 1] = undefined;
        this.length--;
    }
    
    this.remove = function(index){
        if (this.length <= index) {
            alert('ArrayIndexOutOfException');
            return;
        }
        this.elements[index] = undefined;
        var n = -1;
        for (var i = 0; i < this.length; i++) {
            if (this.elements[i] !== undefined) {
                this.elements[++n] = this.elements[i];
            }
        }
        this.length--;
		this.elements[this.length] = undefined;
    }
    
    this.removeEntity = function(entity){
        var noFound = true;
        for (var n = 0; n < this.length; n++) {
            if (this.elements[n] == entity) {
                this.elements[n] = undefined;
                noFound = false;
                break;
            }
        }
        if (noFound) {
            alert(entity + " is not found in this List.");
            return;
        }
		
		var index = -1;
        for (var i = 0; i < this.length; i++) {
            if (this.elements[i] !== undefined) {
                this.elements[++index] = this.elements[i];
            }
        }
        this.length--;
		this.elements[this.length] = undefined;
    }
	
	this.replace = function(newElement, oldElement){
		var noFound = true;
		for(var n = 0; n<this.length; n++){
			if(this.elements[n] == oldElement){
				this.elements[n] = newElement;
				noFound = false;
				break;
			}
		}
		if(noFound){
			alert(oldElement + " is not found in this list");
		}
	}
    
    this.get = function(index){
        return this.elements[index];
    }
    
    this.size = function(){
        return this.length;
    }
    
}
