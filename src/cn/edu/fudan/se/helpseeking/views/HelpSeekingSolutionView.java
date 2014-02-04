package cn.edu.fudan.se.helpseeking.views;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import cn.edu.fudan.se.helpseeking.web.SimpleBrower;

public class HelpSeekingSolutionView extends ViewPart {

 
//	  OleControlSite clientsite = null;
//        OleAutomation browser = null;
        

     SimpleBrower myBrower;  
   
	public SimpleBrower getMyBrower() {
		return myBrower;
	}



	public void setMyBrower(SimpleBrower myBrower) {
		this.myBrower = myBrower;
	}

	
	public void useOleBrowser() {
		 
//      OleFrame frame = new OleFrame(arg0, SWT.NONE);
//         try {
//	            clientsite = new OleControlSite(frame, SWT.NONE, "Shell.Explorer");
//	            browser = new OleAutomation(clientsite);
//	            clientsite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
//	        
//	            int[] browserIDs = browser.getIDsOfNames(new String[] { "Navigate", "URL" });
////	            Variant[] address = new Variant[] { new Variant( "http://stackoverflow.com/questions/11243805/left-hand-side-of-assignment-must-be-a-variable") };
//	            Variant[] address = new Variant[] { new Variant( "http://localhost:8080/WEB/eclipseView/query.jsp?queryId=1") };
//	            
//	            browser.invoke(browserIDs[0], address, new int[] { browserIDs[1] });
//	            
//	        } catch (Exception ex) {
//	            System.out.println("Failed to create IE! " + ex.getMessage());
//	            return;
//	        }
//		
	}	


	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
			

		
		  String newUrl="about:blank";
		  
		   myBrower = new SimpleBrower();  	
            myBrower.setMyComposite(arg0);
	        myBrower.createShow();  
	        myBrower.setNewUrl( newUrl);
	        myBrower.refreshBrowser();
//            myBrower.getMyComposite().pack();  
          
	
		 
	       
	    }




	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
//myBrower.getMyComposite().pack();
//myBrower.refreshBrowser();
//		text.setFocus();
	}


}
