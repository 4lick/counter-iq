using Toybox.WatchUi as Ui;
using Toybox.Graphics as Gfx;
using Toybox.Communications as Comm;
using Toybox.System as Sys;

class counterwidgetView extends Ui.View {
    var screenShape;

    function initialize() { 
        View.initialize();       
        Comm.setMailboxListener(method(:onMail));
    }

    // Load your resources here
    function onLayout(dc) {
        setLayout(Rez.Layouts.MainLayout(dc));
        screenShape = Sys.getDeviceSettings().screenShape;
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() {
    }

    // Update the view
    function onUpdate(dc) {
        dc.setColor(Gfx.COLOR_TRANSPARENT, Gfx.COLOR_BLACK);
        dc.clear();
        dc.setColor(Gfx.COLOR_WHITE, Gfx.COLOR_TRANSPARENT);

        dc.drawText(dc.getWidth() / 2, 20,  Gfx.FONT_MEDIUM, "Counter", Gfx.TEXT_JUSTIFY_CENTER);
        dc.drawText(dc.getWidth() / 2, 50,  Gfx.FONT_SYSTEM_NUMBER_THAI_HOT, counter, Gfx.TEXT_JUSTIFY_CENTER);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() {
    }
    
    function onMail(mailIter) {
	    var mail;
	
	    mail = mailIter.next();
	
	    while (mail != null) {
	        counter = mail;
	        mail = mailIter.next();
	    }
	
	    Comm.emptyMailbox();
	    Ui.requestUpdate();
    }
}
