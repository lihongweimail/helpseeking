package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import cn.edu.fudan.se.helpseeking.Activator;
import cn.edu.fudan.se.helpseeking.dal.HelpSeekingDAL;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;


public class HelpSeekingSearchView extends ViewPart  implements ISelectionProvider{
	public HelpSeekingSearchView() {
		
	}

	Text text=null;
	Button searchButton=null;
	
	Composite viewComposite=null;
	Composite upComposite=null;
	Composite downComposite=null;
	
    LoopGoogleAPICall myGoogleSearch=new LoopGoogleAPICall();
     List<WEBResult> searchResults=new ArrayList<WEBResult>();
     
    
	OleFrame frame =null;
	 OleControlSite clientsite = null;
     OleAutomation browser = null;
    Variant[] address= new Variant[] { new Variant("http://localhost:8080/WEB/eclipseView/queryList.jsp?queryId=1") }; 
     int[] browserIDs 	;
     int queryId=1;
     
     GridData data3=null;
     GridData data2=null;
     GridData data1=null;

	 
	   public void layoutForView() {
			
//			System.out.println("search view class name: "+this.getClass().getName());
//			viewComposite=arg0;
//		     arg0.setBounds(0, 0, 300, 300);
//		     arg0.addControlListener(new ControlListener() {
//				
//				@Override
//				public void controlResized(ControlEvent e) {
//					// TODO Auto-generated method stub
//					System.out.println("this is search view  arg0  resized");
//					int width=viewComposite.getParent().getBounds().width;
//					int height=viewComposite.getParent().getBounds().height;
//					viewComposite.setBounds(0, 0, width, height);
//				}
//				
//				@Override
//				public void controlMoved(ControlEvent e) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//			 locationComposite=new Composite(arg0, SWT.NONE);
//			 locationComposite.setBounds(0, 0, 300, 300);
//			 locationComposite.addControlListener(new ControlListener() {
//				
//				@Override
//				public void controlResized(ControlEvent e) {
//					// TODO Auto-generated method stub
//					System.out.println("this is locationcomposite resized");
//					int width=locationComposite.getParent().getBounds().width;
//					int height=locationComposite.getParent().getBounds().height;
//					locationComposite.setBounds(0, 0, width, height);
//					
//				}
//				
//				@Override
//				public void controlMoved(ControlEvent e) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//			
//			locationComposite.setLayout(new GridLayout(2, true));
//					 
//			 
//			text = new Text(locationComposite, SWT.BORDER | SWT.WRAP);
//			text.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
//			String  textString="search text";
//			text.setText(textString);	
//			text.setBounds(0, 0, (int)(locationComposite.getBounds().width*0.9), 20);
//			
//			
//		    searchButton=new Button(locationComposite, SWT.PUSH);
//			searchButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));		
//			searchButton.setText("search");
//			searchButton.setBounds( (int)(locationComposite.getBounds().width*0.9),0,(int)(locationComposite.getBounds().width*0.1), 20);
//			
//			searchButton.addMouseListener(new MouseListener() {
//				@Override
//				public void mouseUp(MouseEvent e) {
//					// TODO Auto-generated method stub
//					System.out.println("Button of search view mouse up");
//					System.out.println(searchButton.getParent().getParent().getClass().getName().toString());
//				}
//				@Override
//				public void mouseDown(MouseEvent e) {
//					// TODO Auto-generated method stub
//				}
//				@Override
//				public void mouseDoubleClick(MouseEvent e) {
//					// TODO Auto-generated method stub
//				}
//			});		
	//	
//			
	//	
//			GridData dowGridData = new GridData();  
//			dowGridData.horizontalSpan = 2;  
//			dowGridData.horizontalAlignment = SWT.FILL;  
//			dowGridData.verticalAlignment = SWT.FILL;  
//			dowGridData.grabExcessVerticalSpace = true;  
//		   
//		   	
//			resultListViewer=new List(locationComposite,SWT.NONE);
//			resultListViewer.setLayoutData(dowGridData);  
//			resultListViewer.add("good 1");
//			resultListViewer.add("good 2");
	} 
	
		public void updateSearchResults() {
			String queryTextString=text.getText();
			if(queryTextString.isEmpty()||(queryTextString.length()==0))
			     	return;

			try {
				
				searchResults=myGoogleSearch.searchWeb(queryTextString);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			queryId=HelpSeekingDAL.getMaxQueryId(Activator.helpseekingDB);
			queryId=queryId +1;

			HelpSeekingDAL.insertQuery(Activator.helpseekingDB,queryId,queryTextString );
	
			HelpSeekingDAL.doQueryandSearchResult(Activator.helpseekingDB, searchResults ,queryId);
	
			String query="http://localhost:8080/WEB/eclipseView/queryList.jsp?queryId=" +queryId;
            address = new Variant[] { new Variant(query) };
            browser.invoke(browserIDs[0], address, new int[] { browserIDs[1] });
		}
		
	   @Override
	public void createPartControl(Composite arg0) {
		
		
			viewComposite=arg0;
		
			GridLayout layout= new GridLayout();
			layout.numColumns=2;
			layout.makeColumnsEqualWidth=false;
			viewComposite.setLayout(layout);
			
			data1=new GridData(GridData.FILL_BOTH);
			data1.widthHint=400;
			data1.heightHint=3;
			text = new Text(viewComposite, SWT.BORDER | SWT.WRAP);
			text.setLayoutData(data1);
			text.setText("search text");	
			
			
			data3=new GridData(GridData.FILL_BOTH);
			data1.widthHint=72;
			data1.heightHint=3;
		    searchButton=new Button(viewComposite, SWT.PUSH);
			searchButton.setLayoutData(data3);		
			searchButton.setText("search");
			
			
			searchButton.addMouseListener(new MouseListener() {
				@Override
				public void mouseUp(MouseEvent e) {
				
					updateSearchResults();
					
				}
	
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
				}
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub
				}
			});	
	

			data2 = new GridData();
			data2.horizontalAlignment=GridData.FILL;
			data2.grabExcessHorizontalSpace=true;
			data2.horizontalSpan=2;
			data2.heightHint=900;
			
			
	      frame = new OleFrame(viewComposite, SWT.NONE);
	      frame.setLayoutData(data2);
//			int width=viewComposite.getParent().getBounds().width;
//			int height=viewComposite.getParent().getBounds().height;
//			
//	       frame.setBounds(0, 0, width, height);
//	       System.out.println("width:"+width+"height:"+height);
	      
	      
	       frame.addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent e) {
				// TODO Auto-generated method stub
				
				int width=viewComposite.getParent().getBounds().width;
				int height=viewComposite.getParent().getBounds().height;
				 System.out.println("ole browser frame size -- width:"+width+"height:"+height);
				 int textHeight=text.getBounds().height;
				 int textWidth=text.getBounds().width;
				 text.setBounds(0, 0,textWidth, textHeight);
				 searchButton.setBounds(textWidth+2, 0, width- textWidth-3, textHeight);
				 frame.setBounds(0,textHeight, width, height-textHeight-2);
				 
				 viewComposite.pack();
			}
			
			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	       
	       

	       
	         try {
		            clientsite = new OleControlSite(frame, SWT.NONE, "Shell.Explorer");
		            browser = new OleAutomation(clientsite);
		            clientsite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
		        
		            browserIDs = browser.getIDsOfNames(new String[] { "Navigate", "URL" });
//		            Variant[] address = new Variant[] { new Variant( "http://stackoverflow.com/questions/11243805/left-hand-side-of-assignment-must-be-a-variable") };
		            address = new Variant[] { new Variant("http://localhost:8080/WEB/eclipseView/queryList.jsp?queryId=1") };
		            
		            browser.invoke(browserIDs[0], address, new int[] { browserIDs[1] });
		            
		        } catch (Exception ex) {
		            System.out.println("Failed to create IE! " + ex.getMessage());
		            return;
		        }
	
		       viewComposite.pack();
		
	}


	protected void doSaveToDB(List<WEBResult> searchResults2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		text.setFocus();
	}


	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public ISelection getSelection() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	
}
