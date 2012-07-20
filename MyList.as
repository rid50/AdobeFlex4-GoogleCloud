package net.insolutions.rtquote.components
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import mx.events.FlexEvent;
	
	import spark.components.Group;
	import spark.components.List;
	import spark.components.supportClasses.ItemRenderer;
	import spark.events.ListEvent;

	import net.insolutions.rtquote.components.DeleteButton;
	import net.insolutions.rtquote.events.MyCustomEvent;
	
	
	//import net.insolutions.rtquote.collections.VehicleList;
	
	[Event(name="removeEntity", type="net.insolutions.rtquote.events.MyCustomEvent")] 
	
	public class MyList extends List
	{
		public var heightInLines:int = 0;
		
		public function MyList() 
		{
			super();

			addEventListener(spark.events.IndexChangeEvent.CHANGING, aboutToChange);
			addEventListener(mx.events.FlexEvent.UPDATE_COMPLETE, updateComplete);
		
			this.visible = false;
		}

		
		private function updateComplete(event:Event):void {
			if (this.visible)
				return;
			
			var renderer:ItemRenderer = dataGroup.getElementAt(0) as ItemRenderer;
			if (renderer) {
				this.height = 
					heightInLines * renderer.height + (heightInLines - 1) * 5 + 10;
				this.visible = true;
			}
		}
		
		private function recursiveSearchForCloseButton(group:DisplayObject):Boolean {
			var obj:*;
			for(var i:int = 0; i < Group(group).numChildren; i++)
			{
				obj = Group(group).getChildAt(i);
//				trace("  recurse " + obj);
				
				if (obj is Group) {
//					trace("  recurse2 " + obj);
					if (recursiveSearchForCloseButton(obj))
						return true;
					
				} else {
					if (obj is DeleteButton && DeleteButton(obj).skin.currentState == "over") {
						return true;
					}
				}
			}
			
			return false;
		}

		private function aboutToChange(e:Event):void {

			var group:DisplayObject;
			
			for (var i:int = 0; i < this.dataGroup.numElements; i++) {
				var renderer:ItemRenderer = dataGroup.getElementAt(i) as ItemRenderer;
				if (renderer) {
					for (var j:int = 0; j < renderer.numChildren; j++) {
						group = renderer.getChildAt(j);
						if (group is Group) {
//							trace("recurse " + group);
							if (recursiveSearchForCloseButton(group)) {
								e.preventDefault();
								break;
							}
						}
					}
				}
			}
		}
		
		private function checkEvent(e:Event):void {
			trace("Alert!\n" + "Current Target: " + e.currentTarget + "\n Target:" + e.target +
				"\n Phase: " + e.eventPhase);
			trace("Event properties - Bubbles: "  + e.bubbles + "\n Cancelable: " + e.cancelable +
				"\n Type: " + e.type);
		}
	}
}