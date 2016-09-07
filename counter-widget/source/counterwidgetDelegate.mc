using Toybox.WatchUi as Ui;
using Toybox.System as Sys;
using Toybox.Communications as Comm;

var last_key = null;

class CommListener extends Comm.ConnectionListener {
    function initialize() { 
        ConnectionListener.initialize();       
    }

    function onComplete() {
        Sys.println( "Transmit Complete" );
    }

    function onError() {
        Sys.println( "Transmit Failed" );
    }
}

class counterwidgetDelegate extends Ui.BehaviorDelegate {
    function initialize() { 
        BehaviorDelegate.initialize();       
    }

    function onNextPage() {
        var listener = new CommListener();
		counter++;
        
		try {
		   Comm.transmit(counter, null, listener); 
		}
		catch( ex instanceof Comm.ServiceUnavailableException ) {
			Sys.println(ex);
		}
		finally {
		   Sys.println("DOWN...");
		}        
                   
        return false;
    }

    function onPreviousPage() {
        var listener = new CommListener();
		counter--;
		
		try {
		   Comm.transmit(counter, null, listener); 
		}
		catch( ex ) {
		
		}
		finally {
	        Sys.println("UP...");       
		} 
        return false;
    }
    
    function onSelect() {
        var listener = new CommListener();
		counter = 0;
		
		try {
		   Comm.transmit(counter, null, listener); 
		}
		catch( ex ) {
		
		}
		finally {
	        Sys.println("Enter...");       
		} 
		           
        return false;
    }

    function onKey(evt) {
        var key = evt.getKey();

        if( key != null ) {
            Ui.requestUpdate();
        }

        if (key == KEY_ESC) {
            if (last_key == KEY_ESC) {
                Sys.exit();
            }
        }

        last_key = key;
        return true;
    }
}