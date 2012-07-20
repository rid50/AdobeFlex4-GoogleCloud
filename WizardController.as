package net.insolutions.rtquote.controllers
{
  import mx.core.FlexGlobals;

  import mx.containers.Accordion;
  import mx.core.UIComponent;
  import flash.events.Event;
  import mx.events.FlexEvent;
  //import mx.events.IndexChangedEvent;
  
  import net.insolutions.rtquote.events.*;
  import net.insolutions.rtquote.models.*;

  // This class forms the "controller" of the MVC
  // (Model-View-Controller) architecture used in
  // this application. Rather than have code in 
  // the application and in components, the MVC 
  // architecture relies on events
  // handled in the controller.
  // Extend UIComponent so this class has a reference 
  // to SystemManager, so all events that bubble can 
  // be handled.
  public class WizardController extends UIComponent{
    // The constructor uses this classes creationComplete event
    // to call the setupEventListeners method to register events
    // with the controller that allow it to "control" much of 
    // the application.
    public function WizardController(){
      addEventListener( FlexEvent.CREATION_COMPLETE, setupEventListeners );
    }
    // Add event listeners to the system manager so it can 
    // handle events of interest bubbling up from 
    // anywhere in the application.
    private function setupEventListeners( event:Event ):void{
      systemManager.addEventListener(WizardTitleChangeEvent.WIZARD_TITLE_CHANGE, wizardTitleChangeHandler);
      systemManager.addEventListener(WizardScreenChangeEvent.WIZARD_SCREEN_CHANGE, wizardScreenChangeHandler);
    }
    // This event changes the title of the Panel that 
    // encloses the ViewStack. If no argument is passed 
    // when dispatching the WizardTitleChangeEvent event,
    // then the title will be the default value of the 
    // model wizardTitleBase variable, otherwise make 
    // use of the ... (rest) parameter feature of
    // ActionScript to allow any number of variables 
    // to be passed, and they will be used to 
    // construct the Panel title, separating the title
    // "parts" by hyphens ( - ).
    private function wizardTitleChangeHandler(event:WizardTitleChangeEvent):void{
//trace("event.newTitleParts: " + event.newTitleParts);
	WizardModel.wizardTitle = WizardModel.wizardTitleBase;
      	for each(var titlePart:String in event.newTitleParts){
            WizardModel.wizardTitle = 
		WizardModel.getInstance().wizardScreenIndex +
		"  " + WizardModel.wizardTitle + " - " + titlePart;

//trace("WizardModel.wizardTitle: " + WizardModel.wizardTitle);
      	}
    }
    // This event controls the model wizardScreenIndex 
    // variable, which is used to indirectly affect when 
    // the user goes to the next or previous screeen,
    // hen the wizard resets, and when the Back and Next 
    // buttons are enabled. When on the first screen, 
    // the Back button should be disabled, and
    // when on the last screen, the Next button should 
    // be disabled.
    private function wizardScreenChangeHandler(event:WizardScreenChangeEvent):void{
	var accordion:Accordion = FlexGlobals.topLevelApplication.applicantListsBox.accordion;
	var currentState:String = FlexGlobals.topLevelApplication.screens.getChildAt(WizardModel.app.screens.selectedIndex).getElementAt(0).currentState;
	var wizardScreenIndex:int = WizardModel.getInstance().wizardScreenIndex;

//trace("State-: " + currentState + " --- " + String(WizardModel.getInstance().wizardScreenIndex));

      	if(event.mode == WizardScreenChangeEvent.WIZARD_SCREEN_BACK) {
  	    WizardModel.getInstance().wizardScreenIndex--;

	    if (currentState != null && currentState.substr(0, "accordion".length) == "accordion") {
	        if (accordion.selectedIndex > 0) {
  	      	    accordion.selectedIndex--;
	        }
	    }


//trace("accordion.selectedIndex" + " --- " + String(accordion.selectedIndex));
//trace("wizardScreenIndex" + " --- " + String(WizardModel.getInstance().wizardScreenIndex));
          
      	} else if(event.mode == WizardScreenChangeEvent.WIZARD_SCREEN_NEXT) {
  	    WizardModel.getInstance().wizardScreenIndex++;

	    if (currentState != null && currentState.substr(0, "accordion".length) == "accordion") {
	        if (accordion.selectedIndex < accordion.numChildren - 1) {
	      	    accordion.selectedIndex++;
	        }
	    }

//trace("accordion.selectedIndex" + " --- " + String(accordion.selectedIndex));
//trace("wizardScreenIndex" + " --- " + String(WizardModel.getInstance().wizardScreenIndex));

      	} else if(event.mode == WizardScreenChangeEvent.WIZARD_SCREEN_CANCEL) {
            WizardModel.getInstance().wizardScreenIndex = 0;
	    WizardModel.app.reset();
      	}
    }
  }
}