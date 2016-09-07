using Toybox.Application as App;

var counter;

class counterwidgetApp extends App.AppBase {

    function initialize() {
        AppBase.initialize();
    }

    // onStart() is called on application start up
    function onStart(state) {
        counter = 0; 
    }

    // onStop() is called when your application is exiting
    function onStop(state) {
    }

    // Return the initial view of your application here
    function getInitialView() {
        return [ new counterwidgetView(), new counterwidgetDelegate() ];
    }
}